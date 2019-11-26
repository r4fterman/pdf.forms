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
* LayoutPropertiesPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.layout;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class LayoutPropertiesPanel extends JPanel/*extends BasicPropertiesPanel*/ implements TristateCheckBoxParent {

    private final Logger logger = LoggerFactory.getLogger(LayoutPropertiesPanel.class);

    private final int units = (int) (Rule.INCH / 2.54);

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    // todo tidy up this from netbeans
    private JComboBox<String> anchorLocationBox;
    private ButtonGroup buttonGroup1;
    private JComboBox<String> captionLocationBox;
    private JTextField heightBox;
    private JTextField widthBox;
    private JTextField xBox;
    private JCheckBox xExpandToFitBox;
    private JTextField yBox;
    private JCheckBox yExpandToFitBox;

    // called via reflection
    private JToggleButton rotate0;
    private JToggleButton rotate90;
    private JToggleButton rotate180;
    private JToggleButton rotate270;

    LayoutPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        buttonGroup1 = new ButtonGroup();
        final JFileChooser jFileChooser1 = new JFileChooser();
        final JPanel jPanel1 = new JPanel();
        final JLabel jLabel1 = new JLabel();
        final JLabel jLabel2 = new JLabel();
        xBox = new JTextField();
        widthBox = new JTextField();
        xExpandToFitBox = new TristateCheckBox("Expand to fit", TristateCheckBox.NOT_SELECTED, this);
        final JLabel jLabel3 = new JLabel();
        final JLabel jLabel4 = new JLabel();
        heightBox = new JTextField();
        yBox = new JTextField();
        yExpandToFitBox = new TristateCheckBox("Expand to fit", TristateCheckBox.NOT_SELECTED, this);
        final JLabel jLabel5 = new JLabel();
        anchorLocationBox = new JComboBox<>();
        rotate0 = new JToggleButton();
        rotate90 = new JToggleButton();
        rotate180 = new JToggleButton();
        rotate270 = new JToggleButton();
        final JPanel jPanel2 = new JPanel();
        final JLabel jLabel6 = new JLabel();
        final JLabel jLabel7 = new JLabel();
        final JTextField leftMargingBox = new JTextField();
        final JTextField rightMarginBox = new JTextField();
        final JLabel jLabel8 = new JLabel();
        final JLabel jLabel9 = new JLabel();
        final JTextField topMarginBox = new JTextField();
        final JTextField bottomMarginBox = new JTextField();
        final JPanel jPanel3 = new JPanel();
        final JLabel jLabel10 = new JLabel();
        captionLocationBox = new JComboBox<>();
        final JLabel jLabel11 = new JLabel();
        final JTextField reserveBox = new JTextField();

        jPanel1.setBorder(BorderFactory.createTitledBorder("Size & Position"));
        jLabel1.setText("X:");

        jLabel2.setText("Width:");

        xBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition(evt);
            }
        });

        widthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition(evt);
            }
        });

        xExpandToFitBox.setText("Expand to fit");
        xExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xExpandToFitBox.setEnabled(false);
        xExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel3.setText("Y:");

        jLabel4.setText("Height:");

        heightBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition(evt);
            }
        });

        yBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition(evt);
            }
        });

        yExpandToFitBox.setText("Expand to fit");
        yExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        yExpandToFitBox.setEnabled(false);
        yExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel5.setText("Anchor:");
        jLabel5.setEnabled(false);

        anchorLocationBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Top Left", "Top Middle", "Top Right", "Middle Left", "Center", "Middle Right", "Middle Right", "Bottom Middle", "Bottom Right" }));
        anchorLocationBox.setEnabled(false);
        anchorLocationBox.addActionListener(this::updateAnchor);

        buttonGroup1.add(rotate0);
        rotate0.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 0.png")));
        rotate0.setEnabled(false);
        rotate0.setName("0");
        rotate0.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate90);
        rotate90.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 90.png")));
        rotate90.setEnabled(false);
        rotate90.setName("90");
        rotate90.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate180);
        rotate180.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 180.png")));
        rotate180.setEnabled(false);
        rotate180.setName("180");
        rotate180.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate270);
        rotate270.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 270.png")));
        rotate270.setEnabled(false);
        rotate270.setName("270");
        rotate270.addActionListener(this::updateRotation);

        final GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(xExpandToFitBox))
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(jLabel2)
                                                        .add(jLabel1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(xBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .add(widthBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                                .add(25, 25, 25)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(jLabel4)
                                                        .add(jLabel3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(yBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .add(heightBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(yExpandToFitBox))))
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel5)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(anchorLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate90, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate180, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate270, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(jLabel3)
                                        .add(xBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(yBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(jLabel4)
                                        .add(widthBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(heightBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(xExpandToFitBox)
                                        .add(yExpandToFitBox))
                                .addPreferredGap(LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel5)
                                        .add(anchorLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate90, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate180, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate270, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(BorderFactory.createTitledBorder("Margins"));
        jLabel6.setText("Left:");
        jLabel6.setEnabled(false);

        jLabel7.setText("Right");
        jLabel7.setEnabled(false);

        leftMargingBox.setEnabled(false);

        rightMarginBox.setEnabled(false);

        jLabel8.setText("Top:");
        jLabel8.setEnabled(false);

        jLabel9.setText("Bottom:");
        jLabel9.setEnabled(false);

        topMarginBox.setEnabled(false);

        bottomMarginBox.setEnabled(false);

        final GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jLabel6)
                                        .add(jLabel7))
                                .add(26, 26, 26)
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(leftMargingBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .add(rightMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .add(29, 29, 29)
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jLabel8)
                                        .add(jLabel9))
                                .add(19, 19, 19)
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(topMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .add(bottomMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel6)
                                        .add(leftMargingBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel8)
                                        .add(topMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jPanel2Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jPanel2Layout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(jLabel9)
                                                .add(bottomMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .add(jLabel7)
                                        .add(rightMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel3.setBorder(BorderFactory.createTitledBorder("Caption"));
        jLabel10.setText("Position:");

        captionLocationBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Left", "Right", "Top", "Bottom", "None" }));
        captionLocationBox.addActionListener(this::updateCaptionPosition);

        jLabel11.setText("Reserve:");
        jLabel11.setEnabled(false);

        reserveBox.setEnabled(false);

        final GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel3Layout.createSequentialGroup()
                                .add(jLabel10)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(captionLocationBox, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                .add(22, 22, 22)
                                .add(jLabel11)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(reserveBox, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel3Layout.createParallelGroup(GroupLayout.BASELINE)
                                .add(jLabel10)
                                .add(jLabel11)
                                .add(captionLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .add(reserveBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(GroupLayout.LEADING, jPanel2, 0, 266, Short.MAX_VALUE)
                                        .add(GroupLayout.LEADING, layout.createParallelGroup(GroupLayout.TRAILING, false)
                                                .add(GroupLayout.LEADING, jPanel3, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                                .add(GroupLayout.LEADING, jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .add(142, 142, 142))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jPanel2, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(106, Short.MAX_VALUE))
        );
    }

    private void updateCaptionPosition(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final Object captionPosition = captionLocationBox.getSelectedItem();

        for (final IWidget widget : widgets) {
            if (widget.isComponentSplit() && captionPosition != null) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element captionPositionElement = XMLUtils.getPropertyElement(widgetProperties, "Position").get();

                captionPositionElement.getAttributeNode("value").setValue(captionPosition.toString());

                widget.setLayoutProperties(widgetProperties);
            }
        }

        designerPanel.repaint();

    }

    private void updateRotation(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String alignment = ((JComponent) evt.getSource()).getName();

        for (final IWidget widget : widgets) {
            final Element widgetProperties = widgetsAndProperties.get(widget);

            final Element rotationElement = XMLUtils.getPropertyElement(widgetProperties, "Rotation").get();

            rotationElement.getAttributeNode("value").setValue(alignment);
        }
    }

    private void updateAnchor(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final Object anchor = anchorLocationBox.getSelectedItem();

        for (final IWidget widget : widgets) {
            if (anchor != null) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element anchorElement = XMLUtils.getPropertyElement(widgetProperties, "Anchor").get();

                anchorElement.getAttributeNode("value").setValue(anchor.toString());
            }
        }

    }

    private void updateSizeAndPosition(final FocusEvent evt) {
        final Integer[] props = new Integer[4];

        if (!xBox.getText().equals("mixed")) {
            final String xText = xBox.getText().replaceAll("cm", "");

            double x = Double.parseDouble(xText);
            xBox.setText(x + " cm");

            x = x * units;

            props[0] = (int) (Math.round(x) + IMainFrame.INSET);
        }
        if (!yBox.getText().equals("mixed")) {
            final String yText = yBox.getText().replaceAll("cm", "");

            double y = Double.parseDouble(yText);
            yBox.setText(y + " cm");

            y = y * units;

            props[1] = (int) (Math.round(y) + IMainFrame.INSET);
        }
        if (!widthBox.getText().equals("mixed")) {
            final String widthText = widthBox.getText().replaceAll("cm", "");

            double width = Double.parseDouble(widthText);
            widthBox.setText(width + " cm");

            width = width * units;

            props[2] = (int) Math.round(width);
        }
        if (!heightBox.getText().equals("mixed")) {
            final String heightText = heightBox.getText().replaceAll("cm", "");

            double height = Double.parseDouble(heightText);
            heightBox.setText(height + " cm");

            height = height * units;

            props[3] = (int) Math.round(height);
        }

        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        PropertyChanger.updateSizeAndPosition(widgets, props);

        for (final IWidget widget : widgets) {
            widget.setLayoutProperties(widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }

    @Override
    public void checkboxClicked(final MouseEvent e) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final TristateCheckBox.State xExpandState = (((TristateCheckBox) xExpandToFitBox).getState());
        final TristateCheckBox.State yExpandState = (((TristateCheckBox) yExpandToFitBox).getState());

        for (final IWidget widget : widgets) {
            final List<Element> layoutProperties = XMLUtils.getElementsFromNodeList(
                    widgetsAndProperties.get(widget).getChildNodes());

            /* add size & position properties */
            final List<Element> sizeAndPosition = XMLUtils.getElementsFromNodeList(
                    layoutProperties.get(0).getChildNodes());

            if (xExpandState != TristateCheckBox.DONT_CARE) {
                final Element xExpand = sizeAndPosition.get(4);
                final String value;
                if (xExpandState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                xExpand.getAttributeNode("value").setValue(value);
            }

            if (yExpandState != TristateCheckBox.DONT_CARE) {
                final Element yExpand = sizeAndPosition.get(5);
                final String value;
                if (yExpandState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                yExpand.getAttributeNode("value").setValue(value);
            }
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String xCordToUse = null;
        String yCordToUse = null;
        String widthToUse = null;
        String heightToUse = null;
        //        TristateCheckBox.State xExpandStateToUse = null, yExpandStateToUse = null;
        String anchorLocationToUse = null;
        String rotationToUse = null;
        String captionPositionToUse = null;

        boolean isComponentSplit = false;

        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.isComponentSplit()) {
                isComponentSplit = true;
                break;
            }
        }

        /* set the caption alignment box */
        captionLocationBox.setEnabled(isComponentSplit);

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element props = widgetsAndProperties.get(widget);

            /* add size & position properties */
            final Element sizeAndPosition = (Element) props.getElementsByTagName("sizeandposition").item(0);

            final String xCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "X").get();
            final String width = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Width").get();
            final String yCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Y").get();
            final String height = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Height").get();

            //            boolean xExpand = Boolean.valueOf(XMLUtils.getAttribute(sizeAndPosition, 4)).booleanValue();
            //            boolean yExpand = Boolean.valueOf(XMLUtils.getAttribute(sizeAndPosition, 5)).booleanValue();

            final String anchor = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Anchor").get();
            final String rotation = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Rotation").get();

            /* add caption properties */
            String captionPosition = null;
            if (widget.isComponentSplit()) {
                final Element caption = (Element) props.getElementsByTagName("caption").item(0);
                captionPosition = XMLUtils.getAttributeFromChildElement(caption, "Position").get();
            }

            if (captionPositionToUse == null) {
                captionPositionToUse = captionPosition;
            } else {
                if (widget.isComponentSplit() && !captionPositionToUse.equals(captionPosition)) {
                    captionPositionToUse = "mixed";
                }
            }

            if (xCordToUse == null) { // this must be the first time round
                xCordToUse = xCord;
                yCordToUse = yCord;
                widthToUse = width;
                heightToUse = height;

                //                xExpandStateToUse = xExpand ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;
                //                yExpandStateToUse = yExpand ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;

                anchorLocationToUse = anchor;
                rotationToUse = rotation;

            } else { // check for subsequent widgets

                if (!xCordToUse.equals(xCord)) {
                    xCordToUse = "mixed";
                }

                if (!yCordToUse.equals(yCord)) {
                    yCordToUse = "mixed";
                }

                if (!widthToUse.equals(width)) {
                    widthToUse = "mixed";
                }

                if (!heightToUse.equals(height)) {
                    heightToUse = "mixed";
                }

                //                if (xExpandStateToUse != TristateCheckBox.DONT_CARE) {
                //                    if (xExpandStateToUse == TristateCheckBox.SELECTED && !xExpand) {
                //                        xExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    } else if (xExpandStateToUse == TristateCheckBox.NOT_SELECTED && xExpand) {
                //                        xExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    }
                //                }

                //                if (yExpandStateToUse != TristateCheckBox.DONT_CARE) {
                //                    if (yExpandStateToUse == TristateCheckBox.SELECTED && !yExpand) {
                //                        yExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    } else if (yExpandStateToUse == TristateCheckBox.NOT_SELECTED && yExpand) {
                //                        yExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    }
                //                }

                if (!anchorLocationToUse.equals(anchor)) {
                    anchorLocationToUse = "mixed";
                }

                if (!rotationToUse.equals(rotation)) {
                    rotationToUse = "mixed";
                }

            }
        }

        if ("mixed".equals(xCordToUse)) {
            xBox.setText("mixed");
        } else {
            double x = Integer.parseInt(xCordToUse);
            x = round((x - IMainFrame.INSET) / units);
            xBox.setText(x + " cm");
        }

        if ("mixed".equals(yCordToUse)) {
            yBox.setText("mixed");
        } else {
            double y = Integer.parseInt(yCordToUse);
            y = round((y - IMainFrame.INSET) / units);
            yBox.setText(y + " cm");
        }

        if ("mixed".equals(widthToUse)) {
            widthBox.setText("mixed");
        } else {
            double width = Integer.parseInt(widthToUse);
            width = round(width / units);
            widthBox.setText(width + " cm");
        }

        if ("mixed".equals(heightToUse)) {
            heightBox.setText("mixed");
        } else {
            double height = Integer.parseInt(heightToUse);
            height = round(height / units);
            heightBox.setText(height + " cm");
        }

        //        ((TristateCheckBox) xExpandToFitBox).setState(xExpandStateToUse);
        //        ((TristateCheckBox) yExpandToFitBox).setState(yExpandStateToUse);

        final Object selectedItem1;
        if ("mixed".equals(anchorLocationToUse)) {
            selectedItem1 = null;
        } else {
            selectedItem1 = anchorLocationToUse;
        }
        anchorLocationBox.setSelectedItem(selectedItem1);

        if ("mixed".equals(rotationToUse)) {
            buttonGroup1.setSelected(new JToggleButton("").getModel(), true);
        } else {

            /* use reflection to set the required rotation button selected */
            try {
                final Field field = getClass().getDeclaredField("rotate" + rotationToUse);
                final JToggleButton toggleButton = (JToggleButton) field.get(this);
                toggleButton.setSelected(true);
            } catch (final Exception e) {
                logger.error("Error finding rotation toggle button {}", rotationToUse, e);
            }
        }

        if (isComponentSplit) {
            final Object selectedItem;
            if ("mixed".equals(captionPositionToUse)) {
                selectedItem = null;
            } else {
                selectedItem = captionPositionToUse;
            }
            captionLocationBox.setSelectedItem(selectedItem);
        } else {
            captionLocationBox.setSelectedItem(null);
        }

        //        /** add margin properties */
        //        List margins = propertiesSet.item(1).getChildNodes();
        //
        //        String left = XMLUtils.getAttribute(margins, 0);
        //        String right = XMLUtils.getAttribute(margins, 1);
        //        String top = XMLUtils.getAttribute(margins, 2);
        //        String bottom = XMLUtils.getAttribute(margins, 3);
        //
        //        leftMargingBox.setText(left);
        //        rightMarginBox.setText(right);
        //        topMarginBox.setText(top);
        //        bottomMarginBox.setText(bottom);
        //
        //        /** add caption properties */
        //        List caption = propertiesSet.item(2).getChildNodes();
        //
        //        String position = XMLUtils.getAttribute(caption, 0);
        //        String reserve = XMLUtils.getAttribute(caption, 1);
        //
        //        captionLocationBox.setSelectedItem(position);
        //        reserveBox.setText(reserve);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return number;
    }

}
