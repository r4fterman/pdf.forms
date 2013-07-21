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
 * WidgetFactory.java
 * ---------------
 */
package org.pdf.forms.widgets.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.*;
import org.pdf.forms.widgets.components.*;
import org.w3c.dom.Element;

public class WidgetFactory {

    private static Rectangle bounds;

    private static Element root;

    private static JFrame w = new JFrame();

    //private static PdfDecoder pdfDecoder = new PdfDecoder();

    public static JLabel createResizedComponent(JComponent comp, int width, int height) {
        return WidgetFactory.getComponentAsLabel(comp, width, height);
    }

    public static IWidget createCheckBoxWidget(Page page, Rectangle bounds, String groupName) {
        IWidget widget = createWidget(IWidget.CHECK_BOX, bounds);

        ButtonGroup cbg;
        if (groupName == null) {
            List checkBoxGroups = page.getCheckBoxGroups();
            cbg = new ButtonGroup(IWidget.CHECK_BOX);
            checkBoxGroups.add(cbg);
        } else {
            cbg = page.getCheckBoxGroup(groupName);
            if (cbg == null) {
                List checkBoxGroups = page.getCheckBoxGroups();
                cbg = new ButtonGroup(IWidget.CHECK_BOX, groupName);
                checkBoxGroups.add(cbg);
            }
        }
        ((CheckBoxWidget) widget).setCheckBoxGroupName(cbg.getName());

        return widget;
    }

    public static IWidget createCheckBoxWidget(Page page, Rectangle bounds) {
        return createCheckBoxWidget(page, bounds, null);
    }

    public static IWidget createRadioButtonWidget(Page page, Rectangle bounds) {
        IWidget widget = createWidget(IWidget.RADIO_BUTTON, bounds);

        List radioButtonGroups = page.getRadioButtonGroups();
        ButtonGroup rbg;
        if (radioButtonGroups.isEmpty()) {
            rbg = new ButtonGroup(IWidget.RADIO_BUTTON);
            radioButtonGroups.add(rbg);
        } else {
            rbg = (ButtonGroup) radioButtonGroups.get(radioButtonGroups.size() - 1);
        }

        ((RadioButtonWidget) widget).setRadioButtonGroupName(rbg.getName());

        return widget;
    }

    public static IWidget createWidget(int widgetToAdd, Rectangle bounds) {
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

    public static IWidget createWidget(int widgetToAdd, Element root) {
        WidgetFactory.root = root;

        IWidget widget = createWidget(widgetToAdd);

        WidgetFactory.root = null;

        return widget;
    }

    private static IWidget createWidget(int widgetToAdd) {
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
                System.out.println("Manual exit because of imposible sitation " +
                        "in WidgetFactory, trying to add widget type = " + widgetToAdd);

                new Exception().printStackTrace();
                //System.exit(1);

                break;
        }

        return w;
    }

    private static IWidget createImageWidget() {
        JLabel image = new JLabel();
        image.setBackground(new Color(255, 255, 255));

        int size[] = getSize(150, 150);

        int width = size[0], height = size[1];

        if (root == null)
            return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height));

        return new ImageWidget(IWidget.IMAGE, image, getComponentAsLabel(image, width, height), root);
    }

    private static IWidget createTextField() {
        SplitComponent tf = new SplitComponent("Text Field", new PdfTextField("Text Field"), SplitComponent.CAPTION_LEFT);

        int size[] = getSize(150, 20);

        int width = size[0], height = size[1];

        if (root == null)
            return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height));

        return new TextFieldWidget(IWidget.TEXT_FIELD, tf, getComponentAsLabel(tf, width, height), root);
    }

    private static IWidget createLabel() {
        PdfCaption label = new PdfCaption("Text");
        label.setBackground(IDesigner.PAGE_COLOR);

        int size[] = getSize(100, 20);

        int width = size[0], height = size[1];

        if (root == null)
            return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height));

        return new TextWidget(IWidget.TEXT, label, getComponentAsLabel(label, width, height), root);
    }

    private static IWidget createCheckBox() {
        int size[] = getSize(100, 20);
        int width = size[0], height = size[1];

        PdfCheckBox cb = new PdfCheckBox();
        cb.setBackground(IDesigner.PAGE_COLOR);

        SplitComponent checkBox = new SplitComponent("Check Box", cb, SplitComponent.CAPTION_RIGHT);

        if (root == null)
            return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height));

        return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, getComponentAsLabel(checkBox, width, height), root);
    }

    private static IWidget createComboBox() {
        PdfComboBox c = new PdfComboBox(/*new String[]{"first", "second"}*/);

        SplitComponent combo = new SplitComponent("Drop-down list", c, SplitComponent.CAPTION_LEFT);

        int size[] = getSize(200, 20);

        int width = size[0], height = size[1];

        if (root == null)
            return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height));

        return new ComboBoxWidget(IWidget.COMBO_BOX, combo, getComponentAsLabel(combo, width, height), root);
    }

    private static IWidget createListBox() {
        PdfList list = new PdfList();
        SplitComponent scroll = new SplitComponent("List", list, SplitComponent.CAPTION_TOP);

        int size[] = getSize(100, 100);

        int width = size[0], height = size[1];

        if (root == null)
            return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height));

        return new ListBoxWidget(IWidget.LIST_BOX, scroll, getComponentAsLabel(scroll, width, height), root);
    }

    private static IWidget createButton() {
        PdfButton button = new PdfButton("Button");

        int size[] = getSize(100, 50);

        int width = size[0], height = size[1];

        if (root == null)
            return new ButtonWidget(IWidget.BUTTON, button, getComponentAsLabel(button, width, height));

        return new ButtonWidget(IWidget.BUTTON, button, getComponentAsLabel(button, width, height), root);
    }

    private static IWidget createRadioButton() {
        PdfRadioButton rb = new PdfRadioButton();
        rb.setBackground(IDesigner.PAGE_COLOR);

        SplitComponent radioButton = new SplitComponent("Radio Button", rb, SplitComponent.CAPTION_RIGHT);

        int size[] = getSize(200, 20);

        int width = size[0], height = size[1];

        if (root == null)
            return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height));

        return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, getComponentAsLabel(radioButton, width, height), root);
    }

    private static int[] getSize(int defaultWidth, int defaultHeight) {
        if (WidgetFactory.bounds != null) {

            if (WidgetFactory.bounds.width != 0)
                defaultWidth = WidgetFactory.bounds.width;

            if (WidgetFactory.bounds.height != 0)
                defaultHeight = WidgetFactory.bounds.height;

            WidgetFactory.bounds = null;
        }

        return new int[]{defaultWidth, defaultHeight};
    }

    private static JLabel getComponentAsLabel(JComponent comp, int width, int height) {

        comp.setSize(width, height);

        w.getContentPane().setLayout(null);
        w.getContentPane().add(comp);
        w.pack();

        JLabel l = new JLabel(new ImageIcon(createImage(comp)));
        l.setSize(width, height);
        return l;
    }

    private static BufferedImage createImage(JComponent component) {
        Dimension d = component.getSize();
        Rectangle region = new Rectangle(0, 0, d.width, d.height);
        component.setOpaque(true);
        BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setClip(region);
        component.paint(g2d);
        g2d.dispose();
        return image;
    }
}
