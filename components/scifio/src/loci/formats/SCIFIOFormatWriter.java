package loci.formats;

import java.awt.image.ColorModel;
import java.io.IOException;

import loci.formats.codec.CodecOptions;
import loci.formats.meta.MetadataRetrieve;
import ome.scifio.Writer;

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
    try {
      writer.changeOutputFile(id);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#saveBytes(int, byte[]) */
  public void saveBytes(int no, byte[] buf) throws FormatException, IOException
  {
    try {
      writer.saveBytes(getSeries(), no, buf);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#savePlane(int, Object) */
  public void savePlane(int no, Object plane)
    throws FormatException, IOException
  {
    try {
      writer.savePlane(getSeries(), no, plane);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#savePlane(int, Object, int, int, int, int) */
  public void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    // NB: Writers use byte arrays by default as the native type.
    if (!(plane instanceof byte[])) {
      throw new IllegalArgumentException("Object to save must be a byte[]");
    }
    try {
      writer.savePlane(getSeries(), no, plane, x, y, w, h);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#setSeries(int) */
  public void setSeries(int series) throws FormatException {
    if (series < 0) throw new FormatException("Series must be > 0.");
    if (series >= writer.getMetadata().getImageCount()) {
      throw new FormatException("Series is '" + series +
        "' but MetadataRetrieve only defines " +
        writer.getMetadata().getImageCount() + " series.");
    }
    this.series = series;
  }

  /* @see IFormatWriter#getSeries() */
  public int getSeries() {
    return series;
  }

  /* @see IFormatWriter#setInterleaved(boolean) */
  public void setInterleaved(boolean interleaved) {
    //TODO need setters
  }

  /* @see IFormatWriter#isInterleaved() */
  public boolean isInterleaved() {
    return writer.getMetadata().isInterleaved(getSeries());
  }

  /* @see IFormatWriter#setValidBitsPerPixel(int) */
  public void setValidBitsPerPixel(int bits) {
    //TODO need setters
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
    writer.setMetadata(getMetadataRetrieve().getSCIFIOMeta());
  }

  /* @see IFormatWriter#getMetadataRetrieve() */
  public MetadataRetrieve getMetadataRetrieve() {
    return metadataRetrieve;
  }

  /* @see IFormatWriter#setColorModel(ColorModel) */
  public void setColorModel(ColorModel model) { writer.setColorModel(model); }

  /* @see IFormatWriter#getColorModel() */
  public ColorModel getColorModel() { return writer.getColorModel(); }

  /* @see IFormatWriter#setFramesPerSecond(int) */
  public void setFramesPerSecond(int rate) { writer.setFramesPerSecond(rate); }

  /* @see IFormatWriter#getFramesPerSecond() */
  public int getFramesPerSecond() { return writer.getFramesPerSecond(); }

  /* @see IFormatWriter#getCompressionTypes() */
  public String[] getCompressionTypes() { return writer.getCompressionTypes(); }

  /* @see IFormatWriter#setCompression(compress) */
  public void setCompression(String compress) throws FormatException {
    // check that this is a valid type
    try {
      writer.setCompression(compress);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#setCodecOptions(CodecOptions) */
  public void setCodecOptions(CodecOptions options) {
    //TODO writer.setCodecOptions(options);
  }

  /* @see IFormatWriter#getCompression() */
  public String getCompression() {
    return writer.getCompression();
  }

  /* @see IFormatWriter#getPixelTypes() */
  public int[] getPixelTypes() {
    return writer.getPixelTypes(getCompression());
  }

  /* @see IFormatWriter#getPixelTypes(String) */
  public int[] getPixelTypes(String codec) {
    return writer.getPixelTypes(codec);
  }

  /* @see IFormatWriter#isSupportedType(int) */
  public boolean isSupportedType(int type) {
    return writer.isSupportedType(type);
  }

  /* @see IFormatWriter#setWriteSequentially(boolean) */
  public void setWriteSequentially(boolean sequential) {
    writer.setWriteSequentially(sequential);
  }

  // -- Deprecated IFormatWriter API methods --

  /**
   * @deprecated
   * @see IFormatWriter#saveBytes(byte[], boolean)
   */
  public void saveBytes(byte[] bytes, boolean last)
    throws FormatException, IOException
  {
    try {
      writer.saveBytes(bytes, 0, last, last);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /**
   * @deprecated
   * @see IFormatWriter#saveBytes(byte[], int, boolean, boolean)
   */
  public void saveBytes(byte[] bytes, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    try {
      writer.saveBytes(bytes, series, lastInSeries, last);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /**
   * @deprecated
   * @see IFormatWriter#savePlane(Object, boolean)
   */
  public void savePlane(Object plane, boolean last)
    throws FormatException, IOException
  {
    try {
      writer.savePlane(plane, 0, last, last);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
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
    try {
      writer.saveBytes((byte[]) plane, series, lastInSeries, last);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  // -- IFormatHandler API methods --

  /* @see IFormatHandler#setId(String) */
  public void setId(String id) throws FormatException, IOException {
    if (id.equals(currentId)) return;
    writer.close();
    currentId = id;
    try {
      writer.setDest(id);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatHandler#close() */
  public void close() throws IOException {
    writer.close();
  }
}
