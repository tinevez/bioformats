package scifio;

/**
 * Interface for all SciFIO components dealing with Metadata.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface MetadataHandler<M extends Metadata> {
	
	// MetadataHandler API methods
	
	/** Returns the Metadata type this component takes as input */
	Class<M> getMetadataType();
}
