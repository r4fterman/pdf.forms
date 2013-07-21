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
 * ValuePanel.java
 * ---------------
 */
package org.pdf.forms.gui.properties.object.value;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class ValuePanel extends JPanel {
    private Map widgetsAndProperties;
    private IDesigner designerPanel;

    /**
     * Creates new form TextFieldValuePanel
     */
    public ValuePanel() {
        initComponents();
    }

    public void setDesignerPanel(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        typeBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        defaultBox = new javax.swing.JComboBox();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                shown(evt);
            }
        });

        jLabel1.setText("Type:");
        jLabel1.setEnabled(false);

        typeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only"}));
        typeBox.setEnabled(false);

        jLabel2.setText("Default:");

        defaultBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDefaultText(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(typeBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(jLabel2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(defaultBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 175, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(typeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(defaultBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void shown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_shown
        setProperties(widgetsAndProperties);
    }//GEN-LAST:event_shown

    private void updateDefaultText(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDefaultText
        Set widgets = widgetsAndProperties.keySet();

        IWidget testWidget = (IWidget) widgets.iterator().next();

        String selectedItem = String.valueOf(defaultBox.getSelectedItem());

        switch (testWidget.getType()) {
            case IWidget.CHECK_BOX:

                if (selectedItem.equals("null"))
                    break;

                for (Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext(); ) {

                    IWidget widget = (IWidget) it.next();

                    Element objectProperties = (Element) widgetsAndProperties.get(widget);

                    Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                    defaultElement.getAttributeNode("value").setValue(selectedItem);

                    widget.setObjectProperties(objectProperties);
                }

                break;
            case IWidget.RADIO_BUTTON:

                Element objectProperties = (Element) widgetsAndProperties.get(testWidget);

                Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

                if (selectedItem.equals("On")) {
                    List allwidgets = designerPanel.getWidgets(); // todo flattern to account for groups
                    for (Iterator it = allwidgets.iterator(); it.hasNext(); ) {
                        IWidget widget = (IWidget) it.next();
                        if (widget.getType() == IWidget.RADIO_BUTTON && widget != testWidget) {
                            RadioButtonWidget rbw = (RadioButtonWidget) widget;
                            if (rbw.getRadioButtonGroupName().equals(((RadioButtonWidget) testWidget).getRadioButtonGroupName())) {
                                objectProperties = rbw.getProperties().getDocumentElement();
                                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");
                                defaultElement.getAttributeNode("value").setValue("Off");
                                rbw.setObjectProperties(objectProperties);
                            }
                        }
                    }
                }

                break;
            default:

                objectProperties = (Element) widgetsAndProperties.get(testWidget);

                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

                break;
        }

        designerPanel.repaint();
    }//GEN-LAST:event_updateDefaultText

    public void setProperties(Map widgetsAndProperties) {

        this.widgetsAndProperties = widgetsAndProperties;

        IWidget testWidget = (IWidget) widgetsAndProperties.keySet().iterator().next();

        ActionListener listener = defaultBox.getActionListeners()[0];
        defaultBox.removeActionListener(listener);

        defaultBox.removeAllItems();

        int type = testWidget.getType();
        switch (type) {
            case IWidget.CHECK_BOX:

                setupToggleWidget();

                break;
            case IWidget.RADIO_BUTTON:

                if (widgetsAndProperties.size() == 1) {
                    setItemsEnabled(true);
                    setupToggleWidget();
                } else {
                    setItemsEnabled(false);
                }

                break;
            default:

                if (widgetsAndProperties.size() == 1) { // only 1 widget selected
                    setItemsEnabled(true);

                    Element objectProperties = (Element) widgetsAndProperties.get(testWidget);

                    /** add field properties */

//                    if (widgetsAndProperties.size() == 1) { // only 1 widget is currently selected

//                    List objectProperties = XMLUtils.getElementsFromNodeList(
//                            ((Element) widgetsAndProperties.get(testWidget)).getChildNodes());

                    if (widgetsAndProperties.size() == 1) {

                        Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);

                        List items = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());

                        if (type == IWidget.COMBO_BOX)
                            defaultBox.addItem("< None >");

                        for (Iterator it = items.iterator(); it.hasNext(); ) {
                            Element item = (Element) it.next();

                            String value = XMLUtils.getAttributeFromElement(item, "item");

                            defaultBox.addItem(value);
                        }
                    }

                    String defaultText = getDefault(objectProperties);

                    defaultBox.setSelectedItem(defaultText);

                } else {
                    setItemsEnabled(false);
                }

                break;
        }

        defaultBox.addActionListener(listener);
    }

    private void setupToggleWidget() {
        String defaultStateToUse = null;

        defaultBox.addItem("On");
        defaultBox.addItem("Off");

        /** iterate through the widgets */
        for (Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext(); ) {

            IWidget widget = (IWidget) it.next();

            Element objectProperties = (Element) widgetsAndProperties.get(widget);

            String defaultState = getDefault(objectProperties);

            if (defaultStateToUse == null) { // this must be the first time round
                defaultStateToUse = defaultState;

            } else { // check for subsequent widgets

                if (!defaultStateToUse.equals(defaultState))
                    defaultStateToUse = "mixed";
            }

        }

        defaultBox.setSelectedItem(defaultStateToUse.equals("mixed") ? null : defaultStateToUse);
    }

    private String getDefault(Element objectProperties) {
        Element valueProperties = (Element) objectProperties.getElementsByTagName("value").item(0);

        String defaultText = XMLUtils.getAttributeFromChildElement(valueProperties, "Default");
        return defaultText;
    }

    private void setItemsEnabled(boolean b) {
        defaultBox.setEnabled(b);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox defaultBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox typeBox;
    // End of variables declaration//GEN-END:variables
}
