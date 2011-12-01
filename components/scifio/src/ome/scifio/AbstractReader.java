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
  
  /* @see Reader#openBytes(int, int) */
  public byte[] openBytes(final int iNo, final int no) throws FormatException, IOException {
    return openBytes(iNo, no, 0, 0, metadata.getSizeX(iNo), metadata.getSizeY(iNo));
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
    return openBytes(iNo, no, buf, 0, 0, metadata.getSizeX(iNo), metadata.getSizeY(iNo));
  }

  /* @see Reader#openBytes(int, int, byte[], int, int, int, int) */
  public abstract byte[] openBytes(int iNo, int no, byte[] buf, int x, int y, int w,
    int h) throws FormatException, IOException;
  
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

  @Override
  public String[] getDomains() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getIndex(final int z, final int c, final int t) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int[] getZCTCoords(final int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setStream(final RandomAccessInputStream stream) {
    this.in = stream;
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

  @Override
  public int getOptimalTileWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getOptimalTileHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setNormalized(final boolean normalize) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isNormalized() {
    // TODO Auto-generated method stub
    return false;
  }

  // -- AbstractReader Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? 
   * should this even be in reader at all? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
  }


  protected void setIn(RandomAccessInputStream stream) {
    this.in = stream;
  }

  /**
   * Sets the source for this reader to read from.
   * @param file
   * @throws IOException 
   */
  public void setSource(File file) throws IOException {
    setSource(file.getName());
  }

  /**
   * Sets the source for this reader to read from.
   * @param fileName
   * @throws IOException 
   */
  public void setSource(String fileName) throws IOException {
    setSource(new RandomAccessInputStream(fileName));
  }

  /**
   * Sets the source for this reader to read from.
   * @param in
   */
  public void setSource(RandomAccessInputStream stream) {
    this.in = stream;
  }
}
