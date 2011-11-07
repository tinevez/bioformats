package ome.scifio.in.apng;

import java.io.IOException;
import ome.scifio.AbstractChecker;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.io.StreamTools;

/**
 * File format SCIFIO Checker for Animated Portable Network Graphics
 * (APNG) images.
 *
 */
public class APNGChecker extends AbstractChecker<APNGMetadata>{

	// -- Fields --
	
	// -- Constructor --

	/** Constructs a new APNGChecker */
	public APNGChecker() {
		super("Animated PNG", "png");
		suffixNecessary = false;
		// TODO Auto-generated constructor stub
	}
	
	// -- Checker API Methods --
	
	/* @see ome.scifio.Checker#isFormat(RandomAccessInputStream stream) */
	public boolean isFormat(RandomAccessInputStream stream) throws IOException {
	    final int blockLen = 8;
	    if (!StreamTools.validStream(stream, blockLen, false)) return false;

	    byte[] signature = new byte[blockLen];
	    stream.read(signature);

	    if (signature[0] != (byte) 0x89 || signature[1] != 0x50 ||
	      signature[2] != 0x4e || signature[3] != 0x47 || signature[4] != 0x0d ||
	      signature[5] != 0x0a || signature[6] != 0x1a || signature[7] != 0x0a)
	    {
	      return false;
	    }
	    return true;
	}
	
	// -- MetadataHandler API Methods --

	/* @see MetadataHandler#getMetadataTypes() */
	public Class<APNGMetadata> getMetadataType() {
		return APNGMetadata.class;
	}
}
