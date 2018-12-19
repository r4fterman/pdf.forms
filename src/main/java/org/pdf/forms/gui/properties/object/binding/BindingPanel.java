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
* BindingPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.binding;

import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BindingPanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    /**
     * Creates new form TextFieldBindingPanel
     */
    public BindingPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        arrayField = new javax.swing.JTextField();

        jLabel1.setText("Name:");

        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final java.awt.event.FocusEvent evt) {
                updateName(evt);
            }
        });

        arrayField.setEnabled(false);

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 152, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(arrayField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(162, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(arrayField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(268, Short.MAX_VALUE))
        );
    }

    private void updateName(final java.awt.event.FocusEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String name = nameField.getText();

        for (final IWidget widget : widgets) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            final Element widgetProperties = widgetsAndProperties.get(widget);
            if (name != null && !name.equals("mixed")) {

                final String oldName = widget.getWidgetName();
                if (!oldName.equals(name)) {
                    mainFrame.renameWidget(oldName, name, widget);
                }

                final Element nameElement = XMLUtils.getPropertyElement(widgetProperties, "Name");
                nameElement.getAttributeNode("value").setValue(name);

                final int arrayNumber = mainFrame.getNextArrayNumberForName(name, widget);
                final Element arrayNumberElement = XMLUtils.getPropertyElement(widgetProperties, "Array Number");
                arrayNumberElement.getAttributeNode("value").setValue(arrayNumber + "");
            }

            widget.setObjectProperties(widgetProperties);

            mainFrame.updateHierarchyPanelUI();
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        if (widgetsAndProperties.size() == 1) {
            nameField.setEnabled(true);

            final IWidget widget = widgetsAndProperties.keySet().iterator().next();

            final Element objectProperties = widgetsAndProperties.get(widget);

            /* add binding properties */
            final Element valueProperties = (Element) objectProperties.getElementsByTagName("binding").item(0);

            final String name = XMLUtils.getAttributeFromChildElement(valueProperties, "Name");
            final String arrayNumber = XMLUtils.getAttributeFromChildElement(valueProperties, "Array Number");

            nameField.setText(name);
            arrayField.setText(arrayNumber);
        } else {
            nameField.setEnabled(false);
        }
    }

    private javax.swing.JTextField arrayField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameField;

}
