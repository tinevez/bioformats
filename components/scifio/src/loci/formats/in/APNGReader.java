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

import java.io.IOException;
import ome.scifio.in.apng.APNGChecker;
import ome.scifio.in.apng.APNGMetadata;
import ome.scifio.in.apng.APNGParser;

import ome.scifio.io.RandomAccessInputStream;
import loci.formats.FormatException;


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

  // -- Fields --

  // -- Constructor --

  /** Constructs a new APNGReader. */
  public APNGReader() {
    super("Animated PNG", "png");
    checker = new APNGChecker();
    parser = new APNGParser();
	  reader = new ome.scifio.in.apng.APNGReader();
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Deprecated
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return checker.isFormat(stream);
  }

  /* @see loci.formats.IFormatReader#get8BitLookupTable() */
  @Deprecated
  public byte[][] get8BitLookupTable() {
    try {
      return reader.getMetadata().get8BitLookupTable(0);
    }
    catch (ome.scifio.FormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  /* @see IFormatReader#openBytes(int) */
  @Override
  @Deprecated
  public byte[] openBytes(int no) throws FormatException, IOException {
    try {
		return reader.openBytes(this.getSeries(), no);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see IFormatReader#openBytes(int, byte[]) */
  @Override
  @Deprecated
  public byte[] openBytes(int no, byte[] buf)
    throws FormatException, IOException
  {
    try {
		return reader.openBytes(this.getSeries(), no, buf);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see IFormatReader#openBytes(int, int, int, int, int) */
  @Override
  @Deprecated
  public byte[] openBytes(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    try {
		return reader.openBytes(this.getSeries(), no, x, y, w, h);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e);
	}
  }

  /* @see loci.formats.IFormatReader#openPlane(int, int, int, int, int int) */
  @Deprecated
  public Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
	  try {
		return reader.openPlane(this.getSeries(), no, x, y, w, h);
	} catch (ome.scifio.FormatException e) {
		throw new FormatException(e.getCause());
	}
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    parser.close(fileOnly);
    reader.close(fileOnly);
  }

  // -- Internal FormatReader methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Deprecated
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    APNGMetadata meta = null;
    try {
      meta = (APNGMetadata) parser.parse(id);
    } catch (ome.scifio.FormatException e) {
      throw new FormatException(e.getCause());
    }
    reader.setSource(id);
    ((ome.scifio.in.apng.APNGReader)reader).setMetadata(meta);
  }
}
