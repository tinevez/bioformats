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
    dest.resetMeta();
    CoreImageMetadata coreMeta = new CoreImageMetadata();
    dest.add(coreMeta);
    
    coreMeta.setSizeX(source.sizeX);
    coreMeta.setSizeY(source.sizeY);
    coreMeta.setSizeZ(source.sizeZ);
    coreMeta.setSizeC(source.sizeC);
    coreMeta.setSizeT(source.sizeT);
    coreMeta.setThumbSizeX(source.thumbSizeX);
    coreMeta.setThumbSizeY(source.thumbSizeY);
    coreMeta.setPixelType(source.pixelType);
    coreMeta.setBitsPerPixel(source.bitsPerPixel);
    coreMeta.setcLengths(source.cLengths);
    coreMeta.setcTypes(source.cTypes);
    coreMeta.setDimensionOrder(source.dimensionOrder);
    coreMeta.setOrderCertain(source.orderCertain);
    coreMeta.setRgb(source.rgb);
    coreMeta.setLittleEndian(source.littleEndian);
    coreMeta.setInterleaved(source.interleaved);
    coreMeta.setIndexed(source.indexed);
    coreMeta.setFalseColor(true);
    coreMeta.setMetadataComplete(source.metadataComplete);
    coreMeta.setImageMetadata(source.imageMetadata);
    coreMeta.setThumbnail(source.thumbnail);
  }

  // -- MetadataHandler API Methods --

  /* @see MetadataHandler#getMetadataTypes() */
  public Class<APNGMetadata> getMetadataType() {
    return APNGMetadata.class;
  }
}
