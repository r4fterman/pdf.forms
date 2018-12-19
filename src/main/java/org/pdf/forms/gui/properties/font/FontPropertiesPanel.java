/*
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
* FontPropertiesPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

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

    /**
     * Creates new form FontPropertiesPanel
     */
    public FontPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {

        final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        currentlyEditingBox = new javax.swing.JComboBox<>();
        final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        final javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        final String[] fontFamilies = getFonts();
        fontNameBox = new JComboBox<>(fontFamilies);
        fontStyleBox = new javax.swing.JComboBox<>();
        underlineBox = new javax.swing.JComboBox<>();
        strikethroughBox = new javax.swing.JComboBox<>();
        colorBox = new javax.swing.JComboBox<>();
        fontSizeBox = new javax.swing.JComboBox<>();

        jLabel1.setText("Currently Editing:");

        currentlyEditingBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Caption and Value", "Caption properties", "Value properties" }));
        currentlyEditingBox.addActionListener(evt -> updateCurrentlyEditingBox(evt));

        jLabel2.setText("Font:");

        jLabel3.setText("Font Size:");

        jLabel4.setText("Font Style:");

        jLabel5.setText("Underline:");
        jLabel5.setEnabled(false);

        jLabel6.setText("Strikethrough:");
        jLabel6.setEnabled(false);

        jLabel7.setText("Color:");

        fontNameBox.addActionListener(evt -> updateFont(evt));

        fontStyleBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Plain", "Bold", "Italic", "Bold Italic" }));
        fontStyleBox.addActionListener(evt -> updateFont(evt));

        underlineBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Underline", "Underline", "Double Underline", "Word Underline", "Word Double Underline" }));
        underlineBox.setEnabled(false);
        underlineBox.addActionListener(evt -> updateFont(evt));

        strikethroughBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Off", "On" }));
        strikethroughBox.setEnabled(false);
        strikethroughBox.addActionListener(evt -> updateFont(evt));

        colorBox.setEditable(true);
        colorBox.setMaximumRowCount(5);
        colorBox.setModel(new javax.swing.DefaultComboBoxModel(new Object[] { Color.black, Color.blue, Color.cyan, Color.green, Color.red, Color.white, Color.yellow, "Custom" }));
        final Color color = (Color) colorBox.getSelectedItem();
        editor = new ColorComboBoxEditor(color, colorBox);
        colorBox.setEditor(editor);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(evt -> updateColor(evt));

        fontSizeBox.setEditable(true);
        fontSizeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72" }));
        fontSizeBox.addActionListener(evt -> updateFont(evt));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel4)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                                                .add(fontStyleBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(currentlyEditingBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel7)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 59, Short.MAX_VALUE)
                                                .add(colorBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel5)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                                                .add(underlineBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel6)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                                                .add(strikethroughBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jLabel2)
                                                        .add(jLabel3))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(fontNameBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(fontSizeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(currentlyEditingBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(fontNameBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel3)
                                        .add(fontSizeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel4)
                                        .add(fontStyleBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel5)
                                        .add(underlineBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel6)
                                        .add(strikethroughBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel7)
                                        .add(colorBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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

    private void updateFont(final java.awt.event.ActionEvent evt) {
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
            setProperty(index == -1 ? null : index + "", captionFontStyle, valueFontStyle);

            index = underlineBox.getSelectedIndex();
            setProperty(index == -1 ? null : index + "", captionUnderline, valueUnderline);

            index = strikethroughBox.getSelectedIndex();
            setProperty(index == -1 ? null : index + "", captionStrikethrough, valueStrikethrough);

            final Color color = ((Color) colorBox.getSelectedItem());
            setProperty(color == null ? null : color.getRGB() + "", captionColor, valueColor);

            widget.setFontProperties(widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);

        designerPanel.repaint();
    }

    private void updateColor(final java.awt.event.ActionEvent evt) {
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

    private void updateCurrentlyEditingBox(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCurrentlyEditingBox
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }//GEN-LAST:event_updateCurrentlyEditingBox

    public void updateAvailiableFonts() {
        final String[] fonts = getFonts();
        final DefaultComboBoxModel model = new DefaultComboBoxModel(fonts);
        fontNameBox.setModel(model);
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            int currentlyEditing) {
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

        if (!allowEditCaptionAndValue) {
            currentlyEditing = 1;
        }

        /* set the currently editing box */
        currentlyEditingBox.setSelectedIndex(currentlyEditing);
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

        setComboValue(fontNameBox, "mixed".equals(fontNameToUse) ? null : fontNameToUse);
        setComboValue(fontSizeBox, "mixed".equals(fontSizeToUse) ? null : fontSizeToUse);
        setComboValue(fontStyleBox, "mixed".equals(fontStyleToUse) ? null : Integer.valueOf(fontStyleToUse));
        setComboValue(underlineBox, "mixed".equals(underlineToUse) ? null : Integer.valueOf(underlineToUse));
        setComboValue(strikethroughBox, "mixed".equals(strikethroughToUse) ? null : Integer.valueOf(strikethroughToUse));
        setComboValue(colorBox, "mixed".equals(colorToUse) ? null : new Color(Integer.parseInt(colorToUse)));
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
        }

        return propertyToUse;
    }

    private javax.swing.JComboBox colorBox;
    private javax.swing.JComboBox<String> currentlyEditingBox;
    private javax.swing.JComboBox<String> fontNameBox;
    private javax.swing.JComboBox<String> fontSizeBox;
    private javax.swing.JComboBox<String> fontStyleBox;
    private javax.swing.JComboBox<String> strikethroughBox;
    private javax.swing.JComboBox<String> underlineBox;

}

