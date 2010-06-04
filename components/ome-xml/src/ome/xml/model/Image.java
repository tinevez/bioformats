/*
 * ome.xml.model.Image
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) @year@ Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee,
 *      University of Wisconsin-Madison
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *-----------------------------------------------------------------------------
 */

/*-----------------------------------------------------------------------------
 *
 * THIS IS AUTOMATICALLY GENERATED CODE.  DO NOT MODIFY.
 * Created by melissa via xsd-fu on 2010-06-03 10:29:33.559265
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ome.xml.model.enums.*;
import ome.xml.model.primitives.*;

public class Image extends AbstractOMEModelObject
{
	// Base:  -- Name: Image -- Type: Image -- javaBase: AbstractOMEModelObject -- javaType: Object

	// -- Constants --

	public static final String NAMESPACE = "http://www.openmicroscopy.org/Schemas/OME/2010-04";

	/** Logger for this class. */
	private static final Logger LOGGER =
		LoggerFactory.getLogger(Image.class);

	// -- Instance variables --


	// Property
	private String id;

	// Property
	private String name;

	// Property
	private String acquiredDate;

	// Property
	private Experimenter experimenter;

	// Property
	private String description;

	// Property
	private Experiment experiment;

	// Property
	private Group group;

	// Reference DatasetRef
	private List<Dataset> datasetList = new ArrayList<Dataset>();

	// Property
	private Instrument instrument;

	// Property
	private ObjectiveSettings objectiveSettings;

	// Property
	private ImagingEnvironment imagingEnvironment;

	// Property
	private StageLabel stageLabel;

	// Property
	private Pixels pixels;

	// Reference ROIRef
	private List<ROI> roiList = new ArrayList<ROI>();

	// Reference MicrobeamManipulationRef
	private List<MicrobeamManipulation> microbeamManipulationList = new ArrayList<MicrobeamManipulation>();

	// Reference AnnotationRef
	private List<Annotation> annotationList = new ArrayList<Annotation>();

	// Back reference WellSample_BackReference
	private List<WellSample> wellSample_BackReferenceList = new ArrayList<WellSample>();

	// -- Constructors --

	/** Default constructor. */
	public Image()
	{
		super();
	}

	/** 
	 * Constructs Image recursively from an XML DOM tree.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @param model Handler for the OME model which keeps track of instances
	 * and references seen during object population.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public Image(Element element, OMEModel model)
	    throws EnumerationException
	{
		update(element, model);
	}

	// -- Custom content from Image specific template --


	// -- OMEModelObject API methods --

	/** 
	 * Updates Image recursively from an XML DOM tree. <b>NOTE:</b> No
	 * properties are removed, only added or updated.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @param model Handler for the OME model which keeps track of instances
	 * and references seen during object population.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public void update(Element element, OMEModel model)
	    throws EnumerationException
	{
		super.update(element, model);
		String tagName = element.getTagName();
		if (!"Image".equals(tagName))
		{
			LOGGER.debug("Expecting node name of Image got {}", tagName);
		}
		if (!element.hasAttribute("ID") && getID() == null)
		{
			// TODO: Should be its own exception
			throw new RuntimeException(String.format(
					"Image missing required ID property."));
		}
		if (element.hasAttribute("ID"))
		{
			// ID property
			setID(String.valueOf(
						element.getAttribute("ID")));
			// Adding this model object to the model handler
			model.addModelObject(getID(), this);
		}
		if (element.hasAttribute("Name"))
		{
			// Attribute property Name
			setName(String.valueOf(
					element.getAttribute("Name")));
		}
		List<Element> AcquiredDate_nodeList =
				getChildrenByTagName(element, "AcquiredDate");
		if (AcquiredDate_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"AcquiredDate node list size %d != 1",
					AcquiredDate_nodeList.size()));
		}
		else if (AcquiredDate_nodeList.size() != 0)
		{
			// Element property AcquiredDate which is not complex (has no
			// sub-elements)
			setAcquiredDate(
					String.valueOf(AcquiredDate_nodeList.get(0).getTextContent()));
		}
		// Element reference ExperimenterRef
		List<Element> ExperimenterRef_nodeList =
				getChildrenByTagName(element, "ExperimenterRef");
		for (Element ExperimenterRef_element : ExperimenterRef_nodeList)
		{
			ExperimenterRef experimenter_reference = new ExperimenterRef();
			experimenter_reference.setID(ExperimenterRef_element.getAttribute("ID"));
			model.addReference(this, experimenter_reference);
		}
		List<Element> Description_nodeList =
				getChildrenByTagName(element, "Description");
		if (Description_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"Description node list size %d != 1",
					Description_nodeList.size()));
		}
		else if (Description_nodeList.size() != 0)
		{
			// Element property Description which is not complex (has no
			// sub-elements)
			setDescription(
					String.valueOf(Description_nodeList.get(0).getTextContent()));
		}
		// Element reference ExperimentRef
		List<Element> ExperimentRef_nodeList =
				getChildrenByTagName(element, "ExperimentRef");
		for (Element ExperimentRef_element : ExperimentRef_nodeList)
		{
			ExperimentRef experiment_reference = new ExperimentRef();
			experiment_reference.setID(ExperimentRef_element.getAttribute("ID"));
			model.addReference(this, experiment_reference);
		}
		// Element reference GroupRef
		List<Element> GroupRef_nodeList =
				getChildrenByTagName(element, "GroupRef");
		for (Element GroupRef_element : GroupRef_nodeList)
		{
			GroupRef group_reference = new GroupRef();
			group_reference.setID(GroupRef_element.getAttribute("ID"));
			model.addReference(this, group_reference);
		}
		// Element reference DatasetRef
		List<Element> DatasetRef_nodeList =
				getChildrenByTagName(element, "DatasetRef");
		for (Element DatasetRef_element : DatasetRef_nodeList)
		{
			DatasetRef datasetList_reference = new DatasetRef();
			datasetList_reference.setID(DatasetRef_element.getAttribute("ID"));
			model.addReference(this, datasetList_reference);
		}
		// Element reference InstrumentRef
		List<Element> InstrumentRef_nodeList =
				getChildrenByTagName(element, "InstrumentRef");
		for (Element InstrumentRef_element : InstrumentRef_nodeList)
		{
			InstrumentRef instrument_reference = new InstrumentRef();
			instrument_reference.setID(InstrumentRef_element.getAttribute("ID"));
			model.addReference(this, instrument_reference);
		}
		List<Element> ObjectiveSettings_nodeList =
				getChildrenByTagName(element, "ObjectiveSettings");
		if (ObjectiveSettings_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"ObjectiveSettings node list size %d != 1",
					ObjectiveSettings_nodeList.size()));
		}
		else if (ObjectiveSettings_nodeList.size() != 0)
		{
			// Element property ObjectiveSettings which is complex (has
			// sub-elements)
			setObjectiveSettings(new ObjectiveSettings(
					(Element) ObjectiveSettings_nodeList.get(0), model));
		}
		List<Element> ImagingEnvironment_nodeList =
				getChildrenByTagName(element, "ImagingEnvironment");
		if (ImagingEnvironment_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"ImagingEnvironment node list size %d != 1",
					ImagingEnvironment_nodeList.size()));
		}
		else if (ImagingEnvironment_nodeList.size() != 0)
		{
			// Element property ImagingEnvironment which is complex (has
			// sub-elements)
			setImagingEnvironment(new ImagingEnvironment(
					(Element) ImagingEnvironment_nodeList.get(0), model));
		}
		List<Element> StageLabel_nodeList =
				getChildrenByTagName(element, "StageLabel");
		if (StageLabel_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"StageLabel node list size %d != 1",
					StageLabel_nodeList.size()));
		}
		else if (StageLabel_nodeList.size() != 0)
		{
			// Element property StageLabel which is complex (has
			// sub-elements)
			setStageLabel(new StageLabel(
					(Element) StageLabel_nodeList.get(0), model));
		}
		List<Element> Pixels_nodeList =
				getChildrenByTagName(element, "Pixels");
		if (Pixels_nodeList.size() > 1)
		{
			// TODO: Should be its own Exception
			throw new RuntimeException(String.format(
					"Pixels node list size %d != 1",
					Pixels_nodeList.size()));
		}
		else if (Pixels_nodeList.size() != 0)
		{
			// Element property Pixels which is complex (has
			// sub-elements)
			setPixels(new Pixels(
					(Element) Pixels_nodeList.get(0), model));
		}
		// Element reference ROIRef
		List<Element> ROIRef_nodeList =
				getChildrenByTagName(element, "ROIRef");
		for (Element ROIRef_element : ROIRef_nodeList)
		{
			ROIRef roiList_reference = new ROIRef();
			roiList_reference.setID(ROIRef_element.getAttribute("ID"));
			model.addReference(this, roiList_reference);
		}
		// Element reference MicrobeamManipulationRef
		List<Element> MicrobeamManipulationRef_nodeList =
				getChildrenByTagName(element, "MicrobeamManipulationRef");
		for (Element MicrobeamManipulationRef_element : MicrobeamManipulationRef_nodeList)
		{
			MicrobeamManipulationRef microbeamManipulationList_reference = new MicrobeamManipulationRef();
			microbeamManipulationList_reference.setID(MicrobeamManipulationRef_element.getAttribute("ID"));
			model.addReference(this, microbeamManipulationList_reference);
		}
		// Element reference AnnotationRef
		List<Element> AnnotationRef_nodeList =
				getChildrenByTagName(element, "AnnotationRef");
		for (Element AnnotationRef_element : AnnotationRef_nodeList)
		{
			AnnotationRef annotationList_reference = new AnnotationRef();
			annotationList_reference.setID(AnnotationRef_element.getAttribute("ID"));
			model.addReference(this, annotationList_reference);
		}
		// *** IGNORING *** Skipped back reference WellSample_BackReference
	}

	// -- Image API methods --

	public void link(Reference reference, OMEModelObject o)
	{
		if (reference instanceof ExperimenterRef)
		{
			Experimenter o_casted = (Experimenter) o;
			o_casted.linkImage(this);
			experimenter = o_casted;
			return;
		}
		if (reference instanceof ExperimentRef)
		{
			Experiment o_casted = (Experiment) o;
			o_casted.linkImage(this);
			experiment = o_casted;
			return;
		}
		if (reference instanceof GroupRef)
		{
			Group o_casted = (Group) o;
			o_casted.linkImage(this);
			group = o_casted;
			return;
		}
		if (reference instanceof DatasetRef)
		{
			Dataset o_casted = (Dataset) o;
			o_casted.linkImage(this);
			datasetList.add(o_casted);
			return;
		}
		if (reference instanceof InstrumentRef)
		{
			Instrument o_casted = (Instrument) o;
			o_casted.linkImage(this);
			instrument = o_casted;
			return;
		}
		if (reference instanceof ROIRef)
		{
			ROI o_casted = (ROI) o;
			o_casted.linkImage(this);
			roiList.add(o_casted);
			return;
		}
		if (reference instanceof MicrobeamManipulationRef)
		{
			MicrobeamManipulation o_casted = (MicrobeamManipulation) o;
			o_casted.linkImage(this);
			microbeamManipulationList.add(o_casted);
			return;
		}
		if (reference instanceof AnnotationRef)
		{
			Annotation o_casted = (Annotation) o;
			o_casted.linkImage(this);
			annotationList.add(o_casted);
			return;
		}
		// TODO: Should be its own Exception
		throw new RuntimeException(
				"Unable to handle reference of type: " + reference.getClass());
	}


	// Property
	public String getID()
	{
		return id;
	}

	public void setID(String id)
	{
		this.id = id;
	}

	// Property
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// Property
	public String getAcquiredDate()
	{
		return acquiredDate;
	}

	public void setAcquiredDate(String acquiredDate)
	{
		this.acquiredDate = acquiredDate;
	}

	// Reference
	public Experimenter getLinkedExperimenter()
	{
		return experimenter;
	}

	public void linkExperimenter(Experimenter o)
	{
		experimenter = o;
	}

	public void unlinkExperimenter(Experimenter o)
	{
		if (experimenter == o)
		{
			experimenter = null;
		}
	}

	// Property
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	// Reference
	public Experiment getLinkedExperiment()
	{
		return experiment;
	}

	public void linkExperiment(Experiment o)
	{
		experiment = o;
	}

	public void unlinkExperiment(Experiment o)
	{
		if (experiment == o)
		{
			experiment = null;
		}
	}

	// Reference
	public Group getLinkedGroup()
	{
		return group;
	}

	public void linkGroup(Group o)
	{
		group = o;
	}

	public void unlinkGroup(Group o)
	{
		if (group == o)
		{
			group = null;
		}
	}

	// Reference which occurs more than once
	public int sizeOfLinkedDatasetList()
	{
		return datasetList.size();
	}

	public List<Dataset> copyLinkedDatasetList()
	{
		return new ArrayList<Dataset>(datasetList);
	}

	public Dataset getLinkedDataset(int index)
	{
		return datasetList.get(index);
	}

	public Dataset setLinkedDataset(int index, Dataset o)
	{
		return datasetList.set(index, o);
	}

	public boolean linkDataset(Dataset o)
	{
		o.linkImage(this);
		return datasetList.add(o);
	}

	public boolean unlinkDataset(Dataset o)
	{
		o.unlinkImage(this);
		return datasetList.remove(o);
	}

	// Reference
	public Instrument getLinkedInstrument()
	{
		return instrument;
	}

	public void linkInstrument(Instrument o)
	{
		instrument = o;
	}

	public void unlinkInstrument(Instrument o)
	{
		if (instrument == o)
		{
			instrument = null;
		}
	}

	// Property
	public ObjectiveSettings getObjectiveSettings()
	{
		return objectiveSettings;
	}

	public void setObjectiveSettings(ObjectiveSettings objectiveSettings)
	{
		this.objectiveSettings = objectiveSettings;
	}

	// Property
	public ImagingEnvironment getImagingEnvironment()
	{
		return imagingEnvironment;
	}

	public void setImagingEnvironment(ImagingEnvironment imagingEnvironment)
	{
		this.imagingEnvironment = imagingEnvironment;
	}

	// Property
	public StageLabel getStageLabel()
	{
		return stageLabel;
	}

	public void setStageLabel(StageLabel stageLabel)
	{
		this.stageLabel = stageLabel;
	}

	// Property
	public Pixels getPixels()
	{
		return pixels;
	}

	public void setPixels(Pixels pixels)
	{
		this.pixels = pixels;
	}

	// Reference which occurs more than once
	public int sizeOfLinkedROIList()
	{
		return roiList.size();
	}

	public List<ROI> copyLinkedROIList()
	{
		return new ArrayList<ROI>(roiList);
	}

	public ROI getLinkedROI(int index)
	{
		return roiList.get(index);
	}

	public ROI setLinkedROI(int index, ROI o)
	{
		return roiList.set(index, o);
	}

	public boolean linkROI(ROI o)
	{
		o.linkImage(this);
		return roiList.add(o);
	}

	public boolean unlinkROI(ROI o)
	{
		o.unlinkImage(this);
		return roiList.remove(o);
	}

	// Reference which occurs more than once
	public int sizeOfLinkedMicrobeamManipulationList()
	{
		return microbeamManipulationList.size();
	}

	public List<MicrobeamManipulation> copyLinkedMicrobeamManipulationList()
	{
		return new ArrayList<MicrobeamManipulation>(microbeamManipulationList);
	}

	public MicrobeamManipulation getLinkedMicrobeamManipulation(int index)
	{
		return microbeamManipulationList.get(index);
	}

	public MicrobeamManipulation setLinkedMicrobeamManipulation(int index, MicrobeamManipulation o)
	{
		return microbeamManipulationList.set(index, o);
	}

	public boolean linkMicrobeamManipulation(MicrobeamManipulation o)
	{
		o.linkImage(this);
		return microbeamManipulationList.add(o);
	}

	public boolean unlinkMicrobeamManipulation(MicrobeamManipulation o)
	{
		o.unlinkImage(this);
		return microbeamManipulationList.remove(o);
	}

	// Reference which occurs more than once
	public int sizeOfLinkedAnnotationList()
	{
		return annotationList.size();
	}

	public List<Annotation> copyLinkedAnnotationList()
	{
		return new ArrayList<Annotation>(annotationList);
	}

	public Annotation getLinkedAnnotation(int index)
	{
		return annotationList.get(index);
	}

	public Annotation setLinkedAnnotation(int index, Annotation o)
	{
		return annotationList.set(index, o);
	}

	public boolean linkAnnotation(Annotation o)
	{
		o.linkImage(this);
		return annotationList.add(o);
	}

	public boolean unlinkAnnotation(Annotation o)
	{
		o.unlinkImage(this);
		return annotationList.remove(o);
	}

	// Reference which occurs more than once
	public int sizeOfLinkedWellSampleList()
	{
		return wellSample_BackReferenceList.size();
	}

	public List<WellSample> copyLinkedWellSampleList()
	{
		return new ArrayList<WellSample>(wellSample_BackReferenceList);
	}

	public WellSample getLinkedWellSample(int index)
	{
		return wellSample_BackReferenceList.get(index);
	}

	public WellSample setLinkedWellSample(int index, WellSample o)
	{
		return wellSample_BackReferenceList.set(index, o);
	}

	public boolean linkWellSample(WellSample o)
	{
		return wellSample_BackReferenceList.add(o);
	}

	public boolean unlinkWellSample(WellSample o)
	{
		return wellSample_BackReferenceList.remove(o);
	}

	public Element asXMLElement(Document document)
	{
		return asXMLElement(document, null);
	}

	protected Element asXMLElement(Document document, Element Image_element)
	{
		// Creating XML block for Image

		if (Image_element == null)
		{
			Image_element =
					document.createElementNS(NAMESPACE, "Image");
		}

		if (id != null)
		{
			// Attribute property ID
			Image_element.setAttribute("ID", id.toString());
		}
		if (name != null)
		{
			// Attribute property Name
			Image_element.setAttribute("Name", name.toString());
		}
		if (acquiredDate != null)
		{
			// Element property AcquiredDate which is not complex (has no
			// sub-elements)
			Element acquiredDate_element = 
					document.createElementNS(NAMESPACE, "AcquiredDate");
			acquiredDate_element.setTextContent(acquiredDate.toString());
			Image_element.appendChild(acquiredDate_element);
		}
		if (experimenter != null)
		{
			// Reference property ExperimenterRef
			ExperimenterRef o = new ExperimenterRef();
			o.setID(experimenter.getID());
			Image_element.appendChild(o.asXMLElement(document));
		}
		if (description != null)
		{
			// Element property Description which is not complex (has no
			// sub-elements)
			Element description_element = 
					document.createElementNS(NAMESPACE, "Description");
			description_element.setTextContent(description.toString());
			Image_element.appendChild(description_element);
		}
		if (experiment != null)
		{
			// Reference property ExperimentRef
			ExperimentRef o = new ExperimentRef();
			o.setID(experiment.getID());
			Image_element.appendChild(o.asXMLElement(document));
		}
		if (group != null)
		{
			// Reference property GroupRef
			GroupRef o = new GroupRef();
			o.setID(group.getID());
			Image_element.appendChild(o.asXMLElement(document));
		}
		if (datasetList != null)
		{
			// Reference property DatasetRef which occurs more than once
			for (Dataset datasetList_value : datasetList)
			{
				DatasetRef o = new DatasetRef();
				o.setID(datasetList_value.getID());
				Image_element.appendChild(o.asXMLElement(document));
			}
		}
		if (instrument != null)
		{
			// Reference property InstrumentRef
			InstrumentRef o = new InstrumentRef();
			o.setID(instrument.getID());
			Image_element.appendChild(o.asXMLElement(document));
		}
		if (objectiveSettings != null)
		{
			// Element property ObjectiveSettings which is complex (has
			// sub-elements)
			Image_element.appendChild(objectiveSettings.asXMLElement(document));
		}
		if (imagingEnvironment != null)
		{
			// Element property ImagingEnvironment which is complex (has
			// sub-elements)
			Image_element.appendChild(imagingEnvironment.asXMLElement(document));
		}
		if (stageLabel != null)
		{
			// Element property StageLabel which is complex (has
			// sub-elements)
			Image_element.appendChild(stageLabel.asXMLElement(document));
		}
		if (pixels != null)
		{
			// Element property Pixels which is complex (has
			// sub-elements)
			Image_element.appendChild(pixels.asXMLElement(document));
		}
		if (roiList != null)
		{
			// Reference property ROIRef which occurs more than once
			for (ROI roiList_value : roiList)
			{
				ROIRef o = new ROIRef();
				o.setID(roiList_value.getID());
				Image_element.appendChild(o.asXMLElement(document));
			}
		}
		if (microbeamManipulationList != null)
		{
			// Reference property MicrobeamManipulationRef which occurs more than once
			for (MicrobeamManipulation microbeamManipulationList_value : microbeamManipulationList)
			{
				MicrobeamManipulationRef o = new MicrobeamManipulationRef();
				o.setID(microbeamManipulationList_value.getID());
				Image_element.appendChild(o.asXMLElement(document));
			}
		}
		if (annotationList != null)
		{
			// Reference property AnnotationRef which occurs more than once
			for (Annotation annotationList_value : annotationList)
			{
				AnnotationRef o = new AnnotationRef();
				o.setID(annotationList_value.getID());
				Image_element.appendChild(o.asXMLElement(document));
			}
		}
		if (wellSample_BackReferenceList != null)
		{
			// *** IGNORING *** Skipped back reference WellSample_BackReference
		}
		return super.asXMLElement(document, Image_element);
	}
}