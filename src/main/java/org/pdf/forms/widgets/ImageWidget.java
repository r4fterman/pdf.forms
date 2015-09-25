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
* ImageWidget.java
* ---------------
*/
package org.pdf.forms.widgets;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ImageWidget extends Widget implements IWidget {

    private static int nextWidgetNumber = 1;

    private final static int STRETCH = 0;
    private final static int FULL_SIZE = 1;

    private Image currentImage;

    private int sizing;

    public ImageWidget(int type, JComponent baseComponent, JComponent component) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Image.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;
        allowEditOfCaptionOnClick = false;

        String widgetName = "Image" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "IMAGE", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        addProperties(rootElement);
    }

    public ImageWidget(int type, JComponent baseComponent, JComponent component, Element root) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Image.gif");

        isComponentSplit = false;
        allowEditCaptionAndValue = false;

        this.widgetName = XMLUtils.getAttributeFromChildElement(root, "Name");

        Element rootElement = setupProperties();
        Node newRoot = properties.importNode(root, true);

        properties.replaceChild(newRoot, rootElement);

        setAllProperties();
    }

    private void addProperties(Element rootElement) {
        Element propertiesElement = XMLUtils.createAndAppendElement(properties, "properties", rootElement);

        addObjectProperties(propertiesElement);

        addLayoutProperties(propertiesElement);
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

    private void addObjectProperties(Element propertiesElement) {
        Element objectElement = XMLUtils.createAndAppendElement(properties, "object", propertiesElement);

        Element fieldElement = XMLUtils.createAndAppendElement(properties, "field", objectElement);
        XMLUtils.addBasicProperty(properties, "Presence", "Visible", fieldElement);

        Element drawElement = XMLUtils.createAndAppendElement(properties, "draw", objectElement);
        XMLUtils.addBasicProperty(properties, "Location", "", drawElement);
        XMLUtils.addBasicProperty(properties, "Sizing", "Stretch Image To Fit", drawElement);
    }

    public void setLayoutProperties(Element layoutProperties) {
        setSizeAndPosition(layoutProperties);
    }

    public void setObjectProperties(Element objectElement) {
        Element drawElement = (Element) objectElement.getElementsByTagName("draw").item(0);

        String location = XMLUtils.getAttributeFromChildElement(drawElement, "Location");
        String sizing = XMLUtils.getAttributeFromChildElement(drawElement, "Sizing");

        if (sizing.equals("Stretch Image To Fit"))
            this.sizing = STRETCH;
        else if (sizing.equals("Use Image Size"))
            this.sizing = FULL_SIZE;

        if (new File(location).exists()) {
            currentImage = Toolkit.getDefaultToolkit().createImage(location);
        } else {
            currentImage = null;
        }

        JLabel imageLabel = (JLabel) baseComponent;

        if (currentImage == null)
            imageLabel.setIcon(null);
        else {
            if (this.sizing == STRETCH)
                imageLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
            else if (this.sizing == FULL_SIZE)
                imageLabel.setIcon(new ImageIcon(currentImage));
        }

        setSize(getWidth(), getHeight());
    }

    public void setSize(int width, int height) {
        JLabel imageLabel = (JLabel) baseComponent;

        if (currentImage == null)
            imageLabel.setIcon(null);
        else {
            if (this.sizing == STRETCH)
                imageLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            else if (this.sizing == FULL_SIZE)
                imageLabel.setIcon(new ImageIcon(currentImage));
        }

        component = WidgetFactory.createResizedComponent(baseComponent, width, height);
    }
}
