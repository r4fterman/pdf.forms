/*
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

import java.util.List;
import java.util.Set;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PropertyChanger {

    private static void setProperty(
            final String value,
            final Element captionElement,
            final Element valueElement) {
        if (value != null) {
            captionElement.getAttributeNode("value").setValue(value);
            if (valueElement != null) {
                valueElement.getAttributeNode("value").setValue(value);
            }
        }
    }

    public static void updateFont(
            final Set<IWidget> widgets,
            final String fontName,
            final String fontSize,
            final int fontStyle) {
        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();

            final Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            final List fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = (Element) fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name");
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size");
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style");

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

    public static void updateSizeAndPosition(final Set<IWidget> widgetSelection) {
        for (final IWidget widget : widgetSelection) {
            final Document properties = widget.getProperties();

            final Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            final Element xCord = XMLUtils.getPropertyElement(element, "X");
            xCord.getAttributeNode("value").setValue(widget.getX() + "");

            final Element yCord = XMLUtils.getPropertyElement(element, "Y");
            yCord.getAttributeNode("value").setValue(widget.getY() + "");

            final Element width = XMLUtils.getPropertyElement(element, "Width");
            width.getAttributeNode("value").setValue(widget.getWidth() + "");

            final Element height = XMLUtils.getPropertyElement(element, "Height");
            height.getAttributeNode("value").setValue(widget.getHeight() + "");
        }
    }

    public static void updateSizeAndPosition(
            final Set<IWidget> widgetSelection,
            final Integer[] props) {
        //todo can we change the props parameter type to int[]?

        for (final IWidget widget : widgetSelection) {
            final Document properties = widget.getProperties();

            final Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            if (props[0] != null) {
                final Element xCord = XMLUtils.getPropertyElement(element, "X");
                xCord.getAttributeNode("value").setValue(props[0] + "");
            }

            if (props[1] != null) {
                final Element yCord = XMLUtils.getPropertyElement(element, "Y");
                yCord.getAttributeNode("value").setValue(props[1] + "");
            }

            if (props[2] != null) {
                final Element width = XMLUtils.getPropertyElement(element, "Width");
                width.getAttributeNode("value").setValue(props[2] + "");
            }

            if (props[3] != null) {
                final Element height = XMLUtils.getPropertyElement(element, "Height");
                height.getAttributeNode("value").setValue(props[3] + "");
            }
        }
    }
}
