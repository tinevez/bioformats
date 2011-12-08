package loci.formats;

import java.awt.image.ColorModel;
import java.io.IOException;

import loci.common.DataTools;
import loci.common.RandomAccessOutputStream;
import loci.formats.codec.CodecOptions;
import loci.formats.meta.MetadataRetrieve;
import ome.scifio.Writer;
import ome.xml.model.primitives.PositiveInteger;

/**
 * Abstract superclass of all biological file format writers.
 * Defers to ome.scifio.Writer 
 *
 */
public abstract class SCIFIOFormatWriter extends FormatWriter {

  // -- Fields --

  /** Scifio Writer for deference */
  protected Writer writer;
  
  // -- Constructor --

  public SCIFIOFormatWriter(String format, String suffix) {
    super(format, suffix);
  }
  
  public SCIFIOFormatWriter(String format, String[] suffixes) {
    super(format, suffixes);
  }
  
  // -- IFormatWriter API methods --

  /* @see IFormatWriter#changeOutputFile(String) */
  public void changeOutputFile(String id) throws FormatException, IOException {
    setId(id);
  }

  /* @see IFormatWriter#saveBytes(int, byte[]) */
  public void saveBytes(int no, byte[] buf) throws FormatException, IOException
  {
    int width = metadataRetrieve.getPixelsSizeX(getSeries()).getValue();
    int height = metadataRetrieve.getPixelsSizeY(getSeries()).getValue();
    saveBytes(no, buf, 0, 0, width, height);
  }

  /* @see IFormatWriter#savePlane(int, Object) */
  public void savePlane(int no, Object plane)
    throws FormatException, IOException
  {
    int width = metadataRetrieve.getPixelsSizeX(getSeries()).getValue();
    int height = metadataRetrieve.getPixelsSizeY(getSeries()).getValue();
    savePlane(no, plane, 0, 0, width, height);
  }

  /* @see IFormatWriter#savePlane(int, Object, int, int, int, int) */
  public void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    // NB: Writers use byte arrays by default as the native type.
    if (!(plane instanceof byte[])) {
      throw new IllegalArgumentException("Object to save must be a byte[]");
    }
    saveBytes(no, (byte[]) plane, x, y, w, h);
  }

  /* @see IFormatWriter#setSeries(int) */
  public void setSeries(int series) throws FormatException {
    if (series < 0) throw new FormatException("Series must be > 0.");
    if (series >= metadataRetrieve.getImageCount()) {
      throw new FormatException("Series is '" + series +
        "' but MetadataRetrieve only defines " +
        metadataRetrieve.getImageCount() + " series.");
    }
    this.series = series;
  }

  /* @see IFormatWriter#getSeries() */
  public int getSeries() {
    return series;
  }

  /* @see IFormatWriter#setInterleaved(boolean) */
  public void setInterleaved(boolean interleaved) {
    this.interleaved = interleaved;
  }

  /* @see IFormatWriter#isInterleaved() */
  public boolean isInterleaved() {
    return interleaved;
  }

  /* @see IFormatWriter#setValidBitsPerPixel(int) */
  public void setValidBitsPerPixel(int bits) {
    validBits = bits;
  }

  /* @see IFormatWriter#canDoStacks() */
  public boolean canDoStacks() { return false; }

  /* @see IFormatWriter#setMetadataRetrieve(MetadataRetrieve) */
  public void setMetadataRetrieve(MetadataRetrieve retrieve) {
    FormatTools.assertId(currentId, false, 1);
    if (retrieve == null) {
      throw new IllegalArgumentException("Metadata object is null");
    }
    metadataRetrieve = retrieve;
  }

  /* @see IFormatWriter#getMetadataRetrieve() */
  public MetadataRetrieve getMetadataRetrieve() {
    return metadataRetrieve;
  }

  /* @see IFormatWriter#setColorModel(ColorModel) */
  public void setColorModel(ColorModel model) { cm = model; }

  /* @see IFormatWriter#getColorModel() */
  public ColorModel getColorModel() { return cm; }

  /* @see IFormatWriter#setFramesPerSecond(int) */
  public void setFramesPerSecond(int rate) { fps = rate; }

  /* @see IFormatWriter#getFramesPerSecond() */
  public int getFramesPerSecond() { return fps; }

  /* @see IFormatWriter#getCompressionTypes() */
  public String[] getCompressionTypes() { return compressionTypes; }

  /* @see IFormatWriter#setCompression(compress) */
  public void setCompression(String compress) throws FormatException {
    // check that this is a valid type
    for (int i=0; i<compressionTypes.length; i++) {
      if (compressionTypes[i].equals(compress)) {
        compression = compress;
        return;
      }
    }
    throw new FormatException("Invalid compression type: " + compress);
  }

  /* @see IFormatWriter#setCodecOptions(CodecOptions) */
  public void setCodecOptions(CodecOptions options) {
    this.options = options;
  }

  /* @see IFormatWriter#getCompression() */
  public String getCompression() {
    return compression;
  }

  /* @see IFormatWriter#getPixelTypes() */
  public int[] getPixelTypes() {
    return getPixelTypes(getCompression());
  }

  /* @see IFormatWriter#getPixelTypes(String) */
  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.INT8, FormatTools.UINT8, FormatTools.INT16,
      FormatTools.UINT16, FormatTools.INT32, FormatTools.UINT32,
      FormatTools.FLOAT};
  }

  /* @see IFormatWriter#isSupportedType(int) */
  public boolean isSupportedType(int type) {
    int[] types = getPixelTypes();
    for (int i=0; i<types.length; i++) {
      if (type == types[i]) return true;
    }
    return false;
  }

  /* @see IFormatWriter#setWriteSequentially(boolean) */
  public void setWriteSequentially(boolean sequential) {
    this.sequential = sequential;
  }

  // -- Deprecated IFormatWriter API methods --

  /**
   * @deprecated
   * @see IFormatWriter#saveBytes(byte[], boolean)
   */
  public void saveBytes(byte[] bytes, boolean last)
    throws FormatException, IOException
  {
    saveBytes(bytes, 0, last, last);
  }

  /**
   * @deprecated
   * @see IFormatWriter#saveBytes(byte[], int, boolean, boolean)
   */
  public void saveBytes(byte[] bytes, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    setSeries(series);
  }

  /**
   * @deprecated
   * @see IFormatWriter#savePlane(Object, boolean)
   */
  public void savePlane(Object plane, boolean last)
    throws FormatException, IOException
  {
    savePlane(plane, 0, last, last);
  }

  /**
   * @deprecated
   * @see IFormatWriter#savePlane(Object, int, boolean, boolean)
   */
  public void savePlane(Object plane, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // NB: Writers use byte arrays by default as the native type.
    if (!(plane instanceof byte[])) {
      throw new IllegalArgumentException("Object to save must be a byte[]");
    }
    saveBytes((byte[]) plane, series, lastInSeries, last);
  }

  // -- IFormatHandler API methods --

  /* @see IFormatHandler#setId(String) */
  public void setId(String id) throws FormatException, IOException {
    if (id.equals(currentId)) return;
    close();
    currentId = id;
    out = new RandomAccessOutputStream(currentId);

    MetadataRetrieve r = getMetadataRetrieve();
    initialized = new boolean[r.getImageCount()][];
    int oldSeries = series;
    for (int i=0; i<r.getImageCount(); i++) {
      setSeries(i);
      initialized[i] = new boolean[getPlaneCount()];
    }
    setSeries(oldSeries);
  }

  /* @see IFormatHandler#close() */
  public void close() throws IOException {
    if (out != null) out.close();
    out = null;
    currentId = null;
    initialized = null;
  }

  // -- Helper methods --

  /**
   * Ensure that the arguments that are being passed to saveBytes(...) are
   * valid.
   * @throws FormatException if any of the arguments is invalid.
   */
  protected void checkParams(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException
  {
    MetadataRetrieve r = getMetadataRetrieve();
    MetadataTools.verifyMinimumPopulated(r, series);

    if (buf == null) throw new FormatException("Buffer cannot be null.");
    int z = r.getPixelsSizeZ(series).getValue().intValue();
    int t = r.getPixelsSizeT(series).getValue().intValue();
    int c = r.getChannelCount(series);
    int planes = z * c * t;

    if (no < 0) throw new FormatException(String.format(
        "Plane index:%d must be >= 0", no));
    if (no >= planes) {
      throw new FormatException(String.format(
          "Plane index:%d must be < %d", no, planes));
    }

    int sizeX = r.getPixelsSizeX(series).getValue().intValue();
    int sizeY = r.getPixelsSizeY(series).getValue().intValue();
    if (x < 0) throw new FormatException(String.format("X:%d must be >= 0", x));
    if (y < 0) throw new FormatException(String.format("Y:%d must be >= 0", y));
    if (x >= sizeX) {
      throw new FormatException(String.format(
          "X:%d must be < %d", x, sizeX));
    }
    if (y >= sizeY) {
      throw new FormatException(String.format("Y:%d must be < %d", y, sizeY));
    }
    if (w <= 0) throw new FormatException(String.format(
        "Width:%d must be > 0", w));
    if (h <= 0) throw new FormatException(String.format(
        "Height:%d must be > 0", h));
    if (x + w > sizeX) throw new FormatException(String.format(
        "(w:%d + x:%d) must be <= %d", w, x, sizeX));
    if (y + h > sizeY) throw new FormatException(String.format(
        "(h:%d + y:%d) must be <= %d", h, y, sizeY));

    int pixelType =
      FormatTools.pixelTypeFromString(r.getPixelsType(series).toString());
    int bpp = FormatTools.getBytesPerPixel(pixelType);
    PositiveInteger samples = r.getChannelSamplesPerPixel(series, 0);
    if (samples == null) samples = new PositiveInteger(1);
    int minSize = bpp * w * h * samples.getValue();
    if (buf.length < minSize) {
      throw new FormatException("Buffer is too small; expected " + minSize +
        " bytes, got " + buf.length + " bytes.");
    }

    if (!DataTools.containsValue(getPixelTypes(compression), pixelType)) {
      throw new FormatException("Unsupported image type '" +
        FormatTools.getPixelTypeString(pixelType) + "'.");
    }
  }

  /**
   * Seek to the given (x, y) coordinate of the image that starts at
   * the given offset.
   */
  protected void seekToPlaneOffset(long baseOffset, int x, int y)
    throws IOException
  {
    out.seek(baseOffset);

    MetadataRetrieve r = getMetadataRetrieve();
    int samples = getSamplesPerPixel();
    int pixelType =
      FormatTools.pixelTypeFromString(r.getPixelsType(series).toString());
    int bpp = FormatTools.getBytesPerPixel(pixelType);

    if (interleaved) bpp *= samples;

    int sizeX = r.getPixelsSizeX(series).getValue().intValue();

    out.skipBytes(bpp * (y * sizeX + x));
  }

  /**
   * Returns true if the given rectangle coordinates correspond to a full
   * image in the given series.
   */
  protected boolean isFullPlane(int x, int y, int w, int h) {
    MetadataRetrieve r = getMetadataRetrieve();
    int sizeX = r.getPixelsSizeX(series).getValue().intValue();
    int sizeY = r.getPixelsSizeY(series).getValue().intValue();
    return x == 0 && y == 0 && w == sizeX && h == sizeY;
  }

  /** Retrieve the number of samples per pixel for the current series. */
  protected int getSamplesPerPixel() {
    MetadataRetrieve r = getMetadataRetrieve();
    PositiveInteger samples = r.getChannelSamplesPerPixel(series, 0);
    if (samples == null) {
      LOGGER.warn("SamplesPerPixel #0 is null. It is assumed to be 1.");
    }
    return samples == null ? 1 : samples.getValue();
  }

  /** Retrieve the total number of planes in the current series. */
  protected int getPlaneCount() {
    MetadataRetrieve r = getMetadataRetrieve();
    int z = r.getPixelsSizeZ(series).getValue().intValue();
    int t = r.getPixelsSizeT(series).getValue().intValue();
    int c = r.getPixelsSizeC(series).getValue().intValue();
    c /= r.getChannelSamplesPerPixel(series, 0).getValue().intValue();
    return z * c * t;
  }

}
