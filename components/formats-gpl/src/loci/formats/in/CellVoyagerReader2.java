package loci.formats.in;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import loci.common.services.ServiceException;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.ome.OMEXMLMetadataImpl;

public class CellVoyagerReader2 extends OMETiffReader
{
	public CellVoyagerReader2()
	{
		this.format = "CellVoyager";
		this.suffixes = new String[] { "tif", "xml" };
		this.suffixNecessary = false;
		this.suffixSufficient = false;
		this.hasCompanionFiles = true;
		this.datasetDescription = "Directory with two master files 'MeasurementResult.xml' and 'MeasurementResult.ome.xml', used to stich together several TIF files.";
		this.domains = new String[] { FormatTools.HISTOLOGY_DOMAIN, FormatTools.LM_DOMAIN, FormatTools.HCS_DOMAIN };
	}

	/*
	 * METHODS
	 */

	@Override
	public void setId( final String id ) throws FormatException, IOException
	{
		setMetadataStore( new OMEXMLMetadataImpl() );
		super.setId( id );

		final OMEXMLMetadataImpl meta = ( OMEXMLMetadataImpl ) getMetadataStore();
	}

	/*
	 * MAIN METHOD
	 */

	public static void main( final String[] args ) throws IOException, FormatException, ServiceException
	{
//		final String id = "/Users/tinevez/Projects/EArena/Data/O2Hypoxia/150423_invivoO2_ACQ03/3_1_2_1_2/20150423T092216/MeasurementResult.xml";
		final String id = "/Users/tinevez/Projects/EArena/Data/O2Hypoxia/150423_invivoO2_ACQ03/3_1_2_1_2/20150423T092216/MeasurementResult.ome.tif";

		final CellVoyagerReader2 importer = new CellVoyagerReader2();
		importer.setId( id );

		final List< CoreMetadata > cms = importer.getCoreMetadataList();
		System.out.println( "Core metadata:" );// DEBUG
		for ( final CoreMetadata coreMetadata : cms )
		{
			System.out.println( coreMetadata );
		}

		System.out.println( "\nGlobal metadata:" );// DEBUG
		final Hashtable< String, Object > meta = importer.getGlobalMetadata();
		final String[] keys = MetadataTools.keys( meta );
		for ( final String key : keys )
		{
			System.out.println( key + " = " + meta.get( key ) );
		}

		System.out.println( "\nImporter: " + importer );// DEBUG

//		System.out.println( "\nFiles:" );// DEBUG
//		final String[] usedFiles = importer.getSeriesUsedFiles();
//		for ( final String file : usedFiles )
//		{
//			System.out.println( "  " + file );
//		}

//		System.out.println( "\n\nSeries 0" );// DEBUG
//		importer.setSeries( 0 );
//		final byte[] bytes0 = importer.openBytes( 0 );
//		System.out.println( "Read " + bytes0.length + " bytes for plane 0." );// DEBUG

		System.out.println( "\n\nSeries 1" );// DEBUG
		importer.setSeries( 1 );
		final byte[] bytes1 = importer.openBytes( 0 );
		System.out.println( "Read " + bytes1.length + " bytes for plane 0." );// DEBUG

		importer.close();

	}
}
