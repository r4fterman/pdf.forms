package org.pdf.forms.gui.properties.border;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BorderPropertiesPanel extends JPanel {

    private static final String[] BORDER_STYLES = {
            "None",
            "Solid",
            "Beveled"
    };
    private static final String[] BACKGROUND_FILL_STYLES = {
            "None",
            "Solid"
    };

    private static final int UNITS = (int) (Rule.INCH / 2.54);

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private JComboBox<String> backgroundFillBox;
    private JButton backgroundFillColorButton;
    private JButton borderColorButton;
    private JComboBox<String> borderStyleBox;
    private JTextField borderWidthBox;

    BorderPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JPanel panel = createBordersPanel();
        final JPanel backgroundPanel = createBackgroundPanel();

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(backgroundPanel,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .add(panel,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(panel,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(backgroundPanel,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(16, Short.MAX_VALUE))
        );
    }

    private JPanel createBordersPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Borders"));

        final JLabel leftEdgesIcon = new JLabel(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Border Together.png")));
        leftEdgesIcon.setOpaque(true);

        borderStyleBox = new JComboBox<>(BORDER_STYLES);
        borderStyleBox.addActionListener(this::updateBorderStyle);

        borderWidthBox = new JTextField();
        borderWidthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent event) {
                updateBorderStyle(null);
            }
        });

        borderColorButton = new JButton();
        borderColorButton.setContentAreaFilled(false);
        borderColorButton.setOpaque(true);
        borderColorButton.addActionListener(this::borderColorButtonClicked);

        final GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(GroupLayout.TRAILING, groupLayout.createSequentialGroup()
                                .add(12, 12, 12)
                                .add(leftEdgesIcon, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(borderStyleBox, 0, 135, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(borderWidthBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(borderColorButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(groupLayout.createSequentialGroup()
                                                .add(leftEdgesIcon,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        16,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .add(4, 4, 4))
                                        .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(borderColorButton,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        23,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .add(borderStyleBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .add(borderWidthBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return panel;
    }

    private JPanel createBackgroundPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Background Fill"));

        final JLabel styleLabel = new JLabel("Style:");

        backgroundFillBox = new JComboBox<>(BACKGROUND_FILL_STYLES);
        backgroundFillBox.setEnabled(false);
        backgroundFillBox.addActionListener(this::updateFillStyle);

        backgroundFillColorButton = new JButton();
        backgroundFillColorButton.setEnabled(false);
        backgroundFillColorButton.setContentAreaFilled(false);
        backgroundFillColorButton.setOpaque(true);
        backgroundFillColorButton.addActionListener(this::fillColorClicked);

        final GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(styleLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(backgroundFillBox, 0, 191, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(backgroundFillColorButton,
                                        GroupLayout.PREFERRED_SIZE,
                                        23,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(styleLabel)
                                        .add(backgroundFillColorButton,
                                                GroupLayout.PREFERRED_SIZE,
                                                23,
                                                GroupLayout.PREFERRED_SIZE)
                                        .add(backgroundFillBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return panel;
    }

    private void fillColorClicked(final ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void updateFillStyle(final ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void borderColorButtonClicked(final ActionEvent evt) {
        final Color currentBackground = borderColorButton.getBackground();
        final Color color = JColorChooser.showDialog(null, "Color Chooser", currentBackground);
        if (color != null) {
            borderColorButton.setIcon(null);
            borderColorButton.setContentAreaFilled(false);
            borderColorButton.setOpaque(true);
            borderColorButton.setBackground(color);
        }

        updateBorderStyle(null);
    }

    private void updateBorderStyle(final ActionEvent evt) {
        final String style = (String) borderStyleBox.getSelectedItem();
        final boolean borderEnabled = style != null && !style.equals("None");

        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);

        final Set<IWidget> widgets = widgetsAndProperties.keySet();
        for (final IWidget widget: widgets) {
            final Element borderProperties = widgetsAndProperties.get(widget);

            if (style != null) {
                setProperty(borderProperties, "Border Style", style);
            }

            final Color color = borderColorButton.getBackground();
            if (color != null) {
                setProperty(borderProperties, "Border Color", color.getRGB() + "");
            }

            if (!borderWidthBox.getText().equals("mixed")) {
                final String widthText = borderWidthBox.getText().replace("cm", "");

                final double customWidth = Double.parseDouble(widthText);
                borderWidthBox.setText(customWidth + " cm");

                final double width = customWidth * UNITS;
                setProperty(borderProperties, "Border Width", String.valueOf(Math.round(width)));
            }

            widget.setBorderAndBackgroundProperties(borderProperties);
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final String borderStyleToUse = getBorderStyle(widgetsAndProperties.values());
        applyBorderStyle(borderStyleToUse);

        final String borderWidthToUse = getBorderWidth(widgetsAndProperties.values());
        applyBorderWidth(borderWidthToUse);

        final String borderColorToUse = getBorderColor(widgetsAndProperties.values());
        setButtonBackgroundColor(borderColorToUse, borderColorButton);

        final String backgroundStyleToUse = getBackgroundStyle(widgetsAndProperties.values());
        // TODO: apply background style

        final String backgroundColorToUse = getBackgroundColor(widgetsAndProperties.values());
        setComboBoxValue(backgroundColorToUse, backgroundFillBox);
        setButtonBackgroundColor(backgroundColorToUse, backgroundFillColorButton);
    }

    private void applyBorderStyle(final String borderStyle) {
        setComboBoxValue(borderStyle, borderStyleBox);

        final boolean borderEnabled = !"None".equals(borderStyle);
        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);
    }

    private void applyBorderWidth(final String borderWidth) {
        final double borderWidthAsDouble = Integer.parseInt(borderWidth);
        final double convertedBorderWidth = round(borderWidthAsDouble / UNITS);
        borderWidthBox.setText(convertedBorderWidth + " cm");
    }

    private String getBorderStyle(final Collection<Element> elements) {
        return getPropertyValueFromCollection(elements, "borders", "Border Style");
    }

    private String getBorderWidth(final Collection<Element> elements) {
        return getPropertyValueFromCollection(elements, "borders", "Border Width");
    }

    private String getBorderColor(final Collection<Element> elements) {
        return getPropertyValueFromCollection(elements, "borders", "Border Color");
    }

    private String getBackgroundStyle(final Collection<Element> elements) {
        return getPropertyValueFromCollection(elements, "backgroundfill", "Style");
    }

    private String getBackgroundColor(final Collection<Element> elements) {
        return getPropertyValueFromCollection(elements, "backgroundfill", "Fill Color");
    }

    private void setButtonBackgroundColor(
            final String color,
            final JButton button) {
        if (color.equals("mixed")) {
            final BufferedImage bufferedImage = new BufferedImage(button.getWidth(),
                    button.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
            g2.setColor(Color.red);
            g2.drawLine(-1, 0, bufferedImage.getWidth() - 1, bufferedImage.getHeight());
            g2.drawLine(-1, bufferedImage.getHeight() - 1, bufferedImage.getWidth() - 1, -1);

            button.setContentAreaFilled(true);
            button.setBackground(null);
            button.setIcon(new ImageIcon(bufferedImage));
        } else {
            button.setIcon(null);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setBackground(new Color(Integer.parseInt(color)));
        }
    }

    private void setComboBoxValue(
            final String value,
            final JComboBox<String> comboBox) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);

        final String comboboxValue = convertMixedValueForComboBoxSelection(value);
        comboBox.setSelectedItem(comboboxValue);

        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    private String convertMixedValueForComboBoxSelection(final String value) {
        if ("mixed".equals(value)) {
            return null;
        }
        return value;
    }

    private String getPropertyValueFromCollection(
            final Collection<Element> elements,
            final String propertyName,
            final String attributeName) {
        final List<String> values = elements.stream()
                .map(borderProperties -> {
                    final Element border = (Element) borderProperties.getElementsByTagName(propertyName).item(0);
                    return XMLUtils.getAttributeFromChildElement(border, attributeName).orElse("");
                })
                .collect(toUnmodifiableList());

        final int numberOfIdenticalItems = Collections.frequency(values, values.get(0));
        final boolean listContainsOnlyEqualValues = numberOfIdenticalItems == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

    private void setProperty(
            final Element borderProperties,
            final String attribute,
            final String value) {
        XMLUtils.getPropertyElement(borderProperties, attribute)
                .ifPresent(leftEdgeWidthElement -> leftEdgeWidthElement.getAttributeNode("value").setValue(value));
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }
}
