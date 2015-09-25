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
* ButtonWidget.java
* ---------------
*/
package org.pdf.forms.widgets;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ButtonWidget extends Widget implements IWidget {

    private static int nextWidgetNumber = 1;

    public ButtonWidget(int type, JComponent baseComponent, JComponent component) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Button.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;

        String widgetName = "Button" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "BUTTON", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        addProperties(rootElement);

        addJavaScript(rootElement);
    }

    public ButtonWidget(int type, JComponent baseComponent, JComponent component, Element root) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Button.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;

        Element bindingElement = (Element) XMLUtils.getElementsFromNodeList(root.getElementsByTagName("binding")).get(0);
        widgetName = XMLUtils.getAttributeFromChildElement(bindingElement, "Name");
        arrayNumber = Integer.parseInt(XMLUtils.getAttributeFromChildElement(bindingElement, "Array Number"));

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

        Element valueElement = XMLUtils.createAndAppendElement(properties, "value", objectElement);
        XMLUtils.addBasicProperty(properties, "Default", "Button", valueElement);

        Element bindingElement = XMLUtils.createAndAppendElement(properties, "binding", objectElement);
        XMLUtils.addBasicProperty(properties, "Name", widgetName, bindingElement);
        XMLUtils.addBasicProperty(properties, "Array Number", "0", bindingElement);
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
        XMLUtils.addBasicProperty(properties, "Fill Color", getValueComponent().getBackground().getRGB() + "", backgorundFill);
    }

    private void addParagraphProperties(Element propertiesElement) {
        Element paragraphElement = XMLUtils.createAndAppendElement(properties, "paragraph", propertiesElement);

        Element value = XMLUtils.createAndAppendElement(properties, "paragraph_caption", paragraphElement);
        XMLUtils.addBasicProperty(properties, "Horizontal Alignment", "center", value);
        XMLUtils.addBasicProperty(properties, "Vertical Alignment", "center", value);
    }

    public void setParagraphProperties(Element paragraphPropertiesElememt, int currentlyEditing) {

        IPdfComponent button = (IPdfComponent) baseComponent;

        Element paragraphCaptionElement =
                (Element) paragraphPropertiesElememt.getElementsByTagName("paragraph_caption").item(0);

        setParagraphProperties(paragraphCaptionElement, button);
    }

    public void setLayoutProperties(Element layoutProperties) {
        setSizeAndPosition(layoutProperties);
    }

    public void setFontProperties(Element fontProperties, int currentlyEditing) {

        IPdfComponent button = (IPdfComponent) baseComponent;

        Element captionProperties = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

        setFontProperties(captionProperties, button);

        setSize(getWidth(), getHeight());
    }

    public void setObjectProperties(Element objectProperties) {
        JButton button = (JButton) getValueComponent();

        /** set value properties */
        Element valueElement = (Element) objectProperties.getElementsByTagName("value").item(0);

        String defaultText = XMLUtils.getAttributeFromChildElement(valueElement, "Default");

        button.setText(defaultText);

        setSize(getWidth(), getHeight());

        /** set binding properties */
        setBindingProperties(objectProperties);
    }

    public Point getAbsoluteLocationsOfValue() {
        return new Point(getX(), getY());
    }
}
