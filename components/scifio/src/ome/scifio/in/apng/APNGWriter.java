package ome.scifio.in.apng;

import java.awt.image.ColorModel;
import java.io.IOException;

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
  }

  // -- APNGWriter Methods --
}
