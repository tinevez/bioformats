package scifio;

import java.io.File;
import java.io.IOException;

import scifio.io.RandomAccessInputStream;

public abstract class AbstractParser<M extends Metadata> implements Parser<M> {

	/* @see Parser#parse(String fileName) */
	public M parse(String fileName) throws IOException {
		return parse(new RandomAccessInputStream(fileName));
	}
	
	/* @see Parser#parse(File file) */
	public M parse(File file) throws IOException {
		return parse(new RandomAccessInputStream(file.getName()));
	}
}