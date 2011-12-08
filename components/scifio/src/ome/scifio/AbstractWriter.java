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
  }

  /** Constructs a writer with the given name and default suffixes */
  public AbstractWriter(String format, String[] suffixes) {
    super(format, suffixes);
  }

  // -- Writer API Methods --

  /* @see ome.scifio.Writer#setMetadata(M) */
  public void setMetadata(M meta) {
    this.metadata = meta;
  }

  /* @see ome.scifio.Writer#getMetadata() */
  public M getMetadata() {
    return this.metadata;
  }

  /* @see ome.scifio.Writer#setStream(File) */
  public void setDest(File file) throws FormatException, IOException {
    setDest(file.getName(), 0);
  }

  /* @see ome.scifio.Writer#setStream(String) */
  public void setDest(String fileName) throws FormatException, IOException {
    setDest(new RandomAccessOutputStream(fileName), 0);
  }

  /* @see ome.scifio.Writer#setStream(RandomAccessOutputStream) */
  public void setDest(RandomAccessOutputStream out)
    throws FormatException, IOException
  {
    setDest(out, 0);
  }

  /* @see ome.scifio.Writer#setStream(File, int) */

  public void setDest(File file, int iNo) throws FormatException, IOException {
    setDest(file.getName());
  }

  /* @see ome.scifio.Writer#setStream(String, int) */
  public void setDest(String fileName, int iNo)
    throws FormatException, IOException
  {
    setDest(new RandomAccessOutputStream(fileName));
  }

  /* @see ome.scifio.Writer#setStream(RandomAccessOutputStream, int) */
  public void setDest(RandomAccessOutputStream out, int iNo)
    throws FormatException, IOException
  {
    this.close();
    this.out = out;
    initialize(iNo);
  }

  /* @see ome.scifio.Writer#getStream() */
  public RandomAccessOutputStream getStream() {
    return this.out;
  }

  /* @see ome.scifio.Writer#saveBytes(int, int, byte[]) */
  public void saveBytes(int iNo, int no, byte[] buf)
    throws FormatException, IOException
  {
    int width = metadata.getSizeX(iNo);
    int height = metadata.getSizeY(iNo);
    saveBytes(iNo, no, buf, 0, 0, width, height);
  }

  /* @see ome.scifio.Writer#savePlane(int, int, Object) */
  public void savePlane(int iNo, int no, Object plane)
    throws FormatException, IOException
  {
    int width = metadata.getSizeX(iNo);
    int height = metadata.getSizeY(iNo);
    savePlane(iNo, no, plane, 0, 0, width, height);
  }

  /* @see ome.scifio.Writer#savePlane(int, int, Object, int, int, int, int) */
  public void savePlane(int iNo, int no, Object plane, int x, int y, int w,
    int h) throws FormatException, IOException
  {
    // NB: Writer use byte arrays by default as the native type.
    if (!(plane instanceof byte[])) {
      throw new IllegalArgumentException("Object to save must be a byte[]");
    }
    saveBytes(iNo, no, (byte[]) plane, x, y, w, h);
  }

  /* @see ome.scifio.Writer#canDoStacks() */
  public boolean canDoStacks() {
    return false;
  }

  /* @see ome.scifio.Writer#setColorModel(ColorModel) */
  public void setColorModel(ColorModel cm) {
    this.cm = cm;
  }

  /* @see ome.scifio.Writer#getColorModel() */
  public ColorModel getColorModel() {
    return cm;
  }

  /* @see ome.scifio.Writer#setFramesPerSecond(int) */
  public void setFramesPerSecond(int rate) {
    this.fps = rate;
  }

  /* @see ome.scifio.Writer#getFramesPerSecond() */
  public int getFramesPerSecond() {
    return fps;
  }

  /* @see ome.scifio.Writer#getCompressionTypes() */
  public String[] getCompressionTypes() {
    return compressionTypes;
  }

  /* @see ome.scifio.Writer#getPixelTypes() */
  public int[] getPixelTypes() {
    return getPixelTypes(getCompression());
  }

  /* @see ome.scifio.Writer#getPixelTypes(String) */
  public int[] getPixelTypes(String codec) {
    return new int[] {
        FormatTools.INT8, FormatTools.UINT8, FormatTools.INT16,
        FormatTools.UINT16, FormatTools.INT32, FormatTools.UINT32,
        FormatTools.FLOAT};
  }

  /* @see ome.scifio.Writer#isSupportedType(int) */
  public boolean isSupportedType(int type) {
    int[] types = getPixelTypes();
    for (int i = 0; i < types.length; i++) {
      if (type == types[i]) return true;
    }
    return false;
  }

  /* @see ome.scifio.Writer#setCompression(String) */
  public void setCompression(String compress) throws FormatException {
    for (int i=0; i<compressionTypes.length; i++) {
      if (compressionTypes[i].equals(compress)) {
        compression = compress;
        return;
      }
    }
    throw new FormatException("Invalid compression type: " + compress);
  }

  /* @see ome.scifio.Writer#setCodecOptions(CodecOptions) */
  public void setCodecOptions(CodecOptions options) {
    this.options = options;
  }

  /* @see ome.scifio.Writer#getCompression() */
  public String getCompression() {
    return compression;
  }

  /* @see ome.scifio.Writer#changeOutputFile(String) */
  public void changeOutputFile(String id) throws FormatException, IOException {
    setDest(id);
  }

  /* @see ome.scifio.Writer#setWriterSequentially(boolean) */
  public void setWriteSequentially(boolean sequential) {
    this.sequential = sequential;
  }

  /* @see ome.scifio.Writer#close() */
  public void close() throws IOException {
    if (out != null) out.close();
    out = null;
    initialized = null;
  }

  // -- Deprecated Writer API Methods --

  /**
   * @deprecated
   * @see ome.scifio.Writer#saveBytes(byte[], boolean)
   */
  public void saveBytes(byte[] bytes, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub

  }

  /**
   * @deprecated
   * @see ome.scifio.Writer#saveBytes(byte[], int, boolean, boolean)
   */
  public void saveBytes(byte[] bytes, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // TODO Auto-generated method stub

  }

  /**
   * @deprecated
   * @see ome.scifio.Writer#savePlane(Object, boolean)
   */
  public void savePlane(Object plane, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub

  }

  /**
   * @deprecated
   * @see ome.scifio.Writer#savePlane(Object, int, boolean, boolean)
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

  /** Sets up the initialized array and ensures this Writer is ready for writing */
  public void initialize(int iNo) throws FormatException, IOException {
    MetadataTools.verifyMinimumPopulated(metadata, out);
    initialized = new boolean[metadata.getImageCount()][];
    for (int i = 0; i < metadata.getImageCount(); i++) {
      initialized[i] = new boolean[getPlaneCount(i)];
    }
  }

  /** Retrieve the total number of planes in the current series. */
  protected int getPlaneCount(int iNo) {
    int z = metadata.getSizeZ(iNo);
    int t = metadata.getSizeT(iNo);
    int c = metadata.getEffectiveSizeC(iNo);
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
  protected void checkParams(int iNo, int no, byte[] buf, int x, int y, int w,
    int h) throws FormatException
  {
    MetadataTools.verifyMinimumPopulated(metadata, out, iNo, no);

    if (buf == null) throw new FormatException("Buffer cannot be null.");
    int z = metadata.getSizeZ(iNo);
    int t = metadata.getSizeT(iNo);
    int c = metadata.getSizeC(iNo);
    int planes = z * c * t;

    if (no < 0)
      throw new FormatException(
        String.format("Plane index:%d must be >= 0", no));
    if (no >= planes) {
      throw new FormatException(String.format(
        "Plane index:%d must be < %d", no, planes));
    }

    int sizeX = metadata.getSizeX(iNo);
    int sizeY = metadata.getSizeY(iNo);
    if (x < 0)
      throw new FormatException(String.format("X:%d must be >= 0", x));
    if (y < 0)
      throw new FormatException(String.format("Y:%d must be >= 0", y));
    if (x >= sizeX) {
      throw new FormatException(String.format("X:%d must be < %d", x, sizeX));
    }
    if (y >= sizeY) {
      throw new FormatException(String.format("Y:%d must be < %d", y, sizeY));
    }
    if (w <= 0)
      throw new FormatException(String.format("Width:%d must be > 0", w));
    if (h <= 0)
      throw new FormatException(String.format("Height:%d must be > 0", h));
    if (x + w > sizeX)
      throw new FormatException(String.format(
        "(w:%d + x:%d) must be <= %d", w, x, sizeX));
    if (y + h > sizeY)
      throw new FormatException(String.format(
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
