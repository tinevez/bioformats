package ome.scifio;

import java.io.IOException;

import ome.scifio.FormatException;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.FormatTools;

/**
 * Abstract superclass of all SCIFIO reader components.
 *
 */
public abstract class AbstractReader<M extends Metadata>
  extends AbstractFormatHandler implements Reader<M> {

  // -- Fields --
  /** Metadata values. */
  protected M[] metadata;
  
  /** Current file. */
  protected RandomAccessInputStream in;
  
  /** List of domains in which this format is used. */
  protected String[] domains = new String[0];

  /** Name of current file. */
  protected String currentId;
  
  /** The number of the current series. */
  private int series;
  
  // -- Constructors --

  /** Constructs a reader with the given name and default suffix */
  public AbstractReader(String format, String suffix) {
    super(format, suffix);
    // TODO Auto-generated constructor stub
  }

  /** Constructs a reader with the given name and default suffixes */
  public AbstractReader(String format, String[] suffixes) {
    super(format, suffixes);
    // TODO Auto-generated constructor stub
  }

  // -- Reader API Methods --
  public void setMetadataArray(M[] meta) {
    this.metadata = meta;
  }

  public M[] getMetadataArray() {
    return this.metadata;
  }
  
  /* @see IFormatReader#isIndexed() */
  public boolean isIndexed() {
    FormatTools.assertId(currentId, true, 1);
    return metadata[series].isIndexed();
  }
  
  /* @see IFormatReader#isInterleaved() */
  public boolean isInterleaved() {
    return isInterleaved(0);
  }

  /* @see IFormatReader#isInterleaved(int) */
  public boolean isInterleaved(int subC) {
    FormatTools.assertId(currentId, true, 1);
    return metadata[series].isInterleaved();
  }
  
  /* @see IFormatReader#get8BitLookupTable() */
  public byte[][] get8BitLookupTable() throws FormatException, IOException {
    return null;
  }

  /* @see IFormatReader#get16BitLookupTable() */
  public short[][] get16BitLookupTable() throws FormatException, IOException {
    return null;
  }
  
  /* @see IFormatReader.isLittleEndian() */
  public boolean isLittleEndian() {
    FormatTools.assertId(currentId, true, 1);
    return metadata[series].isLittleEndian();
  }
  
  /* @see Parser#setSeries(int) */
  public void setSeries(int no) {
    if (no < 0 || no >= getSeriesCount()) {
      throw new IllegalArgumentException("Invalid series: " + no);
    }
    series = no;
  }
  
  /* @see Parser#getSeries() */
  public int getSeries() {
    return series;
  }
  
  /* @see IFormatReader#getEffectiveSizeC() */
  public int getEffectiveSizeC() {
    // NB: by definition, imageCount == effectiveSizeC * sizeZ * sizeT
    int sizeZT = getSizeZ() * getSizeT();
    if (sizeZT == 0) return 0;
    return getImageCount() / sizeZT;
  }

	@Override
	public int getRGBChannelCount() {
	    int effSizeC = getEffectiveSizeC();
	    if (effSizeC == 0) return 0;
	    return getSizeC() / effSizeC;
	}

	@Override
	public int getSizeX() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getSizeX();
	}

	@Override
	public int getSizeY() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getSizeY();
	}

	@Override
	public int getSizeZ() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getSizeZ();
	}

	@Override
	public int getSizeC() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getSizeC();
	}

	@Override
	public int getSizeT() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getSizeT();
	}

	@Override
	public int getImageCount() {
	    FormatTools.assertId(currentId, true, 1);
	    return metadata[series].getImageCount();
	}

	@Override
	public int getPixelType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBitsPerPixel() {
		// TODO Auto-generated method stub
		return 0;
	}
	
    @Override
    public int[] getChannelDimLengths()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getChannelDimTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getThumbSizeX()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getThumbSizeY()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isThumbnailSeries()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public byte[] openThumbBytes(final int no) throws FormatException,
            IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSeriesCount()
    {
    	System.out.println("AR current id: " + currentId);
        FormatTools.assertId(currentId, true, 1);
        return metadata.length;
    }

    @Override
    public void setGroupFiles(final boolean group)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isGroupFiles()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int fileGroupOption(final String id) throws FormatException,
            IOException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String[] getUsedFiles()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getUsedFiles(final boolean noPixels)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSeriesUsedFiles()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSeriesUsedFiles(final boolean noPixels)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInfo[] getAdvancedUsedFiles(final boolean noPixels)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileInfo[] getAdvancedSeriesUsedFiles(final boolean noPixels)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCurrentFile()
    {
        return currentId;
    }

    @Override
    public String[] getDomains()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIndex(final int z, final int c, final int t)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int[] getZCTCoords(final int index)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStream(final RandomAccessInputStream stream)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public RandomAccessInputStream getStream()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reader<Metadata>[] getUnderlyingReaders()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOptimalTileWidth()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getOptimalTileHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setNormalized(final boolean normalize)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isNormalized()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte[] openBytes(final int no) throws FormatException, IOException
    {
        return openBytes(no, 0, 0, getSizeX(), getSizeY());
    }
    
	@Override
	public byte[] openBytes(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
	    int bpp = FormatTools.getBytesPerPixel(getPixelType());
	    int ch = getRGBChannelCount();
	    byte[] newBuffer = new byte[w * h * ch * bpp];
	    return openBytes(no, newBuffer, x, y, w, h);
	}

	@Override
	public byte[] openBytes(int no, byte[] buf) throws FormatException,
			IOException {
		return openBytes(no, buf, 0, 0, getSizeX(), getSizeY());
	}

	@Override
	public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
			throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}


  // -- AbstractReader Methods --

  /* TODO seems a bit off... if we write to an output stream, do we need to know if a filename is valid? 
   * should this even be in reader at all? */
  public boolean supportsFormat(String name) {
    return checkSuffix(name, suffixes);
  }
  
  protected void setCurrentId(String id) {
	  this.currentId = id;
  }
  
  protected void setIn(RandomAccessInputStream stream) {
	  this.in = stream;
  }
}
