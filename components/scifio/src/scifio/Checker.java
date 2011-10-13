package scifio;

import java.io.IOException;

import loci.common.RandomAccessInputStream;

/**
 * Interface for all SciFIO Checker objects.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="">Trac</a>,
 * <a href="">Gitweb</a></dd></dl>
 */
public interface Checker<M extends Metadata> extends MetadataHandler<M>, FormatHandler {

	// -- Checker API methods --
	
	/**
	 * Checks if the given file is a valid instance of this file format.
	 *
	 * @param open If true, and the file extension is insufficient to determine
	 *   the file type, the file may be opened for further analysis, or other
	 *   relatively expensive file system operations (such as file existence
	 *   tests and directory listings) may be performed.  If false, file system
	 *   access is not allowed.
	 */
	boolean supportsFormat(String name, boolean open);

	/** Checks if the given block is a valid header for this file format. */
	boolean supportsFormat(byte[] block);

	/** Checks if the given stream is a valid stream for this file format. */
	boolean supportsFormat(RandomAccessInputStream stream) throws IOException;
}
