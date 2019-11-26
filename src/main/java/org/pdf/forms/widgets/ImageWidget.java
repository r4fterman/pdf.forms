/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
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

    private static final int STRETCH = 0;
    private static final int FULL_SIZE = 1;

    private Image currentImage;

    private int sizing;

    public ImageWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Image.gif");

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(false);

        final String widgetName = "Image" + nextWidgetNumber;
        nextWidgetNumber++;

        setWidgetName(widgetName);

        final Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(getProperties(), "type", "IMAGE", rootElement);
        XMLUtils.addBasicProperty(getProperties(), "name", widgetName, rootElement);

        addProperties(rootElement);
    }

    public ImageWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final Element root) {

        super(type, baseComponent, component, "/org/pdf/forms/res/Image.gif");

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);

        setWidgetName(XMLUtils.getAttributeFromChildElement(root, "Name").orElse(""));

        final Element rootElement = setupProperties();
        final Node newRoot = getProperties().importNode(root, true);

        getProperties().replaceChild(newRoot, rootElement);

        setAllProperties();
    }

    private void addProperties(final Element rootElement) {
        final Element propertiesElement = XMLUtils.createAndAppendElement(getProperties(), "properties", rootElement);

        addObjectProperties(propertiesElement);

        addLayoutProperties(propertiesElement);
    }

    private void addLayoutProperties(final Element propertiesElement) {
        final Element layoutElement = XMLUtils.createAndAppendElement(getProperties(), "layout", propertiesElement);

        final Element sizeAndPositionElement = XMLUtils.createAndAppendElement(getProperties(), "sizeandposition", layoutElement);
        XMLUtils.addBasicProperty(getProperties(), "X", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Width", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Y", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Height", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Anchor", "Top Left", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Rotation", "0", sizeAndPositionElement);

        final Element margins = XMLUtils.createAndAppendElement(getProperties(), "margins", layoutElement);
        XMLUtils.addBasicProperty(getProperties(), "Left", "2", margins);
        XMLUtils.addBasicProperty(getProperties(), "Right", "4", margins);
        XMLUtils.addBasicProperty(getProperties(), "Top", "2", margins);
        XMLUtils.addBasicProperty(getProperties(), "Bottom", "4", margins);
    }

    private void addObjectProperties(final Element propertiesElement) {
        final Element objectElement = XMLUtils.createAndAppendElement(getProperties(), "object", propertiesElement);

        final Element fieldElement = XMLUtils.createAndAppendElement(getProperties(), "field", objectElement);
        XMLUtils.addBasicProperty(getProperties(), "Presence", "Visible", fieldElement);

        final Element drawElement = XMLUtils.createAndAppendElement(getProperties(), "draw", objectElement);
        XMLUtils.addBasicProperty(getProperties(), "Location", "", drawElement);
        XMLUtils.addBasicProperty(getProperties(), "Sizing", "Stretch Image To Fit", drawElement);
    }

    @Override
    public void setLayoutProperties(final Element layoutProperties) {
        setSizeAndPosition(layoutProperties);
    }

    @Override
    public void setObjectProperties(final Element objectElement) {
        final Element drawElement = (Element) objectElement.getElementsByTagName("draw").item(0);

        final String location = XMLUtils.getAttributeFromChildElement(drawElement, "Location").orElse("");
        final String sizing = XMLUtils.getAttributeFromChildElement(drawElement, "Sizing").orElse("Stretch Image To Fit");

        if (sizing.equals("Stretch Image To Fit")) {
            this.sizing = STRETCH;
        } else if (sizing.equals("Use Image Size")) {
            this.sizing = FULL_SIZE;
        }

        if (new File(location).exists()) {
            currentImage = Toolkit.getDefaultToolkit().createImage(location);
        } else {
            currentImage = null;
        }

        final JLabel imageLabel = (JLabel) getBaseComponent();

        if (currentImage == null) {
            imageLabel.setIcon(null);
        } else {
            if (this.sizing == STRETCH) {
                imageLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
            } else if (this.sizing == FULL_SIZE) {
                imageLabel.setIcon(new ImageIcon(currentImage));
            }
        }

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
        final JLabel imageLabel = (JLabel) getBaseComponent();

        if (currentImage == null) {
            imageLabel.setIcon(null);
        } else {
            if (this.sizing == STRETCH) {
                imageLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            } else if (this.sizing == FULL_SIZE) {
                imageLabel.setIcon(new ImageIcon(currentImage));
            }
        }

        setComponent(WidgetFactory.createResizedComponent(getBaseComponent(), width, height));
    }
}
