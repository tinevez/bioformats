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

/**
 * File format SCIFIO Parser for Animated Portable Network Graphics
 * (APNG) images.
 *
 */
public class APNGParser extends AbstractParser<APNGMetadata> {

  // -- Fields --

  private Vector<APNGBlock> blocks;
  private byte[][] lut;
  private Vector<int[]> frameCoordinates;

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

        metadata.imageCount = in.readInt(); // read num_frames
        int loop = in.readInt(); // read num_plays
        addGlobalMeta("Loop count", loop);
      }
      else if (type.equals("fcTL")) {
        in.skipBytes(4); // skips sequence_number
        int w = in.readInt(); // read width
        int h = in.readInt(); // read height 
        int x = in.readInt(); // read x_offset
        int y = in.readInt(); // read y_offset
        frameCoordinates.add(new int[] {x, y, w, h});
        in.skipBytes(length - 20); // not read: delay_num, delay_den, dispose_op, blend_op
      }
      else in.skipBytes(length);

      if (in.getFilePointer() < in.length() - 4) {
        in.skipBytes(4); // skip the CRC
      }
    }

    if (metadata.imageCount == 0)
      metadata.imageCount = 1;
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
    metadata.indexed =
      img.getColorModel() instanceof IndexColorModel;
    metadata.falseColor = false;
    metadata.setBlocks(blocks);
    metadata.setFrameCoordinates(frameCoordinates);

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
