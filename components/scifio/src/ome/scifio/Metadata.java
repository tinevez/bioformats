package ome.scifio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import loci.formats.FormatException;
import loci.formats.FormatTools;

/**
 * Interface for all SciFIO Metadata objects.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Metadata {
  // -- Constants --
  /** Allows for N-dimensional tracking */
  final Map<String, Integer> dimensionSizes = new HashMap<String, Integer>();

  // -- Metadata API methods --

  /** Determines the number of image planes in the current file. */
  int getImageCount();

  /** Sets whether or not the channels in an image are interleaved. */
  void setInterleaved(boolean interleaved);

  /** Gets whether or not the channels in an image are interleaved. */
  boolean isInterleaved();

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
   * Gets the effective size of the C dimension, guaranteeing that
   * getEffectiveSizeC() * getSize("Z") * getSize("T") == getImageCount()
   * regardless of the result of isRGB().
   */
  int getEffectiveSizeC();

  /**
   * Gets the number of channels returned with each call to openBytes.
   * The most common case where this value is greater than 1 is for interleaved
   * RGB data, such as a 24-bit color image plane. However, it is possible for
   * this value to be greater than 1 for non-interleaved data, such as an RGB
   * TIFF with Planar rather than Chunky configuration.
   */
  int getRGBChannelCount();

  /** Gets whether the data is in little-endian format. */
  boolean isLittleEndian();

  /**
   * Gets whether the image planes are indexed color.
   * This value has no impact on {@link #getSizeC()},
   * {@link #getEffectiveSizeC()} or {@link #getRGBChannelCount()}.
   */
  boolean isIndexed();

  /**
   * Returns false if {@link #isIndexed()} is false, or if {@link #isIndexed()}
   * is true and the lookup table represents "real" color data. Returns true
   * if {@link #isIndexed()} is true and the lookup table is only present to aid
   * in visualization.
   */
  boolean isFalseColor();

  /**
   * Obtains the specified metadata field's value for the current file.
   * @param field the name associated with the metadata field
   * @return the value, or null if the field doesn't exist
   */
  Object getMetadataValue(String field);

  /**
   * Obtains the specified metadata field's value for the current series
   * in the current file.
   * @param field the name associated with the metadata field
   * @return the value, or null if the field doesn't exist
   */
  Object getSeriesMetadataValue(String field);

  /**
   * Obtains the hashtable containing the metadata field/value pairs from
   * the current file.
   * @return the hashtable containing all non-series-specific metadata
   * from the file
   */
  Hashtable<String, Object> getGlobalMetadata();

  /**
   * Obtains the hashtable containing metadata field/value pairs from the
   * current series in the current file.
   */
  Hashtable<String, Object> getSeriesMetadata();

  /** Obtains the core metadata values for the current file. */
  CoreMetadata[] getCoreMetadata();

  /** Returns true if this is a single-file format. */
  boolean isSingleFile(String id) throws FormatException, IOException;

  /** Returns a list of scientific domains in which this format is used. */
  String[] getPossibleDomains(String id) throws FormatException, IOException;

  /** Returns true if this format supports multi-file datasets. */
  boolean hasCompanionFiles();

  /** Returns true if this format's metadata is completely parsed. */
  boolean isMetadataComplete();

  // -- Deprecated methods --

  /**
   * Returns a hashtable containing the union of all of the field/value pairs
   * in getGlobalMetadata() and getSeriesMetadata(). The series name is
   * prepended to fields in the getSeriesMetadata() hashtable.
   *
   * @deprecated Use #getGlobalMetadata() or #getSeriesMetadata() instead.
   */
  Hashtable<String, Object> getMetadata();

  /**
   * Checks if the image planes in the file have more than one channel per
   * {@link #openBytes} call.
   * This method returns true if and only if {@link #getRGBChannelCount()}
   * returns a value greater than 1.
   * 
   * @deprecated
   */
  boolean isRGB();
}
