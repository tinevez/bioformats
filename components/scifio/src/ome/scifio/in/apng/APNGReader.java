package ome.scifio.in.apng;

import java.io.IOException;
import java.util.Map;
import java.util.zip.CRC32;

import loci.formats.FileInfo;
import loci.formats.FormatException;
import ome.scifio.AbstractReader;
import ome.scifio.Metadata;
import ome.scifio.Reader;
import ome.scifio.io.RandomAccessInputStream;

/**
 * File format SCIFIO Reader for Animated Portable Network Graphics
 * (APNG) images.
 *
 */
public class APNGReader extends AbstractReader<APNGMetadata> {

	// -- Constructor --
	
	/** Constructs a new APNGReader. */
	public APNGReader(String format, String suffix) {
		super(format, suffix);
		// TODO Auto-generated constructor stub
	}
	
	// -- Reader API Methods --

	@Override
	public byte[][] get8BitLookupTable() throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short[][] get16BitLookupTable() throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getChannelDimLengths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getChannelDimTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getThumbSizeX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getThumbSizeY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isThumbnailSeries() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] openBytes(int no) throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] openBytes(int no, Map<String, Integer> dims)
			throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] openBytes(int no, byte[] buf) throws FormatException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] openBytes(int no, byte[] buf, Map<String, Integer> dims)
			throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object openPlane(int no, Map<String, Integer> dims)
			throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] openThumbBytes(int no) throws FormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSeriesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSeries(int no) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSeries() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setGroupFiles(boolean group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isGroupFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int fileGroupOption(String id) throws FormatException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getUsedFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUsedFiles(boolean noPixels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSeriesUsedFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSeriesUsedFiles(boolean noPixels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileInfo[] getAdvancedUsedFiles(boolean noPixels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileInfo[] getAdvancedSeriesUsedFiles(boolean noPixels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDomains() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex(int z, int c, int t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getZCTCoords(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMetadata(APNGMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public APNGMetadata getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStream(RandomAccessInputStream stream) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RandomAccessInputStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader<Metadata>[] getUnderlyingReaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOptimalTileWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOptimalTileHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNormalized(boolean normalize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNormalized() {
		// TODO Auto-generated method stub
		return false;
	}

	// -- MetadataHandler API Methods --

	/* @see MetadataHandler#getMetadataTypes() */
	public Class<APNGMetadata> getMetadataType() {
		return APNGMetadata.class;
	}

	// -- Helper methods --

	private long computeCRC(byte[] buf, int len) {
		CRC32 crc = new CRC32();
		crc.update(buf, 0, len);
		return crc.getValue();
	}

}
