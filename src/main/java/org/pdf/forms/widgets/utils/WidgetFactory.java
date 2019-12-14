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
 * WidgetFactory.java
 * ---------------
 */
package org.pdf.forms.widgets.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.ButtonWidget;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.ComboBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.ImageWidget;
import org.pdf.forms.widgets.ListBoxWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.pdf.forms.widgets.TextFieldWidget;
import org.pdf.forms.widgets.TextWidget;
import org.pdf.forms.widgets.components.PdfButton;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfCheckBox;
import org.pdf.forms.widgets.components.PdfComboBox;
import org.pdf.forms.widgets.components.PdfList;
import org.pdf.forms.widgets.components.PdfRadioButton;
import org.pdf.forms.widgets.components.PdfTextField;
import org.pdf.forms.widgets.components.SplitComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class WidgetFactory {

    private static final JFrame FRAME = new JFrame();

    public static JLabel createResizedComponent(
            final JComponent comp,
            final int width,
            final int height) {
        return getComponentAsLabel(comp, width, height);
    }

    private static JLabel getComponentAsLabel(
            final JComponent component,
            final int width,
            final int height) {
        component.setSize(width, height);

        FRAME.getContentPane().setLayout(null);
        FRAME.getContentPane().add(component);
        FRAME.pack();

        final JLabel label = new JLabel(new ImageIcon(createImage(component)));
        label.setSize(width, height);
        return label;
    }

    private static BufferedImage createImage(final JComponent component) {
        final Dimension dimension = component.getSize();
        final Rectangle region = new Rectangle(0, 0, dimension.width, dimension.height);
        component.setOpaque(true);

        final BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.setClip(region);
        component.paint(graphics2D);
        graphics2D.dispose();

        return image;
    }

    private final Logger logger = LoggerFactory.getLogger(WidgetFactory.class);

    private final FontHandler fontHandler;
    private Rectangle bounds;
    private Element root;

    public WidgetFactory(final FontHandler fontHandler) {
        this.fontHandler = fontHandler;
    }

    IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds,
            final String groupName) {
        final IWidget widget = createWidget(IWidget.CHECK_BOX, bounds);

        ButtonGroup buttonGroup;
        if (groupName == null) {
            final List<ButtonGroup> checkBoxGroups = page.getCheckBoxGroups();
            buttonGroup = new ButtonGroup(IWidget.CHECK_BOX);
            checkBoxGroups.add(buttonGroup);
        } else {
            buttonGroup = page.getCheckBoxGroup(groupName);
            if (buttonGroup == null) {
                final List<ButtonGroup> checkBoxGroups = page.getCheckBoxGroups();
                buttonGroup = new ButtonGroup(IWidget.CHECK_BOX, groupName);
                checkBoxGroups.add(buttonGroup);
            }
        }
        ((CheckBoxWidget) widget).setCheckBoxGroupName(buttonGroup.getName());

        return widget;
    }

    public IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds) {
        return createCheckBoxWidget(page, bounds, null);
    }

    public IWidget createRadioButtonWidget(
            final Page page,
            final Rectangle bounds) {
        final IWidget widget = createWidget(IWidget.RADIO_BUTTON, bounds);

        final List<ButtonGroup> radioButtonGroups = page.getRadioButtonGroups();
        final ButtonGroup rbg;
        if (radioButtonGroups.isEmpty()) {
            rbg = new ButtonGroup(IWidget.RADIO_BUTTON);
            radioButtonGroups.add(rbg);
        } else {
            rbg = radioButtonGroups.get(radioButtonGroups.size() - 1);
        }

        ((RadioButtonWidget) widget).setRadioButtonGroupName(rbg.getName());

        return widget;
    }

    public IWidget createWidget(
            final int widgetToAdd,
            final Rectangle bounds) {
        this.bounds = bounds;

        if (bounds != null) {
            if (bounds.height < 0) {
                bounds.height = -bounds.height;
                bounds.y = bounds.y - bounds.height;
            }

            if (bounds.width < 0) {
                bounds.width = -bounds.width;
                bounds.x = bounds.x - bounds.width;
            }
        }

        return createWidget(widgetToAdd);
    }

    public IWidget createWidget(
            final int widgetToAdd,
            final Element root) {
        this.root = root;

        final IWidget widget = createWidget(widgetToAdd);

        this.root = null;

        return widget;
    }

    private IWidget createWidget(final int widgetToAdd) {
        IWidget w = null;

        switch (widgetToAdd) {
            case IWidget.TEXT_FIELD:
                w = createTextField();
                break;
            case IWidget.TEXT:
                w = createLabel();
                break;
            case IWidget.BUTTON:
                w = createButton();
                break;
            case IWidget.RADIO_BUTTON:
                w = createRadioButton();
                break;
            case IWidget.CHECK_BOX:
                w = createCheckBox();
                break;
            case IWidget.COMBO_BOX:
                w = createComboBox();
                break;
            case IWidget.LIST_BOX:
                w = createListBox();
                break;
            case IWidget.IMAGE:
                w = createImageWidget();
                break;
            default:
                logger.warn("Manual exit because of impossible situation in WidgetFactory, trying to add widget type {}", widgetToAdd);
                break;
        }

        return w;
    }

    private IWidget createImageWidget() {
        final JLabel image = new JLabel();
        image.setBackground(Color.WHITE);

        final int[] size = getSize(150, 150);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height), fontHandler);
        }

        return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height), root, fontHandler);
    }

    private IWidget createTextField() {
        final SplitComponent tf = new SplitComponent("Text Field", new PdfTextField("Text Field", fontHandler), SplitComponent.CAPTION_LEFT, fontHandler);

        final int[] size = getSize(150, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height), fontHandler);
        }

        return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height), root, fontHandler);
    }

    private IWidget createLabel() {
        final PdfCaption label = new PdfCaption("Text", fontHandler);
        label.setBackground(IDesigner.PAGE_COLOR);

        final int[] size = getSize(100, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height), fontHandler);
        }

        return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height), root, fontHandler);
    }

    private IWidget createCheckBox() {
        final int[] size = getSize(100, 20);
        final int width = size[0];
        final int height = size[1];

        final PdfCheckBox cb = new PdfCheckBox();
        cb.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent checkBox = new SplitComponent("Check Box", cb, SplitComponent.CAPTION_RIGHT, fontHandler);

        if (root == null) {
            return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height), fontHandler);
        }

        return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height), root, fontHandler);
    }

    private IWidget createComboBox() {
        final PdfComboBox pdfComboBox = new PdfComboBox(fontHandler);

        final SplitComponent combo = new SplitComponent("Drop-down list", pdfComboBox, SplitComponent.CAPTION_LEFT, fontHandler);

        final int[] size = getSize(200, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height), fontHandler);
        }

        return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height), root, fontHandler);
    }

    private IWidget createListBox() {
        final PdfList pdfList = new PdfList(fontHandler);
        final SplitComponent scroll = new SplitComponent("List", pdfList, SplitComponent.CAPTION_TOP, fontHandler);

        final int[] size = getSize(100, 100);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height), fontHandler);
        }

        return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height), root, fontHandler);
    }

    private IWidget createButton() {
        final PdfButton pdfButton = new PdfButton("Button", fontHandler);

        final int[] size = getSize(100, 50);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ButtonWidget(IWidget.BUTTON, pdfButton, getComponentAsLabel(pdfButton, width, height), fontHandler);
        }

        return new ButtonWidget(IWidget.BUTTON, pdfButton, getComponentAsLabel(pdfButton, width, height), root, fontHandler);
    }

    private IWidget createRadioButton() {
        final PdfRadioButton pdfRadioButton = new PdfRadioButton();
        pdfRadioButton.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent radioButton = new SplitComponent("Radio Button", pdfRadioButton, SplitComponent.CAPTION_RIGHT, fontHandler);

        final int[] size = getSize(200, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height), fontHandler);
        }

        return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height), root, fontHandler);
    }

    private int[] getSize(
            final int defaultWidth,
            final int defaultHeight) {
        int width = defaultWidth;
        int height = defaultHeight;
        if (this.bounds != null) {
            if (this.bounds.width != 0) {
                width = this.bounds.width;
            }

            if (this.bounds.height != 0) {
                height = this.bounds.height;
            }

            this.bounds = null;
        }

        return new int[] {
                width, height
        };
    }

}
