package ome.scifio;

public abstract class AbstractFormatHandler implements FormatHandler {

	// -- Fields --
	/** Name of this file format. */
	protected String format;

	/** Valid suffixes for this file format. */
	protected String[] suffixes;

	/** Suffixes for supported compression types. */
	public static final String[] COMPRESSION_SUFFIXES = {"bz2", "gz"};

	
	// -- Constructors --
	// TODO do Parsers and Translators really need to track format and suffixes?
	
	/** Constructs a format handler with the given name and default suffix. */
	public AbstractFormatHandler(String format, String suffix) {
		this(format, suffix == null ? null : new String[] {suffix});
	}

	/** Constructs a format handler with the given name and default suffixes. */
	public AbstractFormatHandler(String format, String[] suffixes) {
		this.format = format;
		this.suffixes = suffixes == null ? new String[0] : suffixes;
	}

	// -- FormatHandler API Methods --
	
	/* @see FormatHandler#getFormat() */
	public String getFormat() {
		return format;
	}

	/* @see FormatHandler#getSuffixes() */
	public String[] getSuffixes() {
		return suffixes;
	}

	// -- Utility methods --

	/** Performs suffix matching for the given filename. */
	public static boolean checkSuffix(String name, String suffix) {
		return checkSuffix(name, new String[] {suffix});
	}

	/** Performs suffix matching for the given filename. */
	public static boolean checkSuffix(String name, String[] suffixList) {
		String lname = name.toLowerCase();
		for (int i=0; i<suffixList.length; i++) {
			String s = "." + suffixList[i];
			if (lname.endsWith(s)) return true;
			for (int j=0; j<COMPRESSION_SUFFIXES.length; j++) {
				if (lname.endsWith(s + "." + COMPRESSION_SUFFIXES[j])) return true;
			}
		}
		return false;
	}

}
