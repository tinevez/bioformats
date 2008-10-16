/*
 * ome.xml.r200802.ome.PlaneNode
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) 2007 Open Microscopy Environment
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
 * Created by curtis via xsd-fu on 2008-10-15 21:58:37-0500
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.r200802.ome;

import ome.xml.DOMUtil;
import ome.xml.OMEXMLNode;
import ome.xml.r200802.ome.*;
import ome.xml.r200802.spw.*;

import org.w3c.dom.Element;

public class PlaneNode extends OMEXMLNode
{

	// -- Constructors --

	/** Constructs a Plane node with an associated DOM element. */
	public PlaneNode(Element element)
	{
		super(element);
	}

	/**
	 * Constructs a Plane node with an associated DOM element beneath
	 * a given parent.
	 */
	public PlaneNode(OMEXMLNode parent)
	{
		this(parent, true);
	}

	/**
	 * Constructs a Plane node with an associated DOM element beneath
	 * a given parent.
	 */
	public PlaneNode(OMEXMLNode parent, boolean attach)
	{
		super(DOMUtil.createChild(parent.getDOMElement(),
		                          "Plane", attach));
	}

	// -- Plane API methods --

	// Attribute
	public Integer getTheC()
	{
		return getIntegerAttribute("TheC");
	}

	public void setTheC(Integer theC)
	{
		setAttribute("TheC", theC);
	}

	// Element which is not complex (has only a text node)
	public String getHashSHA1()
	{
		return getStringCData("HashSHA1");
	}

	public void setHashSHA1(String hashSHA1)
	{
		setCData("HashSHA1", hashSHA1);
	}

	// Attribute
	public Integer getTheZ()
	{
		return getIntegerAttribute("TheZ");
	}

	public void setTheZ(Integer theZ)
	{
		setAttribute("TheZ", theZ);
	}

	// Element which is complex (has sub-elements)
	public PlaneTimingNode getPlaneTiming()
	{
		return (PlaneTimingNode)
			getChildNode("PlaneTiming", "PlaneTiming");
	}

	// Attribute
	public Integer getTheT()
	{
		return getIntegerAttribute("TheT");
	}

	public void setTheT(Integer theT)
	{
		setAttribute("TheT", theT);
	}

	// Element which is complex (has sub-elements)
	public StagePositionNode getStagePosition()
	{
		return (StagePositionNode)
			getChildNode("StagePosition", "StagePosition");
	}

	// -- OMEXMLNode API methods --

	public boolean hasID()
	{
		return false;
	}

}
