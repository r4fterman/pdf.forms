package org.pdf.forms.gui.properties.font;

import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.GroupLayout.TRAILING;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorCellRenderer;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorComboBoxEditor;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class FontPropertiesPanel extends JPanel {

    private static final String[] FONT_SIZES = {
            "6",
            "8",
            "10",
            "12",
            "14",
            "16",
            "18",
            "20",
            "24",
            "28",
            "36",
            "48",
            "72"
    };
    private static final String[] FONT_STYLES = {
            "Plain",
            "Bold",
            "Italic",
            "Bold Italic"};
    private static final String PROPERTY_UNDERLINE = "Underline";
    private static final String[] UNDERLINE_TYPES = {
            "No Underline",
            PROPERTY_UNDERLINE,
            "Double Underline",
            "Word Underline",
            "Word Double Underline"};

    private static final String[] STRIKETHROUGH_VALUES = {
            "Off",
            "On"};

    private static final String CUSTOM_COLOR = "Custom";
    private static final Object[] COLORS = {
            Color.black,
            Color.blue,
            Color.cyan,
            Color.green,
            Color.red,
            Color.white,
            Color.yellow,
            CUSTOM_COLOR};

    private static final String[] EDITING_VALUES = {
            "Caption and Value",
            "Caption properties",
            "Value properties"};

    private static final String ATTRIBUTE_VALUE = "value";

    private static final String PROPERTY_COLOR = "Color";
    private static final String PROPERTY_STRIKETHROUGH = "Strikethrough";
    private static final String PROPERTY_FONT_STYLE = "Font Style";
    private static final String PROPERTY_FONT_SIZE = "Font Size";
    private static final String PROPERTY_FONT_NAME = "Font Name";

    private final FontHandler fontHandler;

    private ColorComboBoxEditor editor;
    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private JComboBox<Object> colorBox;
    private JComboBox<String> currentlyEditingBox;
    private JComboBox<String> fontNameBox;
    private JComboBox<String> fontSizeBox;
    private JComboBox<String> fontStyleBox;
    private JComboBox<String> strikethroughBox;
    private JComboBox<String> underlineBox;

    FontPropertiesPanel(final FontHandler fontHandler) {
        this.fontHandler = fontHandler;

        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JLabel currentlyEditingLabel = new JLabel("Currently Editing:");

        currentlyEditingBox = new JComboBox<>(EDITING_VALUES);
        currentlyEditingBox.addActionListener(this::updateCurrentlyEditingBox);

        final JLabel fontLabel = new JLabel("Font:");
        final JLabel fontSizeLabel = new JLabel("Font Size:");
        final JLabel fontStyleLabel = new JLabel("Font Style:");
        final JLabel underlineLabel = new JLabel("Underline:");
        underlineLabel.setEnabled(false);

        final JLabel strikethroughLabel = new JLabel("Strikethrough:");
        strikethroughLabel.setEnabled(false);

        final JLabel colorLabel = new JLabel("Color:");

        fontNameBox = new JComboBox<>(fontHandler.getFontFamilies());
        fontNameBox.addActionListener(this::updateFont);

        fontStyleBox = new JComboBox<>(FONT_STYLES);
        fontStyleBox.addActionListener(this::updateFont);

        underlineBox = new JComboBox<>(UNDERLINE_TYPES);
        underlineBox.setEnabled(false);
        underlineBox.addActionListener(this::updateFont);

        strikethroughBox = new JComboBox<>(STRIKETHROUGH_VALUES);
        strikethroughBox.setEnabled(false);
        strikethroughBox.addActionListener(this::updateFont);

        colorBox = new JComboBox<>(COLORS);
        colorBox.setEditable(true);
        colorBox.setMaximumRowCount(5);

        editor = new ColorComboBoxEditor((Color) colorBox.getSelectedItem(), colorBox);

        colorBox.setEditor(editor);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(this::updateColor);

        fontSizeBox = new JComboBox<>(FONT_SIZES);
        fontSizeBox.setEditable(true);
        fontSizeBox.addActionListener(this::updateFont);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        final GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup()
                .add(fontStyleLabel)
                .addPreferredGap(LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                .add(fontStyleBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);

        final GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup()
                .add(currentlyEditingLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(currentlyEditingBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);
        final GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup()
                .add(colorLabel)
                .addPreferredGap(LayoutStyle.RELATED, 59, Short.MAX_VALUE)
                .add(colorBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);
        final GroupLayout.SequentialGroup sequentialGroup3 = layout.createSequentialGroup()
                .add(underlineLabel)
                .addPreferredGap(LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                .add(underlineBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);
        final GroupLayout.SequentialGroup sequentialGroup4 = layout.createSequentialGroup()
                .add(strikethroughLabel)
                .addPreferredGap(LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                .add(strikethroughBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);

        final GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(TRAILING)
                .add(fontNameBox, PREFERRED_SIZE, 162, PREFERRED_SIZE)
                .add(fontSizeBox, PREFERRED_SIZE, 162, PREFERRED_SIZE);

        final GroupLayout.SequentialGroup sequentialGroup5 = layout.createSequentialGroup()
                .add(layout.createParallelGroup(LEADING).add(fontLabel).add(fontSizeLabel))
                .addPreferredGap(LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                .add(parallelGroup1);

        final GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(LEADING)
                .add(sequentialGroup)
                .add(sequentialGroup1)
                .add(sequentialGroup2)
                .add(sequentialGroup3)
                .add(sequentialGroup4)
                .add(TRAILING, sequentialGroup5);

        final GroupLayout.SequentialGroup sequentialGroup6 = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup)
                .addContainerGap(140, Short.MAX_VALUE);

        layout.setHorizontalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup6));

        final GroupLayout.SequentialGroup sequentialGroup7 = layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(BASELINE)
                        .add(currentlyEditingLabel)
                        .add(currentlyEditingBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(fontLabel)
                        .add(fontNameBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(fontSizeLabel)
                        .add(fontSizeBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(fontStyleLabel)
                        .add(fontStyleBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(underlineLabel)
                        .add(underlineBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(strikethroughLabel)
                        .add(strikethroughBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(BASELINE)
                        .add(colorLabel)
                        .add(colorBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addContainerGap(113, Short.MAX_VALUE);
        layout.setVerticalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup7));
    }

    private void updateFont(final ActionEvent event) {
        for (final Map.Entry<IWidget, Element> entry: widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element fontElement = entry.getValue();

            final List<Element> fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            if (!fontList.isEmpty()) {
                final Element captionElement = fontList.get(0);

                final Element valueElement;
                if (widget.allowEditCaptionAndValue()) {
                    valueElement = fontList.get(1);
                } else {
                    valueElement = null;
                }

                updateFontNameProperty(widget, captionElement, valueElement);
                updateFontSizeProperty(widget, captionElement, valueElement);
                updateFontStyleProperty(widget, captionElement, valueElement);
                updateUnderlineProperty(widget, captionElement, valueElement);
                updateStrikethroughProperty(widget, captionElement, valueElement);
                updateColorProperty(widget, captionElement, valueElement);
            }

            widget.setFontProperties(widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgetsAndProperties.keySet());
        designerPanel.repaint();
    }

    private void updateFontNameProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Object value = fontNameBox.getSelectedItem();

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_FONT_NAME, value);
    }

    private void updateFontSizeProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Object value = fontSizeBox.getSelectedItem();

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_FONT_SIZE, value);
    }

    private void updateFontStyleProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final String value = getComboBoxSelectedValueAsString(fontStyleBox);

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_FONT_STYLE, value);
    }

    private void updateUnderlineProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final String value = getComboBoxSelectedValueAsString(underlineBox);

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_UNDERLINE, value);
    }

    private void updateStrikethroughProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final String value = getComboBoxSelectedValueAsString(strikethroughBox);

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_STRIKETHROUGH, value);
    }

    private void updateColorProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final String value = getSelectedColorValueAsString();

        updatePropertyElementValue(widget, captionElement, valueElement, PROPERTY_COLOR, value);
    }

    private void updateColor(final ActionEvent event) {
        if (CUSTOM_COLOR.equals(colorBox.getSelectedItem())) {
            final Color currentBackground = (Color) editor.getItem();
            final Color color = JColorChooser.showDialog(null, "Color Chooser", currentBackground);
            if ((color != null) && (currentBackground != color)) {
                colorBox.setSelectedItem(color);
            }
        }

        updateFont(null);
    }

    private void updatePropertyElementValue(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement,
            final String propertyName,
            final Object value) {
        final Optional<Element> captionPropertyElement = XMLUtils.getPropertyElement(captionElement, propertyName);
        if (captionPropertyElement.isEmpty()) {
            return;
        }
        final Element propertyElement = captionPropertyElement.get();

        Element propertyValueElement = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> element = XMLUtils.getPropertyElement(valueElement, propertyName);
            if (element.isPresent()) {
                propertyValueElement = element.get();
            }
        }
        setProperty(value, propertyElement, propertyValueElement);
    }

    private void setProperty(
            final Object value,
            final Element captionElement,
            final Element valueElement) {
        if (value == null) {
            return;
        }

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            captionElement.getAttributeNode(ATTRIBUTE_VALUE).setValue(value.toString());
            if (valueElement != null) {
                valueElement.getAttributeNode(ATTRIBUTE_VALUE).setValue(value.toString());
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            captionElement.getAttributeNode(ATTRIBUTE_VALUE).setValue(value.toString());
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem()) && valueElement != null) {
            valueElement.getAttributeNode(ATTRIBUTE_VALUE).setValue(value.toString());
        }
    }

    private void updateCurrentlyEditingBox(final ActionEvent event) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    public void updateAvailableFonts() {
        final String[] fonts = fontHandler.getFontFamilies();
        fontNameBox.setModel(new DefaultComboBoxModel<>(fonts));
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {
        this.widgetsAndProperties = widgetsAndProperties;

        final boolean allowEditCaptionAndValue = widgetsAndProperties.keySet().stream()
                .anyMatch(IWidget::allowEditCaptionAndValue);

        final int currentlyEditingIdx;
        if (!allowEditCaptionAndValue) {
            currentlyEditingIdx = 1;
        } else {
            currentlyEditingIdx = currentlyEditing;
        }

        currentlyEditingBox.setSelectedIndex(currentlyEditingIdx);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        final List<FontProperties> fontProperties = widgetsAndProperties.entrySet().stream()
                .map(entry -> convertToFontProperties(entry, currentlyEditing))
                .collect(Collectors.toList());

        final FontPropertiesList fontPropertiesList = new FontPropertiesList(fontProperties);

        setComboValue(fontNameBox, fontPropertiesList.getFontName());
        setComboValue(fontSizeBox, fontPropertiesList.getFontSize());
        setComboValue(fontStyleBox, fontPropertiesList.getFontStyle());
        setComboValue(underlineBox, fontPropertiesList.getUnderline());
        setComboValue(strikethroughBox, fontPropertiesList.getStrikethrough());
        setComboValue(colorBox, fontPropertiesList.getColor());
    }

    private FontProperties convertToFontProperties(
            final Map.Entry<IWidget, Element> entry,
            final int currentlyEditing) {
        final IWidget widget = entry.getKey();
        final Element fontProperties = entry.getValue();

        final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

        final String captionFontName = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_FONT_NAME)
                .orElse("Arial");
        final String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_FONT_SIZE).orElse("10");
        final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_FONT_STYLE).orElse("1");
        final String captionUnderline = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_UNDERLINE).orElse("1");
        final String captionStrikethrough = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_STRIKETHROUGH)
                .orElse("1");
        final String captionColor = XMLUtils.getAttributeFromChildElement(caption, PROPERTY_COLOR).orElse(String
                .valueOf(Color.BLACK.getRGB()));

        final String valueFontName;
        final String valueFontSize;
        final String valueFontStyle;
        final String valueUnderline;
        final String valueStrikethrough;
        final String valueColor;

        if (widget.allowEditCaptionAndValue()) {
            /* get value properties */
            final Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);

            valueFontName = XMLUtils.getAttributeFromChildElement(value, PROPERTY_FONT_NAME).get();
            valueFontSize = XMLUtils.getAttributeFromChildElement(value, PROPERTY_FONT_SIZE).get();
            valueFontStyle = XMLUtils.getAttributeFromChildElement(value, PROPERTY_FONT_STYLE).get();
            valueUnderline = XMLUtils.getAttributeFromChildElement(value, PROPERTY_UNDERLINE).get();
            valueStrikethrough = XMLUtils.getAttributeFromChildElement(value, PROPERTY_STRIKETHROUGH).get();
            valueColor = XMLUtils.getAttributeFromChildElement(value, PROPERTY_COLOR).get();
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

        return new FontProperties(
                fontName,
                fontSize,
                fontStyle,
                underline,
                strikethrough,
                color
        );
    }

    private void setComboValue(
            final JComboBox<?> comboBox,
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
        if (currentlyEditing == IWidget.COMPONENT_BOTH) {
            if (captionProperty.equals(valueProperty)) {
                // both are the same
                return captionProperty;
            }
            // properties are different
            return "mixed";
        } else if (currentlyEditing == IWidget.COMPONENT_CAPTION) {
            // just set the caption properties
            return captionProperty;
        } else if (currentlyEditing == IWidget.COMPONENT_VALUE) {
            // just set the value properties
            return valueProperty;
        }

        return null;
    }

    private String getComboBoxSelectedValueAsString(final JComboBox<?> comboBox) {
        final int index = comboBox.getSelectedIndex();
        if (index == -1) {
            return null;
        }
        return String.valueOf(index);
    }

    private String getSelectedColorValueAsString() {
        final Color color = ((Color) colorBox.getSelectedItem());
        if (color == null) {
            return null;
        }
        return String.valueOf(color.getRGB());
    }
}

