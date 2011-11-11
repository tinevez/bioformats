package ome.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import loci.formats.IFormatHandler;

import ome.scifio.io.RandomAccessInputStream;

/**
 * Interface for all SciFIO Parsers.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Parser<M extends Metadata> extends MetadataHandler<M> {

  // -- Parser API methods --

  /**
   * Wraps the file corresponding to the given name in a File handle and returns parse(RandomAccessInputStream).
   * 
   * @param fileName Path to an image file to be parsed.  Parsers are typically
   *   specific to the file type discovered here.
   * @return most specific metadata for this type
   * @throws IOException 
   */
  M[] parse(String fileName) throws IOException, FormatException;

  /**
   * Wraps the file in a File handle and returns parse(RandomAccessInputStream).
   * 
   * @param file Path to an image file to be parsed.  Parsers are typically
   *   specific to the file type discovered here.
   * @return most specific metadata for this type
   * @throws IOException 
   */
  M[] parse(File file) throws IOException, FormatException;

  /**
   * Returns the most specific Metadata object possible, for the provided RandomAccessInputStream.
   * 
   * @param stream random access handle to the file to be parsed.
   * @return most specific metadata for this type   
   * @throws IOException 
   */
  M[] parse(RandomAccessInputStream stream) throws IOException, FormatException;

  /**
   * Closes the currently open file. If the flag is set, this is all that
   * happens; if unset, it is equivalent to calling
   * {@link IFormatHandler#close()}.
   */
  void close(boolean fileOnly) throws IOException;

  /** Gets the number of series in this file. */
  int getSeriesCount();

  /** Activates the specified series. */
  void setSeries(int no);

  /**
   * Specifies whether or not to save proprietary metadata
   * in the Metadata.
   */
  void setOriginalMetadataPopulated(boolean populate);

  /**
   * Returns true if we should save proprietary metadata
   * in the Metadata.
   */
  boolean isOriginalMetadataPopulated();

  /** Returns an array of filenames needed to open this dataset. */
  String[] getUsedFiles();

  /**
   * Returns an array of filenames needed to open this dataset.
   * If the 'noPixels' flag is set, then only files that do not contain
   * pixel data will be returned.
   */
  String[] getUsedFiles(boolean noPixels);

  /**
   * Specifies whether ugly metadata (entries with unprintable characters,
   * and extremely large entries) should be discarded from the metadata table.
   */
  void setMetadataFiltered(boolean filter);

  /**
   * Returns true if ugly metadata (entries with unprintable characters,
   * and extremely large entries) are discarded from the metadata table.
   */
  boolean isMetadataFiltered();

  /**
   * Gets whether the image planes are indexed color.
   * This value has no impact on {@link #getSizeC()},
   * {@link #getEffectiveSizeC()} or {@link #getRGBChannelCount()}.
   */
  boolean isIndexed();

  /** Determines the number of image planes in the current file. */
  int getImageCount();

  /**
   * Obtains the hashtable containing the metadata field/value pairs from
   * the current file.
   * @return the hashtable containing all non-series-specific metadata
   * from the file
   */
  public Hashtable<String, Object> getGlobalMetadata();

  /**
   * Returns the current id for the file being parsed.
   * @return
   */
  public String getCurrentId();

  public M[] getMetadataArray();
}
