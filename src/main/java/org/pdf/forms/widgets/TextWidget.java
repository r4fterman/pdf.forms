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
* TextWidget.java
* ---------------
*/
package org.pdf.forms.widgets;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import javax.swing.JComponent;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TextWidget extends Widget implements IWidget {
    private static int nextWidgetNumber = 1;

    public TextWidget(int type, JComponent baseComponent, JComponent component) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Text.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;
        allowEditOfCaptionOnClick = true;

        String widgetName = "Text" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "TEXT", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        addProperties(rootElement);
    }

    public TextWidget(int type, JComponent baseComponent, JComponent component, Element root) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Text.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;
        allowEditOfCaptionOnClick = true;

        List childNodes = XMLUtils.getElementsFromNodeList(root.getChildNodes());

        this.widgetName = XMLUtils.getAttributeFromChildElement(root, "Name");

        Element rootElement = setupProperties();
        Node newRoot = properties.importNode(root, true);

        properties.replaceChild(newRoot, rootElement);

        setAllProperties();
    }

    private void addProperties(Element rootElement) {
        Element propertiesElement = XMLUtils.createAndAppendElement(properties, "properties", rootElement);

        addFontProperties(propertiesElement);

        addObjectProperties(propertiesElement);

        addLayoutProperties(propertiesElement);

        addBorderProperties(propertiesElement);

        addParagraphProperties(propertiesElement);

        addCaptionProperties(propertiesElement);
    }

    private void addCaptionProperties(Element propertiesElement) {
        Element captionElement = XMLUtils.createAndAppendElement(properties, "caption_properties", propertiesElement);
        XMLUtils.addBasicProperty(properties, "Text", "Text", captionElement);
    }

    private void addFontProperties(Element propertiesElement) {

        Element fontElement = XMLUtils.createAndAppendElement(properties, "font", propertiesElement);

        Element caption = XMLUtils.createAndAppendElement(properties, "font_caption", fontElement);
        XMLUtils.addBasicProperty(properties, "Font Name", FontHandler.getInstance().getDefaultFont().getFontName(), caption);
        XMLUtils.addBasicProperty(properties, "Font Size", "11", caption);
        XMLUtils.addBasicProperty(properties, "Font Style", "0", caption);
        XMLUtils.addBasicProperty(properties, "Underline", "0", caption);
        XMLUtils.addBasicProperty(properties, "Strikethrough", "0", caption);
        XMLUtils.addBasicProperty(properties, "Color", Color.BLACK.getRGB() + "", caption);
    }

    private void addObjectProperties(Element propertiesElement) {

        Element objectElement = XMLUtils.createAndAppendElement(properties, "object", propertiesElement);

        Element fieldElement = XMLUtils.createAndAppendElement(properties, "field", objectElement);
        XMLUtils.addBasicProperty(properties, "Presence", "Visible", fieldElement);

//		Element fieldElement = createAndAppendElement("field", objectElement);
//		addBasicProperty("Appearance", "Sunken Box", fieldElement);
//		addBasicProperty("Allow Multiple Lines", "false", fieldElement);
//		addBasicProperty("Limit Length", "false", fieldElement);
//		addBasicProperty("Max Chars", "", fieldElement);
//		addBasicProperty("Presence", "Visible", fieldElement);

//		Element valueElement = createAndAppendElement("value", objectElement);
//		addBasicProperty("Type", "User Entered - Optional", valueElement);
//		addBasicProperty("Default", "", valueElement);
//		addBasicProperty("Empty Message", "", valueElement);

//		Element bindingElement = createAndAppendElement("binding", objectElement);
//		addBasicProperty("Name", widgetName, bindingElement);
//		addBasicProperty("Array Number", "0", bindingElement);
    }

    private void addLayoutProperties(Element propertiesElement) {
        Element layoutElement = XMLUtils.createAndAppendElement(properties, "layout", propertiesElement);

        Element sizeAndPositionElement = XMLUtils.createAndAppendElement(properties, "sizeandposition", layoutElement);
        XMLUtils.addBasicProperty(properties, "X", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Width", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Y", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Height", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Anchor", "Top Left", sizeAndPositionElement);
        XMLUtils.addBasicProperty(properties, "Rotation", "0", sizeAndPositionElement);

        Element margins = XMLUtils.createAndAppendElement(properties, "margins", layoutElement);
        XMLUtils.addBasicProperty(properties, "Left", "2", margins);
        XMLUtils.addBasicProperty(properties, "Right", "4", margins);
        XMLUtils.addBasicProperty(properties, "Top", "2", margins);
        XMLUtils.addBasicProperty(properties, "Bottom", "4", margins);
    }

    private void addBorderProperties(Element propertiesElement) {
        Element borderElement = XMLUtils.createAndAppendElement(properties, "border", propertiesElement);

        Element borders = XMLUtils.createAndAppendElement(properties, "borders", borderElement);
        XMLUtils.addBasicProperty(properties, "Border Style", "None", borders);
        XMLUtils.addBasicProperty(properties, "Border Width", "1", borders);
        XMLUtils.addBasicProperty(properties, "Border Color", Color.BLACK.getRGB() + "", borders);

        Element backgorundFill = XMLUtils.createAndAppendElement(properties, "backgroundfill", borderElement);
        XMLUtils.addBasicProperty(properties, "Style", "Solid", backgorundFill);
        XMLUtils.addBasicProperty(properties, "Fill Color", Color.WHITE.getRGB() + "", backgorundFill);
    }

    private void addParagraphProperties(Element propertiesElement) {
        Element paragraphElement = XMLUtils.createAndAppendElement(properties, "paragraph", propertiesElement);

        Element value = XMLUtils.createAndAppendElement(properties, "paragraph_caption", paragraphElement);
        XMLUtils.addBasicProperty(properties, "Horizontal Alignment", "left", value);
        XMLUtils.addBasicProperty(properties, "Vertical Alignment", "center", value);
    }

    public void setParagraphProperties(Element paragraphPropertiesElement, int currentlyEditing) {

        IPdfComponent text = (IPdfComponent) baseComponent;

        Element paragraphCaptionElement = 
                (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

        setParagraphProperties(paragraphCaptionElement, text);

    }

    public void setLayoutProperties(Element layoutProperties) {
        setSizeAndPosition(layoutProperties);
    }

    public void setFontProperties(Element fontProperties, int currentlyEditing) {

        IPdfComponent text = (IPdfComponent) baseComponent;

        Element captionProperties = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

        setFontProperties(captionProperties, text);

        setSize(getWidth(), getHeight());
    }

    public void setCaptionProperties(Element captionProperties) {
        Element captionElement = (Element) properties.getElementsByTagName("caption_properties").item(0);

        String captionText = XMLUtils.getAttributeFromChildElement(captionElement, "Text");

        // insert line breaks
        captionText.replaceAll("<br>", "\n");

        getCaptionComponent().setText(captionText);

        setSize(getWidth(), getHeight());
    }

    public PdfCaption getCaptionComponent() {
        return (PdfCaption) baseComponent;
    }

    public Point getAbsoluteLocationsOfCaption() {
        return new Point(getX(), getY());
    }
}
