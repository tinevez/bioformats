package ome.scifio.in.apng;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
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
public class APNGReader extends BIFormatReader<APNGMetadata> {

  // -- Fields --

  private BufferedImage lastPlane;
  private int lastPlaneIndex = -1;

  // -- Constructor --

  /** Constructs a new APNGReader. */

  public APNGReader() {
    super("Animated PNG", "png");
  }

  // -- Reader API Methods --

  /* @see ome.scifio.Reader#openPlane(int, int, int, int, int) */
  public Object openPlane(int iNo, int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, -1, x, y, w, h);

    if (no == lastPlaneIndex && lastPlane != null) {
      return AWTImageTools.getSubimage(lastPlane, metadata.isLittleEndian(no), x, y, w, h);
    }

    if (no == 0) {
      in.seek(0);
      DataInputStream dis =
        new DataInputStream(new BufferedInputStream(in, 4096));
      lastPlane = ImageIO.read(dis);
      lastPlaneIndex = 0;
      if (x == 0 && y == 0 && w == metadata.getSizeX(iNo) && h == metadata.getSizeY(iNo)) {
        return lastPlane;
      }
      return AWTImageTools.getSubimage(lastPlane, metadata.isLittleEndian(iNo), x, y, w, h);
    }

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    stream.write(APNGBlock.PNG_SIGNATURE);

    boolean fdatValid = false;
    int fctlCount = 0;

    int[] coords = metadata.getFrameCoordinates().get(no);

    for (APNGBlock block : metadata.getBlocks()) {
      if (!block.type.equals("IDAT") && !block.type.equals("fdAT") &&
        !block.type.equals("acTL") && !block.type.equals("fcTL") &&
        block.length > 0)
      {
        byte[] b = new byte[block.length + 12];
        DataTools.unpackBytes(block.length, b, 0, 4, metadata.isLittleEndian(iNo));
        byte[] typeBytes = block.type.getBytes();
        System.arraycopy(typeBytes, 0, b, 4, 4);
        in.seek(block.offset);
        in.read(b, 8, b.length - 12);
        if (block.type.equals("IHDR")) {
          DataTools.unpackBytes(coords[2], b, 8, 4, metadata.isLittleEndian(iNo));
          DataTools.unpackBytes(coords[3], b, 12, 4, metadata.isLittleEndian(iNo));
        }
        int crc = (int) computeCRC(b, b.length - 4);
        DataTools.unpackBytes(crc, b, b.length - 4, 4, metadata.isLittleEndian(iNo));
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
          DataTools.unpackBytes(block.length - 4, b, 0, 4, metadata.isLittleEndian(iNo));
          b[4] = 'I';
          b[5] = 'D';
          b[6] = 'A';
          b[7] = 'T';
          in.read(b, 8, b.length - 12);
          int crc = (int) computeCRC(b, b.length - 4);
          DataTools.unpackBytes(crc, b, b.length - 4, 4, metadata.isLittleEndian(iNo));
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

    lastPlane = null;
    openPlane(iNo, 0, 0, 0, metadata.getSizeX(iNo), metadata.getSizeY(iNo));

    // paste current image onto first image

    WritableRaster firstRaster = lastPlane.getRaster();
    WritableRaster currentRaster = b.getRaster();

    firstRaster.setDataElements(coords[0], coords[1], currentRaster);
    lastPlane =
      new BufferedImage(lastPlane.getColorModel(), firstRaster, false, null);
    lastPlaneIndex = no;
    return lastPlane;
  }

  // -- MetadataHandler API Methods --

  /* @see MetadataHandler#getMetadataTypes() */
  @Override
  public Class<APNGMetadata> getMetadataType() {
    return APNGMetadata.class;
  }

  // -- Helper methods --

  private long computeCRC(final byte[] buf, final int len) {
    final CRC32 crc = new CRC32();
    crc.update(buf, 0, len);
    return crc.getValue();
  }

  @Override
  public byte[] openBytes(int iNo, int no, byte[] buf, int x, int y, int w,
    int h) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    return null;
  }
}
