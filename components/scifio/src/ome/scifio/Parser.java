package ome.scifio;

import java.io.File;
import java.io.IOException;

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
   * Specifies whether or not to save proprietary metadata
   * in the Metadata.
   */
  void setOriginalMetadataPopulated(boolean populate);

  /**
   * Returns true if we should save proprietary metadata
   * in the Metadata.
   */
  boolean isOriginalMetadataPopulated();

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
   * Closes the currently open file. If the flag is set, this is all that
   * happens; if unset, it is equivalent to calling
   * {@link IFormatHandler#close()}.
   */
  void close(boolean fileOnly) throws IOException;
}
