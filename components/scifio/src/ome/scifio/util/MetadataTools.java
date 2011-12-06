package ome.scifio.util;

import ome.scifio.FormatException;
import ome.scifio.Metadata;
import ome.scifio.io.RandomAccessOutputStream;

/**
 * A utility class for working with metadata objects,
 * including {@link MetadataStore}, {@link MetadataRetrieve},
 * and OME-XML strings.
 * Most of the methods require the optional {@link loci.formats.ome}
 * package, and optional ome-xml.jar library, to be present at runtime.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/MetadataTools.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/MetadataTools.java;hb=HEAD">Gitweb</a></dd></dl>
 */
public class MetadataTools {

  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe an Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(Metadata src,
    RandomAccessOutputStream out) throws FormatException
  {
    verifyMinimumPopulated(src, out, 0, 0);
  }

  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe an Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(Metadata src,
    RandomAccessOutputStream out, int iNo) throws FormatException
  {
    verifyMinimumPopulated(src, out, iNo, 0);
  }

  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe the nth Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(Metadata src,
    RandomAccessOutputStream out, int iNo, int no) throws FormatException
  {
    if (src == null) {
      throw new FormatException("Metadata object is null; "
        + "call Writer.setMetadata() first");
    }

    if (out == null) {
      throw new FormatException("RandomAccessOutputStream object is null; "
        + "call Writer.setSource(<String/File/RandomAccessOutputStream>) first");
    }

    if (src.getDimensionOrder(iNo) == null) {
      throw new FormatException("DimensionOrder #" + iNo + " is null");
    }
  }
}
