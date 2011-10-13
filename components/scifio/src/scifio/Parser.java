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
}
