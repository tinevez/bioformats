package scifio;

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
	 * Returns the most specific Metadata object possible, for the provided file.
	 * 
	 * @param fileName Path to an image file to be parsed.  Parsers are typically
	 *   specific to the file type discovered here.
	 */
	M parse(String fileName);
	
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
	
	/** Specifies whether or not to normalize float data. */
	void setNormalized(boolean normalize);

	/** Returns true if we should normalize float data. */
	boolean isNormalized();
}
