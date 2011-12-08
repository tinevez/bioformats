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
  @Override
  public void changeOutputFile(String id) throws FormatException, IOException {
    try {
      writer.changeOutputFile(id);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see IFormatWriter#saveBytes(int, byte[]) */
  @Override
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
  @Override
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
  @Override
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
  @Override
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
  @Override
  public int getSeries() {
    return series;
  }

  /* @see IFormatWriter#setInterleaved(boolean) */
  @Override
  public void setInterleaved(boolean interleaved) {
    //TODO need setters
  }

  /* @see IFormatWriter#isInterleaved() */
  @Override
  public boolean isInterleaved() {
    return writer.getMetadata().isInterleaved(getSeries());
  }

  /* @see IFormatWriter#setValidBitsPerPixel(int) */
  @Override
  public void setValidBitsPerPixel(int bits) {
    //TODO need setters
  }

  /* @see IFormatWriter#canDoStacks() */
  @Override
  public boolean canDoStacks() { return false; }

  /* @see IFormatWriter#setMetadataRetrieve(MetadataRetrieve) */
  @Override
  public void setMetadataRetrieve(MetadataRetrieve retrieve) {
    FormatTools.assertId(currentId, false, 1);
    if (retrieve == null) {
      throw new IllegalArgumentException("Metadata object is null");
    }
    metadataRetrieve = retrieve;
    writer.setMetadata(getMetadataRetrieve().getSCIFIOMeta());
  }

  /* @see IFormatWriter#getMetadataRetrieve() */
  @Override
  public MetadataRetrieve getMetadataRetrieve() {
    return metadataRetrieve;
  }

  /* @see IFormatWriter#setColorModel(ColorModel) */
  @Override
  public void setColorModel(ColorModel model) { writer.setColorModel(model); }

  /* @see IFormatWriter#getColorModel() */
  @Override
  public ColorModel getColorModel() { return writer.getColorModel(); }

  /* @see IFormatWriter#setFramesPerSecond(int) */
  @Override
  public void setFramesPerSecond(int rate) { writer.setFramesPerSecond(rate); }

  /* @see IFormatWriter#getFramesPerSecond() */
  @Override
  public int getFramesPerSecond() { return writer.getFramesPerSecond(); }

  /* @see IFormatWriter#getCompressionTypes() */
  @Override
  public String[] getCompressionTypes() { return writer.getCompressionTypes(); }

  /* @see IFormatWriter#setCompression(compress) */
  @Override
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
  @Override
  public void setCodecOptions(CodecOptions options) {
    //TODO writer.setCodecOptions(options);
  }

  /* @see IFormatWriter#getCompression() */
  @Override
  public String getCompression() {
    return writer.getCompression();
  }

  /* @see IFormatWriter#getPixelTypes() */
  @Override
  public int[] getPixelTypes() {
    return writer.getPixelTypes(getCompression());
  }

  /* @see IFormatWriter#getPixelTypes(String) */
  @Override
  public int[] getPixelTypes(String codec) {
    return writer.getPixelTypes(codec);
  }

  /* @see IFormatWriter#isSupportedType(int) */
  @Override
  public boolean isSupportedType(int type) {
    return writer.isSupportedType(type);
  }

  /* @see IFormatWriter#setWriteSequentially(boolean) */
  @Override
  public void setWriteSequentially(boolean sequential) {
    writer.setWriteSequentially(sequential);
  }

  // -- Deprecated IFormatWriter API methods --

  /**
   * @deprecated
   * @Override
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
   * @Override
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
   * @Override
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
   * @Override
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
  @Override
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
  @Override
  public void close() throws IOException {
    writer.close();
  }
}
