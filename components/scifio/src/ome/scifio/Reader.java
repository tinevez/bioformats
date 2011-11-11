package ome.scifio;

import java.io.IOException;

import ome.scifio.io.RandomAccessInputStream;

/**
 * Interface for all SciFIO Readers.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Reader<M extends Metadata> extends MetadataHandler<M> {

  // -- Reader API methods --

  /**
   * Gets the 8-bit color lookup table associated with
   * the most recently opened image.
   * If no image planes have been opened, or if {@link #isIndexed()} returns
   * false, then this may return null. Also, if {@link #getPixelType()} returns
   * anything other than {@link FormatTools#INT8} or {@link FormatTools#UINT8},
   * this method will return null.
   */
  byte[][] get8BitLookupTable() throws FormatException, IOException;

  /**
   * Gets the 16-bit color lookup table associated with
   * the most recently opened image.
   * If no image planes have been opened, or if {@link #isIndexed()} returns
   * false, then this may return null. Also, if {@link #getPixelType()} returns
   * anything other than {@link FormatTools#INT16} or {@link
   * FormatTools#UINT16}, this method will return null.
   */
  short[][] get16BitLookupTable() throws FormatException, IOException;

  /**
   * Gets the lengths of each subdimension of C,
   * in fastest-to-slowest rasterization order.
   */
  int[] getChannelDimLengths();

  /**
   * Gets the name of each subdimension of C,
   * in fastest-to-slowest rasterization order.
   * Common subdimensional types are enumerated in {@link FormatTools}.
   */
  String[] getChannelDimTypes();

  /** Get the size of the X dimension for the thumbnail. */
  int getThumbSizeX();

  /** Get the size of the Y dimension for the thumbnail. */
  int getThumbSizeY();

  /**
   * Gets whether the current series is a lower resolution copy of a different
   * series.
   */
  boolean isThumbnailSeries();

  /**
   * Obtains the specified image plane from the current file as a byte array.
   * @see #openBytes(int, byte[])
   */
  byte[] openBytes(int no) throws FormatException, IOException;

  /**
   * Obtains a sub-image of the specified image plane,
   * whose upper-left corner is given by (x, y).
   */
  byte[] openBytes(int no,  int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
   * Obtains the specified image plane from the current file into a
   * pre-allocated byte array of
   * (sizeX * sizeY * bytesPerPixel * RGB channel count).
   *
   * @param no the image index within the file.
   * @param buf a pre-allocated buffer.
   * @return the pre-allocated buffer <code>buf</code> for convenience.
   * @throws FormatException if there was a problem parsing the metadata of the
   *   file.
   * @throws IOException if there was a problem reading the file.
   */
  byte[] openBytes(int no, byte[] buf) throws FormatException, IOException;

  /**
   * Obtains a sub-image of the specified image plane
   * into a pre-allocated byte array.
   *
   * @param no the image index within the file.
   * @param buf a pre-allocated buffer.
   * @param dims a map of dimension labels (e.g., "x", "y") to the size of the
   *             corresponding dimension (e.g., sizeX, sizeY) 
   * @return the pre-allocated buffer <code>buf</code> for convenience.
   * @throws FormatException if there was a problem parsing the metadata of the
   *   file.
   * @throws IOException if there was a problem reading the file.
   */
  byte[] openBytes(int no, byte[] buf,  int x, int y,
		    int w, int h)
    throws FormatException, IOException;

  /**
   * Obtains the specified image plane (or sub-image thereof) in the reader's
   * native data structure. For most readers this is a byte array; however,
   * some readers call external APIs that work with other types such as
   * {@link java.awt.image.BufferedImage}. The openPlane method exists to
   * maintain generality and efficiency while avoiding pollution of the API
   * with AWT-specific logic.
   *
   * @see loci.formats.FormatReader
   * @see loci.formats.gui.BufferedImageReader
   */
  Object openPlane(int no, int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
   * Obtains a thumbnail for the specified image plane from the current file,
   * as a byte array.
   */
  byte[] openThumbBytes(int no) throws FormatException, IOException;

  /** Gets the number of series in this file. */
  int getSeriesCount();

  /** Activates the specified series. */
  void setSeries(int no);

  /** Gets the currently active series. */
  int getSeries();

  /** Specifies whether or not to force grouping in multi-file formats. */
  void setGroupFiles(boolean group);

  /** Returns true if we should group files in multi-file formats.*/
  boolean isGroupFiles();

  /**
   * Returns an int indicating that we cannot, must, or might group the files
   * in a given dataset.
   */
  int fileGroupOption(String id) throws FormatException, IOException;

  /** Returns an array of filenames needed to open this dataset. */
  String[] getUsedFiles();

  /**
   * Returns an array of filenames needed to open this dataset.
   * If the 'noPixels' flag is set, then only files that do not contain
   * pixel data will be returned.
   */
  String[] getUsedFiles(boolean noPixels);

  /** Returns an array of filenames needed to open the current series. */
  String[] getSeriesUsedFiles();

  /**
   * Returns an array of filenames needed to open the current series.
   * If the 'noPixels' flag is set, then only files that do not contain
   * pixel data will be returned.
   */
  String[] getSeriesUsedFiles(boolean noPixels);

  /**
   * Returns an array of FileInfo objects representing the files needed
   * to open this dataset.
   * If the 'noPixels' flag is set, then only files that do not contain
   * pixel data will be returned.
   */
  FileInfo[] getAdvancedUsedFiles(boolean noPixels);

  /**
   * Returns an array of FileInfo objects representing the files needed to
   * open the current series.
   * If the 'noPixels' flag is set, then only files that do not contain
   * pixel data will be returned.
   */
  FileInfo[] getAdvancedSeriesUsedFiles(boolean noPixels);

  /** Returns the current file. */
  String getCurrentFile();

  /** Returns the list of domains represented by the current file. */
  String[] getDomains();

  /**
   * Gets the rasterized index corresponding
   * to the given Z, C and T coordinates.
   */
  int getIndex(int z, int c, int t);

  /**
   * Gets the Z, C and T coordinates corresponding
   * to the given rasterized index value.
   */
  int[] getZCTCoords(int index);

  /**
   * Sets the default input stream for this reader.
   * 
   * @param stream a RandomAccessInputStream for the source being read
   */
  void setStream(RandomAccessInputStream stream);

  /**
   * Retrieves the current input stream for this reader.
   * @return A RandomAccessInputStream
   */
  RandomAccessInputStream getStream();
  
  /** Gets whether the data is in little-endian format. */
  boolean isLittleEndian();

  /**
   * Retrieves all underlying readers.
   * Returns null if there are no underlying readers.
   */
  Reader<Metadata>[] getUnderlyingReaders();

  /** Returns the optimal sub-image width for use with openBytes. */
  int getOptimalTileWidth();

  /** Returns the optimal sub-image height for use with openBytes. */
  int getOptimalTileHeight();

  //TODO remove these
  /** Specifies whether or not to normalize float data. */
  void setNormalized(boolean normalize);

  /** Returns true if we should normalize float data. */
  boolean isNormalized();

  /** Sets the Metadata[] for this Reader */
  public void setMetadataArray(M[] meta);

  /** Gets the Metadata[] for this Reader */
  public M[] getMetadataArray();
  
  //TODO also in Metadata
  /**
   * Gets the number of channels returned with each call to openBytes.
   * The most common case where this value is greater than 1 is for interleaved
   * RGB data, such as a 24-bit color image plane. However, it is possible for
   * this value to be greater than 1 for non-interleaved data, such as an RGB
   * TIFF with Planar rather than Chunky configuration.
   */
  int getRGBChannelCount();
  
  /** Gets the size of the X dimension. */
  int getSizeX();

  /** Gets the size of the Y dimension. */
  int getSizeY();

  /** Gets the size of the Z dimension. */
  int getSizeZ();

  /** Gets the size of the C dimension. */
  int getSizeC();

  /** Gets the size of the T dimension. */
  int getSizeT();
  
  /** Determines the number of image planes in the current file. */
  int getImageCount();
  
  /**
   * Gets the pixel type.
   * @return the pixel type as an enumeration from {@link FormatTools}
   * <i>static</i> pixel types such as {@link FormatTools#INT8}.
   */
  int getPixelType();
  
  /**
   * Gets the number of valid bits per pixel. The number of valid bits per
   * pixel is always less than or equal to the number of bits per pixel
   * that correspond to {@link #getPixelType()}.
   */
  int getBitsPerPixel();
  
  /**
   * Gets whether the image planes are indexed color.
   * This value has no impact on {@link #getSizeC()},
   * {@link #getEffectiveSizeC()} or {@link #getRGBChannelCount()}.
   */
  boolean isIndexed();
  
  /** Gets whether or not the channels in an image are interleaved. */
  boolean isInterleaved();
  
  /**
   * Gets the effective size of the C dimension, guaranteeing that
   * getEffectiveSizeC() * getSizeZ() * getSizeT() == getImageCount()
   * regardless of the result of isRGB().
   */
  public int getEffectiveSizeC();
}
