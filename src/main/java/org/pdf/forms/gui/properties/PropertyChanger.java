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
* PropertyChanger.java
* ---------------
*/
package org.pdf.forms.gui.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PropertyChanger {

    private static void setProperty(String value, Element captionElement, Element valueElement) {
        if (value != null) {
            captionElement.getAttributeNode("value").setValue(value);
            if (valueElement != null)
                valueElement.getAttributeNode("value").setValue(value);
        }
    }

    public static void updateFont(Set widgets, String fontName, String fontSize, int fontStyle) {
        for (Iterator it = widgets.iterator(); it.hasNext();) {

            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();

            Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            List fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            Element captionElement = (Element) fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) fontList.get(1);
            }

            Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name");
            Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size");
            Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style");

            Element valueFontName = null, valueFontSize = null, valueFontStyle = null;

            if (widget.allowEditCaptionAndValue()) {
                valueFontName = XMLUtils.getPropertyElement(valueElement, "Font Name");
                valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size");
                valueFontStyle = XMLUtils.getPropertyElement(valueElement, "Font Style");
            }

            setProperty(fontName, captionFontName, valueFontName);
            setProperty(fontSize, captionFontSize, valueFontSize);

            setProperty(fontStyle + "", captionFontStyle, valueFontStyle);

            widget.setFontProperties(fontElement, IWidget.COMPONENT_BOTH);
        }
    }

    public static void updateSizeAndPosition(Set widgetSelection) {
        for (Iterator it = widgetSelection.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();

            Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            Element xCord = XMLUtils.getPropertyElement(element, "X");
            xCord.getAttributeNode("value").setValue(widget.getX() + "");

            Element yCord = XMLUtils.getPropertyElement(element, "Y");
            yCord.getAttributeNode("value").setValue(widget.getY() + "");

            Element width = XMLUtils.getPropertyElement(element, "Width");
            width.getAttributeNode("value").setValue(widget.getWidth() + "");

            Element height = XMLUtils.getPropertyElement(element, "Height");
            height.getAttributeNode("value").setValue(widget.getHeight() + "");
        }
    }

    public static void updateSizeAndPosition(Set widgetSelection, Integer[] props) { 
    	//todo can we change the props parameter type to int[]?
    	
        for (Iterator it = widgetSelection.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();

            Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            if (props[0] != null) {
                Element xCord = XMLUtils.getPropertyElement(element, "X");
                xCord.getAttributeNode("value").setValue(props[0] + "");
            }

            if (props[1] != null) {
                Element yCord = XMLUtils.getPropertyElement(element, "Y");
                yCord.getAttributeNode("value").setValue(props[1] + "");
            }

            if (props[2] != null) {
                Element width = XMLUtils.getPropertyElement(element, "Width");
                width.getAttributeNode("value").setValue(props[2] + "");
            }

            if (props[3] != null) {
                Element height = XMLUtils.getPropertyElement(element, "Height");
                height.getAttributeNode("value").setValue(props[3] + "");
            }

//            try {
//                StringWriter sw = new StringWriter();
//                //InputStream stylesheet = Thread.currentThread().getContextClassLoader().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
//                TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                Transformer transformer = transformerFactory.newTransformer(/*new StreamSource(stylesheet)*/);
//                transformer.transform(new DOMSource(properties), new StreamResult(sw));
//                System.out.println(sw.toString());
//            } catch (TransformerException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
        }
    }
}
