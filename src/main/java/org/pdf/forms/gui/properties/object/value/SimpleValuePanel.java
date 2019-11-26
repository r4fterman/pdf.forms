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
* SimpleValuePanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.value;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class SimpleValuePanel extends JPanel {
    private Map<IWidget, Element> widgetsAndProperties;

    private JTextField defaultTextBox;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JComboBox<String> typeBox;

    /**
     * Creates new form TextFieldValuePanel.
     */
    public SimpleValuePanel(final int type) {
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
        jLabel1 = new JLabel();
        typeBox = new JComboBox<>();
        final JLabel jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        defaultTextBox = new JTextField();
        jScrollPane1 = new JScrollPane();
        final JTextArea emptyMessageBox = new JTextArea();

        jLabel1.setText("Type:");
        jLabel1.setEnabled(false);

        typeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only" }));
        typeBox.setEnabled(false);

        jLabel2.setText("Default:");

        jLabel3.setText("Empty Message:");

        defaultTextBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateDefaultText(evt);
            }
        });

        emptyMessageBox.setColumns(20);
        emptyMessageBox.setRows(5);
        emptyMessageBox.setEnabled(false);
        jScrollPane1.setViewportView(emptyMessageBox);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(typeBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(jLabel3)
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel2)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(defaultTextBox, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                                                        .add(jScrollPane1, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))))
                                .add(172, 172, 172))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(typeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(defaultTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jScrollPane1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(178, Short.MAX_VALUE))
        );
    }

    private void updateDefaultText(final FocusEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String defaultText = defaultTextBox.getText();

        for (final IWidget widget : widgets) {
            if (defaultText != null && !defaultText.equals("mixed")) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element defaultTextElement = XMLUtils.getPropertyElement(widgetProperties, "Default").get();

                defaultTextElement.getAttributeNode("value").setValue(defaultText);
            }

            widget.setObjectProperties(widgetsAndProperties.get(widget));
        }

        //designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String defaultTextToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectPropertiesElement = widgetsAndProperties.get(widget);

            /* add value properties */
            final Element valueProperties = (Element) objectPropertiesElement.getElementsByTagName("value").item(0);

            final String defaultText = XMLUtils.getAttributeFromChildElement(valueProperties, "Default").orElse("");
            if (defaultTextToUse == null) {
                defaultTextToUse = defaultText;
            } else {
                if (!defaultTextToUse.equals(defaultText)) {
                    defaultTextToUse = "mixed";
                }
            }

            final String mixed;
            if (defaultTextToUse.equals("mixed")) {
                mixed = "mixed";
            } else {
                mixed = defaultTextToUse;
            }
            defaultTextBox.setText(mixed);
        }
    }

}
