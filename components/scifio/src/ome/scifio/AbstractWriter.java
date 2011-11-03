package ome.scifio;

public abstract class AbstractWriter<M extends Metadata> extends AbstractFormatHandler implements Writer<M> {

	// -- Constructors --
	
	public AbstractWriter(String format, String suffix) {
		super(format, suffix);
		// TODO Auto-generated constructor stub
	}
	
	public AbstractWriter(String format, String[] suffixes) {
		super(format, suffixes);
		// TODO Auto-generated constructor stub
	}
	
	/* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? */
	public boolean supportsFormat(String name) {
		return checkSuffix(name, suffixes);
	}
}
