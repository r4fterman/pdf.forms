/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
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

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class ParagraphPropertiesPanel extends JPanel {

    private final Logger logger = LoggerFactory.getLogger(ParagraphPropertiesPanel.class);

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private ButtonGroup buttonGroup1;
    private ButtonGroup buttonGroup2;
    private JComboBox<String> currentlyEditingBox;
    private JToggleButton horizontalAlignJustify;

    // called via reflection
    private JToggleButton horizontalAlignLeft;
    private JToggleButton horizontalAlignCenter;
    private JToggleButton horizontalAlignRight;
    private JToggleButton verticalAlignTop;
    private JToggleButton verticalAlignCenter;
    private JToggleButton verticalAlignBottom;

    /**
     * Creates new form ParagraphPropertiesPanel.
     */
    ParagraphPropertiesPanel() {
        initComponents();
        horizontalAlignJustify.setVisible(false);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JLabel jLabel1;
        final JSeparator jSeparator1;

        buttonGroup1 = new ButtonGroup();
        buttonGroup2 = new ButtonGroup();
        jLabel1 = new JLabel();
        currentlyEditingBox = new JComboBox<>();
        horizontalAlignLeft = new JToggleButton();
        horizontalAlignCenter = new JToggleButton();
        horizontalAlignRight = new JToggleButton();
        horizontalAlignJustify = new JToggleButton();
        jSeparator1 = new JSeparator();
        verticalAlignTop = new JToggleButton();
        verticalAlignCenter = new JToggleButton();
        verticalAlignBottom = new JToggleButton();

        jLabel1.setText("Currently Editing:");

        currentlyEditingBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Caption and Value", "Caption properties", "Value properties" }));

        currentlyEditingBox.addActionListener(this::updateCurrentlyEditingBox);

        buttonGroup1.add(horizontalAlignLeft);
        horizontalAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Left.gif")));
        horizontalAlignLeft.setName("left");
        horizontalAlignLeft.addActionListener(this::updateHorizontalAlignment);

        buttonGroup1.add(horizontalAlignCenter);
        horizontalAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Center.gif")));
        horizontalAlignCenter.setName("center");
        horizontalAlignCenter.addActionListener(this::updateHorizontalAlignment);

        buttonGroup1.add(horizontalAlignRight);
        horizontalAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Right.gif")));
        horizontalAlignRight.setName("right");
        horizontalAlignRight.addActionListener(this::updateHorizontalAlignment);

        buttonGroup1.add(horizontalAlignJustify);
        horizontalAlignJustify.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Justify.gif")));
        horizontalAlignJustify.setName("justify");
        horizontalAlignJustify.addActionListener(this::updateHorizontalAlignment);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonGroup2.add(verticalAlignTop);
        verticalAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Top.gif")));
        verticalAlignTop.setName("top");
        verticalAlignTop.addActionListener(this::updateVerticalAlignment);

        buttonGroup2.add(verticalAlignCenter);
        verticalAlignCenter.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Middle.gif")));
        verticalAlignCenter.setName("center");
        verticalAlignCenter.addActionListener(this::updateVerticalAlignment);

        buttonGroup2.add(verticalAlignBottom);
        verticalAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/org/pdf/forms/res/Paragraph Align Bottom.gif")));
        verticalAlignBottom.setName("bottom");
        verticalAlignBottom.addActionListener(this::updateVerticalAlignment);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(horizontalAlignLeft, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(horizontalAlignCenter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(horizontalAlignRight, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(horizontalAlignJustify, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(verticalAlignTop, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(verticalAlignCenter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(verticalAlignBottom, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(currentlyEditingBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(horizontalAlignLeft, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignCenter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignRight, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .add(horizontalAlignJustify, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(verticalAlignTop, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .add(verticalAlignCenter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .add(verticalAlignBottom, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                        .add(jSeparator1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(236, Short.MAX_VALUE))
        );
    }

    private void updateCurrentlyEditingBox(final ActionEvent evt) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    private void updateVerticalAlignment(final ActionEvent evt) {
        updateAlignment("Vertical Alignment", evt);
    }

    private void updateHorizontalAlignment(final ActionEvent evt) {
        updateAlignment("Horizontal Alignment", evt);
    }

    private void updateAlignment(
            final String propertyName,
            final ActionEvent evt) {

        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        for (final IWidget widget : widgets) {
            final Element paragraphElement = widgetsAndProperties.get(widget);

            final List<Element> paagraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            final Element captionElement = paagraphList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = paagraphList.get(1);
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

            widget.setParagraphProperties(widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);
        designerPanel.repaint();
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {

        this.widgetsAndProperties = widgetsAndProperties;

        String horizontalAlignmentToUse = null;
        String verticalAlignmentToUse = null;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.allowEditCaptionAndValue()) {
                allowEditCaptionAndValue = true;
                break;
            }
        }

        // hard code to remove complications with text fields
        allowEditCaptionAndValue = false;

        int editing = currentlyEditing;
        if (!allowEditCaptionAndValue) {
            editing = 1;
        }

        /* set the currently editing box */
        currentlyEditingBox.setSelectedIndex(editing);
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
        } catch (final Exception e) {
            logger.error("Error set vertical align button", e);
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
                if (captionHorizontalAlignment.equals(valueHorizontalAlignment)) {
                    // both value and caption are the same
                    horizontalAlignmentToUse = captionHorizontalAlignment;
                } else {
                    /* caption and value are different so use hack to push all buttons out */
                    horizontalAlignmentToUse = "mixed";
                }

                if (captionVerticalAlignment.equals(valueVerticalAlignment)) {
                    // both value and caption are the same
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
            default:
                break;
        }

        return new String[] {
                horizontalAlignmentToUse, verticalAlignmentToUse };
    }

}
