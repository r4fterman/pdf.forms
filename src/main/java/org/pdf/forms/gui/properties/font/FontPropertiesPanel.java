package org.pdf.forms.gui.properties.font;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        currentlyEditingBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Caption and Value",
                "Caption properties",
                "Value properties" }));
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
        fontStyleBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Plain",
                "Bold",
                "Italic",
                "Bold Italic" }));
        fontStyleBox.addActionListener(this::updateFont);

        underlineBox = new JComboBox<>();
        underlineBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "No Underline",
                "Underline",
                "Double Underline",
                "Word Underline",
                "Word Double Underline" }));
        underlineBox.setEnabled(false);
        underlineBox.addActionListener(this::updateFont);

        strikethroughBox = new JComboBox<>();
        strikethroughBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Off",
                "On" }));
        strikethroughBox.setEnabled(false);
        strikethroughBox.addActionListener(this::updateFont);

        colorBox = new JComboBox<>();
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


        fontSizeBox = new JComboBox<>();
        fontSizeBox.setEditable(true);
        fontSizeBox.setModel(new DefaultComboBoxModel<>(new String[] { "6", "8", "10", "12", "14", "16", "18", "20", "24", "28", "36", "48", "72" }));
        fontSizeBox.addActionListener(this::updateFont);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(fontStyleLabel)
                                                .addPreferredGap(LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                                                .add(fontStyleBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(currentlyEditingLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(colorLabel)
                                                .addPreferredGap(LayoutStyle.RELATED, 59, Short.MAX_VALUE)
                                                .add(colorBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(underlineLabel)
                                                .addPreferredGap(LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                                                .add(underlineBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(strikethroughLabel)
                                                .addPreferredGap(LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                                                .add(strikethroughBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(fontLabel)
                                                        .add(fontSizeLabel))
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
                                        .add(currentlyEditingLabel)
                                        .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(fontLabel)
                                        .add(fontNameBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(fontSizeLabel)
                                        .add(fontSizeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(fontStyleLabel)
                                        .add(fontStyleBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(underlineLabel)
                                        .add(underlineBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(strikethroughLabel)
                                        .add(strikethroughBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(colorLabel)
                                        .add(colorBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(113, Short.MAX_VALUE))
        );
    }

    private void updateFont(final ActionEvent event) {
        for (final Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element fontElement = entry.getValue();

            final List<Element> fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name").get();
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size").get();
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style").get();
            final Element captionUnderline = XMLUtils.getPropertyElement(captionElement, "Underline").get();
            final Element captionStrikethrough = XMLUtils.getPropertyElement(captionElement, "Strikethrough").get();
            final Element captionColor = XMLUtils.getPropertyElement(captionElement, "Color").get();

            Element valueFontName = null;
            Element valueFontSize = null;
            Element valueFontStyle = null;
            Element valueUnderline = null;
            Element valueStrikethrough = null;
            Element valueColor = null;

            if (widget.allowEditCaptionAndValue()) {
                valueFontName = XMLUtils.getPropertyElement(valueElement, "Font Name").get();
                valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size").get();
                valueFontStyle = XMLUtils.getPropertyElement(valueElement, "Font Style").get();
                valueUnderline = XMLUtils.getPropertyElement(valueElement, "Underline").get();
                valueStrikethrough = XMLUtils.getPropertyElement(valueElement, "Strikethrough").get();
                valueColor = XMLUtils.getPropertyElement(valueElement, "Color").get();
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

        designerPanel.getMainFrame().setPropertiesToolBar(widgetsAndProperties.keySet());
        designerPanel.repaint();
    }

    private void updateColor(final ActionEvent event) {
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

        //        /** update the list of fonts just in case a new one has been added */
        //        String[] fonts = getFonts();
        //        DefaultComboBoxModel model = new DefaultComboBoxModel(fonts);
        //        fontNameBox.setModel(model);

        String fontNameToUse = null;
        String fontSizeToUse = null;
        String fontStyleToUse = null;
        String underlineToUse = null;
        String strikethroughToUse = null;
        String colorToUse = null;

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

        for (Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element fontProperties = entry.getValue();

            final Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

            final String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name").orElse("Arial");
            final String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size").orElse("10");
            final String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style").orElse("1");
            final String captionUnderline = XMLUtils.getAttributeFromChildElement(caption, "Underline").orElse("1");
            final String captionStrikethrough = XMLUtils.getAttributeFromChildElement(caption, "Strikethrough").orElse("1");
            final String captionColor = XMLUtils.getAttributeFromChildElement(caption, "Color").orElse(String.valueOf(Color.BLACK.getRGB()));

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

