package org.pdf.forms.gui.properties.font;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.GroupLayout.TRAILING;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorCellRenderer;
import org.pdf.forms.gui.properties.customcomponents.colorcombobox.ColorComboBoxEditor;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.FontValue;
import org.pdf.forms.widgets.IWidget;

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
    private Set<IWidget> widgets;

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
        widgets.forEach(widget -> {
            updateFontProperties(widget);
            // todo: set widget model font properties
            //widget.setFontProperties(value, currentlyEditingBox.getSelectedIndex());
        });

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);
        designerPanel.repaint();
    }

    private void updateFontProperties(final IWidget widget) {
        final FontCaption fontCaption = widget.getWidgetModel().getProperties().getFont().getFontCaption();

        final Optional<FontValue> fontValue;
        if (widget.allowEditCaptionAndValue()) {
            fontValue = Optional.of(widget.getWidgetModel().getProperties().getFont().getFontValue());
        } else {
            fontValue = Optional.empty();
        }

        updateFontNameProperty(widget, fontCaption, fontValue);
        updateFontSizeProperty(widget, fontCaption, fontValue);
        updateFontStyleProperty(widget, fontCaption, fontValue);
        updateUnderlineProperty(widget, fontCaption, fontValue);
        updateStrikethroughProperty(widget, fontCaption, fontValue);
        updateColorProperty(widget, fontCaption, fontValue);
    }

    private void updateFontNameProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = Optional.ofNullable(fontNameBox.getSelectedItem()).map(Object::toString).orElse("");

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontName(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setFontName(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontName(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setFontName(value));
        }
    }

    private void updateFontSizeProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = Optional.ofNullable(fontSizeBox.getSelectedItem()).map(Object::toString).orElse("");

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontSize(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setFontSize(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontSize(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setFontSize(value));
        }
    }

    private void updateFontStyleProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = getComboBoxSelectedValueAsString(fontStyleBox);

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontStyle(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setFontStyle(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setFontStyle(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setFontStyle(value));
        }
    }

    private void updateUnderlineProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = getComboBoxSelectedValueAsString(underlineBox);

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setUnderline(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setUnderline(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setUnderline(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setUnderline(value));
        }
    }

    private void updateStrikethroughProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = getComboBoxSelectedValueAsString(strikethroughBox);

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setStrikeThrough(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setStrikeThrough(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setStrikeThrough(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setStrikeThrough(value));
        }
    }

    private void updateColorProperty(
            final IWidget widget,
            final FontCaption fontCaption,
            final Optional<FontValue> fontValue) {
        final String value = getSelectedColorValueAsString();

        if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setColor(value);
            if (widget.allowEditCaptionAndValue()) {
                fontValue.ifPresent(font -> font.setColor(value));
            }
        } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontCaption.setColor(value);
        } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
            fontValue.ifPresent(font -> font.setColor(value));
        }
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

    private void updateCurrentlyEditingBox(final ActionEvent event) {
        setProperties(widgets, currentlyEditingBox.getSelectedIndex());
    }

    public void updateAvailableFonts() {
        final String[] fonts = fontHandler.getFontFamilies();
        fontNameBox.setModel(new DefaultComboBoxModel<>(fonts));
    }

    public void setProperties(
            final Set<IWidget> widgets,
            final int currentlyEditing) {
        this.widgets = widgets;

        final boolean allowEditCaptionAndValue = widgets.stream()
                .anyMatch(IWidget::allowEditCaptionAndValue);

        final int currentlyEditingIdx;
        if (!allowEditCaptionAndValue) {
            currentlyEditingIdx = 1;
        } else {
            currentlyEditingIdx = currentlyEditing;
        }

        currentlyEditingBox.setSelectedIndex(currentlyEditingIdx);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        final FontPropertiesList fontPropertiesList = getFontPropertiesList(
                widgets,
                currentlyEditing
        );

        setComboValue(fontNameBox, fontPropertiesList.getFontName());
        setComboValue(fontSizeBox, fontPropertiesList.getFontSize());
        setComboValue(fontStyleBox, fontPropertiesList.getFontStyle());
        setComboValue(underlineBox, fontPropertiesList.getUnderline());
        setComboValue(strikethroughBox, fontPropertiesList.getStrikethrough());
        setComboValue(colorBox, fontPropertiesList.getColor());
    }

    private FontPropertiesList getFontPropertiesList(
            final Set<IWidget> widgets,
            final int currentlyEditing) {
        final List<FontProperties> fontProperties = widgets.stream()
                .map(widget -> convertToFontProperties(widget, currentlyEditing))
                .collect(toUnmodifiableList());

        return new FontPropertiesList(fontProperties);
    }

    private FontProperties convertToFontProperties(
            final IWidget widget,
            final int currentlyEditing) {
        final org.pdf.forms.model.des.FontProperties font = widget.getWidgetModel().getProperties().getFont();

        final FontCaption fontCaption = font.getFontCaption();
        final String captionFontName = fontCaption.getFontName().orElse("Arial");
        final String captionFontSize = fontCaption.getFontSize().orElse("10");
        final String captionFontStyle = fontCaption.getFontStyle().orElse("1");
        final String captionUnderline = fontCaption.getUnderline().orElse("1");
        final String captionStrikethrough = fontCaption.getStrikeThrough().orElse("1");
        final String captionColor = fontCaption.getColor().orElse(String.valueOf(Color.BLACK.getRGB()));

        final FontValue fontValue = font.getFontValue();
        final String valueFontName = fontValue.getFontName().orElse("Arial");
        final String valueFontSize = fontValue.getFontSize().orElse("10");
        final String valueFontStyle = fontValue.getFontStyle().orElse("1");
        final String valueUnderline = fontValue.getUnderline().orElse("1");
        final String valueStrikethrough = fontValue.getStrikeThrough().orElse("1");
        final String valueColor = fontValue.getColor().orElse(String.valueOf(Color.BLACK.getRGB()));

        return new FontProperties(
                getProperty(currentlyEditing, captionFontName, valueFontName),
                getProperty(currentlyEditing, captionFontSize, valueFontSize),
                getProperty(currentlyEditing, captionFontStyle, valueFontStyle),
                getProperty(currentlyEditing, captionUnderline, valueUnderline),
                getProperty(currentlyEditing, captionStrikethrough, valueStrikethrough),
                getProperty(currentlyEditing, captionColor, valueColor)
        );
    }

    private void setComboValue(
            final JComboBox<?> comboBox,
            final Object value) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex((Integer) value);
        } else {
            comboBox.setSelectedItem(value);
        }

        Arrays.stream(listeners).forEach(comboBox::addActionListener);
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

