package ome.scifio;

import java.io.File;
import java.io.IOException;

import ome.scifio.FormatException;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.FormatTools;

/**
 * Abstract superclass of all SCIFIO reader components.
 *
 */
public abstract class AbstractReader<M extends Metadata>
  extends AbstractFormatHandler implements Reader<M> {

  // -- Constants --

  /** Default thumbnail width and height. */
  protected static final int THUMBNAIL_DIMENSION = 128;

  // -- Fields --

  /** Metadata values. */

  protected M metadata;

  /** Whether or not to group multi-file formats. */
  protected boolean group = true;

  /** Whether or not to normalize float data. */
  protected boolean normalizeData;

  /** Current file. */
  protected RandomAccessInputStream in;

  /** List of domains in which this format is used. */
  protected String[] domains = new String[0];

  /** Name of current file. */
  protected String currentId;

  // -- Constructors --

  /** Constructs a reader with the given name and default suffix */
  public AbstractReader(String format, String suffix) {
    super(format, suffix);
  }

  /** Constructs a reader with the given name and default suffixes */
  public AbstractReader(String format, String[] suffixes) {
    super(format, suffixes);
  }

  // -- Reader API Methods --

  /* @see Reader#setSource(File) */
  public void setSource(File file) throws IOException {
    setSource(file.getName());
  }

  /* @see Reader#setSource(String) */
  public void setSource(String fileName) throws IOException {
    setSource(new RandomAccessInputStream(fileName));
  }

  /* @see Reader#setSource(RandomAccessInputStream) */
  public void setSource(RandomAccessInputStream stream) {
    this.in = stream;
  }

  /* @see Reader#openBytes(int, int) */
  public byte[] openBytes(final int iNo, final int no)
    throws FormatException, IOException
  {
    return openBytes(
      iNo, no, 0, 0, metadata.getSizeX(iNo), metadata.getSizeY(iNo));
  }

  /* @see Reader#openBytes(int, int, int, int, int, int) */
  public byte[] openBytes(int iNo, int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    int bpp = FormatTools.getBytesPerPixel(metadata.getPixelType(iNo));
    int ch = metadata.getRGBChannelCount(iNo);
    byte[] newBuffer = new byte[w * h * ch * bpp];
    return openBytes(iNo, no, newBuffer, x, y, w, h);
  }

  /* @see Reader#openBytes(int, int, byte[]) */
  public byte[] openBytes(int iNo, int no, byte[] buf)
    throws FormatException, IOException
  {
    return openBytes(
      iNo, no, buf, 0, 0, metadata.getSizeX(iNo), metadata.getSizeY(iNo));
  }

  /* @see Reader#openBytes(int, int, byte[], int, int, int, int) */
  public abstract byte[] openBytes(int iNo, int no, byte[] buf, int x, int y,
    int w, int h) throws FormatException, IOException;

  /* @see Reader#openPlane(int, int, int, int, int, int int) */
  public Object openPlane(int iNo, int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    // NB: Readers use byte arrays by default as the native type.
    return openBytes(iNo, no, x, y, w, h);
  }

  /* @see Reader#openThumbBytes(int) */
  public byte[] openThumbBytes(final int iNo, final int no)
    throws FormatException, IOException
  {
    FormatTools.assertStream(in, true, 1);
    /* TODO move FormatTools implementation here 
    return FormatTools.openThumbBytes(this, no); */
    return null;
  }

  /* @see Reader#setGroupFiles(boolean) */
  public void setGroupFiles(final boolean groupFiles) {
    group = groupFiles;
  }

  /* @see Reader#isGroupFiles() */
  public boolean isGroupFiles() {
    FormatTools.assertStream(in, false, 1);
    return group;
  }

  /* @see Reader#fileGroupOption(String) */
  public int fileGroupOption(final String id)
    throws FormatException, IOException
  {
    return FormatTools.CANNOT_GROUP;
  }

  /* @see Reader#setMetadata(M) */
  public void setMetadata(M meta) {
    this.metadata = meta;
  }

  /* @see Reader#getMetadata() */
  public M getMetadata() {
    return this.metadata;
  }

  /* @see Reader#getCurrentFile() */
  public String getCurrentFile() {

    FormatTools.assertStream(in, true, 1);
    return in.getFileName();
  }

  /* @see Reader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    if (in != null) in.close();
    if (!fileOnly) {
      in = null;
    }
  }

  /* @see Reader#close() */
  public void close() throws IOException {
    close(false);
  }

  /* @see Reader#isNormalized() */
  public boolean isNormalized() {
    return normalizeData;
  }

  /* @see Reader#setNormalized(boolean) */
  public void setNormalized(final boolean normalize) {
    normalizeData = normalize;
  }

  /* @see Reader#getOptimalTileWidth(int) */
  public int getOptimalTileWidth(int no) {
    return metadata.getSizeX(no);
  }

  /* @see Reader#getOptimalTileHeight(int) */
  public int getOptimalTileHeight(int no) {
    int bpp = FormatTools.getBytesPerPixel(metadata.getPixelType(no));
    int maxHeight =
      (1024 * 1024) /
        (metadata.getSizeX(no) * metadata.getRGBChannelCount(no) * bpp);
    return Math.min(maxHeight, metadata.getSizeY(no));
  }

  /* @see Reader#getDomains() */
  public String[] getDomains() {
    return domains;
  }

  @Override
  public int[] getZCTCoords(final int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RandomAccessInputStream getStream() {
    return in;
  }

  @Override
  public Reader<Metadata>[] getUnderlyingReaders() {
    // TODO Auto-generated method stub
    return null;
  }

  // -- AbstractReader Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? 
   * should this even be in reader at all? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
  }

  public byte[] readPlane(RandomAccessInputStream s, int no, int x, int y,
    int w, int h, byte[] buf) throws IOException
  {
    return readPlane(s, no, x, y, w, h, 0, buf);
  }

  public byte[] readPlane(RandomAccessInputStream s, int no, int x, int y,
    int w, int h, int scanlinePad, byte[] buf) throws IOException
  {
    int c = metadata.getRGBChannelCount(no);
    int bpp = FormatTools.getBytesPerPixel(metadata.getPixelType(no));
    if (x == 0 && y == 0 && w == metadata.getSizeX(no) &&
      h == metadata.getSizeY(no) && scanlinePad == 0)
    {
      s.read(buf);
    }
    else if (x == 0 && w == metadata.getSizeX(no) && scanlinePad == 0) {
      if (metadata.isInterleaved(no)) {
        s.skipBytes(y * w * bpp * c);
        s.read(buf, 0, h * w * bpp * c);
      }
      else {
        int rowLen = w * bpp;
        for (int channel = 0; channel < c; channel++) {
          s.skipBytes(y * rowLen);
          s.read(buf, channel * h * rowLen, h * rowLen);
          if (channel < c - 1) {
            // no need to skip bytes after reading final channel
            s.skipBytes((metadata.getSizeY(no) - y - h) * rowLen);
          }
        }
      }
    }
    else {
      int scanlineWidth = metadata.getSizeX(no) + scanlinePad;
      if (metadata.isInterleaved(no)) {
        s.skipBytes(y * scanlineWidth * bpp * c);
        for (int row = 0; row < h; row++) {
          s.skipBytes(x * bpp * c);
          s.read(buf, row * w * bpp * c, w * bpp * c);
          if (row < h - 1) {
            // no need to skip bytes after reading final row
            s.skipBytes(bpp * c * (scanlineWidth - w - x));
          }
        }
      }
      else {
        for (int channel = 0; channel < c; channel++) {
          s.skipBytes(y * scanlineWidth * bpp);
          for (int row = 0; row < h; row++) {
            s.skipBytes(x * bpp);
            s.read(buf, channel * w * h * bpp + row * w * bpp, w * bpp);
            if (row < h - 1 || channel < c - 1) {
              // no need to skip bytes after reading final row of final channel
              s.skipBytes(bpp * (scanlineWidth - w - x));
            }
          }
          if (channel < c - 1) {
            // no need to skip bytes after reading final channel
            s.skipBytes(scanlineWidth * bpp * (metadata.getSizeY(no) - y - h));
          }
        }
      }
    }
    return buf;
  }

}
