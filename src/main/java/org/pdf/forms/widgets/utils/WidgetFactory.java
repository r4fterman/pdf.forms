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

    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetFactory.class);

    private static final JFrame FRAME = new JFrame();
    private static Rectangle bounds;
    private static Element root;

    public static JLabel createResizedComponent(
            final JComponent comp,
            final int width,
            final int height) {
        return WidgetFactory.getComponentAsLabel(comp, width, height);
    }

    static IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds,
            final String groupName) {
        final IWidget widget = createWidget(IWidget.CHECK_BOX, bounds);

        ButtonGroup cbg;
        if (groupName == null) {
            final List<ButtonGroup> checkBoxGroups = page.getCheckBoxGroups();
            cbg = new ButtonGroup(IWidget.CHECK_BOX);
            checkBoxGroups.add(cbg);
        } else {
            cbg = page.getCheckBoxGroup(groupName);
            if (cbg == null) {
                final List<ButtonGroup> checkBoxGroups = page.getCheckBoxGroups();
                cbg = new ButtonGroup(IWidget.CHECK_BOX, groupName);
                checkBoxGroups.add(cbg);
            }
        }
        ((CheckBoxWidget) widget).setCheckBoxGroupName(cbg.getName());

        return widget;
    }

    public static IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds) {
        return createCheckBoxWidget(page, bounds, null);
    }

    public static IWidget createRadioButtonWidget(
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

    public static IWidget createWidget(
            final int widgetToAdd,
            final Rectangle bounds) {
        WidgetFactory.bounds = bounds;

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

    public static IWidget createWidget(
            final int widgetToAdd,
            final Element root) {
        WidgetFactory.root = root;

        final IWidget widget = createWidget(widgetToAdd);

        WidgetFactory.root = null;

        return widget;
    }

    private static IWidget createWidget(final int widgetToAdd) {
        IWidget w = null;

        switch (widgetToAdd) {
            case IWidget.TEXT_FIELD:
                w = WidgetFactory.createTextField();
                break;
            case IWidget.TEXT:
                w = WidgetFactory.createLabel();
                break;
            case IWidget.BUTTON:
                w = WidgetFactory.createButton();
                break;
            case IWidget.RADIO_BUTTON:
                w = WidgetFactory.createRadioButton();
                break;
            case IWidget.CHECK_BOX:
                w = WidgetFactory.createCheckBox();
                break;
            case IWidget.COMBO_BOX:
                w = WidgetFactory.createComboBox();
                break;
            case IWidget.LIST_BOX:
                w = WidgetFactory.createListBox();
                break;
            case IWidget.IMAGE:
                w = WidgetFactory.createImageWidget();
                break;
            default:
                LOGGER.warn("Manual exit because of impossible situation in WidgetFactory, trying to add widget type {}", widgetToAdd);
                break;
        }

        return w;
    }

    private static IWidget createImageWidget() {
        final JLabel image = new JLabel();
        image.setBackground(Color.WHITE);

        final int[] size = getSize(150, 150);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height));
        }

        return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height), root);
    }

    private static IWidget createTextField() {
        final SplitComponent tf = new SplitComponent("Text Field", new PdfTextField("Text Field"), SplitComponent.CAPTION_LEFT);

        final int[] size = getSize(150, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height));
        }

        return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height), root);
    }

    private static IWidget createLabel() {
        final PdfCaption label = new PdfCaption("Text");
        label.setBackground(IDesigner.PAGE_COLOR);

        final int[] size = getSize(100, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height));
        }

        return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height), root);
    }

    private static IWidget createCheckBox() {
        final int[] size = getSize(100, 20);
        final int width = size[0];
        final int height = size[1];

        final PdfCheckBox cb = new PdfCheckBox();
        cb.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent checkBox = new SplitComponent("Check Box", cb, SplitComponent.CAPTION_RIGHT);

        if (root == null) {
            return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height));
        }

        return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height), root);
    }

    private static IWidget createComboBox() {
        final PdfComboBox c = new PdfComboBox();

        final SplitComponent combo = new SplitComponent("Drop-down list", c, SplitComponent.CAPTION_LEFT);

        final int[] size = getSize(200, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height));
        }

        return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height), root);
    }

    private static IWidget createListBox() {
        final PdfList list = new PdfList();
        final SplitComponent scroll = new SplitComponent("List", list, SplitComponent.CAPTION_TOP);

        final int[] size = getSize(100, 100);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height));
        }

        return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height), root);
    }

    private static IWidget createButton() {
        final PdfButton button = new PdfButton("Button");

        final int[] size = getSize(100, 50);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new ButtonWidget(IWidget.BUTTON, button, getComponentAsLabel(button, width, height));
        }

        return new ButtonWidget(IWidget.BUTTON, button, getComponentAsLabel(button, width, height), root);
    }

    private static IWidget createRadioButton() {
        final PdfRadioButton rb = new PdfRadioButton();
        rb.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent radioButton = new SplitComponent("Radio Button", rb, SplitComponent.CAPTION_RIGHT);

        final int[] size = getSize(200, 20);

        final int width = size[0];
        final int height = size[1];

        if (root == null) {
            return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height));
        }

        return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height), root);
    }

    private static int[] getSize(
            final int defaultWidth,
            final int defaultHeight) {
        int width = defaultWidth;
        int height = defaultHeight;
        if (WidgetFactory.bounds != null) {

            if (WidgetFactory.bounds.width != 0) {
                width = WidgetFactory.bounds.width;
            }

            if (WidgetFactory.bounds.height != 0) {
                height = WidgetFactory.bounds.height;
            }

            WidgetFactory.bounds = null;
        }

        return new int[] {
                width, height
        };
    }

    private static JLabel getComponentAsLabel(
            final JComponent comp,
            final int width,
            final int height) {

        comp.setSize(width, height);

        FRAME.getContentPane().setLayout(null);
        FRAME.getContentPane().add(comp);
        FRAME.pack();

        final JLabel l = new JLabel(new ImageIcon(createImage(comp)));
        l.setSize(width, height);
        return l;
    }

    private static BufferedImage createImage(final JComponent component) {
        final Dimension d = component.getSize();
        final Rectangle region = new Rectangle(0, 0, d.width, d.height);
        component.setOpaque(true);
        final BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2d = image.createGraphics();
        g2d.setClip(region);
        component.paint(g2d);
        g2d.dispose();
        return image;
    }
}
