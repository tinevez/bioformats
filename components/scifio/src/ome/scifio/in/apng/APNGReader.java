package ome.scifio.in.apng;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import java.util.zip.CRC32;

import loci.formats.FileInfo;

import ome.scifio.AbstractReader;
import ome.scifio.FormatException;
import ome.scifio.Metadata;
import ome.scifio.Reader;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.FormatTools;

/**
 * File format SCIFIO Reader for Animated Portable Network Graphics (APNG)
 * images.
 * 
 */
public class APNGReader extends AbstractReader<APNGMetadata>
{

    // -- Fields --

    private final Vector<APNGBlock> blocks;
    private final byte[][] lut;
    private final Vector<int[]> frameCoordinates;
    private String currentId;

    // -- Constructor --

    /** Constructs a new APNGReader. */
    public APNGReader(final APNGParser parser)
    {
        super("Animated PNG", "png");
        this.lut = parser.getLut();
        this.blocks = parser.getBlocks();
        this.frameCoordinates = parser.getFrameCoordinates();
        // TODO Auto-generated constructor stub
    }

    // -- Reader API Methods --

    @Override
    public byte[][] get8BitLookupTable() throws FormatException, IOException
    {
        FormatTools.assertId(currentId, true, 1);
        return null;
    }

    @Override
    public short[][] get16BitLookupTable() throws FormatException, IOException
    {
        // TODO Auto-generated method stub
        return null;
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
    public byte[] openBytes(final int no) throws FormatException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] openBytes(final int no, final Map<String, Integer> dims)
            throws FormatException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] openBytes(final int no, final byte[] buf)
            throws FormatException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] openBytes(final int no, final byte[] buf,
            final Map<String, Integer> dims) throws FormatException,
            IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object openPlane(final int no, final Map<String, Integer> dims)
            throws FormatException, IOException
    {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSeries(final int no)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getSeries()
    {
        // TODO Auto-generated method stub
        return 0;
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
        // TODO Auto-generated method stub
        return null;
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
    public void setMetadata(final APNGMetadata meta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public APNGMetadata getMetadata()
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

    // -- MetadataHandler API Methods --

    /* @see MetadataHandler#getMetadataTypes() */
    @Override
    public Class<APNGMetadata> getMetadataType()
    {
        return APNGMetadata.class;
    }

    // -- Helper methods --

    private long computeCRC(final byte[] buf, final int len)
    {
        final CRC32 crc = new CRC32();
        crc.update(buf, 0, len);
        return crc.getValue();
    }

}
