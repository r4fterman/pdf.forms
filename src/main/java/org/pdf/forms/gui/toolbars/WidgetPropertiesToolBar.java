package org.pdf.forms.gui.toolbars;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

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

    private static final String[] FONT_SIZES = {
            "6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72"
    };

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
        this.fontBox = createFontComboBox(fontHandler);
        this.fontSize = createFontSizeComboBox();
        this.fontBold = createFontBoldButton();
        this.fontItalic = createFontItalicButton();
        this.alignLeft = createAlignLeftButton();
        this.alignCenter = createAlignCenterButton();
        this.alignRight = createAlignRightButton();

        add(fontBox);
        add(fontSize);
        addSeparator();
        add(fontBold);
        add(fontItalic);
        add(createFontUnderlineButton());
        addSeparator();
        add(alignLeft);
        add(alignCenter);
        add(alignRight);

        alignmentGroup = new ButtonGroup();
        alignmentGroup.add(alignLeft);
        alignmentGroup.add(alignCenter);
        alignmentGroup.add(alignRight);
    }

    private ToolBarToggleButton createAlignRightButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Right.gif", "Align Right");
        button.addActionListener(actionEvent -> updateAlignment("right"));
        return button;
    }

    private ToolBarToggleButton createAlignCenterButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Center.gif", "Align Center");
        button.addActionListener(actionEvent -> updateAlignment("center"));
        return button;
    }

    private ToolBarToggleButton createAlignLeftButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Left.gif", "Align Left");
        button.addActionListener(actionEvent -> updateAlignment("left"));
        return button;
    }

    private ToolBarToggleButton createFontUnderlineButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Underline.gif", "Underline");
        button.setEnabled(false);
        return button;
    }

    private ToolBarToggleButton createFontItalicButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Italic.gif", "Italic");
        button.addActionListener(actionEvent -> updateFont());
        return button;
    }

    private ToolBarToggleButton createFontBoldButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Bold.gif", "Bold");
        button.addActionListener(actionEvent -> updateFont());
        return button;
    }

    private JComboBox<String> createFontSizeComboBox() {
        final JComboBox<String> comboBox = new JComboBox<>(FONT_SIZES);
        comboBox.setEditable(true);
        comboBox.setPreferredSize(new Dimension(60, 24));
        comboBox.addActionListener(actionEvent -> updateFont());
        return comboBox;
    }

    private JComboBox<String> createFontComboBox(final FontHandler fontHandler) {
        final JComboBox<String> comboBox = new JComboBox<>(fontHandler.getFontFamilies());
        comboBox.setPreferredSize(new Dimension(160, 24));
        comboBox.addActionListener(actionEvent -> updateFont());
        return comboBox;
    }

    private void updateAlignment(final String alignment) {
        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();
        for (final IWidget widget: widgets) {
            final Document properties = widget.getProperties();
            final Element paragraphElement = (Element) properties.getElementsByTagName("paragraph").item(0);

            final List<Element> paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());
            final Element captionElement = paragraphList.get(0);

            final Optional<Element> horizontalAlignment = XMLUtils.getPropertyElement(captionElement,
                    "Horizontal Alignment");
            if (horizontalAlignment.isPresent()) {
                final Element captionAlignment = horizontalAlignment.get();
                captionAlignment.getAttributeNode("value").setValue(alignment);

                if (widget.allowEditCaptionAndValue()) {
                    Optional.of(paragraphList.get(1))
                            .map(valueElement -> XMLUtils.getPropertyElement(valueElement, "Horizontal Alignment"))
                            .flatMap(value -> value)
                            .ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
                }

                widget.setParagraphProperties(paragraphElement, IWidget.COMPONENT_BOTH);
            }
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void updateFont() {
        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();

        updateFontName(widgets);
        updateFontSize(widgets);
        updateFontStyle(widgets);

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void updateFontName(final Set<IWidget> widgets) {
        final String value = (String) fontBox.getSelectedItem();

        updateFontProperty(widgets, value, "Font Name");
    }

    private void updateFontSize(final Set<IWidget> widgets) {
        final String value = (String) fontSize.getSelectedItem();

        updateFontProperty(widgets, value, "Font Size");
    }

    private void updateFontStyle(final Set<IWidget> widgets) {
        final int fontStyle = getSelectedFontStyle();
        final String value = String.valueOf(fontStyle);

        updateFontProperty(widgets, value, "Font Style");
    }

    private void updateFontProperty(
            final Set<IWidget> widgets,
            final String fontValue,
            final String fontPropertyName) {
        for (final IWidget widget: widgets) {
            final Document properties = widget.getProperties();
            final Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            final List<Element> fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = fontList.get(0);

            final Optional<Element> fontCaption = XMLUtils.getPropertyElement(captionElement, fontPropertyName);
            if (fontCaption.isPresent()) {
                final Element fontCaptionElement = fontCaption.get();

                Element fontPropertyElement = null;
                if (widget.allowEditCaptionAndValue()) {
                    final Element valueElement = fontList.get(1);
                    final Optional<Element> propertyElement = XMLUtils.getPropertyElement(valueElement,
                            fontPropertyName);
                    if (propertyElement.isPresent()) {
                        fontPropertyElement = propertyElement.get();
                    }
                }

                setProperty(fontValue, fontCaptionElement, fontPropertyElement);

                widget.setFontProperties(fontElement, IWidget.COMPONENT_BOTH);
            }
        }
    }

    private int getSelectedFontStyle() {
        if (fontBold.isSelected()) {
            if (fontItalic.isSelected()) {
                return IWidget.STYLE_BOLDITALIC;
            }
            return IWidget.STYLE_BOLD;
        }

        if (fontItalic.isSelected()) {
            return IWidget.STYLE_ITALIC;
        }
        return 0;
    }

    private void setFontProperties(final Set<IWidget> widgets) {
        final Map<IWidget, Element> widgetsAndProperties = widgets.stream()
                .collect(toUnmodifiableMap(
                        widget -> widget,
                        widget -> {
                            final Document properties = widget.getProperties();
                            return (Element) properties.getElementsByTagName("font").item(0);
                        })
                );

        selectFontNameInComboBox(widgetsAndProperties);
        selectFontSizeInComboBox(widgetsAndProperties);
        selectFontStyle(widgetsAndProperties);
    }

    private void selectFontStyle(final Map<IWidget, Element> widgetsAndProperties) {
        final String fontStyleToUse = getFontPropertyValueFromList(widgetsAndProperties, "Font Style");
        if (fontStyleToUse.equals("mixed") || fontStyleToUse.equals("0")) {
            fontBold.setSelected(false);
            fontItalic.setSelected(false);
            return;
        }

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

    private void selectFontSizeInComboBox(final Map<IWidget, Element> widgetsAndProperties) {
        final String fontSizeToUse = getFontPropertyValueFromList(widgetsAndProperties, "Font Size");
        if (fontSizeToUse.equals("mixed")) {
            setComboValue(this.fontSize, null);
        } else {
            setComboValue(this.fontSize, fontSizeToUse);
        }
    }

    private void selectFontNameInComboBox(final Map<IWidget, Element> widgetsAndProperties) {
        final String fontNameToUse = getFontPropertyValueFromList(widgetsAndProperties, "Font Name");
        if (fontNameToUse.equals("mixed")) {
            setComboValue(this.fontBox, null);
        } else {
            setComboValue(this.fontBox, fontNameToUse);
        }
    }

    private String getFontPropertyValueFromList(
            final Map<IWidget, Element> widgetsAndProperties,
            final String fontPropertyName) {
        final List<String> fontSizeValues = widgetsAndProperties.entrySet().stream()
                .map(entry -> getFontPropertyValue(entry, fontPropertyName))
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(fontSizeValues, fontSizeValues.get(0)) == fontSizeValues.size();
        if (listContainsOnlyEqualValues) {
            return fontSizeValues.get(0);
        }
        return "mixed";
    }

    private String getFontPropertyValue(
            final Map.Entry<IWidget, Element> entry,
            final String fontPropertyName) {
        final IWidget widget = entry.getKey();
        final Element fontProperties = entry.getValue();

        final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);
        final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, fontPropertyName)
                .orElse("");

        final String valueFontStyle;
        if (widget.allowEditCaptionAndValue()) {
            final Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);
            valueFontStyle = XMLUtils.getAttributeFromChildElement(value, fontPropertyName).orElse("");
        } else {
            valueFontStyle = captionFontStyle;
        }

        return getComparedPropertyValue(captionFontStyle, valueFontStyle);
    }

    private void setParagraphProperties(final Set<IWidget> widgets) {
        final Map<IWidget, Element> widgetsAndProperties = widgets.stream()
                .collect(toUnmodifiableMap(
                        widget -> widget,
                        widget -> {
                            final Document properties = widget.getProperties();
                            return (Element) properties.getElementsByTagName("paragraph").item(0);
                        }
                ));

        final String horizontalAlignmentToUse = getHorizontalAlignmentToUse(widgetsAndProperties);
        if ("mixed".equals(horizontalAlignmentToUse)) {
            alignmentGroup.clearSelection();
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

    private String getHorizontalAlignmentToUse(final Map<IWidget, Element> widgetsAndProperties) {
        final String captionPropertyName = "paragraph_caption";
        final String valuePropertyName = "paragraph_value";
        final String attributeName = "Horizontal Alignment";

        final List<String> horizontalAlignmentValues = widgetsAndProperties.entrySet().stream()
                .map(entry -> {
                    final Element propertiesElement = entry.getValue();

                    final Element captionElement = (Element) propertiesElement.getElementsByTagName(captionPropertyName)
                            .item(0);
                    final String captionHorizontalAlignment = XMLUtils.getAttributeFromChildElement(captionElement,
                            attributeName).orElse("left");

                    final String valueHorizontalAlignment;
                    if (entry.getKey().allowEditCaptionAndValue()) {
                        final Element valueElement = (Element) propertiesElement
                                .getElementsByTagName(valuePropertyName).item(0);

                        valueHorizontalAlignment = XMLUtils.getAttributeFromChildElement(valueElement,
                                attributeName).orElse("left");
                    } else {
                        valueHorizontalAlignment = captionHorizontalAlignment;
                    }

                    return getComparedPropertyValue(captionHorizontalAlignment, valueHorizontalAlignment);
                })
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(horizontalAlignmentValues, horizontalAlignmentValues.get(0)) == horizontalAlignmentValues
                .size();
        if (listContainsOnlyEqualValues) {
            return horizontalAlignmentValues.get(0);
        }
        return "mixed";
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

        for (final IWidget widget: widgets) {
            if (widget.getType() == IWidget.IMAGE) {
                setState(false);
                return;
            }
        }

        setState(true);

        setFontProperties(widgets);
        setParagraphProperties(widgets);
    }

    private String getComparedPropertyValue(
            final String captionProperty,
            final String valueProperty) {
        // both are the same
        if (captionProperty.equals(valueProperty)) {
            return captionProperty;
        }
        // properties are different
        return "mixed";
    }

    private void setState(final boolean enabled) {
        if (!enabled) {
            setItemQuietly(fontBox);
            setItemQuietly(fontSize);

            setSelectedQuietly(fontBold);
            setSelectedQuietly(fontItalic);

            alignmentGroup.clearSelection();
        }

        fontBox.setEnabled(enabled);
        fontSize.setEnabled(enabled);

        fontBold.setEnabled(enabled);
        fontItalic.setEnabled(enabled);

        alignLeft.setEnabled(enabled);
        alignCenter.setEnabled(enabled);
        alignRight.setEnabled(enabled);
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
            final Object value) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);
        comboBox.setSelectedItem(value);
        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    private void setItemQuietly(final JComboBox<String> comboBox) {
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
