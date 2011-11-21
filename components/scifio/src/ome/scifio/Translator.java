package ome.scifio;

/**
 * Interface for all SciFIO Translators.
 * Translates from Metadata type I to a new Metadata type
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Translator<M extends Metadata, N extends Metadata> extends MetadataHandler<M> {

  // -- Translator API methods --

  /**
   * Converts a type I Metadata object to a new type of Metadata
   * 
   * @param metaIn Metadata object of the Input type
   * @return a new Metadtata object
   */
  void translate(M source, N destination);
}
