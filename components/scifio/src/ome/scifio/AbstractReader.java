package ome.scifio;

public abstract class AbstractReader<M extends Metadata> extends AbstractFormatHandler implements Reader<M> {

	// -- Fields --
	M[] metadata;
	
	// -- Constructors --
	
	public AbstractReader(String format, String suffix) {
		super(format, suffix);
		// TODO Auto-generated constructor stub
	}
	
	public AbstractReader(String format, String[] suffixes) {
		super(format, suffixes);
		// TODO Auto-generated constructor stub
	}
	
	/* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? */
	public boolean supportsFormat(String name) {
		return checkSuffix(name, suffixes);
	}
	
	// -- Reader API Methods --
	public void setMetadataArray(M[] meta) {
		this.metadata = meta;
	}
	
	public M[] getMetadataArray() {
		return this.metadata;
	}
}
