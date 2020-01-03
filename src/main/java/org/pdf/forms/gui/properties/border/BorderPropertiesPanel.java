package org.pdf.forms.gui.properties.border;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BorderPropertiesPanel extends JPanel {

    private final int units = (int) (Rule.INCH / 2.54);

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
                                        .add(backgroundPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(backgroundPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(16, Short.MAX_VALUE))
        );
    }

    private JPanel createBordersPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Borders"));

        final JLabel leftEdgesIcon = new JLabel();
        leftEdgesIcon.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Border Together.png")));
        leftEdgesIcon.setOpaque(true);

        borderStyleBox = new JComboBox<>();
        borderStyleBox.setModel(new DefaultComboBoxModel<>(new String[] { "None", "Solid", "Beveled" }));
        borderStyleBox.addActionListener(this::updateBorderStyle);

        borderWidthBox = new JTextField();
        borderWidthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent event) {
                updateBorderSize(event);
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
                                                .add(leftEdgesIcon, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                                .add(4, 4, 4))
                                        .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(borderColorButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                .add(borderStyleBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .add(borderWidthBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return panel;
    }

    private JPanel createBackgroundPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Background Fill"));

        final JLabel styleLabel = new JLabel();
        styleLabel.setText("Style:");

        backgroundFillBox = new JComboBox<>();
        backgroundFillBox.setModel(new DefaultComboBoxModel<>(new String[] { "None", "Solid" }));
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
                                .add(backgroundFillColorButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(styleLabel)
                                        .add(backgroundFillColorButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(backgroundFillBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        return panel;
    }

    private void fillColorClicked(final ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void setProperty(
            final Element borderProperties,
            final String attribute,
            final String value) {
        final Element leftEdgeWidthElement = XMLUtils.getPropertyElement(borderProperties, attribute).get();
        leftEdgeWidthElement.getAttributeNode("value").setValue(value);
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

    private void updateBorderSize(final FocusEvent evt) {
        updateBorderStyle(null);
    }

    private void updateBorderStyle(final ActionEvent evt) {
        final String style = (String) borderStyleBox.getSelectedItem();
        final boolean borderEnabled = style != null && !style.equals("None");

        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);

        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        for (final IWidget widget : widgets) {
            final Element borderProperties = widgetsAndProperties.get(widget);

            if (style != null) {
                setProperty(borderProperties, "Border Style", style);
            }

            final Color color = borderColorButton.getBackground();
            if (color != null) {
                setProperty(borderProperties, "Border Color", color.getRGB() + "");
            }

            if (!borderWidthBox.getText().equals("mixed")) {
                final String widthText = borderWidthBox.getText().replaceAll("cm", "");

                double width = Double.parseDouble(widthText);
                borderWidthBox.setText(width + " cm");

                width = width * units;

                setProperty(borderProperties, "Border Width", (Math.round(width)) + "");
            }

            widget.setBorderAndBackgroundProperties(borderProperties);
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String borderStyleToUse = null;
        String borderWidthToUse = null;
        String borderColorToUse = null;
        String backgroundStyleToUse = null;
        String backgroundColorToUse = null;

        for (Element borderProperties : widgetsAndProperties.values()) {
            final Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);

            final String borderStyle = XMLUtils.getAttributeFromChildElement(border, "Border Style").get();
            final String borderWidth = XMLUtils.getAttributeFromChildElement(border, "Border Width").get();
            final String borderColor = XMLUtils.getAttributeFromChildElement(border, "Border Color").get();

            /* add background fill properties */
            final Element background = (Element) borderProperties.getElementsByTagName("backgroundfill").item(0);

            final String backgroundStyle = XMLUtils.getAttributeFromChildElement(background, "Style").get();
            final String backgroundColor = XMLUtils.getAttributeFromChildElement(background, "Fill Color").get();

            if (borderStyleToUse == null) { // this must be the first time round
                borderStyleToUse = borderStyle;
                borderWidthToUse = borderWidth;
                borderColorToUse = borderColor;
                backgroundStyleToUse = backgroundStyle;
                backgroundColorToUse = backgroundColor;
            } else {
                // check for subsequent widgets
                if (!borderStyleToUse.equals(borderStyle)) {
                    borderStyleToUse = "mixed";
                }

                if (!borderWidthToUse.equals(borderWidth)) {
                    borderWidthToUse = "mixed";
                }

                if (!borderColorToUse.equals(borderColor)) {
                    borderColorToUse = "mixed";
                }

                if (!backgroundStyleToUse.equals(backgroundStyle)) {
                    backgroundStyleToUse = "mixed";
                }

                if (!backgroundColorToUse.equals(backgroundColor)) {
                    backgroundColorToUse = "mixed";
                }
            }
        }

        /* set borders properties */
        final Object value1;
        if ("mixed".equals(borderStyleToUse)) {
            value1 = null;
        } else {
            value1 = borderStyleToUse;
        }
        setComboValue(borderStyleBox, value1);

        final boolean borderEnabled = !"None".equals(borderStyleToUse);
        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);

        double width = Integer.parseInt(borderWidthToUse);
        width = round(width / units);
        borderWidthBox.setText(width + " cm");

        setButtonColor(borderColorToUse, borderColorButton);

        /* set background fill properties */
        final Object value;
        if ("mixed".equals(backgroundColorToUse)) {
            value = null;
        } else {
            value = backgroundColorToUse;
        }
        setComboValue(backgroundFillBox, value);
        setButtonColor(backgroundColorToUse, backgroundFillColorButton);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }

    private void setButtonColor(
            final String borderColorToUse,
            final JButton button) {
        if (borderColorToUse.equals("mixed")) {
            final BufferedImage bi = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.setColor(Color.red);
            g2.drawLine(-1, 0, bi.getWidth() - 1, bi.getHeight());
            g2.drawLine(-1, bi.getHeight() - 1, bi.getWidth() - 1, -1);

            button.setContentAreaFilled(true);
            button.setBackground(null);
            button.setIcon(new ImageIcon(bi));
        } else {
            button.setIcon(null);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setBackground(new Color(Integer.parseInt(borderColorToUse)));
        }
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
            final Object value) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(value);
        comboBox.addActionListener(listener);
    }

}
