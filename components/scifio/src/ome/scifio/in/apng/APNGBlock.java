package ome.scifio.in.apng;

/**
 * APNG helper class, providing a struct representing
 * a PNG block.
 *
 */
public class APNGBlock {

  // -- Constants --
  public static final byte[] PNG_SIGNATURE = new byte[] {
      (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};

  // -- Fields --

  public long offset;
  public int length;
  public String type;

}
