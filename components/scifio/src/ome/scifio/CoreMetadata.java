package ome.scifio;

import java.io.IOException;
import java.util.Hashtable;

import loci.formats.FormatException;

public class CoreMetadata extends AbstractMetadata {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object getMetadataValue(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSeriesMetadataValue(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hashtable<String, Object> getGlobalMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hashtable<String, Object> getSeriesMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoreMetadata[] getCoreMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOriginalMetadataPopulated(boolean populate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOriginalMetadataPopulated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Hashtable<String, Object> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getImageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setInterleaved(boolean interleaved) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInterleaved() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSize(String dim) {
		// TODO Auto-generated method stub
		return 0;
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
	public int getEffectiveSizeC() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRGBChannelCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLittleEndian() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIndexed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFalseColor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSingleFile(String id) throws FormatException, IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getPossibleDomains(String id) throws FormatException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCompanionFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMetadataComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRGB() {
		// TODO Auto-generated method stub
		return false;
	}

}
