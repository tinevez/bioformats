package ome.scifio.util;

public class FormatTools {
	  /** Identifies the <i>INT8</i> data type used to store pixel values. */
	  public static final int INT8 = 0;

	  /** Identifies the <i>UINT8</i> data type used to store pixel values. */
	  public static final int UINT8 = 1;

	  /** Identifies the <i>INT16</i> data type used to store pixel values. */
	  public static final int INT16 = 2;

	  /** Identifies the <i>UINT16</i> data type used to store pixel values. */
	  public static final int UINT16 = 3;

	  /** Identifies the <i>INT32</i> data type used to store pixel values. */
	  public static final int INT32 = 4;

	  /** Identifies the <i>UINT32</i> data type used to store pixel values. */
	  public static final int UINT32 = 5;

	  /** Identifies the <i>FLOAT</i> data type used to store pixel values. */
	  public static final int FLOAT = 6;

	  /** Identifies the <i>DOUBLE</i> data type used to store pixel values. */
	  public static final int DOUBLE = 7;
	  
	  // -- Utility methods - sanity checking

	  /**
	   * Asserts that the current file is either null, or not, according to the
	   * given flag. If the assertion fails, an IllegalStateException is thrown.
	   * @param currentId File name to test.
	   * @param notNull True iff id should be non-null.
	   * @param depth How far back in the stack the calling method is; this name
	   *   is reported as part of the exception message, if available. Use zero
	   *   to suppress output of the calling method name.
	   */
	  public static void assertId(String currentId, boolean notNull, int depth) {
	    String msg = null;
	    if (currentId == null && notNull) {
	      msg = "Current file should not be null; call setId(String) first";
	    }
	    else if (currentId != null && !notNull) {
	      msg = "Current file should be null, but is '" +
	        currentId + "'; call close() first";
	    }
	    if (msg == null) return;

	    StackTraceElement[] ste = new Exception().getStackTrace();
	    String header;
	    if (depth > 0 && ste.length > depth) {
	      String c = ste[depth].getClassName();
	      if (c.startsWith("loci.formats.")) {
	        c = c.substring(c.lastIndexOf(".") + 1);
	      }
	      header = c + "." + ste[depth].getMethodName() + ": ";
	    }
	    else header = "";
	    throw new IllegalStateException(header + msg);
	  }
}
