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
 * WidgetPropertiesToolBar.java
 * ---------------
 */
package org.pdf.forms.gui.toolbars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vlsolutions.swing.toolbars.VLToolBar;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WidgetPropertiesToolBar extends VLToolBar {
    private ButtonGroup alignmentGroup;
    private JComboBox fontBox, fontSize;
    private ToolBarToggleButton fontBold, fontItalic;
    private ToolBarToggleButton alignLeft, alignCenter, alignRight;

    private IDesigner designerPanel;

    public WidgetPropertiesToolBar(IDesigner designerPanel) {

        this.designerPanel = designerPanel;

        Map fontFileMap = FontHandler.getInstance().getFontFileMap();
        Font[] fonts = (Font[]) fontFileMap.keySet().toArray(new Font[fontFileMap.size()]);
        String[] fontFamilies = new String[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontFamilies[i] = fonts[i].getFontName();
        }
        fontBox = new JComboBox(fontFamilies);
        fontBox.setPreferredSize(new Dimension(160, 24));
        fontBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateFont();
            }
        });
        add(fontBox);

        fontSize = new JComboBox(new String[]{"6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72"});
        fontSize.setEditable(true);
        fontSize.setPreferredSize(new Dimension(60, 24));
        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateFont();
            }
        });
        add(fontSize);

        addSeparator();

        fontBold = new ToolBarToggleButton();
        fontBold.init("/org/pdf/forms/res/Bold.gif", "Bold");
        fontBold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateFont();
            }
        });
        add(fontBold);

        fontItalic = new ToolBarToggleButton();
        fontItalic.init("/org/pdf/forms/res/Italic.gif", "Italic");
        fontItalic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateFont();
            }
        });
        add(fontItalic);

        ToolBarToggleButton fontUnderline = new ToolBarToggleButton();
        fontUnderline.init("/org/pdf/forms/res/Underline.gif", "Underline");
        fontUnderline.setEnabled(false);
        //fontItalic.addActionListener(commandListener);
        add(fontUnderline);
        addSeparator();

        alignLeft = new ToolBarToggleButton();
        alignLeft.init("/org/pdf/forms/res/Paragraph Align Left.gif", "Align Left");
        alignLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateAlignment("left");
            }
        });
        add(alignLeft);

        alignCenter = new ToolBarToggleButton();
        alignCenter.init("/org/pdf/forms/res/Paragraph Align Center.gif", "Align Center");
        alignCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateAlignment("center");
            }
        });
        add(alignCenter);

        alignRight = new ToolBarToggleButton();
        alignRight.init("/org/pdf/forms/res/Paragraph Align Right.gif", "Align Right");
        alignRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateAlignment("right");
            }
        });
        add(alignRight);

        alignmentGroup = new ButtonGroup();
        alignmentGroup.add(alignLeft);
        alignmentGroup.add(alignCenter);
        alignmentGroup.add(alignRight);
    }

    private void updateAlignment(String alignment) {

        Set widgets = designerPanel.getSelectedWidgets();

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {

            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();
            Element paragraphElement = (Element) properties.getElementsByTagName("paragraph").item(0);

            List paagraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            Element captionElement = (Element) paagraphList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) paagraphList.get(1);
            }

            Element captionAlignment = XMLUtils.getPropertyElement(captionElement, "Horizontal Alignment");
            Element valueAlignment = null;
            if (widget.allowEditCaptionAndValue()) {
                valueAlignment = XMLUtils.getPropertyElement(valueElement, "Horizontal Alignment");
            }

            captionAlignment.getAttributeNode("value").setValue(alignment);
            if (valueAlignment != null)
                valueAlignment.getAttributeNode("value").setValue(alignment);

            widget.setParagraphProperties(paragraphElement, IWidget.COMPONENT_BOTH);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void updateFont() {
        int fontStyle = 0;

        if (fontBold.isSelected()) {
            if (fontItalic.isSelected()) {
                fontStyle = IWidget.STYLE_BOLDITALIC;
            } else {
                fontStyle = IWidget.STYLE_BOLD;
            }
        } else if (fontItalic.isSelected()) {
            fontStyle = IWidget.STYLE_ITALIC;
        }

        Set widgets = designerPanel.getSelectedWidgets();
        for (Iterator it = widgets.iterator(); it.hasNext(); ) {

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

            setProperty((String) fontBox.getSelectedItem(), captionFontName, valueFontName);
            setProperty((String) fontSize.getSelectedItem(), captionFontSize, valueFontSize);

            setProperty(fontStyle + "", captionFontStyle, valueFontStyle);

            widget.setFontProperties(fontElement, IWidget.COMPONENT_BOTH);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void setFontProperties(Set widgets) {
        Map widgetsAndProperties = new HashMap();

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();

            Element layoutProperties = (Element) properties.getElementsByTagName("font").item(0);

            widgetsAndProperties.put(widget, layoutProperties);
        }

        String fontNameToUse = null, fontSizeToUse = null, fontStyleToUse = null;

        /** iterate through the widgets */
        for (Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext(); ) {

            IWidget widget = (IWidget) it.next();

            Element fontProperties = (Element) widgetsAndProperties.get(widget);

            /** get caption properties */
            Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

            String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name");
            String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size");
            String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style");

            String valueFontName, valueFontSize, valueFontStyle;

            if (widget.allowEditCaptionAndValue()) {

                /** get value properties */
                Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                valueFontName = XMLUtils.getAttributeFromChildElement(value, "Font Name");
                valueFontSize = XMLUtils.getAttributeFromChildElement(value, "Font Size");
                valueFontStyle = XMLUtils.getAttributeFromChildElement(value, "Font Style");
            } else {
                valueFontName = captionFontName;
                valueFontSize = captionFontSize;
                valueFontStyle = captionFontStyle;
            }

            /** get properties to use */
            String fontName = getProperty(captionFontName, valueFontName);
            String fontSize = getProperty(captionFontSize, valueFontSize);
            String fontStyle = getProperty(captionFontStyle, valueFontStyle);

            if (fontNameToUse == null) { // this must be the first time round
                fontNameToUse = fontName;
                fontSizeToUse = fontSize;
                fontStyleToUse = fontStyle;

            } else { // check for subsequent widgets

                if (!fontNameToUse.equals(fontName))
                    fontNameToUse = "mixed";

                if (!fontSizeToUse.equals(fontSize))
                    fontSizeToUse = "mixed";

                if (!fontStyleToUse.equals(fontStyle))
                    fontStyleToUse = "mixed";
            }
        }

        setComboValue(fontBox, fontNameToUse.equals("mixed") ? null : fontNameToUse);
        setComboValue(fontSize, fontSizeToUse.equals("mixed") ? null : fontSizeToUse);

        //setComboValue(fontStyleBox, fontStyleToUse.equals("mixed") ? null : new Integer(fontStyleToUse));

        if (fontStyleToUse.equals("mixed") || fontStyleToUse.equals("0")) {
            fontBold.setSelected(false);
            fontItalic.setSelected(false);
        } else {
            switch (Integer.parseInt(fontStyleToUse)) {
                case IWidget.STYLE_BOLD:
                    fontBold.setSelected(true);
                    break;
                case IWidget.STYLE_ITALIC:
                    fontItalic.setSelected(true);
                    break;
                case IWidget.STYLE_BOLDITALIC:
                    fontBold.setSelected(true);
                    fontItalic.setSelected(true);
                    break;
            }
        }
    }

    private void setParagraphProperties(Set widgets) {

        Map widgetsAndProperties = new HashMap();

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();

            Document properties = widget.getProperties();

            Element layoutProperties = (Element) properties.getElementsByTagName("paragraph").item(0);

            widgetsAndProperties.put(widget, layoutProperties);
        }

        String horizontalAlignmentToUse = null;

        /** iterate through the widgets */
        for (Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext(); ) {

            IWidget widget = (IWidget) it.next();

            Element paragraphPropertiesElement = (Element) widgetsAndProperties.get(widget);

            /** get caption properties */
            Element captionElement =
                    (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

            String captionHorizontalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Horizontal Alignment");
            String valueHorizontalAlignment;

            /** get value properties */
            if (widget.allowEditCaptionAndValue()) {
                Element valueElement =
                        (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_value").item(0);

                valueHorizontalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Horizontal Alignment");
            } else {
                valueHorizontalAlignment = captionHorizontalAlignment;
            }

            String horizontalAlignment = getHorizontalAlignment(captionHorizontalAlignment, valueHorizontalAlignment);

            if (horizontalAlignmentToUse == null) {
                horizontalAlignmentToUse = horizontalAlignment;
            } else {

                if (!horizontalAlignmentToUse.equals(horizontalAlignment))
                    horizontalAlignmentToUse = "mixed";
            }
        }

        try {
            if (horizontalAlignmentToUse.equals("mixed")) {
                alignmentGroup.setSelected(new JToggleButton("").getModel(), true);
            } else {
                horizontalAlignmentToUse = horizontalAlignmentToUse.substring(0, 1).toUpperCase() + horizontalAlignmentToUse.substring(1);

                Field field = getClass().getDeclaredField("align" + horizontalAlignmentToUse);
                JToggleButton toggleButton = (JToggleButton) field.get(this);
                toggleButton.setSelected(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setProperty(String value, Element captionElement, Element valueElement) {
        if (value != null && !value.equals("")) {
            captionElement.getAttributeNode("value").setValue(value);
            if (valueElement != null)
                valueElement.getAttributeNode("value").setValue(value);
        }
    }

    public void setProperties(Set widgets) {
        if (widgets.isEmpty()) {
            setState(false);
            return;
        }

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();

            if (widget.getType() == IWidget.IMAGE) {
                setState(false);
                return;
            }
        }

        setState(true);

        setFontProperties(widgets);
        setParagraphProperties(widgets);
    }

    private void setComboValue(JComboBox comboBox, Object value) {
        ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);

        comboBox.setSelectedItem(value);

        comboBox.addActionListener(listener);
    }

    private String getProperty(String captionProperty, String valueProperty) {
        String propertyToUse = null;

        if (captionProperty.equals(valueProperty)) { // both are the same
            propertyToUse = captionProperty;
        } else {
            /** properties are different */
            propertyToUse = "mixed";
        }

        return propertyToUse;
    }

    private String getHorizontalAlignment(String captionHorizontalAlignment, String valueHorizontalAlignment) {

        String horizontalAlignmentToUse;

        if (captionHorizontalAlignment.equals(valueHorizontalAlignment)) { // both value and caption are the same
            horizontalAlignmentToUse = captionHorizontalAlignment;
        } else {
            horizontalAlignmentToUse = "mixed";
        }

        return horizontalAlignmentToUse;
    }

    private void setState(boolean enabled) {

        if (!enabled) {
            setItemQuietly(fontBox, null);
            setItemQuietly(fontSize, null);

            setSelectedQuietly(fontBold, false);
            setSelectedQuietly(fontItalic, false);

            alignmentGroup.setSelected(new JToggleButton("").getModel(), true);
        }

        fontBox.setEnabled(enabled);
        fontSize.setEnabled(enabled);

        fontBold.setEnabled(enabled);
        fontItalic.setEnabled(enabled);

        alignLeft.setEnabled(enabled);
        alignCenter.setEnabled(enabled);
        alignRight.setEnabled(enabled);
    }

    private void setItemQuietly(JComboBox comboBox, Object item) {
        ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(item);
        comboBox.addActionListener(listener);
    }

    private void setSelectedQuietly(JToggleButton toggleButton, boolean selected) {
        ActionListener listener = toggleButton.getActionListeners()[0];
        toggleButton.removeActionListener(listener);
        toggleButton.setSelected(selected);
        toggleButton.addActionListener(listener);
    }
}
