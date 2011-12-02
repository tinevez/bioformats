package ome.scifio;

import java.util.Hashtable;

/**
 * CoreImageMetadata consists of the metadata fields common 
 * to any image type, expressed in a standardized way within
 *  SCIFIO.
 *
 */
public class CoreImageMetadata {
  // -- Fields --
  
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
   * pixel types (e.g. <code>INT8</code>) in {@link ome.scifio.util.FormatTools}.
   */
  @Field(label = "pixelType")
  protected int pixelType;

  /** Number of valid bits per pixel. */
  @Field(label = "bitsPerPixel")
  protected int bitsPerPixel;
  
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

  /** Non-core metadata associated with this series. */
  @Field(label = "imageMetadata")
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
  private static final long serialVersionUID = 1L;

  // -- Constructors --
  public CoreImageMetadata() {
    imageMetadata = new Hashtable<String, Object>();
  }

  public void setLut(byte[][] lut) {
    this.lut = lut;
  }

  public void setSizeX(int sizeX) {
    this.sizeX = sizeX;
  }

  public void setSizeY(int sizeY) {
    this.sizeY = sizeY;
  }

  public void setSizeZ(int sizeZ) {
    this.sizeZ = sizeZ;
  }

  public void setSizeC(int sizeC) {
    this.sizeC = sizeC;
  }

  public void setSizeT(int sizeT) {
    this.sizeT = sizeT;
  }

  public void setThumbSizeX(int thumbSizeX) {
    this.thumbSizeX = thumbSizeX;
  }

  public void setThumbSizeY(int thumbSizeY) {
    this.thumbSizeY = thumbSizeY;
  }

  public void setPixelType(int pixelType) {
    this.pixelType = pixelType;
  }

  public void setBitsPerPixel(int bitsPerPixel) {
    this.bitsPerPixel = bitsPerPixel;
  }

  public void setcLengths(int[] cLengths) {
    this.cLengths = cLengths;
  }

  public void setcTypes(String[] cTypes) {
    this.cTypes = cTypes;
  }

  public void setDimensionOrder(String dimensionOrder) {
    this.dimensionOrder = dimensionOrder;
  }

  public void setOrderCertain(boolean orderCertain) {
    this.orderCertain = orderCertain;
  }

  public void setRgb(boolean rgb) {
    this.rgb = rgb;
  }

  public void setLittleEndian(boolean littleEndian) {
    this.littleEndian = littleEndian;
  }

  public void setInterleaved(boolean interleaved) {
    this.interleaved = interleaved;
  }

  public void setIndexed(boolean indexed) {
    this.indexed = indexed;
  }

  public void setFalseColor(boolean falseColor) {
    this.falseColor = falseColor;
  }

  public void setMetadataComplete(boolean metadataComplete) {
    this.metadataComplete = metadataComplete;
  }

  public void setImageMetadata(Hashtable<String, Object> imageMetadata) {
    this.imageMetadata = imageMetadata;
  }

  public void setThumbnail(boolean thumbnail) {
    this.thumbnail = thumbnail;
  }
}
