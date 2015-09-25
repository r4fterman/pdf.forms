/**
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* CustomWidgetsFile.java
* ---------------
*/
package org.pdf.forms.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * holds values stored in XML file on disk
 */
public class CustomWidgetsFile extends PropertiesFile {

	private static CustomWidgetsFile instance;

	private CustomWidgetsFile(String fileName) {
		super(fileName);
	}

    public static CustomWidgetsFile getInstance() {
        if (instance == null)
            // it's ok, we can call this constructor
            instance = new CustomWidgetsFile(".custom_components.xml");

        return instance;
    }

    public boolean checkAllElementsPresent() throws Exception {
    	
    	//assume true and set to false if wrong
        boolean hasAllElements=true;
        
        NodeList allElements = doc.getElementsByTagName("*");
        List elementsInTree = new ArrayList(allElements.getLength());

        for(int i=0;i<allElements.getLength();i++)
			elementsInTree.add(allElements.item(i).getNodeName());
        
        if (!elementsInTree.contains("custom_components")) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.newDocument();

            Element customComponentElement = doc.createElement("custom_components");
            doc.appendChild(customComponentElement);
            
            hasAllElements=false;
        }
        
        return hasAllElements;
    }

    public boolean isNameTaken(String name) {
		List components = XMLUtils.getElementsFromNodeList(doc.getElementsByTagName("custom_component"));

		for (Iterator it = components.iterator(); it.hasNext();) {
			Element element = (Element) it.next();
			String nameAtt = XMLUtils.getAttributeFromChildElement(element, "name");

			if (nameAtt.equals(name)) {
				return true;
			}
		}

		return false;
	}

    public void addCustomWidget(String name, Set selectedWidgets) {

        Element customComponentElement = XMLUtils.createAndAppendElement(doc, "custom_component", doc.getDocumentElement());

        XMLUtils.addBasicProperty(doc, "name", name, customComponentElement);

        for (Iterator it = selectedWidgets.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            Document widgetProperties = widget.getProperties();

            Element widgetRoot = widgetProperties.getDocumentElement();

            customComponentElement.appendChild(doc.importNode(widgetRoot, true));
        }

        try {
            writeDoc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List getCustomWidgets() {
        return null;
    }
}
