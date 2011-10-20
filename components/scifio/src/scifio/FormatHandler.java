package scifio;

/**
 * Interface for all SciFIO components dealing with files.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface FormatHandler {

	// -- FormatHandler API --
	//TODO: move these to readers/writers, see what's common
	/** Checks if the given file is a valid instance of this file format. */
	boolean supportsFormat(String name);

	/** Gets the name of this file format. */
	String getFormat();

	/** Gets the default file suffixes for this file format. */
	String[] getSuffixes();
}
