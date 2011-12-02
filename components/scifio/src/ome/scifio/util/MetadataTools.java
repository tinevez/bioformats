package ome.scifio.util;

import ome.scifio.FormatException;
import ome.scifio.Metadata;
import ome.scifio.io.RandomAccessOutputStream;

public class MetadataTools {
  /**
   * Checks whether the given metadata object has the minimum metadata
   * populated to successfully describe an Image.
   *
   * @throws FormatException if there is a missing metadata field,
   *   or the metadata object is uninitialized
   */
  public static void verifyMinimumPopulated(Metadata src, RandomAccessOutputStream out, int iNo)
    throws FormatException
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
  public static void verifyMinimumPopulated(Metadata src, RandomAccessOutputStream out, int iNo, int no)
    throws FormatException
  {
    if (src == null) {
      throw new FormatException("Metadata object is null; " +
          "call Writer.setMetadata() first");
    }
    
    if (out == null) {
      throw new FormatException("RandomAccessOutputStream object is null; " +
          "call Writer.setSource(<String/File/RandomAccessOutputStream>) first");
    }
    
    //TODO check initialize array?
    //TODO remove out?

    if (src.getDimensionOrder(iNo) == null) {
      throw new FormatException("DimensionOrder #" + iNo + " is null");
    }
  }
}
