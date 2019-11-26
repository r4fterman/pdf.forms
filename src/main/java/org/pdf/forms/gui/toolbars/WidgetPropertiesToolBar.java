/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class WidgetPropertiesToolBar extends VLToolBar {

    private final Logger logger = LoggerFactory.getLogger(WidgetPropertiesToolBar.class);

    private final ButtonGroup alignmentGroup;
    private final JComboBox<String> fontBox;
    private final JComboBox<String> fontSize;
    private final ToolBarToggleButton fontBold;
    private final ToolBarToggleButton fontItalic;
    private final ToolBarToggleButton alignLeft;
    private final ToolBarToggleButton alignCenter;
    private final ToolBarToggleButton alignRight;

    private final IDesigner designerPanel;

    public WidgetPropertiesToolBar(final IDesigner designerPanel) {

        this.designerPanel = designerPanel;

        final Map fontFileMap = FontHandler.getInstance().getFontFileMap();
        final Font[] fonts = (Font[]) fontFileMap.keySet().toArray(new Font[0]);
        final String[] fontFamilies = new String[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fontFamilies[i] = fonts[i].getFontName();
        }
        fontBox = new JComboBox<>(fontFamilies);
        fontBox.setPreferredSize(new Dimension(160, 24));
        fontBox.addActionListener(actionEvent -> updateFont());
        add(fontBox);

        fontSize = new JComboBox<>(new String[] {
                "6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72"
        });
        fontSize.setEditable(true);
        fontSize.setPreferredSize(new Dimension(60, 24));
        fontSize.addActionListener(actionEvent -> updateFont());
        add(fontSize);

        addSeparator();

        fontBold = new ToolBarToggleButton();
        fontBold.init("/org/pdf/forms/res/Bold.gif", "Bold");
        fontBold.addActionListener(actionEvent -> updateFont());
        add(fontBold);

        fontItalic = new ToolBarToggleButton();
        fontItalic.init("/org/pdf/forms/res/Italic.gif", "Italic");
        fontItalic.addActionListener(actionEvent -> updateFont());
        add(fontItalic);

        final ToolBarToggleButton fontUnderline = new ToolBarToggleButton();
        fontUnderline.init("/org/pdf/forms/res/Underline.gif", "Underline");
        fontUnderline.setEnabled(false);
        //fontItalic.addActionListener(commandListener);
        add(fontUnderline);
        addSeparator();

        alignLeft = new ToolBarToggleButton();
        alignLeft.init("/org/pdf/forms/res/Paragraph Align Left.gif", "Align Left");
        alignLeft.addActionListener(actionEvent -> updateAlignment("left"));
        add(alignLeft);

        alignCenter = new ToolBarToggleButton();
        alignCenter.init("/org/pdf/forms/res/Paragraph Align Center.gif", "Align Center");
        alignCenter.addActionListener(actionEvent -> updateAlignment("center"));
        add(alignCenter);

        alignRight = new ToolBarToggleButton();
        alignRight.init("/org/pdf/forms/res/Paragraph Align Right.gif", "Align Right");
        alignRight.addActionListener(actionEvent -> updateAlignment("right"));
        add(alignRight);

        alignmentGroup = new ButtonGroup();
        alignmentGroup.add(alignLeft);
        alignmentGroup.add(alignCenter);
        alignmentGroup.add(alignRight);
    }

    private void updateAlignment(final String alignment) {

        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();

        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();
            final Element paragraphElement = (Element) properties.getElementsByTagName("paragraph").item(0);

            final List paagraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            final Element captionElement = (Element) paagraphList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) paagraphList.get(1);
            }

            final Element captionAlignment = XMLUtils.getPropertyElement(captionElement, "Horizontal Alignment").get();
            Optional<Element> valueAlignment = Optional.empty();
            if (widget.allowEditCaptionAndValue()) {
                valueAlignment = XMLUtils.getPropertyElement(valueElement, "Horizontal Alignment");
            }

            captionAlignment.getAttributeNode("value").setValue(alignment);
            valueAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));

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

        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();
        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();
            final Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            final List fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = (Element) fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name").get();
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size").get();
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style").get();

            Element valueFontName = null;
            Element valueFontSize = null;
            Element valueFontStyle = null;

            if (widget.allowEditCaptionAndValue()) {
                valueFontName = XMLUtils.getPropertyElement(valueElement, "Font Name").get();
                valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size").get();
                valueFontStyle = XMLUtils.getPropertyElement(valueElement, "Font Style").get();
            }

            setProperty((String) fontBox.getSelectedItem(), captionFontName, valueFontName);
            setProperty((String) fontSize.getSelectedItem(), captionFontSize, valueFontSize);

            setProperty(fontStyle + "", captionFontStyle, valueFontStyle);

            widget.setFontProperties(fontElement, IWidget.COMPONENT_BOTH);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void setFontProperties(final Set<IWidget> widgets) {
        final Map<IWidget, Element> widgetsAndProperties = new HashMap<>();

        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();

            final Element layoutProperties = (Element) properties.getElementsByTagName("font").item(0);

            widgetsAndProperties.put(widget, layoutProperties);
        }

        String fontNameToUse = null, fontSizeToUse = null, fontStyleToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element fontProperties = widgetsAndProperties.get(widget);

            /* get caption properties */
            final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

            final String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name").get();
            final String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size").get();
            final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style").get();

            final String valueFontName;
            final String valueFontSize;
            final String valueFontStyle;

            if (widget.allowEditCaptionAndValue()) {
                /* get value properties */
                final Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                valueFontName = XMLUtils.getAttributeFromChildElement(value, "Font Name").get();
                valueFontSize = XMLUtils.getAttributeFromChildElement(value, "Font Size").get();
                valueFontStyle = XMLUtils.getAttributeFromChildElement(value, "Font Style").get();
            } else {
                valueFontName = captionFontName;
                valueFontSize = captionFontSize;
                valueFontStyle = captionFontStyle;
            }

            /* get properties to use */
            final String fontName = getProperty(captionFontName, valueFontName);
            final String fontSize = getProperty(captionFontSize, valueFontSize);
            final String fontStyle = getProperty(captionFontStyle, valueFontStyle);

            if (fontNameToUse == null) { // this must be the first time round
                fontNameToUse = fontName;
                fontSizeToUse = fontSize;
                fontStyleToUse = fontStyle;

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

        setComboValue(fontBox, fontName);
        setComboValue(this.fontSize, fontSize);

        //setComboValue(fontStyleBox, fontStyleToUse.equals("mixed") ? null : new Integer(fontStyleToUse));

        if ("mixed".equals(fontStyleToUse) || "0".equals(fontStyleToUse)) {
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
                default:
                    break;
            }
        }
    }

    private void setParagraphProperties(final Set<IWidget> widgets) {
        final Map<IWidget, Element> widgetsAndProperties = new HashMap<>();

        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();

            final Element layoutProperties = (Element) properties.getElementsByTagName("paragraph").item(0);

            widgetsAndProperties.put(widget, layoutProperties);
        }

        String horizontalAlignmentToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element paragraphPropertiesElement = widgetsAndProperties.get(widget);

            /* get caption properties */
            final Element captionElement =
                    (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

            final String captionHorizontalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Horizontal Alignment").orElse("left");
            final String valueHorizontalAlignment;

            /* get value properties */
            if (widget.allowEditCaptionAndValue()) {
                final Element valueElement =
                        (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_value").item(0);

                valueHorizontalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Horizontal Alignment").orElse("left");
            } else {
                valueHorizontalAlignment = captionHorizontalAlignment;
            }

            final String horizontalAlignment = getHorizontalAlignment(captionHorizontalAlignment, valueHorizontalAlignment);

            if (horizontalAlignmentToUse == null) {
                horizontalAlignmentToUse = horizontalAlignment;
            } else {

                if (!horizontalAlignmentToUse.equals(horizontalAlignment)) {
                    horizontalAlignmentToUse = "mixed";
                }
            }
        }

        try {
            if ("mixed".equals(horizontalAlignmentToUse)) {
                alignmentGroup.setSelected(new JToggleButton("").getModel(), true);
            } else {
                horizontalAlignmentToUse = horizontalAlignmentToUse.substring(0, 1).toUpperCase() + horizontalAlignmentToUse.substring(1);

                final Field field = getClass().getDeclaredField("align" + horizontalAlignmentToUse);
                final JToggleButton toggleButton = (JToggleButton) field.get(this);
                toggleButton.setSelected(true);
            }
        } catch (final Exception e) {
            logger.error("Error setting paragraph properties", e);
        }
    }

    private void setProperty(
            final String value,
            final Element captionElement,
            final Element valueElement) {
        if (value != null && !value.equals("")) {
            captionElement.getAttributeNode("value").setValue(value);
            if (valueElement != null) {
                valueElement.getAttributeNode("value").setValue(value);
            }
        }
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            setState(false);
            return;
        }

        for (final IWidget widget : widgets) {
            if (widget.getType() == IWidget.IMAGE) {
                setState(false);
                return;
            }
        }

        setState(true);

        setFontProperties(widgets);
        setParagraphProperties(widgets);
    }

    private void setComboValue(
            final JComboBox comboBox,
            final Object value) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);

        comboBox.setSelectedItem(value);

        comboBox.addActionListener(listener);
    }

    private String getProperty(
            final String captionProperty,
            final String valueProperty) {
        String propertyToUse = null;

        if (captionProperty.equals(valueProperty)) { // both are the same
            propertyToUse = captionProperty;
        } else {
            /* properties are different */
            propertyToUse = "mixed";
        }

        return propertyToUse;
    }

    private String getHorizontalAlignment(
            final String captionHorizontalAlignment,
            final String valueHorizontalAlignment) {
        if (captionHorizontalAlignment.equals(valueHorizontalAlignment)) {
            // both value and caption are the same
            return captionHorizontalAlignment;
        } else {
            return "mixed";
        }
    }

    private void setState(final boolean enabled) {
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

    private void setItemQuietly(
            final JComboBox comboBox,
            final Object item) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(item);
        comboBox.addActionListener(listener);
    }

    private void setSelectedQuietly(
            final JToggleButton toggleButton,
            final boolean selected) {
        final ActionListener listener = toggleButton.getActionListeners()[0];
        toggleButton.removeActionListener(listener);
        toggleButton.setSelected(selected);
        toggleButton.addActionListener(listener);
    }
}
