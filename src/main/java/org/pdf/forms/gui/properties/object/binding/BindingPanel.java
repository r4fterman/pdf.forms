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
* BindingPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.binding;

import java.awt.event.FocusEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BindingPanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    private JTextField arrayField;
    private JTextField nameField;

    /**
     * Creates new form TextFieldBindingPanel.
     */
    public BindingPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JLabel jLabel1 = new JLabel();
        nameField = new JTextField();
        arrayField = new JTextField();

        jLabel1.setText("Name:");

        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateName(evt);
            }
        });

        arrayField.setEnabled(false);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(nameField, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(arrayField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(162, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(arrayField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(268, Short.MAX_VALUE))
        );
    }

    private void updateName(final FocusEvent evt) {
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

                final Element nameElement = XMLUtils.getPropertyElement(widgetProperties, "Name").get();
                nameElement.getAttributeNode("value").setValue(name);

                final int arrayNumber = mainFrame.getNextArrayNumberForName(name, widget);
                final Element arrayNumberElement = XMLUtils.getPropertyElement(widgetProperties, "Array Number").get();
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

            final String name = XMLUtils.getAttributeFromChildElement(valueProperties, "Name").orElse("");
            final String arrayNumber = XMLUtils.getAttributeFromChildElement(valueProperties, "Array Number").orElse("0");

            nameField.setText(name);
            arrayField.setText(arrayNumber);
        } else {
            nameField.setEnabled(false);
        }
    }

}
