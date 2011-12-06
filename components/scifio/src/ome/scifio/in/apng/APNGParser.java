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
import ome.scifio.util.FormatTools;

/**
 * File format SCIFIO Parser for Animated Portable Network Graphics
 * (APNG) images.
 *
 */
public class APNGParser extends AbstractParser<APNGMetadata> {

  // -- Fields --

  private Vector<APNGChunk> blocks;

  // -- Constructor --

  /** Constructs a new APNGParser. */
  public APNGParser() {
    metadata = new APNGMetadata();
    metadata.orderCertain = true;
  }

  // -- Parser API Methods --

  /* @see ome.scifio.AbstractParser#parse(RandomAccessInputStream stream) */
  @Override
  public APNGMetadata parse(RandomAccessInputStream stream)
    throws IOException, FormatException
  {
    super.parse(stream);

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

    blocks = new Vector<APNGChunk>();

    while (in.getFilePointer() < in.length()) {
      int length = in.readInt();
      String type = in.readString(4);
      long offset = in.getFilePointer();

      APNGChunk block = new APNGChunk();

      if (type.equals("acTL")) {
        // APNG-specific chunk
        metadata.numFrames = in.readInt(); // read num_frames
        metadata.numPlays = in.readInt(); // read num_plays
      }
      else if (type.equals("fcTL")) {
        block = new APNGfcTLChunk();
        ((APNGfcTLChunk) block).sequenceNumber = in.readInt();
        ((APNGfcTLChunk) block).width = in.readInt();
        ((APNGfcTLChunk) block).height = in.readInt();
        ((APNGfcTLChunk) block).xOffset = in.readInt();
        ((APNGfcTLChunk) block).yOffset = in.readInt();
        ((APNGfcTLChunk) block).delayNum = in.readShort();
        ((APNGfcTLChunk) block).delayDen = in.readShort();
        ((APNGfcTLChunk) block).disposeOp = in.readByte();
        ((APNGfcTLChunk) block).blendOp = in.readByte();
      }
      else in.skipBytes(length);

      block.length = length;
      block.type = type;
      block.offset = offset;
      blocks.add(block);

      if (in.getFilePointer() < in.length() - 4) {
        in.skipBytes(4); // skip the CRC
      }
    }

    if (metadata.imageCount == 0) metadata.imageCount = 1;
    metadata.sizeZ = 1;
    metadata.sizeT = metadata.imageCount;

    metadata.dimensionOrder = "XYCTZ";
    metadata.interleaved = false;

    //RandomAccessInputStream ras = new RandomAccessInputStream(currentId);
    //DataInputStream dis = new DataInputStream(ras);
    in.seek(0);
    DataInputStream dis = new DataInputStream(in);
    BufferedImage img = ImageIO.read(dis);
    dis.close();

    metadata.sizeX = img.getWidth();
    metadata.sizeY = img.getHeight();
    metadata.rgb = img.getRaster().getNumBands() > 1;
    metadata.sizeC = img.getRaster().getNumBands();
    metadata.pixelType = BufferedImageTools.getPixelType(img);
    metadata.bitsPerPixel = FormatTools.getBitsPerPixel(metadata.pixelType);
    metadata.indexed = img.getColorModel() instanceof IndexColorModel;
    metadata.falseColor = false;
    metadata.setBlocks(blocks);

    if (metadata.indexed) {
      byte[][] lut = new byte[3][256];
      IndexColorModel model = (IndexColorModel) img.getColorModel();
      model.getReds(lut[0]);
      model.getGreens(lut[1]);
      model.getBlues(lut[2]);

      metadata.setLut(lut);
    }

    return metadata;
  }

  // -- MetadataHandler API Methods --

  /* @see MetadataHandler#getMetadataTypes() */
  public Class<APNGMetadata> getMetadataType() {
    return APNGMetadata.class;
  }
}
