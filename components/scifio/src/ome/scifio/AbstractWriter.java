package ome.scifio;

import java.awt.image.ColorModel;

import ome.scifio.codec.CodecOptions;
import ome.scifio.common.DataTools;
import ome.scifio.util.FormatTools;
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
   * Whether each plane in each series of the current file has been
   * prepped for writing.
   */
  protected boolean[][] initialized;

  /** Whether the channels in an RGB image are interleaved. */
  protected boolean interleaved;

  /** The number of valid bits per pixel. */
  protected int validBits;

  /** Whether or not we are writing planes sequentially. */
  protected boolean sequential;
  
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
  
  // -- AbstractWriter Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
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
    MetadataTools.verifyMinimumPopulated(r, no);

    if (buf == null) throw new FormatException("Buffer cannot be null.");
    int z = r.getPixelsSizeZ(no).getValue().intValue();
    int t = r.getPixelsSizeT(no).getValue().intValue();
    int c = r.getChannelCount(no);
    int planes = z * c * t;

    if (no < 0) throw new FormatException(String.format(
        "Plane index:%d must be >= 0", no));
    if (no >= planes) {
      throw new FormatException(String.format(
          "Plane index:%d must be < %d", no, planes));
    }

    int sizeX = r.getPixelsSizeX(no).getValue().intValue();
    int sizeY = r.getPixelsSizeY(no).getValue().intValue();
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
      FormatTools.pixelTypeFromString(r.getPixelsType(no).toString());
    int bpp = FormatTools.getBytesPerPixel(pixelType);
    PositiveInteger samples = r.getChannelSamplesPerPixel(no, 0);
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
