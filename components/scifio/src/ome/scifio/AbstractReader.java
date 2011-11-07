package ome.scifio;

/**
 * Abstract superclass of all SCIFIO reader components.
 *
 */
public abstract class AbstractReader<M extends Metadata>
  extends AbstractFormatHandler implements Reader<M> {

  // -- Fields --
  M[] metadata;

  // -- Constructors --

  /** Constructs a reader with the given name and default suffix */
  public AbstractReader(String format, String suffix) {
    super(format, suffix);
    // TODO Auto-generated constructor stub
  }

  /** Constructs a reader with the given name and default suffixes */
  public AbstractReader(String format, String[] suffixes) {
    super(format, suffixes);
    // TODO Auto-generated constructor stub
  }

  // -- Reader API Methods --
  public void setMetadataArray(M[] meta) {
    this.metadata = meta;
  }

  public M[] getMetadataArray() {
    return this.metadata;
  }

  // -- AbstractReader Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? 
   * should this even be in reader at all? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
  }
}
