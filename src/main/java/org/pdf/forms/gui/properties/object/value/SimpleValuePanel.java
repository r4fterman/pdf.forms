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
* SimpleValuePanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.value;

import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class SimpleValuePanel extends JPanel {
    private Map<IWidget, Element> widgetsAndProperties;

    private int type;

    /**
     * Creates new form TextFieldValuePanel
     */
    public SimpleValuePanel(int type) {
        this.type = type;

        initComponents();

        if (type == IWidget.TEXT_FIELD) {
            jLabel1.setVisible(true);
            typeBox.setVisible(true);
        } else {
            jLabel1.setVisible(false);
            typeBox.setVisible(false);
        }

        jLabel3.setVisible(false);
        jScrollPane1.setVisible(false);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        typeBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        defaultTextBox = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        emptyMessageBox = new javax.swing.JTextArea();

        jLabel1.setText("Type:");
        jLabel1.setEnabled(false);

        typeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only" }));
        typeBox.setEnabled(false);

        jLabel2.setText("Default:");

        jLabel3.setText("Empty Message:");

        defaultTextBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateDefaultText(evt);
            }
        });

        emptyMessageBox.setColumns(20);
        emptyMessageBox.setRows(5);
        emptyMessageBox.setEnabled(false);
        jScrollPane1.setViewportView(emptyMessageBox);

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
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jLabel3)
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel2)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(defaultTextBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 218, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                .add(172, 172, 172))
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
                                        .add(defaultTextBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(178, Short.MAX_VALUE))
        );
    }

    private void updateDefaultText(java.awt.event.FocusEvent evt) {
        Set<IWidget> widgets = widgetsAndProperties.keySet();

        String defaultText = defaultTextBox.getText();

        for (IWidget widget : widgets) {
            if (defaultText != null && !defaultText.equals("mixed")) {
                Element widgetProperties = (Element) widgetsAndProperties.get(widget);

                Element defaultTextElement = XMLUtils.getPropertyElement(widgetProperties, "Default");

                defaultTextElement.getAttributeNode("value").setValue(defaultText);
            }

            widget.setObjectProperties((Element) widgetsAndProperties.get(widget));
        }

        //designerPanel.repaint();
    }

    public void setProperties(Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String defaultTextToUse = null;

        /* iterate through the widgets */
        for (IWidget widget : widgetsAndProperties.keySet()) {
            Element objectPropertiesElement = widgetsAndProperties.get(widget);

            /* add value properties */
            Element valueProperties = (Element) objectPropertiesElement.getElementsByTagName("value").item(0);

            String defaultText = XMLUtils.getAttributeFromChildElement(valueProperties, "Default");

            if (defaultTextToUse == null) {
                defaultTextToUse = defaultText;
            } else {
                if (!defaultTextToUse.equals(defaultText)) {
                    defaultTextToUse = "mixed";
                }
            }

            defaultTextBox.setText(defaultTextToUse.equals("mixed") ? "mixed" : defaultTextToUse);
        }
    }

    private javax.swing.JTextField defaultTextBox;
    private javax.swing.JTextArea emptyMessageBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox typeBox;
}
