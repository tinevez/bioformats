package ome.scifio.in.apng;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;


import ome.scifio.AbstractParser;
import ome.scifio.FormatException;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.BufferedImageTools;

public class APNGParser extends AbstractParser<APNGMetadata> {
	
	  // -- Fields --

	  private Vector<APNGBlock> blocks;
	  private byte[][] lut;

	  private Vector<int[]> frameCoordinates;
	  
	  public APNGParser() {
		  metadata = new APNGMetadata[1];
		  metadata[0] = new APNGMetadata();
		  metadata[0].orderCertain = true;
	  }
	
	@Override
	public APNGMetadata[] parse(RandomAccessInputStream stream)
			throws IOException, FormatException {
		super.parse(stream);
	    in = stream;

	    // check that this is a valid PNG file
	    byte[] signature = new byte[8];
	    in.read(signature);

	    if (signature[0] != (byte) 0x89 || signature[1] != 0x50 ||
	      signature[2] != 0x4e || signature[3] != 0x47 || signature[4] != 0x0d ||
	      signature[5] != 0x0a || signature[6] != 0x1a || signature[7] != 0x0a)
	    {
	      throw new FormatException("Invalid PNG signature.");
	    }

	    // read data chunks - each chunk consists of the following:
	    // 1) 32 bit length
	    // 2) 4 char type
	    // 3) 'length' bytes of data
	    // 4) 32 bit CRC

	    blocks = new Vector<APNGBlock>();
	    frameCoordinates = new Vector<int[]>();

	    while (in.getFilePointer() < in.length()) {
	      int length = in.readInt();
	      String type = in.readString(4);

	      APNGBlock block = new APNGBlock();
	      block.length = length;
	      block.type = type;
	      block.offset = in.getFilePointer();
	      blocks.add(block);

	      if (type.equals("acTL")) {
	        // APNG-specific chunk
	        metadata[0].imageCount = in.readInt();
	        int loop = in.readInt();
	        addGlobalMeta("Loop count", loop);
	      }
	      else if (type.equals("fcTL")) {
	        in.skipBytes(4);
	        int w = in.readInt();
	        int h = in.readInt();
	        int x = in.readInt();
	        int y = in.readInt();
	        frameCoordinates.add(new int[] {x, y, w, h});
	        in.skipBytes(length - 20);
	      }
	      else in.skipBytes(length);

	      if (in.getFilePointer() < in.length() - 4) {
	        in.skipBytes(4); // skip the CRC
	      }
	    }

	    if (metadata[0].imageCount == 0) metadata[0].imageCount = 1;
	    metadata[0].sizeZ = 1;
	    metadata[0].sizeT = getImageCount();

	    metadata[0].dimensionOrder = "XYCTZ";
	    metadata[0].interleaved = false;

	    //RandomAccessInputStream ras = new RandomAccessInputStream(currentId);
	    //DataInputStream dis = new DataInputStream(ras);
	    in.seek(0);
	    DataInputStream dis = new DataInputStream(in);
	    BufferedImage img = ImageIO.read(dis);
	    dis.close();

	    metadata[0].sizeX = img.getWidth();
	    metadata[0].sizeY = img.getHeight();
	    metadata[0].rgb = img.getRaster().getNumBands() > 1;
	    metadata[0].sizeC = img.getRaster().getNumBands();
	    metadata[0].pixelType = BufferedImageTools.getPixelType(img);
	    metadata[0].indexed = img.getColorModel() instanceof IndexColorModel;
	    metadata[0].falseColor = false;

	    if (isIndexed()) {
	      lut = new byte[3][256];
	      IndexColorModel model = (IndexColorModel) img.getColorModel();
	      model.getReds(lut[0]);
	      model.getGreens(lut[1]);
	      model.getBlues(lut[2]);
	    }
	    
	    //TODO create actual Metadata object
	    return metadata;
	}

	@Override
	public void setOriginalMetadataPopulated(boolean populate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOriginalMetadataPopulated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMetadataFiltered(boolean filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMetadataFiltered() {
		// TODO Auto-generated method stub
		return false;
	}

	// -- MetadataHandler API Methods --

	/* @see MetadataHandler#getMetadataTypes() */
	public Class<APNGMetadata> getMetadataType() {
		return APNGMetadata.class;
	}
	
	// -- APNGParser Methods --
	public Vector<APNGBlock> getBlocks() {
		return blocks;
	}
	
	public byte[][] getLut() {
		return lut;
	}
	
	public Vector<int[]> getFrameCoordinates() {
		return frameCoordinates;
	}
}
