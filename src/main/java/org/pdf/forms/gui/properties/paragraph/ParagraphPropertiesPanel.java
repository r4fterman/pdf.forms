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
* ParagraphPropertiesPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.paragraph;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class ParagraphPropertiesPanel extends JPanel {

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    /**
     * Creates new form ParagraphPropertiesPanel
     */
    public ParagraphPropertiesPanel() {
        initComponents();
        horizontalAlignJustify.setVisible(false);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final javax.swing.JLabel jLabel1;
        final javax.swing.JSeparator jSeparator1;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        currentlyEditingBox = new javax.swing.JComboBox();
        horizontalAlignLeft = new javax.swing.JToggleButton();
        horizontalAlignCenter = new javax.swing.JToggleButton();
        horizontalAlignRight = new javax.swing.JToggleButton();
        horizontalAlignJustify = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        verticalAlignTop = new javax.swing.JToggleButton();
        verticalAlignCenter = new javax.swing.JToggleButton();
        verticalAlignBottom = new javax.swing.JToggleButton();

        jLabel1.setText("Currently Editing:");

        currentlyEditingBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Caption and Value", "Caption properties", "Value properties" }));
        currentlyEditingBox.addActionListener(evt -> updateCurrentlyEditingBox(evt));

        buttonGroup1.add(horizontalAlignLeft);
        horizontalAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Left.gif")));
        horizontalAlignLeft.setName("left");
        horizontalAlignLeft.addActionListener(evt -> updateHorizontalAlignment(evt));

        buttonGroup1.add(horizontalAlignCenter);
        horizontalAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Center.gif")));
        horizontalAlignCenter.setName("center");
        horizontalAlignCenter.addActionListener(evt -> updateHorizontalAlignment(evt));

        buttonGroup1.add(horizontalAlignRight);
        horizontalAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Right.gif")));
        horizontalAlignRight.setName("right");
        horizontalAlignRight.addActionListener(evt -> updateHorizontalAlignment(evt));

        buttonGroup1.add(horizontalAlignJustify);
        horizontalAlignJustify.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Justify.gif")));
        horizontalAlignJustify.setName("justify");
        horizontalAlignJustify.addActionListener(evt -> updateHorizontalAlignment(evt));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonGroup2.add(verticalAlignTop);
        verticalAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Top.gif")));
        verticalAlignTop.setName("top");
        verticalAlignTop.addActionListener(evt -> updateVerticalAlignment(evt));

        buttonGroup2.add(verticalAlignCenter);
        verticalAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Middle.gif")));
        verticalAlignCenter.setName("center");
        verticalAlignCenter.addActionListener(evt -> updateVerticalAlignment(evt));

        buttonGroup2.add(verticalAlignBottom);
        verticalAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Bottom.gif")));
        verticalAlignBottom.setName("bottom");
        verticalAlignBottom.addActionListener(evt -> updateVerticalAlignment(evt));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(currentlyEditingBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(horizontalAlignLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(horizontalAlignCenter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(horizontalAlignRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(horizontalAlignJustify, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(verticalAlignTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(verticalAlignCenter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(verticalAlignBottom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(currentlyEditingBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                .add(horizontalAlignLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignCenter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignJustify, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                .add(verticalAlignTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(verticalAlignCenter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(verticalAlignBottom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(236, Short.MAX_VALUE))
        );
    }

    private void updateCurrentlyEditingBox(final java.awt.event.ActionEvent evt) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    private void updateVerticalAlignment(final java.awt.event.ActionEvent evt) {
        updateAlignment("Vertical Alignment", evt);
    }

    private void updateHorizontalAlignment(final java.awt.event.ActionEvent evt) {
        updateAlignment("Horizontal Alignment", evt);
    }

    private void updateAlignment(
            final String propertyName,
            final java.awt.event.ActionEvent evt) {

        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        for (final IWidget widget : widgets) {
            final Element paragraphElement = widgetsAndProperties.get(widget);

            final List paagraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            final Element captionElement = (Element) paagraphList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) paagraphList.get(1);
            }

            final Element captionAlignment = XMLUtils.getPropertyElement(captionElement, propertyName);
            Element valueAlignment = null;
            if (widget.allowEditCaptionAndValue()) {
                valueAlignment = XMLUtils.getPropertyElement(valueElement, propertyName);
            }

            final String alignment = ((JComponent) evt.getSource()).getName();

            if ("Caption and Value".equals(currentlyEditingBox.getSelectedItem())) {
                captionAlignment.getAttributeNode("value").setValue(alignment);
                if (valueAlignment != null) {
                    valueAlignment.getAttributeNode("value").setValue(alignment);
                }
            } else if ("Caption properties".equals(currentlyEditingBox.getSelectedItem())) {
                captionAlignment.getAttributeNode("value").setValue(alignment);
            } else if ("Value properties".equals(currentlyEditingBox.getSelectedItem())) {
                if (valueAlignment != null) {
                    valueAlignment.getAttributeNode("value").setValue(alignment);
                }
            }

            widget.setParagraphProperties((Element) widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);
        designerPanel.repaint();
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            int currentlyEditing) {

        this.widgetsAndProperties = widgetsAndProperties;

        String horizontalAlignmentToUse = null, verticalAlignmentToUse = null;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.allowEditCaptionAndValue()) {
                allowEditCaptionAndValue = true;
                break;
            }
        }

        // hard code to remove complications with text fields 
        allowEditCaptionAndValue = false;

        if (!allowEditCaptionAndValue) {
            currentlyEditing = 1;
        }

        /* set the currently editing box */
        currentlyEditingBox.setSelectedIndex(currentlyEditing);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element paragraphPropertiesElement = widgetsAndProperties.get(widget);

            /* get caption properties */
            final Element captionElement =
                    (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

            final String captionHorizontalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Horizontal Alignment");
            final String captionVerticalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Vertical Alignment");

            final String valueHorizontalAlignment;
            final String valueVerticalAlignment;

            /* get value properties */
            if (widget.allowEditCaptionAndValue()) {
                final Element valueElement =
                        (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_value").item(0);

                valueHorizontalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Horizontal Alignment");
                valueVerticalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Vertical Alignment");
            } else {
                valueHorizontalAlignment = captionHorizontalAlignment;
                valueVerticalAlignment = captionVerticalAlignment;
            }

            final String[] alignments = getAlignments(currentlyEditing, captionHorizontalAlignment, valueHorizontalAlignment,
                    captionVerticalAlignment, valueVerticalAlignment);

            final String horizontalAlignment = alignments[0];
            final String verticalAlignment = alignments[1];

            if (horizontalAlignmentToUse == null) {
                horizontalAlignmentToUse = horizontalAlignment;
                verticalAlignmentToUse = verticalAlignment;
            } else {

                if (!horizontalAlignmentToUse.equals(horizontalAlignment)) {
                    horizontalAlignmentToUse = "mixed";
                }

                if (!verticalAlignmentToUse.equals(verticalAlignment)) {
                    verticalAlignmentToUse = "mixed";
                }
            }
        }

        try {
            if ("mixed".equals(horizontalAlignmentToUse)) {
                buttonGroup1.setSelected(new JToggleButton("").getModel(), true);
            } else {
                horizontalAlignmentToUse = horizontalAlignmentToUse.substring(0, 1).toUpperCase() + horizontalAlignmentToUse.substring(1);

                final Field field = getClass().getDeclaredField("horizontalAlign" + horizontalAlignmentToUse);
                final JToggleButton toggleButton = (JToggleButton) field.get(this);
                toggleButton.setSelected(true);
            }

            if ("mixed".equals(verticalAlignmentToUse)) {
                buttonGroup2.setSelected(new JToggleButton("").getModel(), true);
            } else {
                verticalAlignmentToUse = verticalAlignmentToUse.substring(0, 1).toUpperCase() + verticalAlignmentToUse.substring(1);

                final Field field = getClass().getDeclaredField("verticalAlign" + verticalAlignmentToUse);
                final JToggleButton toggleButton = (JToggleButton) field.get(this);
                toggleButton.setSelected(true);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private String[] getAlignments(
            final int currentlyEditing,
            final String captionHorizontalAlignment,
            final String valueHorizontalAlignment,
            final String captionVerticalAlignment,
            final String valueVerticalAlignment) {

        String verticalAlignmentToUse = null;
        String horizontalAlignmentToUse = null;

        switch (currentlyEditing) {
            case IWidget.COMPONENT_BOTH:
                if (captionHorizontalAlignment.equals(valueHorizontalAlignment)) { // both value and caption are the same
                    horizontalAlignmentToUse = captionHorizontalAlignment;
                } else {
                    /* caption and value are different so use hack to push all buttons out */
                    horizontalAlignmentToUse = "mixed";
                }

                if (captionVerticalAlignment.equals(valueVerticalAlignment)) { // both value and caption are the same
                    verticalAlignmentToUse = captionVerticalAlignment;
                } else {
                    /* caption and value are different so use hack to push all buttons out */
                    verticalAlignmentToUse = "mixed";
                }

                break;
            case IWidget.COMPONENT_CAPTION:
                /* just set the caption properties */
                horizontalAlignmentToUse = captionHorizontalAlignment;
                verticalAlignmentToUse = captionVerticalAlignment;

                break;

            case IWidget.COMPONENT_VALUE:
                /* just set the value properties */
                horizontalAlignmentToUse = valueHorizontalAlignment;
                verticalAlignmentToUse = valueVerticalAlignment;

                break;
        }

        return new String[] { horizontalAlignmentToUse, verticalAlignmentToUse };
    }

    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox currentlyEditingBox;
    private javax.swing.JToggleButton horizontalAlignCenter;
    private javax.swing.JToggleButton horizontalAlignJustify;
    private javax.swing.JToggleButton horizontalAlignLeft;
    private javax.swing.JToggleButton horizontalAlignRight;
    private javax.swing.JToggleButton verticalAlignBottom;
    private javax.swing.JToggleButton verticalAlignCenter;
    private javax.swing.JToggleButton verticalAlignTop;
}
