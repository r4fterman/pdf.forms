package org.pdf.forms.gui.toolbars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vlsolutions.swing.toolbars.VLToolBar;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WidgetPropertiesToolBar extends VLToolBar {

    private final Logger logger = LoggerFactory.getLogger(WidgetPropertiesToolBar.class);

    private final IDesigner designerPanel;

    private final ButtonGroup alignmentGroup;
    private final JComboBox<String> fontBox;
    private final JComboBox<String> fontSize;
    private final ToolBarToggleButton fontBold;
    private final ToolBarToggleButton fontItalic;
    private final ToolBarToggleButton alignLeft;
    private final ToolBarToggleButton alignCenter;
    private final ToolBarToggleButton alignRight;


    public WidgetPropertiesToolBar(
            final FontHandler fontHandler,
            final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        fontBox = new JComboBox<>(fontHandler.getFontFamilies());
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

            final List paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            final Element captionElement = (Element) paragraphList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) paragraphList.get(1);
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

        String fontNameToUse = null;
        String fontSizeToUse = null;
        String fontStyleToUse = null;

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

            if (fontNameToUse == null) {
                // this must be the first time round
                fontNameToUse = fontName;
                fontSizeToUse = fontSize;
                fontStyleToUse = fontStyle;
            } else {
                // check for subsequent widgets
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
            final int style = Integer.parseInt(fontStyleToUse);
            if (style == IWidget.STYLE_BOLD) {
                fontBold.setSelected(true);
            } else if (style == IWidget.STYLE_ITALIC) {
                fontItalic.setSelected(true);
            } else if (style == IWidget.STYLE_BOLDITALIC) {
                fontBold.setSelected(true);
                fontItalic.setSelected(true);
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

        if ("mixed".equals(horizontalAlignmentToUse)) {
            alignmentGroup.setSelected(new JToggleButton("").getModel(), true);
        } else if ("left".equals(horizontalAlignmentToUse)) {
            alignLeft.setSelected(true);
        } else if ("center".equals(horizontalAlignmentToUse)) {
            alignCenter.setSelected(true);
        } else if ("right".equals(horizontalAlignmentToUse)) {
            alignRight.setSelected(true);
        } else {
            logger.warn("Unexpected horizontal alignment {}", horizontalAlignmentToUse);
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
        final String propertyToUse;
        if (captionProperty.equals(valueProperty)) {
            // both are the same
            propertyToUse = captionProperty;
        } else {
            // properties are different
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
            setItemQuietly(fontBox);
            setItemQuietly(fontSize);

            setSelectedQuietly(fontBold);
            setSelectedQuietly(fontItalic);

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

    private void setItemQuietly(final JComboBox comboBox) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(null);
        comboBox.addActionListener(listener);
    }

    private void setSelectedQuietly(final JToggleButton toggleButton) {
        final ActionListener listener = toggleButton.getActionListeners()[0];
        toggleButton.removeActionListener(listener);
        toggleButton.setSelected(false);
        toggleButton.addActionListener(listener);
    }
}
