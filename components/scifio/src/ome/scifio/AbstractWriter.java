package ome.scifio;

/**
 * Abstract superclass of all SCIFIO Writer components.
 *
 */
public abstract class AbstractWriter<M extends Metadata> extends AbstractFormatHandler implements Writer<M> {

	// -- Constructors --

	/** Constructs a writer with the given name and default suffix */
	public AbstractWriter(String format, String suffix) {
		super(format, suffix);
		// TODO Auto-generated constructor stub
	}

	/** Constructs a writer with the given name and default suffixes */
	public AbstractWriter(String format, String[] suffixes) {
		super(format, suffixes);
		// TODO Auto-generated constructor stub
	}
	
	// -- AbstractWriter Methods --
	
	/* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? */
	public boolean supportsFormat(String name) {
		return checkSuffix(name, suffixes);
	}
}
