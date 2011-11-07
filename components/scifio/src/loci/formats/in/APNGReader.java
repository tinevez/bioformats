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
	reader = new ome.scifio.in.apng.APNGReader((APNGParser)parser);
	
	// TODO
    domains = new String[] {FormatTools.GRAPHICS_DOMAIN};
    suffixNecessary = false;
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

  /* @see loci.formats.IFormatReader#openPlane(int, int, int, int, int int) */
  public Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, -1, x, y, w, h);

    if (no == lastImageIndex && lastImage != null) {
      return AWTImageTools.getSubimage(lastImage, isLittleEndian(), x, y, w, h);
    }

    if (no == 0) {
      in.seek(0);
      DataInputStream dis =
        new DataInputStream(new BufferedInputStream(in, 4096));
      lastImage = ImageIO.read(dis);
      lastImageIndex = 0;
      if (x == 0 && y == 0 && w == getSizeX() && h == getSizeY()) {
        return lastImage;
      }
      return AWTImageTools.getSubimage(lastImage, isLittleEndian(), x, y, w, h);
    }

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    stream.write(PNG_SIGNATURE);

    boolean fdatValid = false;
    int fctlCount = 0;

    int[] coords = frameCoordinates.get(no);

    // TODO lost computeCRC method?
    /*
    for (PNGBlock block : blocks) {
      if (!block.type.equals("IDAT") && !block.type.equals("fdAT") &&
        !block.type.equals("acTL") && !block.type.equals("fcTL") &&
        block.length > 0)
      {
        byte[] b = new byte[block.length + 12];
        DataTools.unpackBytes(block.length, b, 0, 4, isLittleEndian());
        byte[] typeBytes = block.type.getBytes();
        System.arraycopy(typeBytes, 0, b, 4, 4);
        in.seek(block.offset);
        in.read(b, 8, b.length - 12);
        if (block.type.equals("IHDR")) {
          DataTools.unpackBytes(coords[2], b, 8, 4, isLittleEndian());
          DataTools.unpackBytes(coords[3], b, 12, 4, isLittleEndian());
        }
        int crc = (int) computeCRC(b, b.length - 4);
        DataTools.unpackBytes(crc, b, b.length - 4, 4, isLittleEndian());
        stream.write(b);
        b = null;
      }
      else if (block.type.equals("fcTL")) {
        fdatValid = fctlCount == no;
        fctlCount++;
      }
      else if (block.type.equals("fdAT")) {
        in.seek(block.offset + 4);
        if (fdatValid) {
          byte[] b = new byte[block.length + 8];
          DataTools.unpackBytes(block.length - 4, b, 0, 4, isLittleEndian());
          b[4] = 'I';
          b[5] = 'D';
          b[6] = 'A';
          b[7] = 'T';
          in.read(b, 8, b.length - 12);
          int crc = (int) computeCRC(b, b.length - 4);
          DataTools.unpackBytes(crc, b, b.length - 4, 4, isLittleEndian());
          stream.write(b);
          b = null;
        }
      }
      
    }

    RandomAccessInputStream s =
      new RandomAccessInputStream(stream.toByteArray());
    DataInputStream dis = new DataInputStream(new BufferedInputStream(s, 4096));
    BufferedImage b = ImageIO.read(dis);
    dis.close();

    lastImage = null;
    openPlane(0, 0, 0, getSizeX(), getSizeY());
	
    // paste current image onto first image

    WritableRaster firstRaster = lastImage.getRaster();
    WritableRaster currentRaster = b.getRaster();
	
    firstRaster.setDataElements(coords[0], coords[1], currentRaster);
    lastImage =
      new BufferedImage(lastImage.getColorModel(), firstRaster, false, null);
    lastImageIndex = no;
    return lastImage;
    */ 
    return null;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      lut = null;
      frameCoordinates = null;
      //blocks = null;
      lastImage = null;
      lastImageIndex = -1;
    }
  }

  // -- Internal FormatReader methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Deprecated
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
  }
  
  // -- Helper Methods --
  
  private long computeCRC(byte[] buf, int len) {
	  CRC32 crc = new CRC32();
	  crc.update(buf, 0, len);
	  return crc.getValue();
	  }
}
