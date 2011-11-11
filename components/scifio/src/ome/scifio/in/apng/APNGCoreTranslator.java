package ome.scifio.in.apng;

import ome.scifio.AbstractTranslator;
import ome.scifio.Metadata;

/**
 * File format SCIFIO Translator for Animated Portable Network Graphics
 * (APNG) images to the Core SCIFIO image type.
 *
 */
public class APNGCoreTranslator extends AbstractTranslator<APNGMetadata> {

  // -- Translator API Methods --

  @Override
  public Metadata translate(APNGMetadata metaIn) {
    // TODO Auto-generated method stub
    return null;
  }

  // -- MetadataHandler API Methods --

  /* @see MetadataHandler#getMetadataTypes() */
  public Class<APNGMetadata> getMetadataType() {
    return APNGMetadata.class;
  }
}
