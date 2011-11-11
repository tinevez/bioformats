package ome.scifio;

import java.io.IOException;
import java.util.Hashtable;

import loci.formats.FormatException;

/**
 * Format-agnostic metadata representation, with no
 * backing XML information (minimum information knowable
 * by SCIFIO).
 *
 */
public class CoreMetadata extends AbstractMetadata {

  // -- Fields --
  /** Width (in pixels) of images in this series. */
  public int sizeX;

  /** Height (in pixels) of images in this series. */
  public int sizeY;

  /** Number of Z sections. */
  public int sizeZ;

  /** Number of channels. */
  public int sizeC;

  /** Number of timepoints. */
  public int sizeT;

  /** Width (in pixels) of thumbnail images in this series. */
  public int thumbSizeX;

  /** Height (in pixels) of thumbnail images in this series. */
  public int thumbSizeY;

  /**
   * Describes the number of bytes per pixel.  Must be one of the <i>static</i>
   * pixel types (e.g. <code>INT8</code>) in {@link loci.formats.FormatTools}.
   */
  public int pixelType;

  /** Number of valid bits per pixel. */
  public int bitsPerPixel;

  /** Total number of images. */
  public int imageCount;

  /** Length of each subdimension of C. */
  public int[] cLengths;

  /** Name of each subdimension of C. */
  public String[] cTypes;

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
  public String dimensionOrder;

  /**
   * Indicates whether or not we are confident that the
   * dimension order is correct.
   */
  public boolean orderCertain;

  /**
   * Indicates whether or not the images are stored as RGB
   * (multiple channels per plane).
   */
  public boolean rgb;

  /** Indicates whether or not each pixel's bytes are in little endian order. */
  public boolean littleEndian;

  /**
   * True if channels are stored RGBRGBRGB...; false if channels are stored
   * RRR...GGG...BBB...
   */
  public boolean interleaved;

  /** Indicates whether or not the images are stored as indexed color. */
  public boolean indexed;

  /** Indicates whether or not we can ignore the color map (if present). */
  public boolean falseColor = true;

  /**
   * Indicates whether or not we are confident that all of the metadata stored
   * within the file has been parsed.
   */
  public boolean metadataComplete;

  /** Non-core metadata associated with this series. */
  public Hashtable<String, Object> seriesMetadata;

  /**
   * Indicates whether or not this series is a lower-resolution copy of
   * another series.
   */
  public boolean thumbnail;

  // -- Constructors --

  public CoreMetadata() {
    seriesMetadata = new Hashtable<String, Object>();
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  // -- Metadata API Methods --
  @Override
  public Object getMetadataValue(String field) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getSeriesMetadataValue(String field) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Hashtable<String, Object> getGlobalMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Hashtable<String, Object> getSeriesMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CoreMetadata[] getCoreMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Hashtable<String, Object> getMetadata() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getImageCount() {
    return this.imageCount;
  }

  @Override
  public void setInterleaved(boolean interleaved) {
    this.interleaved = interleaved;
  }

  @Override
  public boolean isInterleaved() {
    return this.interleaved;
  }

  @Override
  public int getPixelType() {
    return this.pixelType;
  }

  @Override
  public int getBitsPerPixel() {
    return this.bitsPerPixel;
  }

  @Override
  public int getEffectiveSizeC() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getRGBChannelCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isLittleEndian() {
    return this.littleEndian;
  }

  @Override
  public boolean isIndexed() {
    return this.indexed;
  }

  @Override
  public boolean isFalseColor() {
    return this.falseColor;
  }

  @Override
  public boolean isSingleFile(String id) throws FormatException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String[] getPossibleDomains(String id)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasCompanionFiles() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isMetadataComplete() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isRGB() {
    return this.rgb;
  }

@Override
public int getSizeX() {
	return this.sizeX;
}

@Override
public int getSizeY() {
	return this.sizeY;
}

@Override
public int getSizeZ() {
	return this.sizeZ;
}

@Override
public int getSizeC() {
	return this.sizeC;
}

@Override
public int getSizeT() {
	return this.sizeT;
}

}
