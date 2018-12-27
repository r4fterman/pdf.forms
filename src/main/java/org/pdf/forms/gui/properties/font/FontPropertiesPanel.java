/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
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
* FontPropertiesPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorCellRenderer;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorComboBoxEditor;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class FontPropertiesPanel extends javax.swing.JPanel {

    //fontNameBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique", "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Symbol", "ZapfDingbats" }));

    private ColorComboBoxEditor editor;
    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private javax.swing.JComboBox<Object> colorBox;
    private javax.swing.JComboBox<String> currentlyEditingBox;
    private javax.swing.JComboBox<String> fontNameBox;
    private javax.swing.JComboBox<String> fontSizeBox;
    private javax.swing.JComboBox<String> fontStyleBox;
    private javax.swing.JComboBox<String> strikethroughBox;
    private javax.swing.JComboBox<String> underlineBox;

    /**
     * Creates new form FontPropertiesPanel.
     */
    FontPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {

        final JLabel jLabel1 = new JLabel();
        currentlyEditingBox = new JComboBox<>();
        final JLabel jLabel2 = new JLabel();
        final JLabel jLabel3 = new JLabel();
        final JLabel jLabel4 = new JLabel();
        final JLabel jLabel5 = new JLabel();
        final JLabel jLabel6 = new JLabel();
        final JLabel jLabel7 = new JLabel();
        final String[] fontFamilies = getFonts();
        fontNameBox = new JComboBox<>(fontFamilies);
        fontStyleBox = new JComboBox<>();
        underlineBox = new JComboBox<>();
        strikethroughBox = new JComboBox<>();
        colorBox = new JComboBox<>();
        fontSizeBox = new JComboBox<>();

        jLabel1.setText("Currently Editing:");

        currentlyEditingBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Caption and Value",
                "Caption properties",
                "Value properties" }));
        currentlyEditingBox.addActionListener(this::updateCurrentlyEditingBox);

        jLabel2.setText("Font:");

        jLabel3.setText("Font Size:");

        jLabel4.setText("Font Style:");

        jLabel5.setText("Underline:");
        jLabel5.setEnabled(false);

        jLabel6.setText("Strikethrough:");
        jLabel6.setEnabled(false);

        jLabel7.setText("Color:");

        fontNameBox.addActionListener(this::updateFont);

        fontStyleBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Plain",
                "Bold",
                "Italic",
                "Bold Italic" }));
        fontStyleBox.addActionListener(this::updateFont);

        underlineBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "No Underline",
                "Underline",
                "Double Underline",
                "Word Underline",
                "Word Double Underline" }));
        underlineBox.setEnabled(false);
        underlineBox.addActionListener(this::updateFont);

        strikethroughBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Off",
                "On" }));
        strikethroughBox.setEnabled(false);
        strikethroughBox.addActionListener(this::updateFont);

        colorBox.setEditable(true);
        colorBox.setMaximumRowCount(5);
        colorBox.setModel(new DefaultComboBoxModel<>(new Object[] {
                Color.black,
                Color.blue,
                Color.cyan,
                Color.green,
                Color.red,
                Color.white,
                Color.yellow,
                "Custom" }));
        final Color color = (Color) colorBox.getSelectedItem();
        editor = new ColorComboBoxEditor(color, colorBox);
        colorBox.setEditor(editor);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(this::updateColor);

        fontSizeBox.setEditable(true);
        fontSizeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72" }));
        fontSizeBox.addActionListener(this::updateFont);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel4)
                                                .addPreferredGap(LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                                                .add(fontStyleBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel7)
                                                .addPreferredGap(LayoutStyle.RELATED, 59, Short.MAX_VALUE)
                                                .add(colorBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel5)
                                                .addPreferredGap(LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                                                .add(underlineBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel6)
                                                .addPreferredGap(LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                                                .add(strikethroughBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(jLabel2)
                                                        .add(jLabel3))
                                                .addPreferredGap(LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                                        .add(fontNameBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
                                                        .add(fontSizeBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(fontNameBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel3)
                                        .add(fontSizeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel4)
                                        .add(fontStyleBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel5)
                                        .add(underlineBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel6)
                                        .add(strikethroughBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel7)
                                        .add(colorBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(113, Short.MAX_VALUE))
        );
    }

    private String[] getFonts() {
        final FontHandler fontHandler = FontHandler.getInstance();
        final Map fontFileMap = fontHandler.getFontFileMap();
        final Font[] fonts = (Font[]) fontFileMap.keySet().toArray(new Font[0]);
        final String[] fontFamilies = new String[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontFamilies[i] = fonts[i].getFontName();
        }

        return fontFamilies;
    }

    private void updateFont(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        for (final IWidget widget : widgets) {
            final Element fontElement = widgetsAndProperties.get(widget);

            final List<Element> fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name");
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size");
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style");
            final Element captionUnderline = XMLUtils.getPropertyElement(captionElement, "Underline");
            final Element captionStrikethrough = XMLUtils.getPropertyElement(captionElement, "Strikethrough");
            final Element captionColor = XMLUtils.getPropertyElement(captionElement, "Color");

            Element valueFontName = null;
            Element valueFontSize = null;
            Element valueFontStyle = null;
            Element valueUnderline = null;
            Element valueStrikethrough = null;
            Element valueColor = null;

            if (widget.allowEditCaptionAndValue()) {
                valueFontName = XMLUtils.getPropertyElement(valueElement, "Font Name");
                valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size");
                valueFontStyle = XMLUtils.getPropertyElement(valueElement, "Font Style");
                valueUnderline = XMLUtils.getPropertyElement(valueElement, "Underline");
                valueStrikethrough = XMLUtils.getPropertyElement(valueElement, "Strikethrough");
                valueColor = XMLUtils.getPropertyElement(valueElement, "Color");
            }

            setProperty(fontNameBox.getSelectedItem(), captionFontName, valueFontName);
            setProperty(fontSizeBox.getSelectedItem(), captionFontSize, valueFontSize);

            int index = fontStyleBox.getSelectedIndex();
            String selectedIndex;
            if (index == -1) {
                selectedIndex = null;
            } else {
                selectedIndex = index + "";
            }
            setProperty(selectedIndex, captionFontStyle, valueFontStyle);

            index = underlineBox.getSelectedIndex();
            if (index == -1) {
                selectedIndex = null;
            } else {
                selectedIndex = index + "";
            }
            setProperty(selectedIndex, captionUnderline, valueUnderline);

            index = strikethroughBox.getSelectedIndex();
            if (index == -1) {
                selectedIndex = null;
            } else {
                selectedIndex = index + "";
            }
            setProperty(selectedIndex, captionStrikethrough, valueStrikethrough);

            final Color color = ((Color) colorBox.getSelectedItem());
            final String selectedColor;
            if (color == null) {
                selectedColor = null;
            } else {
                selectedColor = color.getRGB() + "";
            }
            setProperty(selectedColor, captionColor, valueColor);

            widget.setFontProperties(widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);

        designerPanel.repaint();
    }

    private void updateColor(final ActionEvent evt) {
        if ("Custom".equals(colorBox.getSelectedItem())) {
            final Color currentBackground = (Color) editor.getItem();
            final Color color = JColorChooser.showDialog(null, "Color Chooser", currentBackground);
            if ((color != null) && (currentBackground != color)) {
                colorBox.setSelectedItem(color);
            }
        }

        updateFont(null);
    }

    private void setProperty(
            final Object value,
            final Element captionElement,
            final Element valueElement) {
        if (value != null) {
            if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
                captionElement.getAttributeNode("value").setValue(value.toString());
                if (valueElement != null) {
                    valueElement.getAttributeNode("value").setValue(value.toString());
                }
            } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
                captionElement.getAttributeNode("value").setValue(value.toString());
            } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
                if (valueElement != null) {
                    valueElement.getAttributeNode("value").setValue(value.toString());
                }
            }
        }
    }

    private void updateCurrentlyEditingBox(final ActionEvent evt) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    public void updateAvailiableFonts() {
        final String[] fonts = getFonts();
        final DefaultComboBoxModel model = new DefaultComboBoxModel<>(fonts);
        fontNameBox.setModel(model);
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {
        this.widgetsAndProperties = widgetsAndProperties;

        //        /** update the list of fonts just in case a new one has been added */
        //        String[] fonts = getFonts();
        //        DefaultComboBoxModel model = new DefaultComboBoxModel(fonts);
        //        fontNameBox.setModel(model);

        String fontNameToUse = null, fontSizeToUse = null, fontStyleToUse = null, underlineToUse = null,
                strikethroughToUse = null, colorToUse = null;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.allowEditCaptionAndValue()) {
                allowEditCaptionAndValue = true;
                break;
            }
        }

        int currentlyEditingIdx = currentlyEditing;
        if (!allowEditCaptionAndValue) {
            currentlyEditingIdx = 1;
        }

        /* set the currently editing box */
        currentlyEditingBox.setSelectedIndex(currentlyEditingIdx);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element fontProperties = widgetsAndProperties.get(widget);

            /* get caption properties */
            final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

            final String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name");
            final String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size");
            final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style");
            final String captionUnderline = XMLUtils.getAttributeFromChildElement(caption, "Underline");
            final String captionStrikethrough = XMLUtils.getAttributeFromChildElement(caption, "Strikethrough");
            final String captionColor = XMLUtils.getAttributeFromChildElement(caption, "Color");

            final String valueFontName;
            final String valueFontSize;
            final String valueFontStyle;
            final String valueUnderline;
            final String valueStrikethrough;
            final String valueColor;

            if (widget.allowEditCaptionAndValue()) {
                /* get value properties */
                final Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                valueFontName = XMLUtils.getAttributeFromChildElement(value, "Font Name");
                valueFontSize = XMLUtils.getAttributeFromChildElement(value, "Font Size");
                valueFontStyle = XMLUtils.getAttributeFromChildElement(value, "Font Style");
                valueUnderline = XMLUtils.getAttributeFromChildElement(value, "Underline");
                valueStrikethrough = XMLUtils.getAttributeFromChildElement(value, "Strikethrough");
                valueColor = XMLUtils.getAttributeFromChildElement(value, "Color");
            } else {
                valueFontName = captionFontName;
                valueFontSize = captionFontSize;
                valueFontStyle = captionFontStyle;
                valueUnderline = captionUnderline;
                valueStrikethrough = captionStrikethrough;
                valueColor = captionColor;
            }

            /* get properties to use */
            final String fontName = getProperty(currentlyEditing, captionFontName, valueFontName);
            final String fontSize = getProperty(currentlyEditing, captionFontSize, valueFontSize);
            final String fontStyle = getProperty(currentlyEditing, captionFontStyle, valueFontStyle);
            final String underline = getProperty(currentlyEditing, captionUnderline, valueUnderline);
            final String strikethrough = getProperty(currentlyEditing, captionStrikethrough, valueStrikethrough);
            final String color = getProperty(currentlyEditing, captionColor, valueColor);

            if (fontNameToUse == null) { // this must be the first time round
                fontNameToUse = fontName;
                fontSizeToUse = fontSize;
                fontStyleToUse = fontStyle;
                underlineToUse = underline;
                strikethroughToUse = strikethrough;
                colorToUse = color;

            } else { // check for subsequent widgets

                if (!fontNameToUse.equals(fontName)) {
                    fontNameToUse = "mixed";
                }

                if (!fontSizeToUse.equals(fontSize)) {
                    fontSizeToUse = "mixed";
                }

                if (!fontStyleToUse.equals(fontStyle)) {
                    fontStyleToUse = "mixed";
                }

                if (!underlineToUse.equals(underline)) {
                    underlineToUse = "mixed";
                }

                if (!strikethroughToUse.equals(strikethrough)) {
                    strikethroughToUse = "mixed";
                }

                if (!colorToUse.equals(color)) {
                    colorToUse = "mixed";
                }
            }
        }

        final String fontName;
        if ("mixed".equals(fontNameToUse)) {
            fontName = null;
        } else {
            fontName = fontNameToUse;
        }

        final String fontSize;
        if ("mixed".equals(fontSizeToUse)) {
            fontSize = null;
        } else {
            fontSize = fontSizeToUse;
        }

        final Integer fontStyle;
        if ("mixed".equals(fontStyleToUse)) {
            fontStyle = null;
        } else {
            fontStyle = Integer.valueOf(fontStyleToUse);
        }

        final Integer underline;
        if ("mixed".equals(underlineToUse)) {
            underline = null;
        } else {
            underline = Integer.valueOf(underlineToUse);
        }

        final Integer strikethrough;
        if ("mixed".equals(strikethroughToUse)) {
            strikethrough = null;
        } else {
            strikethrough = Integer.valueOf(strikethroughToUse);
        }

        final Color color;
        if ("mixed".equals(colorToUse)) {
            color = null;
        } else {
            color = new Color(Integer.parseInt(colorToUse));
        }

        setComboValue(fontNameBox, fontName);
        setComboValue(fontSizeBox, fontSize);
        setComboValue(fontStyleBox, fontStyle);
        setComboValue(underlineBox, underline);
        setComboValue(strikethroughBox, strikethrough);
        setComboValue(colorBox, color);
    }

    private void setComboValue(
            final JComboBox comboBox,
            final Object value) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex((Integer) value);
        } else {
            comboBox.setSelectedItem(value);
        }

        comboBox.addActionListener(listener);
    }

    private String getProperty(
            final int currentlyEditing,
            final String captionProperty,
            final String valueProperty) {
        String propertyToUse = null;

        switch (currentlyEditing) {
            case IWidget.COMPONENT_BOTH:
                if (captionProperty.equals(valueProperty)) { // both are the same
                    propertyToUse = captionProperty;
                } else {
                    /* properties are different */
                    propertyToUse = "mixed";
                }

                break;
            case IWidget.COMPONENT_CAPTION:
                /* just set the caption properties */
                propertyToUse = captionProperty;

                break;

            case IWidget.COMPONENT_VALUE:
                /* just set the value properties */
                propertyToUse = valueProperty;

                break;
            default:
                break;
        }

        return propertyToUse;
    }

}

