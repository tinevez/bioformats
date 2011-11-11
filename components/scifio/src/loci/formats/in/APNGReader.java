//
// APNGReader.java
//

/*
OME Bio-Formats package for reading and converting biological file formats.
Copyright (C) 2005-@year@ UW-Madison LOCI and Glencoe Software, Inc.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package loci.formats.in;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;

import ome.scifio.in.apng.APNGChecker;
import ome.scifio.in.apng.APNGParser;

import loci.common.DataTools;
import ome.scifio.io.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.gui.AWTImageTools;
import loci.formats.meta.MetadataStore;

/**
 * APNGReader is the file format reader for
 * Animated Portable Network Graphics (APNG) images.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/APNGReader.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/APNGReader.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Melissa Linkert melissa at glencoesoftware.com
 */
public class APNGReader extends BIFormatReader {

  // -- Constants --

  private static final byte[] PNG_SIGNATURE = new byte[] {
    (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
  };

  // -- Fields --

  private Vector<int[]> frameCoordinates;

  private byte[][] lut;

  private BufferedImage lastImage;
  private int lastImageIndex = -1;

  // -- Constructor --

  /** Constructs a new APNGReader. */
  public APNGReader() {
    super("Animated PNG", "png");
	checker = new APNGChecker();
	parser = new APNGParser();
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Deprecated
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return checker.isFormat(stream);
  }

  /* @see loci.formats.IFormatReader#get8BitLookupTable() */
  public byte[][] get8BitLookupTable() {
    FormatTools.assertId(currentId, true, 1);
    return lut;
  }
  
  /* @see IFormatReader#openBytes(int) */
  @Override
  public byte[] openBytes(int no) throws FormatException, IOException {
    try {
		return reader.openBytes(no);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see IFormatReader#openBytes(int, byte[]) */
  @Override
  public byte[] openBytes(int no, byte[] buf)
    throws FormatException, IOException
  {
    try {
		return reader.openBytes(no, buf);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see IFormatReader#openBytes(int, int, int, int, int) */
  @Override
  public byte[] openBytes(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    try {
		return reader.openBytes(no, x, y, w, h);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see loci.formats.IFormatReader#openPlane(int, int, int, int, int int) */
  public Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
	  try {
		return reader.openPlane(no, x, y, w, h);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e.getCause());
	}
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    //super.close(fileOnly);
    parser.close(fileOnly);
    //TODO reader.close(fileOnly);
  }

  // -- Internal FormatReader methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Deprecated
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    try {
		parser.parse(id);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e.getCause());
	}
	reader = new ome.scifio.in.apng.APNGReader((APNGParser)parser);
  }
  
  // -- Helper Methods --
  
  private long computeCRC(byte[] buf, int len) {
	  CRC32 crc = new CRC32();
	  crc.update(buf, 0, len);
	  return crc.getValue();
	  }
}
