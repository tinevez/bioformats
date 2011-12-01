package ome.scifio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * CoreMetadata represents the metadata for a complete dataset, consisting of an
 * arbitrary number of images (and corresponding CoreImageMetadata objects).
 *
 */
public class CoreMetadata extends AbstractMetadata {

  
  // -- Fields --
  
  /** Contains global metadata key, value pairs for this dataset */
  Hashtable<String, Object> globalMeta;
  
  /** Contains a list of metadata objects for each image in this dataset */
  @Field(label = "imageMeta", isList = true)
  List<CoreImageMetadata> imageMeta;
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  // -- Constructors --
  
  public CoreMetadata() {
    globalMeta = new Hashtable<String, Object>();
    imageMeta = new ArrayList<CoreImageMetadata>();
  }

  // -- Metadata API Methods --
  
  /* @see Metadata#getMetadataValue() */
  public Object getMetadataValue(int no, String field) {
    return globalMeta.get(field);
  }
  
  /* @see Metadata#getImageMetadataValue() */
  public Object getImageMetadataValue(int no, String field) {
    return imageMeta.get(no).imageMetadata.get(field);

  }
  
  /* @see Metadata#getGlobalMetadata() */
  public Hashtable<String, Object> getGlobalMetadata() {
    return this.globalMeta;
  }

  /* @see Metadata#getImageMetadata() */
  public Hashtable<String, Object> getImageMetadata(int no) {
    return imageMeta.get(no).imageMetadata;
  }
  
  /* @see Metadata#getImageCount() */
  public int getImageCount() {
    return imageMeta.size();
  }
  
  /* @see Metadata#isInterleaved(int) */
  public boolean isInterleaved(int no) {
    return imageMeta.get(no).interleaved;
  }
  
  /* @see Metadata#getPixelType(int) */
  public int getPixelType(int no) {
    return imageMeta.get(no).pixelType;
  }
  
  public int getBitsPerPixel(int no) {
    return imageMeta.get(no).bitsPerPixel;
  }
  
  public int getEffectiveSizeC(int no) {
    int sizeZT = getSizeZ(no) * getSizeT(no);
    if (sizeZT == 0) return 0;
    return getImageCount() / sizeZT;
  }
  
  /* @see Metadata#getRGBChannelCount(int) */
  public int getRGBChannelCount(int no) {
    int effSizeC = getEffectiveSizeC(no);
    if (effSizeC == 0) return 0;
    return getSizeC(no) / effSizeC;
  }
  
  /* @see Metadata#isLittleEndian(int) */
  public boolean isLittleEndian(int no) {
    return imageMeta.get(no).littleEndian;
  }
  
  /* @see Metadata#isIndexed(int) */
  public boolean isIndexed(int no) {
    return imageMeta.get(no).indexed;
  }

  /* @see Metadata#getSizeX(int) */
  public int getSizeX(int no) {
    return imageMeta.get(no).sizeX;
  }

  /* @see Metadata#getSizeY(int) */
  public int getSizeY(int no) {
    return imageMeta.get(no).sizeY;
  }

  /* @see Metadata#getSizeZ(int) */
  public int getSizeZ(int no) {
    return imageMeta.get(no).sizeZ;
  }

  /* @see Metadata#getSizeC(int) */
  public int getSizeC(int no) {
    return imageMeta.get(no).sizeC;
  }

  /* @see Metadata#getSizeT(int) */
  public int getSizeT(int no) {
    return imageMeta.get(no).sizeT;
  }

  /* @see Metadata#get8BitLookupTable(int) */
  public byte[][] get8BitLookupTable(int no)
    throws ome.scifio.FormatException, IOException
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* @see Metadata#get16BitLookupTable(int) */
  public short[][] get16BitLookupTable(int no)
    throws ome.scifio.FormatException, IOException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  // -- Helper Methods --
  
  public void resetMeta() {
    super.resetMeta(this.getClass());
    globalMeta = new Hashtable<String, Object>();
    imageMeta = new ArrayList<CoreImageMetadata>();
  }
  
  public Collection<CoreImageMetadata> getImageMetadata() {
    return Collections.unmodifiableCollection(imageMeta);
  }
  
  public void add(final CoreImageMetadata meta) {
    imageMeta.add(meta);
  }
  
  /* TODO not sure if these are necessary
  public boolean isFalseColor(int no) {
    return get(no).falseColor;
  }
  
  public boolean isSingleFile() {
    return this.size() <= 1;
  }
  
  public boolean hasCompanionFiles() {
    // TODO Auto-generated method stub
    return false;
  }

  
  public boolean isMetadataComplete() {
    // TODO Auto-generated method stub
    return false;
  }
  
  public boolean isRGB(int no) {
    return get(no).rgb;
  }
  */
}
