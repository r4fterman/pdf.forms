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
* BorderPropertiesPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.border;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BorderPropertiesPanel extends JPanel {

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private final int units = (int) (Rule.INCH / 2.54);

    /**
     * Creates new form BorderPropertiesPanel
     */
    public BorderPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        leftEdgesIcon = new javax.swing.JLabel();
        borderStyleBox = new javax.swing.JComboBox<>();
        borderWidthBox = new javax.swing.JTextField();
        borderColorButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        backgroundFillBox = new javax.swing.JComboBox<>();
        backgroundFillColorButton = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Borders"));

        leftEdgesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/Border Together.png"))); // NOI18N
        leftEdgesIcon.setOpaque(true);

        borderStyleBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"None", "Solid", "Beveled"}));
        borderStyleBox.addActionListener(evt -> updateBorderStyle(evt));

        borderWidthBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateBorderSize(evt);
            }
        });

        borderColorButton.setContentAreaFilled(false);
        borderColorButton.setOpaque(true);
        borderColorButton.addActionListener(evt -> borderColorButtonClicked(evt));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(leftEdgesIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(borderStyleBox, 0, 135, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(borderWidthBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(borderColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jPanel1Layout.createSequentialGroup()
                                        .add(leftEdgesIcon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(4, 4, 4))
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(borderColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(borderStyleBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(borderWidthBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Background Fill"));

        jLabel6.setText("Style:");

        backgroundFillBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"None", "Solid"}));
        backgroundFillBox.setEnabled(false);
        backgroundFillBox.addActionListener(evt -> updateFillStyle(evt));

        backgroundFillColorButton.setEnabled(false);
        backgroundFillColorButton.setContentAreaFilled(false);
        backgroundFillColorButton.setOpaque(true);
        backgroundFillColorButton.addActionListener(evt -> fillColorClicked(evt));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backgroundFillBox, 0, 191, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backgroundFillColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jLabel6)
                                .add(backgroundFillColorButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(backgroundFillBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))
        );
    }

    private void fillColorClicked(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void setProperty(Element borderProperties, String attribute, String value) {
        Element leftEdgeWidthElement = XMLUtils.getPropertyElement(borderProperties, attribute);
        leftEdgeWidthElement.getAttributeNode("value").setValue(value);
    }

    private void updateFillStyle(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void borderColorButtonClicked(java.awt.event.ActionEvent evt) {
        Color currentBackground = borderColorButton.getBackground();
        Color color = JColorChooser.showDialog(null, "Color Chooser", currentBackground);
        if (color != null) {
            borderColorButton.setIcon(null);
            borderColorButton.setContentAreaFilled(false);
            borderColorButton.setOpaque(true);
            borderColorButton.setBackground(color);
        }

        updateBorderStyle(null);
    }

    private void updateBorderSize(java.awt.event.FocusEvent evt) {
        updateBorderStyle(null);
    }

    private void updateBorderStyle(java.awt.event.ActionEvent evt) {
        String style = (String) borderStyleBox.getSelectedItem();
        boolean borderEnabled = style != null && !style.equals("None");

        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);

        Set<IWidget> widgets = widgetsAndProperties.keySet();

        for (IWidget widget : widgets) {
            Element borderProperties = widgetsAndProperties.get(widget);

            if (style != null) {
                setProperty(borderProperties, "Border Style", style);
            }

            Color color = borderColorButton.getBackground();
            if (color != null) {
                setProperty(borderProperties, "Border Color", color.getRGB() + "");
            }

            if (!borderWidthBox.getText().equals("mixed")) {
                String widthText = borderWidthBox.getText().replaceAll("cm", "");

                double width = Double.parseDouble(widthText);
                borderWidthBox.setText(width + " cm");

                width = width * units;

                setProperty(borderProperties, "Border Width", (Math.round(width)) + "");
            }

            widget.setBorderAndBackgroundProperties(borderProperties);
        }

        designerPanel.repaint();
    }

    public void setProperties(Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String borderStyleToUse = null;
        String borderWidthToUse = null;
        String borderColorToUse = null;
        String backgroundStyleToUse = null;
        String backgroundColorToUse = null;

        /* iterate through the widgets */
        for (IWidget widget : widgetsAndProperties.keySet()) {
            Element borderProperties = widgetsAndProperties.get(widget);

            /* add borders properties */
            Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);

            String borderStyle = XMLUtils.getAttributeFromChildElement(border, "Border Style");
            String borderWidth = XMLUtils.getAttributeFromChildElement(border, "Border Width");
            String borderColor = XMLUtils.getAttributeFromChildElement(border, "Border Color");

            /* add background fill properties */
            Element background = (Element) borderProperties.getElementsByTagName("backgroundfill").item(0);

            String backgroundStyle = XMLUtils.getAttributeFromChildElement(background, "Style");
            String backgroundColor = XMLUtils.getAttributeFromChildElement(background, "Fill Color");

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
        setComboValue(borderStyleBox, "mixed".equals(borderStyleToUse) ? null : borderStyleToUse);

        boolean borderEnabled = !"None".equals(borderStyleToUse);
        borderWidthBox.setEnabled(borderEnabled);
        borderColorButton.setEnabled(borderEnabled);

        double width = Integer.parseInt(borderWidthToUse);
        width = round(width / units, 3);
        borderWidthBox.setText(width + " cm");

        setButtonColor(borderColorToUse, borderColorButton);

        /* set background fill properties */
        setComboValue(backgroundFillBox, "mixed".equals(backgroundColorToUse) ? null : backgroundColorToUse);
        setButtonColor(backgroundColorToUse, backgroundFillColorButton);
    }

    private double round(double number, int decPlaces) {
        double exponential = Math.pow(10, decPlaces);

        number *= exponential;
        number = Math.round(number);
        number /= exponential;

        return number;
    }

    private void setButtonColor(String borderColorToUse, JButton button) {
        if (borderColorToUse.equals("mixed")) {
            BufferedImage bi = new BufferedImage(button.getWidth(), button.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
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

    private void setComboValue(JComboBox comboBox, Object value) {
        ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(value);
        comboBox.addActionListener(listener);
    }

    private javax.swing.JComboBox<String> backgroundFillBox;
    private javax.swing.JButton backgroundFillColorButton;
    private javax.swing.JButton borderColorButton;
    private javax.swing.JComboBox<String> borderStyleBox;
    private javax.swing.JTextField borderWidthBox;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel leftEdgesIcon;


}
    

