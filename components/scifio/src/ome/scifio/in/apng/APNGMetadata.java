package ome.scifio.in.apng;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import ome.scifio.AbstractMetadata;
import ome.scifio.Field;
import ome.scifio.FormatException;

/**
 * File format SCIFIO Metadata for Animated Portable Network Graphics
 * (APNG) images.
 *
 */
public class APNGMetadata extends AbstractMetadata {

  // -- APNG Specific Fields --
  
  /** Number of frames in this APNG image */
  @Field(label = "num_frames")
  protected int numFrames;
  
  /** Number of times the frames are played.  0 = infinite */
  @Field(label = "num_plays")
  protected int numPlays;
  
  /** The collection of APNG blocks for this image */
  @Field(label = "blocks", isList = true)
  protected Vector<APNGChunk> blocks;

  // -- Common Fields --

  /** 8-bit lookup table for this image */
  @Field(label = "lut")
  protected byte[][] lut;
  
  /** Width (in pixels) of planes in this image. */
  @Field(label = "sizeX")
  protected int sizeX;

  /** Height (in pixels) of planes in this image. */
  @Field(label = "sizeY")
  protected int sizeY;

  /** Number of Z sections. */
  @Field(label = "sizeZ")
  protected int sizeZ;

  /** Number of channels. */
  @Field(label = "sizeC")
  protected int sizeC;

  /** Number of timepoints. */
  @Field(label = "sizeT")
  protected int sizeT;

  /** Width (in pixels) of thumbnail planes in this image. */
  @Field(label = "thumbSizeX")
  protected int thumbSizeX;

  /** Height (in pixels) of thumbnail planes in this image. */
  @Field(label = "thumbSizeY")
  protected int thumbSizeY;

  /**
   * Describes the number of bytes per pixel.  Must be one of the <i>static</i>
   * pixel types (e.g. <code>INT8</code>) in {@link loci.formats.FormatTools}.
   */
  @Field(label = "pixelType")
  protected int pixelType;

  /** Number of valid bits per pixel. */
  @Field(label = "bitsPerPixel")
  protected int bitsPerPixel;

  /** Total number of images. */
  @Field(label = "imageCount")
  protected int imageCount = 1; // always 1 for APNG
  
  /** Length of each subdimension of C. */
  @Field(label = "cLengths")
  protected int[] cLengths;

  /** Name of each subdimension of C. */
  @Field(label = "cTypes")
  protected String[] cTypes;

  /**
   * Order in which dimensions are stored.  Must be one of the following:<ul>
   *  <li>XYCZT</li>
   *  <li>XYCTZ</li>
   *  <li>XYZCT</li>
   *  <li>XYZTC</li>
   *  <li>XYTCZ</li>
   *  <li>XYTZC</li>
   * </ul>
   */
  @Field(label = "dimensionOrder")
  protected String dimensionOrder;

  /**
   * Indicates whether or not we are confident that the
   * dimension order is correct.
   */
  @Field(label = "orderCertain")
  protected boolean orderCertain;

  /**
   * Indicates whether or not the images are stored as RGB
   * (multiple channels per plane).
   */
  @Field(label = "rgb")
  protected boolean rgb;

  /** Indicates whether or not each pixel's bytes are in little endian order. */
  @Field(label = "littleEndian")
  protected boolean littleEndian;

  /**
   * True if channels are stored RGBRGBRGB...; false if channels are stored
   * RRR...GGG...BBB...
   */
  @Field(label = "interleaved")
  protected boolean interleaved;

  /** Indicates whether or not the images are stored as indexed color. */
  @Field(label = "indexed")
  protected boolean indexed;

  /** Indicates whether or not we can ignore the color map (if present). */
  @Field(label = "falseColor")
  protected boolean falseColor = true;

  /**
   * Indicates whether or not we are confident that all of the metadata stored
   * within the file has been parsed.
   */
  @Field(label = "metadataComplete")
  protected boolean metadataComplete;

  /** Original metadata associated with this series. */
  protected Hashtable<String, Object> imageMetadata;

  /**
   * Indicates whether or not this series is a lower-resolution copy of
   * another series.
   */
  @Field(label = "thumbnail")
  protected boolean thumbnail;
  
  /**
   * 
   */

  protected static final long serialVersionUID = 1L;
  
  // -- Constructor --
  public APNGMetadata() {
    this.imageMetadata = new Hashtable<String, Object>();
  }

  // -- APNGMetadata Methods --

  public void setBlocks(Vector<APNGChunk> blocks) {
    this.blocks = blocks;
  }

  public Vector<APNGChunk> getBlocks() {
    return blocks;
  }

  public void setLut(byte[][] lut) {
    this.lut = lut;
  }
  
  public int getEffectiveSizeC(int no) {
    int sizeZT = getSizeZ(no) * getSizeT(no);
    if (sizeZT == 0) return 0;
    return getImageCount() / sizeZT;
  }

  // -- Metadata API Methods --
  
  /* @see Metadata#isLittleEndian(int) */
  public boolean isLittleEndian(int no) {
    return this.littleEndian;
  }

  /* @see Metadata#getSizeX(int) */
  public int getSizeX(int no) {
    return this.sizeX;
  }

  /* @see Metadata#getSizeY(int) */
  public int getSizeY(int no) {
    return this.sizeY;
  }

  /* @see Metadata#getSizeZ(int) */
  public int getSizeZ(int no) {
    return this.sizeZ;
  }

  /* @see Metadata#getSizeC(int) */
  public int getSizeC(int no) {
    return this.sizeC;
  }

  /* @see Metadata#getSizeT(int) */
  public int getSizeT(int no) {
    return this.sizeT;
  }

  /* @see Metadata#getPixelType(int) */
  public int getPixelType(int no) {
    return this.pixelType;
  }

  /* @see Metadata#getRGBChannelCount(int) */
  public int getRGBChannelCount(int no) {
    int effSizeC = getEffectiveSizeC(no);
    if (effSizeC == 0) return 0;
    return getSizeC(no) / effSizeC;
  }

  /* @see Metadata#getImageCount() */
  public int getImageCount() {
    return this.imageCount;
  }

  /* @see Metadata#isInterleaved(int) */
  public boolean isInterleaved(int no) {
    return this.indexed;
  }

  /* @see Metadata#isIndexed(int) */
  public boolean isIndexed(int no) {
    return this.indexed;
  }

  /* @see Metadata#get8BitLookupTable(int) */
  public byte[][] get8BitLookupTable(int no) throws FormatException, IOException {
    return lut;
  }

  /* @see Metadata#get16BitLookupTable(int) */
  public short[][] get16BitLookupTable(int no) throws FormatException, IOException {
    // TODO Auto-generated method stub
    return null;
  }
  
  /* @see Metadata#getGlobalMetadata() */
  public Hashtable<String, Object> getGlobalMetadata() {
    return this.imageMetadata;
  }

  /* @see Metadata#getImageMetadata() */
  public Hashtable<String, Object> getImageMetadata(int no) {
    return this.imageMetadata;
  }

  /* @see Metadata#getMetadataValue() */
  public Object getMetadataValue(int no, String field) {
    return this.imageMetadata.get(field);
  }

  /* @see Metadata#getImageMetadataValue() */
  public Object getImageMetadataValue(int no, String field) {
    return this.imageMetadata.get(field);
  }

  // -- Helper Methods --
  
  public void resetMeta() {
    super.resetMeta(this.getClass());
    this.imageMetadata = new Hashtable<String, Object>();
  }
}
