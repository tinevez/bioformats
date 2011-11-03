package ome.scifio.in.apng;

import ome.scifio.AbstractTranslator;
import ome.scifio.Metadata;

public class APNGCoreTranslator extends AbstractTranslator<APNGMetadata>{

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
