package ome.scifio.in.apng;

import java.awt.image.ColorModel;
import java.io.IOException;

import loci.formats.codec.CodecOptions;

import ome.scifio.AbstractWriter;
import ome.scifio.FormatException;

public class APNGWriter extends AbstractWriter<APNGMetadata> {

  // -- Constants --

  private static final byte[] PNG_SIG = new byte[] {
    (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
  };

  // -- Fields --

  private int numFrames = 0;
  private long numFramesPointer = 0;
  private int nextSequenceNumber;
  private boolean littleEndian;
  
  // -- Constructor --
  
  public APNGWriter() {
    super("Animated PNG", "png");
  }

  // -- Writer API Methods --
  
  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    /* TODO
    checkParams(no, buf, x, y, w, h);
    if (!isFullPlane(x, y, w, h)) {
      throw new FormatException(
        "APNGWriter does not yet support saving image tiles.");
    }
    MetadataRetrieve meta = getMetadataRetrieve();

    int width = meta.getPixelsSizeX(series).getValue().intValue();
    int height = meta.getPixelsSizeY(series).getValue().intValue();

    if (!initialized[series][no]) {
      writeFCTL(width, height);
      if (numFrames == 0) writePLTE();
      initialized[series][no] = true;
    }

    writePixels(numFrames == 0 ? "IDAT" : "fdAT", buf, x, y, w, h);
    numFrames++;
    */
  }

  @Override
  public void saveBytes(int no, byte[] buf) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void savePlane(int no, Object plane)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void savePlane(int no, Object plane, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setSeries(int series) throws FormatException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getSeries() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setValidBitsPerPixel(int bits) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean canDoStacks() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setColorModel(ColorModel cm) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public ColorModel getColorModel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setFramesPerSecond(int rate) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getFramesPerSecond() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String[] getCompressionTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getPixelTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getPixelTypes(String codec) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSupportedType(int type) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setCompression(String compress) throws FormatException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setCodecOptions(CodecOptions options) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getCompression() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void changeOutputFile(String id) throws FormatException, IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setWriteSequentially(boolean sequential) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void saveBytes(byte[] bytes, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void saveBytes(byte[] bytes, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void savePlane(Object plane, boolean last)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void savePlane(Object plane, int series, boolean lastInSeries,
    boolean last) throws FormatException, IOException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Class<APNGMetadata> getMetadataType() {
    // TODO Auto-generated method stub
    return null;
  }

  // -- APNGWriter Methods --
}
