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
* TextFieldFieldPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.field;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class TextFieldFieldPanel extends JPanel implements TristateCheckBoxParent {
    private Map<IWidget, Element> widgetsAndProperties;

    public TextFieldFieldPanel() {
        initComponents();
    }

    private void initComponents() {
        allowMultipleLinesBox = new TristateCheckBox("Allow Multiple Line", TristateCheckBox.NOT_SELECTED, this);
        limitLengthBox = new TristateCheckBox("Limit Length", TristateCheckBox.NOT_SELECTED, this);
        jLabel2 = new javax.swing.JLabel();
        maxCharsBox = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        presenceBox = new javax.swing.JComboBox();
        appearanceBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        allowMultipleLinesBox.setText("Allow Multiple Lines");
        allowMultipleLinesBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowMultipleLinesBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        limitLengthBox.setText("Limit Length");
        limitLengthBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        limitLengthBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel2.setText("Max Chars:");

        maxCharsBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final java.awt.event.FocusEvent evt) {
                updateMaxChars(evt);
            }
        });

        jLabel3.setText("Presence");

        presenceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Visible" }));

        appearanceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Underline", "Solid", "Sunken Box", "Custom..." }));
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        jLabel1.setText("Apperance:");
        jLabel1.setEnabled(false);

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(appearanceBox, 0, 190, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                        .add(layout.createSequentialGroup()
                                                                .add(limitLengthBox)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 42, Short.MAX_VALUE)
                                                                .add(jLabel2)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(maxCharsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                        .add(allowMultipleLinesBox)))
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(jLabel3)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(presenceBox, 0, 202, Short.MAX_VALUE)))
                                .add(16, 16, 16))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(appearanceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(allowMultipleLinesBox)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(limitLengthBox)
                                        .add(jLabel2)
                                        .add(maxCharsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel3)
                                        .add(presenceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void updateMaxChars(final java.awt.event.FocusEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String maxChars = maxCharsBox.getText();

        for (final IWidget widget : widgets) {
            if (maxChars != null && !maxChars.equals("mixed")) {
                final Element widgetProperties = (Element) widgetsAndProperties.get(widget);

                final Element maxCharsElement = XMLUtils.getPropertyElement(widgetProperties, "Max Chars");

                maxCharsElement.getAttributeNode("value").setValue(maxChars);
            }
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        TristateCheckBox.State allowMultipleLinesToUse = null, limitLengthToUse = null;

        //Boolean allowMultipleLinesToUse = null, limitLengthToUse = null;
        String maxCharsToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectPropertiesElement = widgetsAndProperties.get(widget);

            /* add field properties */
            final Element fieldProperties = (Element) objectPropertiesElement.getElementsByTagName("field").item(0);

            final boolean allowMultipleLines = Boolean.valueOf(XMLUtils.getAttributeFromChildElement(fieldProperties, "Allow Multiple Lines"));
            final boolean limitLength = Boolean.valueOf(XMLUtils.getAttributeFromChildElement(fieldProperties, "Limit Length"));
            final String maxChars = XMLUtils.getAttributeFromChildElement(fieldProperties, "Max Chars");

            if (maxCharsToUse == null) {

                allowMultipleLinesToUse = allowMultipleLines ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;
                limitLengthToUse = limitLength ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;

                maxCharsToUse = maxChars;
            } else {

                if (allowMultipleLinesToUse != TristateCheckBox.DONT_CARE) {
                    if (allowMultipleLinesToUse == TristateCheckBox.SELECTED && !allowMultipleLines) {
                        allowMultipleLinesToUse = TristateCheckBox.DONT_CARE;
                    } else if (allowMultipleLinesToUse == TristateCheckBox.NOT_SELECTED && allowMultipleLines) {
                        allowMultipleLinesToUse = TristateCheckBox.DONT_CARE;
                    }
                }

                if (limitLengthToUse != TristateCheckBox.DONT_CARE) {
                    if (limitLengthToUse == TristateCheckBox.SELECTED && !limitLength) {
                        limitLengthToUse = TristateCheckBox.DONT_CARE;
                    } else if (limitLengthToUse == TristateCheckBox.NOT_SELECTED && limitLength) {
                        limitLengthToUse = TristateCheckBox.DONT_CARE;
                    }
                }

                if (!maxCharsToUse.equals(maxChars)) {
                    maxCharsToUse = "mixed";
                }
            }

            ((TristateCheckBox) allowMultipleLinesBox).setState(allowMultipleLinesToUse);
            ((TristateCheckBox) limitLengthBox).setState(limitLengthToUse);

            maxCharsBox.setText(maxCharsToUse.equals("mixed") ? "mixed" : maxCharsToUse);
        }
    }

    @Override
    public void checkboxClicked(final MouseEvent e) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final TristateCheckBox.State allowMultipleLinesState = (((TristateCheckBox) allowMultipleLinesBox).getState());
        final TristateCheckBox.State limitLengthState = (((TristateCheckBox) limitLengthBox).getState());

        for (final IWidget widget : widgets) {
            final List objectProperties = XMLUtils.getElementsFromNodeList(
                    widgetsAndProperties.get(widget).getChildNodes());

            /* add field properties */
            final List<Element> fieldProperties = XMLUtils.getElementsFromNodeList(
                    ((Element) objectProperties.get(0)).getChildNodes());

            if (allowMultipleLinesState != TristateCheckBox.DONT_CARE) {
                final Element allowMultipleLines = fieldProperties.get(1);
                allowMultipleLines.getAttributeNode("value").setValue(allowMultipleLinesState == TristateCheckBox.SELECTED ? "true" : "false");
            }

            if (limitLengthState != TristateCheckBox.DONT_CARE) {
                final Element limitLength = fieldProperties.get(2);
                limitLength.getAttributeNode("value").setValue(limitLengthState == TristateCheckBox.SELECTED ? "true" : "false");
            }
        }
    }

    private javax.swing.JCheckBox allowMultipleLinesBox;
    private javax.swing.JComboBox appearanceBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox limitLengthBox;
    private javax.swing.JTextField maxCharsBox;
    private javax.swing.JComboBox presenceBox;

}
