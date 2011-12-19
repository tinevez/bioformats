package ome.scifio;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import ome.scifio.common.DataTools;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.FormatTools;

/**
 * Abstract superclass of all SCIFIO Parser components.
 *
 */
public abstract class AbstractParser<M extends Metadata> implements Parser<M> {

  // -- Fields --
  /** Current file. */
  protected RandomAccessInputStream in;

  /** Core metadata values. */
  protected M metadata;

  /** Name of current file. */
  protected String currentId;

  /** Whether or not to filter out invalid metadata. */
  protected boolean filterMetadata;

  /** Whether or not to save proprietary metadata in the MetadataStore. */
  protected boolean saveOriginalMetadata = false;

  // -- Constructors --

  // -- Parser API Methods --

  /* @see Parser#parse(File file) */

  public M parse(File file) throws IOException, FormatException {
    return parse(file.getName());
  }

  /* @see Parser#parse(String fileName) */

  public M parse(String fileName) throws IOException, FormatException {
    return parse(new RandomAccessInputStream(fileName));
  }

  /* @see Parser#parse(RandomAccessInputStream stream) */
  public M parse(RandomAccessInputStream stream)
    throws IOException, FormatException
  {
    if (in == null || !in.getFileName().equals(stream.getFileName())) {
      init(stream);

      if (saveOriginalMetadata) {
        //TODO store all metadata in OMEXML store.. or equivalent function? as per setId
      }
    }
    return metadata;
  }

  /* @see Parser#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    if (in != null) in.close();
    if (!fileOnly) {
      in = null;
    }
  }

  /* @see Parser#close() */
  public void close() throws IOException {
    close(false);
  }

  /* @see Parser#setOriginalMetadataPopulated(boolean) */
  public void setOriginalMetadataPopulated(boolean populate) {
    FormatTools.assertStream(in, false, 1);
    saveOriginalMetadata = populate;
  }

  /* @see Parser#isOriginalMetadataPopulated() */
  public boolean isOriginalMetadataPopulated() {
    return saveOriginalMetadata;
  }

  /* @see Parser#getUsedFiles() */
  public String[] getUsedFiles() {
    return getUsedFiles(false);
  }

  /* @see Parser#getUsedFiles() */
  public String[] getUsedFiles(boolean noPixels) {
    Vector<String> files = new Vector<String>();
    for (int i = 0; i < metadata.getImageCount(); i++) {
      String[] s = getImageUsedFiles(i, noPixels);
      if (s != null) {
        for (String file : s) {
          if (!files.contains(file)) {
            files.add(file);
          }
        }
      }
    }
    return files.toArray(new String[files.size()]);
  }

  /* @see Parser#setMetadataFiltered(boolean) */
  public void setMetadataFiltered(boolean filter) {
    FormatTools.assertStream(in, false, 1);
    filterMetadata = filter;
  }

  /* @see Parser#isMetadataFiltered() */
  public boolean isMetadataFiltered() {
    return filterMetadata;
  }

  /* @see Parser#getImageUsedFiles() */
  public String[] getImageUsedFiles(int image) {
    return getImageUsedFiles(image, false);
  }

  /* @see Parser#getImageUsedFiles(boolean) */
  public String[] getImageUsedFiles(int image, boolean noPixels) {
    return noPixels ? null : new String[] {in.getFileName()};
  }

  /* @see Parser#getAdvancedUsedFiles(boolean) */
  public FileInfo[] getAdvancedUsedFiles(final boolean noPixels) {
    // TODO Auto-generated method stub
    return null;
  }

  /* @see Parser#getAdvancedSeriesUsedFiles(boolean) */
  public FileInfo[] getAdvancedSeriesUsedFiles(final boolean noPixels) {
    // TODO Auto-generated method stub
    return null;
  }

  // -- AbstractParser Methods --

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, Object value) {
    addMeta(key, value, metadata.getGlobalMetadata());
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, boolean value) {
    addGlobalMeta(key, new Boolean(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, byte value) {
    addGlobalMeta(key, new Byte(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, short value) {
    addGlobalMeta(key, new Short(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, int value) {
    addGlobalMeta(key, new Integer(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, long value) {
    addGlobalMeta(key, new Long(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, float value) {
    addGlobalMeta(key, new Float(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, double value) {
    addGlobalMeta(key, new Double(value));
  }

  /** Adds an entry to the global metadata table. */
  public void addGlobalMeta(String key, char value) {
    addGlobalMeta(key, new Character(value));
  }

  /** Gets a value from the global metadata table. */
  public Object getGlobalMeta(String key) {
    return metadata.getGlobalMetadata().get(key);
  }

  /** Adds an entry to the specified Hashtable. */
  public void addMeta(String key, Object value,
    Hashtable<String, Object> meta)
  {
    if (key == null || value == null /* || TODO !isMetadataCollected() */) {
      return;
    }

    key = key.trim();

    boolean string = value instanceof String || value instanceof Character;
    boolean simple =
      string || value instanceof Number || value instanceof Boolean;

    // string value, if passed in value is a string
    String val = string ? String.valueOf(value) : null;

    if (filterMetadata || (saveOriginalMetadata
    /* TODO: check if this Parser's metadata is OMEXML metadata &&
     *  (getMetadataStore() instanceof OMEXMLMetadata)*/))
    {
      // filter out complex data types
      if (!simple) return;

      // verify key & value are reasonable length
      int maxLen = 8192;
      if (key.length() > maxLen) return;
      if (string && val.length() > maxLen) return;

      // remove all non-printable characters
      key = DataTools.sanitize(key);
      if (string) val = DataTools.sanitize(val);

      // verify key contains at least one alphabetic character
      if (!key.matches(".*[a-zA-Z].*")) return;

      // remove &lt;, &gt; and &amp; to prevent XML parsing errors
      String[] invalidSequences =
        new String[] {"&lt;", "&gt;", "&amp;", "<", ">", "&"};
      for (int i = 0; i < invalidSequences.length; i++) {
        key = key.replaceAll(invalidSequences[i], "");
        if (string) val = val.replaceAll(invalidSequences[i], "");
      }

      // verify key & value are not empty
      if (key.length() == 0) return;
      if (string && val.trim().length() == 0) return;

      if (string) value = val;
    }

    meta.put(key, val == null ? value : val);
  }

  private void init(RandomAccessInputStream stream) throws IOException {

    if (in != null) {
      String[] s = getUsedFiles();
      for (int i = 0; i < s.length; i++) {
        if (in.getFileName().equals(s[i])) return;
      }
    }

    close();
    this.in = stream;
  }
}