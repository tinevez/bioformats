package ome.scifio.in.apng;

import ome.scifio.AbstractTranslator;
import ome.scifio.CoreImageMetadata;
import ome.scifio.CoreMetadata;

/**
 * File format SCIFIO Translator for Animated Portable Network Graphics
 * (APNG) images to the Core SCIFIO image type.
 *
 */
public class APNGCoreTranslator extends AbstractTranslator<APNGMetadata, CoreMetadata> {

  // -- Translator API Methods --

  @Override
  public void translate(APNGMetadata source, CoreMetadata dest) {
    CoreImageMetadata coreMeta = new CoreImageMetadata();
    CoreMetadata meta = new CoreMetadata();
    meta.add(coreMeta);
    
    // TODO use setters.. clear?
    coreMeta.sizeX = source.sizeX;
    coreMeta.sizeY = source.sizeY;
    coreMeta.sizeZ = source.sizeZ;
    coreMeta.sizeC = source.sizeC;
    coreMeta.sizeT = source.sizeT;
    coreMeta.thumbSizeX = source.thumbSizeX;
    coreMeta.thumbSizeY = source.thumbSizeY;
    coreMeta.pixelType = source.pixelType;
    coreMeta.pixelType = source.bitsPerPixel;
    coreMeta.cLengths = source.cLengths;
    coreMeta.cTypes = source.cTypes;
    coreMeta.dimensionOrder = source.dimensionOrder;
    coreMeta.orderCertain = source.orderCertain;
    coreMeta.rgb = source.rgb;
    coreMeta.littleEndian = source.littleEndian;
    coreMeta.interleaved = source.interleaved;
    coreMeta.indexed = source.indexed;
    coreMeta.falseColor = true;
    coreMeta.metadataComplete = source.metadataComplete;
    coreMeta.imageMetadata = source.imageMetadata;
    coreMeta.thumbnail = source.thumbnail;
  }

  // -- MetadataHandler API Methods --

  /* @see MetadataHandler#getMetadataTypes() */
  public Class<APNGMetadata> getMetadataType() {
    return APNGMetadata.class;
  }
}
