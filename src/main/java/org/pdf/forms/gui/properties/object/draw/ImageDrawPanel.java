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
* ImageDrawPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.draw;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class ImageDrawPanel extends javax.swing.JPanel {

    private Map<IWidget, Element> widgetsAndProperties;

    private IDesigner designerPanel;

    /**
     * Creates new form Image
     */
    public ImageDrawPanel() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        imageLocationBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        sizingBox = new javax.swing.JComboBox<>();

        jLabel1.setText("Location:");

        imageLocationBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final java.awt.event.FocusEvent evt) {
                updateImageLocation(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/open.gif")));
        jButton1.addActionListener(evt -> loadImageFileChooser(evt));

        jLabel2.setText("Sizing:");

        sizingBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stretch Image To Fit", "Use Image Size" }));
        sizingBox.addActionListener(evt -> updateSizing(evt));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel1)
                                        .add(jLabel2))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(imageLocationBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(sizingBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                .add(jLabel1)
                                                .add(imageLocationBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(14, 14, 14)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(sizingBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(42, Short.MAX_VALUE))
        );
    }

    private void updateSizing(final java.awt.event.ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String sizing = (String) sizingBox.getSelectedItem();

        for (final IWidget widget : widgets) {
            if (sizing != null) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element sizingElement = XMLUtils.getPropertyElement(widgetProperties, "Sizing");

                sizingElement.getAttributeNode("value").setValue(sizing);
            }

            widget.setObjectProperties(widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }

    private void loadImageFileChooser(final java.awt.event.ActionEvent evt) {
        final String path = imageLocationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        final String[] images = new String[] { "gif", "jpeg", "png" }; // gif jpeg png
        chooser.addChoosableFileFilter(new FileFilterer(images, "Images (gif, jpeg, png)"));

        final int state = chooser.showOpenDialog(null);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            imageLocationBox.setText(fileToOpen.getAbsolutePath());
            updateImageLocation(null);
        }
    }

    private void updateImageLocation(final java.awt.event.FocusEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String location = imageLocationBox.getText();

        for (final IWidget widget : widgets) {
            if (location != null && !location.equals("mixed")) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element locationElement = XMLUtils.getPropertyElement(widgetProperties, "Location");

                locationElement.getAttributeNode("value").setValue(location);
            }

            widget.setObjectProperties(widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String locationToUse = null;
        String sizingToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectProperties = widgetsAndProperties.get(widget);

            /* get draw properties */
            final Element drawProperties = (Element) objectProperties.getElementsByTagName("draw").item(0);

            final String location = XMLUtils.getAttributeFromChildElement(drawProperties, "Location");
            final String sizing = XMLUtils.getAttributeFromChildElement(drawProperties, "Sizing");

            if (locationToUse == null) { // this must be the first time round
                locationToUse = location;
                sizingToUse = sizing;
            } else { // check for subsequent widgets
                if (!locationToUse.equals(location)) {
                    locationToUse = "mixed";
                }

                if (!sizingToUse.equals(sizing)) {
                    sizingToUse = "mixed";
                }
            }

            imageLocationBox.setText(locationToUse);
            setComboValue(sizingBox, sizingToUse.equals("mixed") ? null : sizingToUse);
        }
    }

    private void setComboValue(
            final JComboBox comboBox,
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

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private javax.swing.JTextField imageLocationBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox<String> sizingBox;

}
