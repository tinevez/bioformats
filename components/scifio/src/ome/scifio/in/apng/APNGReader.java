package ome.scifio.in.apng;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import ome.scifio.FormatException;
import ome.scifio.common.DataTools;
import ome.scifio.in.BIFormatReader;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.AWTImageTools;
import ome.scifio.util.FormatTools;

/**
 * File format SCIFIO Reader for Animated Portable Network Graphics (APNG)
 * images.
 * 
 */
public class APNGReader extends BIFormatReader<APNGMetadata>
{

    // -- Fields --

    private final Vector<APNGBlock> blocks;
    private final byte[][] lut;
    private final Vector<int[]> frameCoordinates;
    private BufferedImage lastImage;
    private int lastImageIndex = -1;

    // -- Constructor --

    /** Constructs a new APNGReader. */
    public APNGReader(final APNGParser parser)
    {
        super("Animated PNG", "png");
        this.lut = parser.getLut();
        this.blocks = parser.getBlocks();
        this.frameCoordinates = parser.getFrameCoordinates();
        domains = new String[] {FormatTools.GRAPHICS_DOMAIN};

        setMetadataArray(parser.getMetadataArray());
        setCurrentId(parser.getCurrentId());
        setSeries(parser.getSeries());
        
        try {
			this.in = new RandomAccessInputStream(currentId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // -- Reader API Methods --

    @Override
    public byte[][] get8BitLookupTable() throws FormatException, IOException
    {
        FormatTools.assertId(currentId, true, 1);
        return lut;
    }


	@Override
	public Object openPlane(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
	    FormatTools.checkPlaneParameters(this, no, -1, x, y, w, h);

	    if (no == lastImageIndex && lastImage != null) {
	      return AWTImageTools.getSubimage(lastImage, isLittleEndian(), x, y, w, h);
	    }

	    if (no == 0) {
	      in.seek(0);
	      DataInputStream dis =
	        new DataInputStream(new BufferedInputStream(in, 4096));
	      lastImage = ImageIO.read(dis);
	      lastImageIndex = 0;
	      if (x == 0 && y == 0 && w == getSizeX() && h == getSizeY()) {
	        return lastImage;
	      }
	      return AWTImageTools.getSubimage(lastImage, isLittleEndian(), x, y, w, h);
	    }

	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    stream.write(APNGBlock.PNG_SIGNATURE);

	    boolean fdatValid = false;
	    int fctlCount = 0;

	    int[] coords = frameCoordinates.get(no);

	    for (APNGBlock block : blocks) {
	      if (!block.type.equals("IDAT") && !block.type.equals("fdAT") &&
	        !block.type.equals("acTL") && !block.type.equals("fcTL") &&
	        block.length > 0)
	      {
	        byte[] b = new byte[block.length + 12];
	        DataTools.unpackBytes(block.length, b, 0, 4, isLittleEndian());
	        byte[] typeBytes = block.type.getBytes();
	        System.arraycopy(typeBytes, 0, b, 4, 4);
	        in.seek(block.offset);
	        in.read(b, 8, b.length - 12);
	        if (block.type.equals("IHDR")) {
	          DataTools.unpackBytes(coords[2], b, 8, 4, isLittleEndian());
	          DataTools.unpackBytes(coords[3], b, 12, 4, isLittleEndian());
	        }
	        int crc = (int) computeCRC(b, b.length - 4);
	        DataTools.unpackBytes(crc, b, b.length - 4, 4, isLittleEndian());
	        stream.write(b);
	        b = null;
	      }
	      else if (block.type.equals("fcTL")) {
	        fdatValid = fctlCount == no;
	        fctlCount++;
	      }
	      else if (block.type.equals("fdAT")) {
	        in.seek(block.offset + 4);
	        if (fdatValid) {
	          byte[] b = new byte[block.length + 8];
	          DataTools.unpackBytes(block.length - 4, b, 0, 4, isLittleEndian());
	          b[4] = 'I';
	          b[5] = 'D';
	          b[6] = 'A';
	          b[7] = 'T';
	          in.read(b, 8, b.length - 12);
	          int crc = (int) computeCRC(b, b.length - 4);
	          DataTools.unpackBytes(crc, b, b.length - 4, 4, isLittleEndian());
	          stream.write(b);
	          b = null;
	        }
	      }
	      
	    }

	    RandomAccessInputStream s =
	      new RandomAccessInputStream(stream.toByteArray());
	    DataInputStream dis = new DataInputStream(new BufferedInputStream(s, 4096));
	    BufferedImage b = ImageIO.read(dis);
	    dis.close();

	    lastImage = null;
	    openPlane(0, 0, 0, getSizeX(), getSizeY());
		
	    // paste current image onto first image

	    WritableRaster firstRaster = lastImage.getRaster();
	    WritableRaster currentRaster = b.getRaster();
		
	    firstRaster.setDataElements(coords[0], coords[1], currentRaster);
	    lastImage =
	      new BufferedImage(lastImage.getColorModel(), firstRaster, false, null);
	    lastImageIndex = no;
	    return lastImage;
	}

    // -- MetadataHandler API Methods --

    /* @see MetadataHandler#getMetadataTypes() */
    @Override
    public Class<APNGMetadata> getMetadataType()
    {
        return APNGMetadata.class;
    }

    // -- Helper methods --

    private long computeCRC(final byte[] buf, final int len)
    {
        final CRC32 crc = new CRC32();
        crc.update(buf, 0, len);
        return crc.getValue();
    }
}
