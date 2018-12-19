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
* ValuePanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.value;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class ValuePanel extends JPanel {
    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    /**
     * Creates new form TextFieldValuePanel
     */
    public ValuePanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        typeBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        defaultBox = new javax.swing.JComboBox<>();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(final java.awt.event.ComponentEvent evt) {
                shown(evt);
            }
        });

        jLabel1.setText("Type:");
        jLabel1.setEnabled(false);

        typeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only" }));
        typeBox.setEnabled(false);

        jLabel2.setText("Default:");

        defaultBox.addActionListener(evt -> updateDefaultText(evt));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
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
    }

    private void shown(final java.awt.event.ComponentEvent evt) {
        setProperties(widgetsAndProperties);
    }

    private void updateDefaultText(final java.awt.event.ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final IWidget testWidget = widgets.iterator().next();

        final String selectedItem = String.valueOf(defaultBox.getSelectedItem());

        switch (testWidget.getType()) {
            case IWidget.CHECK_BOX:
                if (selectedItem.equals("null")) {
                    break;
                }

                for (final IWidget widget : widgetsAndProperties.keySet()) {
                    final Element objectProperties = widgetsAndProperties.get(widget);

                    final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                    defaultElement.getAttributeNode("value").setValue(selectedItem);

                    widget.setObjectProperties(objectProperties);
                }

                break;
            case IWidget.RADIO_BUTTON:
                Element objectProperties = widgetsAndProperties.get(testWidget);

                Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

                if (selectedItem.equals("On")) {
                    final List<IWidget> allwidgets = designerPanel.getWidgets(); // todo flattern to account for groups
                    for (final IWidget widget : allwidgets) {
                        if (widget.getType() == IWidget.RADIO_BUTTON && widget != testWidget) {
                            final RadioButtonWidget rbw = (RadioButtonWidget) widget;
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
                objectProperties = widgetsAndProperties.get(testWidget);

                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

                break;
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final IWidget testWidget = widgetsAndProperties.keySet().iterator().next();

        final ActionListener listener = defaultBox.getActionListeners()[0];
        defaultBox.removeActionListener(listener);

        defaultBox.removeAllItems();

        final int type = testWidget.getType();
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

                    final Element objectProperties = widgetsAndProperties.get(testWidget);

                    /* add field properties */

                    //                    if (widgetsAndProperties.size() == 1) { // only 1 widget is currently selected

                    //                    List objectProperties = XMLUtils.getElementsFromNodeList(
                    //                            ((Element) widgetsAndProperties.get(testWidget)).getChildNodes());

                    if (widgetsAndProperties.size() == 1) {
                        final Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);

                        final List items = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());

                        if (type == IWidget.COMBO_BOX) {
                            defaultBox.addItem("< None >");
                        }

                        for (final Object item1 : items) {
                            final Element item = (Element) item1;

                            final String value = XMLUtils.getAttributeFromElement(item, "item");

                            defaultBox.addItem(value);
                        }
                    }

                    final String defaultText = getDefault(objectProperties);

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

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectProperties = widgetsAndProperties.get(widget);

            final String defaultState = getDefault(objectProperties);

            if (defaultStateToUse == null) { // this must be the first time round
                defaultStateToUse = defaultState;

            } else { // check for subsequent widgets

                if (!defaultStateToUse.equals(defaultState)) {
                    defaultStateToUse = "mixed";
                }
            }

        }

        defaultBox.setSelectedItem("mixed".equals(defaultStateToUse) ? null : defaultStateToUse);
    }

    private String getDefault(final Element objectProperties) {
        final Element valueProperties = (Element) objectProperties.getElementsByTagName("value").item(0);

        return XMLUtils.getAttributeFromChildElement(valueProperties, "Default");
    }

    private void setItemsEnabled(final boolean b) {
        defaultBox.setEnabled(b);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> defaultBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox typeBox;
    // End of variables declaration//GEN-END:variables
}
