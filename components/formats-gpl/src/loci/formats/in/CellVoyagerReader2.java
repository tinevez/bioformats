package loci.formats.in;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import loci.common.services.ServiceException;
import loci.common.xml.XMLTools;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.Modulo;
import loci.formats.ome.OMEXMLMetadata;
import loci.formats.ome.OMEXMLMetadataImpl;
import loci.formats.services.OMEXMLService;
import ome.xml.meta.OMEXMLMetadataRoot;
import ome.xml.model.Annotation;
import ome.xml.model.Image;
import ome.xml.model.XMLAnnotation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CellVoyagerReader2 extends OMETiffReader
{
	private static final String ANNOTATION_MODULO_NAMESPACE = "openmicroscopy.org/omero/dimension/modulo";

	private OMEXMLService service;

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
		final OMEXMLMetadataRoot root = ( OMEXMLMetadataRoot ) meta.getRoot();
		
		/*
		 * Is there a modulo annotation on this file?
		 */
		

		final int xmlAnnotationCount = meta.getXMLAnnotationCount();
		if (xmlAnnotationCount == 0) {
			System.out.println("No xml annotation found, regular tiling.");// DEBUG
			return;
		}

		
		System.out.println("Found " + xmlAnnotationCount + " XML annotations.");// DEBUG
		for ( int anIndex = 0; anIndex < xmlAnnotationCount; anIndex++ )
		{
			final String xmlAnnotationID = meta.getXMLAnnotationID( anIndex );
			final String xmlAnnotationNamespace = meta.getXMLAnnotationNamespace( anIndex );
			if ( ANNOTATION_MODULO_NAMESPACE.equals( xmlAnnotationNamespace ) )
			{
				System.out.println( meta.getXMLAnnotationValue( anIndex ) );
			}
		}


		final int serisCount = getSeriesCount();
		for ( int s = 0; s < serisCount; s++ )
		{
			System.out.println("For series "+s);// DEBUG
			System.out.println( getModuloAlongT( meta, s ) ); // DESPAIR DISMAY
		}

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

	/**
	 * @see OMEXMLService#getModuloAlongZ(OMEXMLMetadata, int)
	 */
	public Modulo getModuloAlongZ( final OMEXMLMetadata omexml, final int image )
	{
		return getModuloAlong( omexml, "ModuloAlongZ", image );
	}

	/**
	 * @see OMEXMLService#getModuloAlongC(OMEXMLMetadata, int)
	 */
	public Modulo getModuloAlongC( final OMEXMLMetadata omexml, final int image )
	{
		return getModuloAlong( omexml, "ModuloAlongC", image );
	}

	/**
	 * @see OMEXMLService#getModuloAlongT(OMEXMLMetadata, int)
	 */
	public Modulo getModuloAlongT( final OMEXMLMetadata omexml, final int image )
	{
		return getModuloAlong( omexml, "ModuloAlongT", image );
	}

	private Modulo getModuloAlong( final OMEXMLMetadata omexml, final String tag, final int image )
	{
		final OMEXMLMetadataRoot root = ( OMEXMLMetadataRoot ) omexml.getRoot();
		final Image img = root.getImage( image );
		if ( img == null ) { return null; }

		for ( int i = 0; i < img.sizeOfLinkedAnnotationList(); i++ )
		{
			final Annotation annotation = img.getLinkedAnnotation( i );
			if ( !( annotation instanceof XMLAnnotation ) )
			{
				continue;
			}

			final String xml = ( ( XMLAnnotation ) annotation ).getValue();

			try
			{
				final Document annotationRoot = XMLTools.parseDOM( xml );
				final NodeList nodes = annotationRoot.getElementsByTagName( tag );

				if ( nodes.getLength() > 0 )
				{
					final Element modulo = ( Element ) nodes.item( 0 );
					final NamedNodeMap attrs = modulo.getAttributes();

					final Modulo m = new Modulo( tag.substring( tag.length() - 1 ) );

					final Node start = attrs.getNamedItem( "Start" );
					final Node end = attrs.getNamedItem( "End" );
					final Node step = attrs.getNamedItem( "Step" );
					final Node type = attrs.getNamedItem( "Type" );
					final Node typeDescription = attrs.getNamedItem( "TypeDescription" );
					final Node unit = attrs.getNamedItem( "Unit" );

					if ( start != null )
					{
						m.start = Double.parseDouble( start.getNodeValue() );
					}
					if ( end != null )
					{
						m.end = Double.parseDouble( end.getNodeValue() );
					}
					if ( step != null )
					{
						m.step = Double.parseDouble( step.getNodeValue() );
					}
					if ( type != null )
					{
						m.type = type.getNodeValue();
					}
					if ( typeDescription != null )
					{
						m.typeDescription = typeDescription.getNodeValue();
					}
					if ( unit != null )
					{
						m.unit = unit.getNodeValue();
					}

					final NodeList labels = modulo.getElementsByTagName( "Label" );
					if ( labels != null && labels.getLength() > 0 )
					{
						m.labels = new String[ labels.getLength() ];
						for ( int q = 0; q < labels.getLength(); q++ )
						{
							m.labels[ q ] = labels.item( q ).getTextContent();
						}
					}

					return m;
				}
			}
			catch ( final ParserConfigurationException e )
			{
				LOGGER.debug( "Failed to parse ModuloAlong", e );
			}
			catch ( final SAXException e )
			{
				LOGGER.debug( "Failed to parse ModuloAlong", e );
			}
			catch ( final IOException e )
			{
				LOGGER.debug( "Failed to parse ModuloAlong", e );
			}
		}
		return null;
	}
}
