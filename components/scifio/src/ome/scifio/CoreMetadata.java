package ome.scifio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import ome.scifio.util.FormatTools;

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

  /* @see Metadata#getEffectiveSizeC(int no) */
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

  /* @see Metadata#getBitsPerPixel(int) */
  public int getBitsPerPixel(int no) {
    return imageMeta.get(no).bitsPerPixel;
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

  /* @see Metadata#getDimensionOrder(int) */
  public String getDimensionOrder(int no) {
    return imageMeta.get(no).dimensionOrder;
  }

  /* @see Metadata#isRGB(int) */
  public boolean isRGB(int no) {
    return getRGBChannelCount(no) > 1;
  }

  /* @see Metadata#isFalsecolor(int) */
  public boolean isFalseColor(int no) {
    return imageMeta.get(no).falseColor;
  }

  /* @see Metadata#getChannelDimLengths(int) */
  public int[] getChannelDimLengths(int no) {
    if (imageMeta.get(no).cLengths == null)
      return new int[] {imageMeta.get(no).sizeC};
    return imageMeta.get(no).cLengths;
  }

  /* @see Metadata#getChannelDimTypes(int) */
  public String[] getChannelDimTypes(int no) {
    if (imageMeta.get(no).cTypes == null)
      return new String[] {FormatTools.CHANNEL};
    return imageMeta.get(no).cTypes;
  }

  /* @see Metadata#getThumbSizeX(int) */
  public int getThumbSizeX(int no) {
    if (imageMeta.get(no).thumbSizeX == 0) {
      int sx = getSizeX(no);
      int sy = getSizeY(no);
      int thumbSizeX = 0;
      if (sx > sy) thumbSizeX = FormatTools.THUMBNAIL_DIMENSION;
      else if (sy > 0) thumbSizeX = sx * FormatTools.THUMBNAIL_DIMENSION / sy;
      if (thumbSizeX == 0) thumbSizeX = 1;
      return thumbSizeX;
    }
    return imageMeta.get(no).thumbSizeX;
  }

  /* @see Metadata#getThumbSizeY(int) */
  public int getThumbSizeY(int no) {
    if (imageMeta.get(no).thumbSizeY == 0) {
      int sx = getSizeX(no);
      int sy = getSizeY(no);
      int thumbSizeY = 1;
      if (sy > sx) thumbSizeY = FormatTools.THUMBNAIL_DIMENSION;
      else if (sx > 0) thumbSizeY = sy * FormatTools.THUMBNAIL_DIMENSION / sx;
      if (thumbSizeY == 0) thumbSizeY = 1;
      return thumbSizeY;
    }
    return imageMeta.get(no).thumbSizeY;
  }

  /* @see Metadata#isOrderCertain(int) */
  public boolean isOrderCertain(int no) {
    return imageMeta.get(no).orderCertain;
  }

  /* @see Metadata#isThumbnailSeries(int) */
  public boolean isThumbnailImage(int no) {
    return imageMeta.get(no).thumbnail;
  }

  /* @see Metadata#isMetadataComplete(int) */
  public boolean isMetadataComplete(int no) {
    return imageMeta.get(no).metadataComplete;
  }

  /* @see Metadata#set8BitLookupTable(int, byte[][]) */
  public void set8BitLookupTable(int iNo, byte[][] lut)
    throws FormatException, IOException
  {
    this.imageMeta.get(iNo).lut = lut;
  }

  /* @see Metadata#set16BitLookupTable(int, short[][]) */
  public void set16BitLookupTable(int iNo, short[][] lut)
    throws FormatException, IOException
  {
    // TODO Auto-generated method stub
  }

  /* @see Metadata#setSizeX(int, int) */
  public void setSizeX(int iNo, int x) {
    this.imageMeta.get(iNo).sizeX = x;
  }

  /* @see Metadata#setSizeY(int, int) */
  public void setSizeY(int iNo, int y) {
    this.imageMeta.get(iNo).sizeY = y;
  }

  /* @see Metadata#setSizeZ(int, int) */
  public void setSizeZ(int iNo, int z) {
    this.imageMeta.get(iNo).sizeZ = z;
  }

  /* @see Metadata#setSizeC(int, int) */
  public void setSizeC(int iNo, int c) {
    this.imageMeta.get(iNo).sizeC = c;
  }

  /* @see Metadata#setSizeT(int, int) */
  public void setSizeT(int iNo, int t) {
    this.imageMeta.get(iNo).sizeT = t;
  }

  /* @see Metadata#setThumbSizeX(int, int) */
  public void setThumbSizeX(int iNo, int thumbX) {
    this.imageMeta.get(iNo).thumbSizeX = thumbX;
  }

  /* @see Metadata#setThumbSizeY(int, int) */
  public void setThumbSizeY(int iNo, int thumbY) {
    this.imageMeta.get(iNo).thumbSizeY = thumbY;
  }

  /* @see Metadata#setPixelType(int, int) */
  public void setPixelType(int iNo, int type) {
    this.imageMeta.get(iNo).pixelType = type;
  }

  /* @see Metadata#setBitsPerPixel(int, int) */
  public void setBitsPerPixel(int iNo, int bpp) {
    this.imageMeta.get(iNo).bitsPerPixel = bpp;
  }

  /* @see Metadata#setChannelDimLengths(int, int[]) */
  public void setChannelDimLengths(int iNo, int[] cLengths) {
    this.imageMeta.get(iNo).cLengths = cLengths;
  }

  /* @see Metadata#setChannelDimTypes(int, String[]) */
  public void setChannelDimTypes(int iNo, String[] cTypes) {
    this.imageMeta.get(iNo).cTypes = cTypes;
  }

  /* @see Metadata#setDimensionOrder(int, String) */
  public void setDimensionOrder(int iNo, String order) {
    this.imageMeta.get(iNo).dimensionOrder = order;
  }

  /* @see Metadata#setOrderCertain(int, boolean) */
  public void setOrderCertain(int iNo, boolean orderCertain) {
    this.imageMeta.get(iNo).orderCertain = orderCertain;
  }

  /* @see Metadata#setRGB(int, boolean) */
  public void setRGB(int iNo, boolean rgb) {
    this.imageMeta.get(iNo).rgb = rgb;
  }

  /* @see Metadata#setLittleEndian(int, boolean) */
  public void setLittleEndian(int iNo, boolean littleEndian) {
    this.imageMeta.get(iNo).littleEndian = littleEndian;
  }

  /* @see Metadata#setInterleaved(int, boolean) */
  public void setInterleaved(int iNo, boolean interleaved) {
    this.imageMeta.get(iNo).interleaved = interleaved;
  }

  /* @see Metadata#setIndexed(int, boolean) */
  public void setIndexed(int iNo, boolean indexed) {
    this.imageMeta.get(iNo).indexed = indexed;
  }

  /* @see Metadata#setFalseColor(int, boolean) */
  public void setFalseColor(int iNo, boolean falseC) {
    this.imageMeta.get(iNo).falseColor = falseC;
  }

  /* @see Metadata#setMetadataComplete(int, metadataComplete) */
  public void setMetadataComplete(int iNo, boolean metadataComplete) {
    this.imageMeta.get(iNo).metadataComplete = metadataComplete;
  }

  /* @see Metadata#setImageMetadata(int, Hashtable<String, Object> */
  public void setImageMetadata(int iNo, Hashtable<String, Object> meta) {
    this.imageMeta.get(iNo).imageMetadata = meta;
  }

  /* @see Metadata#setThumbnail(int, boolean) */
  public void setThumbnailImage(int iNo, boolean thumbnail) {
    this.imageMeta.get(iNo).thumbnail = thumbnail;
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

  /*
  public boolean isSingleFile() {
    return this.size() <= 1;
  }
  
  public boolean hasCompanionFiles() {
    return false;
  }
  */
}
