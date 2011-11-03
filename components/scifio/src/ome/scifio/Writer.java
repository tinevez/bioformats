package ome.scifio;

import java.awt.image.ColorModel;
import java.io.IOException;

import loci.formats.FormatException;
import loci.formats.codec.CodecOptions;

/**
 * Interface for all SciFIO writers.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Writer<M extends Metadata> extends MetadataHandler<M> {

	// -- Writer API methods --
	/**
	 * Saves the given image to the current series in the current file.
	 *
	 * @param no the image index within the current file, starting from 0.
	 * @param buf the byte array that represents the image.
	 * @throws FormatException if one of the parameters is invalid.
	 * @throws IOException if there was a problem writing to the file.
	 */
	void saveBytes(int no, byte[] buf) throws FormatException, IOException;

	/**
	 * Saves the given image tile to the current series in the current file.
	 *
	 * @param no the image index within the current file, starting from 0.
	 * @param buf the byte array that represents the image tile.
	 * @param x the X coordinate of the upper-left corner of the image tile.
	 * @param y the Y coordinate of the upper-left corner of the image tile.
	 * @param w the width (in pixels) of the image tile.
	 * @param h the height (in pixels) of the image tile.
	 * @throws FormatException if one of the parameters is invalid.
	 * @throws IOException if there was a problem writing to the file.
	 */
	void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
	throws FormatException, IOException;

	/**
	 * Saves the given image plane to the current series in the current file.
	 *
	 * @param no the image index within the current file, starting from 0.
	 * @param plane the image plane.
	 * @throws FormatException if one of the parameters is invalid.
	 * @throws IOException if there was a problem writing to the file.
	 */
	void savePlane(int no, Object plane) throws FormatException, IOException;

	/**
	 * Saves the given image plane to the current series in the current file.
	 *
	 * @param no the image index within the current file, starting from 0.
	 * @param plane the image plane.
	 * @param x the X coordinate of the upper-left corner of the image tile.
	 * @param y the Y coordinate of the upper-left corner of the image tile.
	 * @param w the width (in pixels) of the image tile.
	 * @param h the height (in pixels) of the image tile.
	 * @throws FormatException if one of the parameters is invalid.
	 * @throws IOException if there was a problem writing to the file.
	 */
	void savePlane(int no, Object plane, int x, int y, int w, int h)
	throws FormatException, IOException;

	/**
	 * Sets the current series.
	 *
	 * @param series the series index, starting from 0.
	 * @throws FormatException if the specified series is invalid.
	 */
	void setSeries(int series) throws FormatException;

	/** Returns the current series. */
	int getSeries();

	/** Sets the number of valid bits per pixel. */
	void setValidBitsPerPixel(int bits);

	/** Reports whether the writer can save multiple images to a single file. */
	boolean canDoStacks();

	/**
	 * Sets the metadata retrieval object from
	 * which to retrieve standardized metadata.
	 */
	void setMetadata(M r);

	/**
	 * Retrieves the current metadata retrieval object for this writer. You can
	 * be assured that this method will <b>never</b> return a <code>null</code>
	 * metadata retrieval object.
	 * @return A metadata retrieval object.
	 */
	M getMetadata();

	/** Sets the color model. */
	void setColorModel(ColorModel cm);

	/** Gets the color model. */
	ColorModel getColorModel();

	/** Sets the frames per second to use when writing. */
	void setFramesPerSecond(int rate);

	/** Gets the frames per second to use when writing. */
	int getFramesPerSecond();

	/** Gets the available compression types. */
	String[] getCompressionTypes();

	/** Gets the supported pixel types. */
	int[] getPixelTypes();

	/** Gets the supported pixel types for the given codec. */
	int[] getPixelTypes(String codec);

	/** Checks if the given pixel type is supported. */
	boolean isSupportedType(int type);

	/** Sets the current compression type. */
	void setCompression(String compress) throws FormatException;
	
	/**
	 * Sets the codec options.
	 * @param options The options to set.
	 */
	void setCodecOptions(CodecOptions options) ;

	/** Gets the current compression type. */
	String getCompression();

	/** Switch the output file for the current dataset. */
	void changeOutputFile(String id) throws FormatException, IOException;

	/**
	 * Sets whether or not we know that planes will be written sequentially.
	 * If planes are written sequentially and this flag is set, then performance
	 * will be slightly improved.
	 */
	void setWriteSequentially(boolean sequential);

	// -- Deprecated methods --

	/** @deprecated Please use saveBytes(int, byte[]) instead. */
	void saveBytes(byte[] bytes, boolean last)
	throws FormatException, IOException;

	/**
	 * @deprecated Please use saveBytes(int, byte[]) and setSeries(int) instead.
	 */
	void saveBytes(byte[] bytes, int series, boolean lastInSeries, boolean last)
	throws FormatException, IOException;

	/** @deprecated Please use savePlane(int, Object) instead. */
	void savePlane(Object plane, boolean last)
	throws FormatException, IOException;

	/**
	 * @deprecated Please use savePlane(int, Object) and setSeries(int) instead.
	 */
	void savePlane(Object plane, int series, boolean lastInSeries, boolean last)
	throws FormatException, IOException;	
}
