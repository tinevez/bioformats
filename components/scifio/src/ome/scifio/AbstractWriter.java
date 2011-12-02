package ome.scifio;

import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import ome.scifio.codec.CodecOptions;
import ome.scifio.common.DataTools;
import ome.scifio.io.RandomAccessOutputStream;
import ome.scifio.util.FormatTools;
import ome.scifio.util.MetadataTools;
import ome.xml.model.primitives.PositiveInteger;

/**
 * Abstract superclass of all SCIFIO Writer components.
 *
 */
public abstract class AbstractWriter<M extends Metadata>
  extends AbstractFormatHandler implements Writer<M> {

  // -- Fields --
  
  /** Metadata values. */
  protected M metadata;
  
  /** Frame rate to use when writing in frames per second, if applicable. */
  protected int fps = 10;

  /** Default color model. */
  protected ColorModel cm;

  /** Available compression types. */
  protected String[] compressionTypes;

  /** Current compression type. */
  protected String compression;

  /** The options if required. */
  protected CodecOptions options;

  /**
   * Whether each plane in each image of the current file has been
   * prepped for writing.
   */
  protected boolean[][] initialized;

  /** Whether the channels in an RGB image are interleaved. */
  protected boolean interleaved;

  /** The number of valid bits per pixel. */
  protected int validBits;

  /** Whether or not we are writing planes sequentially. */
  protected boolean sequential;
  
  /** Current file. */
  protected RandomAccessOutputStream out;
  
  // -- Constructors --

  /** Constructs a writer with the given name and default suffix */
  public AbstractWriter(String format, String suffix) {
    super(format, suffix);
    // TODO Auto-generated constructor stub
  }

  /** Constructs a writer with the given name and default suffixes */
  public AbstractWriter(String format, String[] suffixes) {
    super(format, suffixes);
    // TODO Auto-generated constructor stub
  }

  // -- Writer API Methods --
  /* @see Writer#setMetadata(M) */
  public void setMetadata(M meta) {
    this.metadata = meta;
  }

  /* @see Writer#getMetadata() */
  public M getMetadata() {
    return this.metadata;
  }
  
  /**
   * Sets the source for this reader to read from.
   * @param file
   * @throws IOException 
   */
  public void setDest(File file) throws IOException {
    setDest(file.getName());
  }

  /**
   * Sets the source for this reader to read from.
   * @param fileName
   * @throws IOException 
   */
  public void setDest(String fileName) throws IOException {
    setDest(new RandomAccessOutputStream(fileName));
  }
  
  /* @see Writer#setStream(RandomAccessOutputStream) */
  public void setDest(RandomAccessOutputStream out) {
    this.out = out;
  }
  
  /* @see Writer#getStream() */
  public RandomAccessOutputStream getStream() {
    return this.out;
  }
  
  /* @see Writer#saveBytes(int, int, byte[]) */
  public void saveBytes(int iNo, int no, byte[] buf) throws FormatException, IOException
  {
    int width = metadata.getSizeX(iNo);
    int height = metadata.getSizeY(iNo);
    saveBytes(iNo, no, buf, 0, 0, width, height);
  }

  public void savePlane(int iNo, int no, Object plane)
    throws FormatException, IOException
  {
    int width = metadata.getSizeX(iNo);
    int height = metadata.getSizeY(iNo);
    savePlane(iNo, no, plane, 0, 0, width, height);    
  }

  public void savePlane(int iNo, int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    // NB: Writers use byte arrays by default as the native type.
    if (!(plane instanceof byte[])) {
      throw new IllegalArgumentException("Object to save must be a byte[]");
    }
    saveBytes(iNo, no, (byte[]) plane, x, y, w, h);    
  }

  public void setValidBitsPerPixel(int bits) {
    // TODO Auto-generated method stub
    
  }

  public boolean canDoStacks() {
    // TODO Auto-generated method stub
    return false;
  }

  public void setColorModel(ColorModel cm) {
    // TODO Auto-generated method stub
    
  }

  public ColorModel getColorModel() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setFramesPerSecond(int rate) {
    // TODO Auto-generated method stub
    
  }

  public int getFramesPerSecond() {
    // TODO Auto-generated method stub
    return 0;
  }

  public String[] getCompressionTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  public int[] getPixelTypes() {
    return getPixelTypes(getCompression());
  }

  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.INT8, FormatTools.UINT8, FormatTools.INT16,
      FormatTools.UINT16, FormatTools.INT32, FormatTools.UINT32,
      FormatTools.FLOAT};
  }

  public boolean isSupportedType(int type) {
    // TODO Auto-generated method stub
    return false;
  }

  public void setCompression(String compress) throws FormatException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setCodecOptions(CodecOptions options) {
    // TODO Auto-generated method stub
    
  }

  public String getCompression() {
    return compression;
  }

  public void changeOutputFile(String id) throws FormatException, IOException {
    // TODO Auto-generated method stub
    
  }

  public void setWriteSequentially(boolean sequential) {
    // TODO Auto-generated method stub
    
  }

  // -- Deprecated Writer API Methods --
  
  /**
   * @deprecated
   * @see Writer#saveBytes(byte[], boolean)
   */
  public void saveBytes(byte[] bytes, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  /**
   * @deprecated
   * @see Writer#saveBytes(byte[], int, boolean, boolean)
   */
  public void saveBytes(byte[] bytes, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  /**
   * @deprecated
   * @see Writer#savePlane(Object, boolean)
   */
  public void savePlane(Object plane, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }
  
  /**
   * @deprecated
   * @see Writer#savePlane(Object, int, boolean, boolean)
   */
  public void savePlane(Object plane, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }
  
  // -- AbstractWriter Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
  }
  
  // -- Helper methods --

  /* @see IFormatHandler#setId(String) */
  public void initialize(int iNo) throws FormatException, IOException {
    MetadataTools.verifyMinimumPopulated(metadata, out, iNo);
    initialized = new boolean[metadata.getImageCount()][];
    for (int i=0; i<metadata.getImageCount(); i++) {
      initialized[i] = new boolean[getPlaneCount(iNo)];
    }
  }

  /* @see IFormatHandler#close() */
  public void close() throws IOException {
    if (out != null) out.close();
    out = null;
    initialized = null;
  }

  /** Retrieve the total number of planes in the current series. */
  protected int getPlaneCount(int iNo) {
    int z = metadata.getSizeZ(iNo);
    int t = metadata.getSizeT(iNo);
    int c = metadata.getSizeC(iNo);
    c /= metadata.getBitsPerPixel(iNo);
    return z * c * t;
  }
  
  /**
   * Returns true if the given rectangle coordinates correspond to a full
   * image in the given series.
   */
  protected boolean isFullPlane(int iNo, int x, int y, int w, int h) {
    int sizeX = metadata.getSizeX(iNo);
    int sizeY = metadata.getSizeY(iNo);
    return x == 0 && y == 0 && w == sizeX && h == sizeY;
  }
  
  /**
   * Ensure that the arguments that are being passed to saveBytes(...) are
   * valid.
   * @throws FormatException if any of the arguments is invalid.
   */
  protected void checkParams(int iNo, int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException
  {
    MetadataTools.verifyMinimumPopulated(metadata, out, iNo, no);

    if (buf == null) throw new FormatException("Buffer cannot be null.");
    int z = metadata.getSizeZ(iNo);
    int t = metadata.getSizeT(iNo);
    int c = metadata.getSizeC(iNo);
    int planes = z * c * t;

    if (no < 0) throw new FormatException(String.format(
        "Plane index:%d must be >= 0", no));
    if (no >= planes) {
      throw new FormatException(String.format(
          "Plane index:%d must be < %d", no, planes));
    }

    int sizeX = metadata.getSizeX(iNo);
    int sizeY = metadata.getSizeY(iNo);
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

    int pixelType = metadata.getPixelType(iNo);
    int bpp = FormatTools.getBytesPerPixel(pixelType);
    PositiveInteger samples = new PositiveInteger(bpp);
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
  
  
}
