package ome.scifio;

import java.io.IOException;

import ome.scifio.io.RandomAccessInputStream;

/**
 * Abstract superclass of all SCIFIO checker components. 
 *
 */
public abstract class AbstractChecker<M extends Metadata>
  extends AbstractFormatHandler implements Checker<M> {

  // -- Fields --
  /**
   * Whether the file extension matching one of the reader's suffixes
   * is necessary to identify the file as an instance of this format.
   */
  protected boolean suffixNecessary = true;

  /**
   * Whether the file extension matching one of the reader's suffixes
   * is sufficient to identify the file as an instance of this format.
   */
  protected boolean suffixSufficient = true;

  /**
   * Checkers are sorted by priority so that, in case multiple checkers 
   * are capable of supporting a given file extension, the most efficient
   * implementation can be taken.
   */
  protected double priority = 0.0;

  // -- Constructors --

  /** Constructs a checker with the given name and default suffix */
  public AbstractChecker(String format, String suffix) {
    super(format, suffix);
    // TODO Auto-generated constructor stub
  }

  /** Constructs a checker with the given name and default suffixes */
  public AbstractChecker(String format, String[] suffixes) {
    super(format, suffixes);
  }

  // -- Checker API Methods --

  /* @see Checker#isFormat(String name, boolean open) */
  public boolean isFormat(String name, boolean open) {
    // if file extension ID is insufficient and we can't open the file, give up
    if (!suffixSufficient && !open) return false;

    if (suffixNecessary || suffixSufficient) {
      // it's worth checking the file extension
      boolean suffixMatch = checkSuffix(name, suffixes);
      ;

      // if suffix match is required but it doesn't match, failure
      if (suffixNecessary && !suffixMatch) return false;

      // if suffix matches and that's all we need, green light it
      if (suffixMatch && suffixSufficient) return true;
    }

    // suffix matching was inconclusive; we need to analyze the file contents
    if (!open) return false; // not allowed to open any files
    try {
      RandomAccessInputStream stream = new RandomAccessInputStream(name);
      boolean isFormat = isFormat(stream);
      stream.close();
      return isFormat;
    }
    catch (IOException exc) {
      //TODO LOGGER.debug("", exc);
      return false;
    }
  }

  /* @see Checker#isFormat(byte[] block) */
  public boolean isFormat(byte[] block) {
    try {
      RandomAccessInputStream stream = new RandomAccessInputStream(block);
      boolean isFormat = isFormat(stream);
      stream.close();
      return isFormat;
    }
    catch (IOException e) {
      //TODO LOGGER.debug("", e);
    }
    return false;
  }

  /* @see Checker#getPriority() */
  public Double getPriority() {
    return priority;
  }

  // -- Comparable API Methods --

  /* @see Checker#compareTo(Checker<?> c) */
  public int compareTo(Checker<?> c) {
    return this.getPriority().compareTo(c.getPriority());
  }
}
