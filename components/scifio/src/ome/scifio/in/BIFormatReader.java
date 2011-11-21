package ome.scifio.in;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ome.scifio.AbstractReader;
import ome.scifio.FormatException;
import ome.scifio.Metadata;
import ome.scifio.common.DataTools;
import ome.scifio.util.AWTImageTools;
import ome.scifio.util.FormatTools;

public abstract class BIFormatReader<M extends Metadata>
  extends AbstractReader<M> {
  // -- Constructors --

  /** Constructs a new BIFormatReader. */
  public BIFormatReader(String name, String suffix) {
    super(name, suffix);
  }

  /** Constructs a new BIFormatReader. */
  public BIFormatReader(String name, String[] suffixes) {
    super(name, suffixes);
  }

  // -- IFormatReader API methods --

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  public byte[] openBytes(int iNo, int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    BufferedImage data = (BufferedImage) openPlane(iNo, no, x, y, w, h);
    switch (data.getColorModel().getComponentSize(0)) {
      case 8:
        byte[] t = AWTImageTools.getBytes(data, false);
        System.arraycopy(t, 0, buf, 0, Math.min(t.length, buf.length));
        break;
      case 16:
        short[][] ts = AWTImageTools.getShorts(data);
        for (int c = 0; c < ts.length; c++) {
          int offset = c * ts[c].length * 2;
          for (int i = 0; i < ts[c].length && offset < buf.length; i++) {
            DataTools.unpackBytes(ts[c][i], buf, offset, 2, metadata.isLittleEndian(no));
            offset += 2;
          }
        }
        break;
    }
    return buf;
  }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#getNativeDataType() */
  public Class<?> getNativeDataType() {
    return BufferedImage.class;
  }

}
