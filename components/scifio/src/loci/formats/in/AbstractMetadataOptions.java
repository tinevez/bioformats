package loci.formats.in;

public abstract class AbstractMetadataOptions implements MetadataOptions {

  protected ome.scifio.MetadataOptions scOptions;
  
  public MetadataLevel convertLevel(ome.scifio.MetadataLevel sclevel) {
    MetadataLevel level = MetadataLevel.ALL;
    switch(sclevel) {
      case ALL: level = MetadataLevel.ALL; break;
      case MINIMUM: level = MetadataLevel.MINIMUM; break;
      case NO_OVERLAYS: level = MetadataLevel.NO_OVERLAYS; break;
    }
    return level;
  }
  
  public ome.scifio.MetadataLevel convertLevel(MetadataLevel level) {
    ome.scifio.MetadataLevel sclevel = ome.scifio.MetadataLevel.ALL;
    switch(level) {
      case ALL: sclevel = ome.scifio.MetadataLevel.ALL; break;
      case MINIMUM: sclevel = ome.scifio.MetadataLevel.MINIMUM; break;
      case NO_OVERLAYS: sclevel = ome.scifio.MetadataLevel.NO_OVERLAYS; break;
    }
    return sclevel;
  }
  
  public ome.scifio.MetadataOptions getSCOptions() {
    return scOptions;
  }

}
