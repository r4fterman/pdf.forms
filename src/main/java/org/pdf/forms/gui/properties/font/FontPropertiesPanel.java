package org.pdf.forms.gui.properties.font;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private static final String[] UNDERLINE_TYPES = {
            "No Underline",
            "Underline",
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
        final String[] fontFamilies = fontHandler.getFontFamilies();

        final JLabel currentlyEditingLabel = new JLabel();
        currentlyEditingLabel.setText("Currently Editing:");

        currentlyEditingBox = new JComboBox<>();
        currentlyEditingBox.setModel(new DefaultComboBoxModel<>(EDITING_VALUES));
        currentlyEditingBox.addActionListener(this::updateCurrentlyEditingBox);

        final JLabel fontLabel = new JLabel();
        fontLabel.setText("Font:");

        final JLabel fontSizeLabel = new JLabel();
        fontSizeLabel.setText("Font Size:");

        final JLabel fontStyleLabel = new JLabel();
        fontStyleLabel.setText("Font Style:");

        final JLabel underlineLabel = new JLabel();
        underlineLabel.setText("Underline:");
        underlineLabel.setEnabled(false);

        final JLabel strikethroughLabel = new JLabel();
        strikethroughLabel.setText("Strikethrough:");
        strikethroughLabel.setEnabled(false);

        final JLabel colorLabel = new JLabel();
        colorLabel.setText("Color:");

        fontNameBox = new JComboBox<>(fontFamilies);
        fontNameBox.addActionListener(this::updateFont);

        fontStyleBox = new JComboBox<>();
        fontStyleBox.setModel(new DefaultComboBoxModel<>(FONT_STYLES));
        fontStyleBox.addActionListener(this::updateFont);

        underlineBox = new JComboBox<>();
        underlineBox.setModel(new DefaultComboBoxModel<>(UNDERLINE_TYPES));
        underlineBox.setEnabled(false);
        underlineBox.addActionListener(this::updateFont);

        strikethroughBox = new JComboBox<>();
        strikethroughBox.setModel(new DefaultComboBoxModel<>(STRIKETHROUGH_VALUES));
        strikethroughBox.setEnabled(false);
        strikethroughBox.addActionListener(this::updateFont);

        colorBox = new JComboBox<>();
        colorBox.setEditable(true);
        colorBox.setMaximumRowCount(5);
        colorBox.setModel(new DefaultComboBoxModel<>(COLORS));

        final Color color = (Color) colorBox.getSelectedItem();
        editor = new ColorComboBoxEditor(color, colorBox);

        colorBox.setEditor(editor);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(this::updateColor);

        fontSizeBox = new JComboBox<>();
        fontSizeBox.setEditable(true);
        fontSizeBox.setModel(new DefaultComboBoxModel<>(FONT_SIZES));
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

        final GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(GroupLayout.TRAILING)
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
                .add(GroupLayout.TRAILING, sequentialGroup5);

        final GroupLayout.SequentialGroup sequentialGroup6 = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup)
                .addContainerGap(140, Short.MAX_VALUE);

        layout.setHorizontalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup6));

        final GroupLayout.SequentialGroup sequentialGroup7 = layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(currentlyEditingLabel)
                             .add(currentlyEditingBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(fontLabel)
                             .add(fontNameBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(fontSizeLabel)
                             .add(fontSizeBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(fontStyleLabel)
                             .add(fontStyleBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(underlineLabel)
                             .add(underlineBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                             .add(strikethroughLabel)
                             .add(strikethroughBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
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
        final Optional<Element> fontName = XMLUtils.getPropertyElement(captionElement, "Font Name");
        if (fontName.isEmpty()) {
            return;
        }

        Element valueFontName = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> fontValue = XMLUtils.getPropertyElement(valueElement, "Font Name");
            if (fontValue.isPresent()) {
                valueFontName = fontValue.get();
            }
        }
        final Element captionFontName = fontName.get();
        setProperty(fontNameBox.getSelectedItem(), captionFontName, valueFontName);
    }

    private void updateFontSizeProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Optional<Element> fontSize = XMLUtils.getPropertyElement(captionElement, "Font Size");
        if (fontSize.isEmpty()) {
            return;
        }

        Element valueFontSize = null;
        if (widget.allowEditCaptionAndValue()) {
            valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size").get();
        }
        final Element captionFontSize = fontSize.get();
        setProperty(fontSizeBox.getSelectedItem(), captionFontSize, valueFontSize);
    }

    private void updateFontStyleProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Optional<Element> fontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style");
        if (fontStyle.isEmpty()) {
            return;
        }

        Element valueFontStyle = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> fontStyleValue = XMLUtils.getPropertyElement(valueElement, "Font Style");
            if (fontStyleValue.isPresent()) {
                valueFontStyle = fontStyleValue.get();
            }
        }

        final String selectedIndex;
        final int index = fontStyleBox.getSelectedIndex();
        if (index == -1) {
            selectedIndex = null;
        } else {
            selectedIndex = String.valueOf(index);
        }

        final Element captionFontStyle = fontStyle.get();
        setProperty(selectedIndex, captionFontStyle, valueFontStyle);
    }

    private void updateUnderlineProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Optional<Element> underline = XMLUtils.getPropertyElement(captionElement, "Underline");
        if (underline.isEmpty()) {
            return;
        }

        Element valueUnderline = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> underlineValue = XMLUtils.getPropertyElement(valueElement, "Underline");
            if (underlineValue.isPresent()) {
                valueUnderline = underlineValue.get();
            }
        }

        final String selectedIndex;
        final int index = underlineBox.getSelectedIndex();
        if (index == -1) {
            selectedIndex = null;
        } else {
            selectedIndex = String.valueOf(index);
        }

        final Element captionUnderline = underline.get();
        setProperty(selectedIndex, captionUnderline, valueUnderline);
    }

    private void updateStrikethroughProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Optional<Element> strikethrough = XMLUtils.getPropertyElement(captionElement, "Strikethrough");
        if (strikethrough.isEmpty()) {
            return;
        }

        Element valueStrikethrough = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> strikethroughValue = XMLUtils.getPropertyElement(valueElement, "Strikethrough");
            if (strikethroughValue.isPresent()) {
                valueStrikethrough = strikethroughValue.get();
            }
        }

        final String selectedIndex;
        final int index = strikethroughBox.getSelectedIndex();
        if (index == -1) {
            selectedIndex = null;
        } else {
            selectedIndex = index + "";
        }

        final Element captionStrikethrough = strikethrough.get();
        setProperty(selectedIndex, captionStrikethrough, valueStrikethrough);
    }

    private void updateColorProperty(
            final IWidget widget,
            final Element captionElement,
            final Element valueElement) {
        final Optional<Element> colorElement = XMLUtils.getPropertyElement(captionElement, "Color");
        if (colorElement.isEmpty()) {
            return;
        }

        Element valueColor = null;
        if (widget.allowEditCaptionAndValue()) {
            final Optional<Element> colorValue = XMLUtils.getPropertyElement(valueElement, "Color");
            if (colorValue.isPresent()) {
                valueColor = colorValue.get();
            }
        }

        final String selectedColor;
        final Color color = ((Color) colorBox.getSelectedItem());
        if (color == null) {
            selectedColor = null;
        } else {
            selectedColor = String.valueOf(color.getRGB());
        }

        final Element captionColor = colorElement.get();
        setProperty(selectedColor, captionColor, valueColor);
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

    private void setProperty(
            final Object value,
            final Element captionElement,
            final Element valueElement) {
        if (value == null) {
            return;
        }

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            captionElement.getAttributeNode("value").setValue(value.toString());
            if (valueElement != null) {
                valueElement.getAttributeNode("value").setValue(value.toString());
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            captionElement.getAttributeNode("value").setValue(value.toString());
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem()) && valueElement != null) {
            valueElement.getAttributeNode("value").setValue(value.toString());
        }
    }

    private void updateCurrentlyEditingBox(final ActionEvent event) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    public void updateAvailableFonts() {
        final String[] fonts = fontHandler.getFontFamilies();
        final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fonts);
        fontNameBox.setModel(model);
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {
        this.widgetsAndProperties = widgetsAndProperties;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget: widgetsAndProperties.keySet()) {
            if (widget.allowEditCaptionAndValue()) {
                allowEditCaptionAndValue = true;
                break;
            }
        }

        final int currentlyEditingIdx;
        if (!allowEditCaptionAndValue) {
            currentlyEditingIdx = 1;
        } else {
            currentlyEditingIdx = currentlyEditing;
        }

        currentlyEditingBox.setSelectedIndex(currentlyEditingIdx);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        String fontNameToUse = null;
        String fontSizeToUse = null;
        String fontStyleToUse = null;
        String underlineToUse = null;
        String strikethroughToUse = null;
        String colorToUse = null;

        for (final Map.Entry<IWidget, Element> entry: widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element fontProperties = entry.getValue();

            final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

            final String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name").orElse("Arial");
            final String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size").orElse("10");
            final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style").orElse("1");
            final String captionUnderline = XMLUtils.getAttributeFromChildElement(caption, "Underline").orElse("1");
            final String captionStrikethrough = XMLUtils.getAttributeFromChildElement(caption, "Strikethrough").orElse(
                    "1");
            final String captionColor = XMLUtils.getAttributeFromChildElement(caption, "Color").orElse(String.valueOf(
                    Color.BLACK.getRGB()));

            final String valueFontName;
            final String valueFontSize;
            final String valueFontStyle;
            final String valueUnderline;
            final String valueStrikethrough;
            final String valueColor;

            if (widget.allowEditCaptionAndValue()) {
                /* get value properties */
                final Element value = (Element) fontProperties.getElementsByTagName("font_value").item(0);

                valueFontName = XMLUtils.getAttributeFromChildElement(value, "Font Name").get();
                valueFontSize = XMLUtils.getAttributeFromChildElement(value, "Font Size").get();
                valueFontStyle = XMLUtils.getAttributeFromChildElement(value, "Font Style").get();
                valueUnderline = XMLUtils.getAttributeFromChildElement(value, "Underline").get();
                valueStrikethrough = XMLUtils.getAttributeFromChildElement(value, "Strikethrough").get();
                valueColor = XMLUtils.getAttributeFromChildElement(value, "Color").get();
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

            if (fontNameToUse == null) {
                // this must be the first time round
                fontNameToUse = fontName;
                fontSizeToUse = fontSize;
                fontStyleToUse = fontStyle;
                underlineToUse = underline;
                strikethroughToUse = strikethrough;
                colorToUse = color;
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

}

