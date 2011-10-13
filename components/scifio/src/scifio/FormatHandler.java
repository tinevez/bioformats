package scifio;

public interface FormatHandler {

	// FormatHandler API Methods

	/** Checks if the given file is a valid instance of this file format. */
	boolean supportsFormat(String name);

	/** Gets the name of this file format. */
	String getFormat();

	/** Gets the default file suffixes for this file format. */
	String[] getSuffixes();
}
