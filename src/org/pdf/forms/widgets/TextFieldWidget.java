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
 * TextFieldWidget.java
 * ---------------
 */
package org.pdf.forms.widgets;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.SplitComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TextFieldWidget extends Widget implements IWidget {
    private static int nextWidgetNumber = 1;

    public TextFieldWidget(int type, JComponent baseComponent, JComponent component) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Text Field.gif");

        isComponentSplit = true;
        allowEditCaptionAndValue = true;
        allowEditOfCaptionOnClick = true;

        String widgetName = "Text Field" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "TEXT_FIELD", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        addProperties(rootElement);

        addJavaScript(rootElement);
    }

    public TextFieldWidget(int type, JComponent baseComponent, JComponent component, Element root) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Text Field.gif");

        isComponentSplit = true;
        allowEditCaptionAndValue = true;
        allowEditOfCaptionOnClick = true;

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

        addCaptionProperties(propertiesElement);
    }

    private void addCaptionProperties(Element propertiesElement) {
        Element captionElement = XMLUtils.createAndAppendElement(properties, "caption_properties", propertiesElement);
        XMLUtils.addBasicProperty(properties, "Text", "Text Field", captionElement);
        XMLUtils.addBasicProperty(properties, "Divisor Location", "", captionElement);
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

        Element value = XMLUtils.createAndAppendElement(properties, "font_value", fontElement);
        XMLUtils.addBasicProperty(properties, "Font Name", FontHandler.getInstance().getDefaultFont().getFontName(), value);
        XMLUtils.addBasicProperty(properties, "Font Size", "11", value);
        XMLUtils.addBasicProperty(properties, "Font Style", "0", value);
        XMLUtils.addBasicProperty(properties, "Underline", "0", value);
        XMLUtils.addBasicProperty(properties, "Strikethrough", "0", value);
        XMLUtils.addBasicProperty(properties, "Color", Color.BLACK.getRGB() + "", value);
    }

    private void addObjectProperties(Element propertiesElement) {

        Element objectElement = XMLUtils.createAndAppendElement(properties, "object", propertiesElement);

        Element fieldElement = XMLUtils.createAndAppendElement(properties, "field", objectElement);
        XMLUtils.addBasicProperty(properties, "Appearance", "Sunken Box", fieldElement);
        XMLUtils.addBasicProperty(properties, "Allow Multiple Lines", "false", fieldElement);
        XMLUtils.addBasicProperty(properties, "Limit Length", "false", fieldElement);
        XMLUtils.addBasicProperty(properties, "Max Chars", "", fieldElement);
        XMLUtils.addBasicProperty(properties, "Presence", "Visible", fieldElement);

        Element valueElement = XMLUtils.createAndAppendElement(properties, "value", objectElement);
        XMLUtils.addBasicProperty(properties, "Type", "User Entered - Optional", valueElement);
        XMLUtils.addBasicProperty(properties, "Default", "Text Field", valueElement);
        XMLUtils.addBasicProperty(properties, "Empty Message", "", valueElement);

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

        Element caption = XMLUtils.createAndAppendElement(properties, "caption", layoutElement);
        XMLUtils.addBasicProperty(properties, "Position", "Left", caption);
        XMLUtils.addBasicProperty(properties, "Reserve", "4", caption);
    }

    private void addBorderProperties(Element propertiesElement) {
        Element borderElement = XMLUtils.createAndAppendElement(properties, "border", propertiesElement);

        Element borders = XMLUtils.createAndAppendElement(properties, "borders", borderElement);
        XMLUtils.addBasicProperty(properties, "Border Style", "None", borders);
        XMLUtils.addBasicProperty(properties, "Border Width", "1", borders);
        XMLUtils.addBasicProperty(properties, "Border Color", Color.BLACK.getRGB() + "", borders);
        ;

        Element backgorundFill = XMLUtils.createAndAppendElement(properties, "backgroundfill", borderElement);
        XMLUtils.addBasicProperty(properties, "Style", "Solid", backgorundFill);
        XMLUtils.addBasicProperty(properties, "Fill Color", Color.WHITE.getRGB() + "", backgorundFill);
    }

    private void addParagraphProperties(Element propertiesElement) {
        Element paragraphElement = XMLUtils.createAndAppendElement(properties, "paragraph", propertiesElement);

        Element caption = XMLUtils.createAndAppendElement(properties, "paragraph_caption", paragraphElement);
        XMLUtils.addBasicProperty(properties, "Horizontal Alignment", "left", caption);
        XMLUtils.addBasicProperty(properties, "Vertical Alignment", "center", caption);

        Element value = XMLUtils.createAndAppendElement(properties, "paragraph_value", paragraphElement);
        XMLUtils.addBasicProperty(properties, "Horizontal Alignment", "left", value);
        XMLUtils.addBasicProperty(properties, "Vertical Alignment", "center", value);
    }

    public void setFontProperties(Element fontProperties, int currentlyEditing) {
        SplitComponent textField = (SplitComponent) baseComponent;

        switch (currentlyEditing) {
            case IWidget.COMPONENT_BOTH: {

                Element captionProperties = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

                setFontProperties(captionProperties, textField.getCaption());

                Element valueProperties = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                setFontProperties(valueProperties, (IPdfComponent) textField.getValue());

                break;

            }
            case IWidget.COMPONENT_CAPTION: {

                Element captionProperties = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

                setFontProperties(captionProperties, textField.getCaption());

                break;

            }
            case IWidget.COMPONENT_VALUE: {

                Element valueProperties = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                setFontProperties(valueProperties, (IPdfComponent) textField.getValue());

                break;
            }
        }

        setSize(getWidth(), getHeight());
    }

    public void setLayoutProperties(Element layoutProperties) {
        SplitComponent textField = (SplitComponent) baseComponent;

        /** set the size and position of the TextField*/
        setSizeAndPosition(layoutProperties);

        /** set the location of the caption */
        Element captionElement = (Element) layoutProperties.getElementsByTagName("caption").item(0);

        String captionPosition = XMLUtils.getAttributeFromChildElement(captionElement, "Position");

        /** use reflection to set the required rotation button selected */
        try {
            Field field = textField.getClass().getDeclaredField("CAPTION_" + captionPosition.toUpperCase());

            int position = field.getInt(this);
            if (position != textField.getCaptionPosition())
                textField.setCaptionPosition(position);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setSize(getWidth(), getHeight());
    }

    public void setParagraphProperties(Element paragraphPropertiesElememt, int currentlyEditing) {
        SplitComponent textField = (SplitComponent) baseComponent;

        switch (currentlyEditing) {
            case IWidget.COMPONENT_BOTH: {
                Element paragraphCaptionElement =
                        (Element) paragraphPropertiesElememt.getElementsByTagName("paragraph_caption").item(0);

                setParagraphProperties(paragraphCaptionElement, textField.getCaption());

                Element paragraphValueElement =
                        (Element) paragraphPropertiesElememt.getElementsByTagName("paragraph_value").item(0);

                setParagraphProperties(paragraphValueElement, (IPdfComponent) textField.getValue());

                break;
            }
            case IWidget.COMPONENT_CAPTION: {
                Element paragraphCaptionElement =
                        (Element) paragraphPropertiesElememt.getElementsByTagName("paragraph_caption").item(0);

                setParagraphProperties(paragraphCaptionElement, textField.getCaption());

                break;
            }
            case IWidget.COMPONENT_VALUE: {
                Element paragraphValueElement =
                        (Element) paragraphPropertiesElememt.getElementsByTagName("paragraph_value").item(0);

                setParagraphProperties(paragraphValueElement, (IPdfComponent) textField.getValue());

                break;
            }
        }

        setSize(getWidth(), getHeight());
    }

    public void setObjectProperties(Element objectProperties) {
        //todo implement field properties
        /** set field properties */

        /** set value properties */
        Element valueElement = (Element) objectProperties.getElementsByTagName("value").item(0);

        String defaultText = XMLUtils.getAttributeFromChildElement(valueElement, "Default");

        JTextField textField = (JTextField) getValueComponent();
        System.out.println("TextFieldWidget.setObjectProperties(): " + defaultText);
        textField.setText(defaultText);

        /** set binding properties */
        setBindingProperties(objectProperties);
    }

}