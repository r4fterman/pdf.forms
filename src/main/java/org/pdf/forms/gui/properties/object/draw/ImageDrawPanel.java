/**
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
import java.util.Iterator;
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

    private Map widgetsAndProperties;

    private IDesigner designerPanel;

    /**
     * Creates new form Image
     */
    public ImageDrawPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        imageLocationBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        sizingBox = new javax.swing.JComboBox();

        jLabel1.setText("Location:");

        imageLocationBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateImageLocation(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/open.gif")));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImageFileChooser(evt);
            }
        });

        jLabel2.setText("Sizing:");

        sizingBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Stretch Image To Fit", "Use Image Size"}));
        sizingBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateSizing(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
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
    }// </editor-fold>//GEN-END:initComponents

    private void updateSizing(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateSizing
        Set widgets = widgetsAndProperties.keySet();

        String sizing = (String) sizingBox.getSelectedItem();

        for (Iterator it = widgets.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            if (sizing != null) {
                Element widgetProperties = (Element) widgetsAndProperties.get(widget);

                Element sizingElement = XMLUtils.getPropertyElement(widgetProperties, "Sizing");

                sizingElement.getAttributeNode("value").setValue(sizing);
            }

            widget.setObjectProperties((Element) widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }//GEN-LAST:event_updateSizing

    private void loadImageFileChooser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadImageFileChooser
        String path = imageLocationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String[] images = new String[]{"gif", "jpeg", "png"}; // gif jpeg png
        chooser.addChoosableFileFilter(new FileFilterer(images, "Images (gif, jpeg, png)"));

        final int state = chooser.showOpenDialog(null);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            imageLocationBox.setText(fileToOpen.getAbsolutePath());
            updateImageLocation(null);
        }
    }//GEN-LAST:event_loadImageFileChooser

    private void updateImageLocation(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_updateImageLocation
        Set widgets = widgetsAndProperties.keySet();

        String location = imageLocationBox.getText();

        for (Iterator it = widgets.iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            if (location != null && !location.equals("mixed")) {
                Element widgetProperties = (Element) widgetsAndProperties.get(widget);

                Element locationElement = XMLUtils.getPropertyElement(widgetProperties, "Location");

                locationElement.getAttributeNode("value").setValue(location);
            }

            widget.setObjectProperties((Element) widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }//GEN-LAST:event_updateImageLocation

    public void setProperties(Map widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String locationToUse = null, sizingToUse = null;

        /** iterate through the widgets */
        for (Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext();) {
            IWidget widget = (IWidget) it.next();

            Element objectProperties = (Element) widgetsAndProperties.get(widget);

            /** get draw properties */
            Element drawProperties = (Element) objectProperties.getElementsByTagName("draw").item(0);

            String location = XMLUtils.getAttributeFromChildElement(drawProperties, "Location");
            String sizing = XMLUtils.getAttributeFromChildElement(drawProperties, "Sizing");

            if (locationToUse == null) { // this must be the first time round
                locationToUse = location;
                sizingToUse = sizing;
            } else { // check for subsequent widgets
                if (!locationToUse.equals(location))
                    locationToUse = "mixed";

                if (!sizingToUse.equals(sizing))
                    sizingToUse = "mixed";
            }

            imageLocationBox.setText(locationToUse);
            setComboValue(sizingBox, sizingToUse.equals("mixed") ? null : sizingToUse);
        }
    }

    private void setComboValue(JComboBox comboBox, Object value) {
        ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex(((Integer) value).intValue());
        } else {
            comboBox.setSelectedItem(value);
        }

        comboBox.addActionListener(listener);
    }

    public void setDesignerPanel(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField imageLocationBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox sizingBox;
    // End of variables declaration//GEN-END:variables
}