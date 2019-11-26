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
* TextFieldFieldPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.field;

import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class TextFieldFieldPanel extends JPanel implements TristateCheckBoxParent {

    private Map<IWidget, Element> widgetsAndProperties;

    private JCheckBox allowMultipleLinesBox;
    private JCheckBox limitLengthBox;
    private JTextField maxCharsBox;

    public TextFieldFieldPanel() {
        initComponents();
    }

    private void initComponents() {
        allowMultipleLinesBox = new TristateCheckBox("Allow Multiple Line", TristateCheckBox.NOT_SELECTED, this);
        limitLengthBox = new TristateCheckBox("Limit Length", TristateCheckBox.NOT_SELECTED, this);
        final JLabel jLabel2 = new JLabel();
        maxCharsBox = new JTextField();
        final JSeparator jSeparator1 = new JSeparator();
        final JLabel jLabel3 = new JLabel();
        final JComboBox<String> presenceBox = new JComboBox<>();
        final JComboBox<String> appearanceBox = new JComboBox<>();
        final JLabel jLabel1 = new JLabel();

        allowMultipleLinesBox.setText("Allow Multiple Lines");
        allowMultipleLinesBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowMultipleLinesBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        limitLengthBox.setText("Limit Length");
        limitLengthBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        limitLengthBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel2.setText("Max Chars:");

        maxCharsBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateMaxChars(evt);
            }
        });

        jLabel3.setText("Presence");

        presenceBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Visible" }));

        appearanceBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "None", "Underline", "Solid", "Sunken Box", "Custom..." }));
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        jLabel1.setText("Apperance:");
        jLabel1.setEnabled(false);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(jSeparator1, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                        .add(GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(appearanceBox, 0, 190, Short.MAX_VALUE))
                                        .add(GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                                        .add(layout.createSequentialGroup()
                                                                .add(limitLengthBox)
                                                                .addPreferredGap(LayoutStyle.RELATED, 42, Short.MAX_VALUE)
                                                                .add(jLabel2)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(maxCharsBox, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
                                                        .add(allowMultipleLinesBox)))
                                        .add(GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(jLabel3)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(presenceBox, 0, 202, Short.MAX_VALUE)))
                                .add(16, 16, 16))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(appearanceBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(allowMultipleLinesBox)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(limitLengthBox)
                                        .add(jLabel2)
                                        .add(maxCharsBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel3)
                                        .add(presenceBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void updateMaxChars(final FocusEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String maxChars = maxCharsBox.getText();

        for (final IWidget widget : widgets) {
            if (maxChars != null && !maxChars.equals("mixed")) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element maxCharsElement = XMLUtils.getPropertyElement(widgetProperties, "Max Chars").get();

                maxCharsElement.getAttributeNode("value").setValue(maxChars);
            }
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        TristateCheckBox.State allowMultipleLinesToUse = null;
        TristateCheckBox.State limitLengthToUse = null;

        //Boolean allowMultipleLinesToUse = null, limitLengthToUse = null;
        String maxCharsToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectPropertiesElement = widgetsAndProperties.get(widget);

            /* add field properties */
            final Element fieldProperties = (Element) objectPropertiesElement.getElementsByTagName("field").item(0);

            final boolean allowMultipleLines = Boolean.parseBoolean(XMLUtils.getAttributeFromChildElement(fieldProperties, "Allow Multiple Lines").orElse("false"));
            final boolean limitLength = Boolean.parseBoolean(XMLUtils.getAttributeFromChildElement(fieldProperties, "Limit Length").orElse("false"));
            final String maxChars = XMLUtils.getAttributeFromChildElement(fieldProperties, "Max Chars").orElse("10");

            if (maxCharsToUse == null) {
                if (allowMultipleLines) {
                    allowMultipleLinesToUse = TristateCheckBox.SELECTED;
                } else {
                    allowMultipleLinesToUse = TristateCheckBox.NOT_SELECTED;
                }
                if (limitLength) {
                    limitLengthToUse = TristateCheckBox.SELECTED;
                } else {
                    limitLengthToUse = TristateCheckBox.NOT_SELECTED;
                }

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

            final String mixed;
            if (maxCharsToUse.equals("mixed")) {
                mixed = "mixed";
            } else {
                mixed = maxCharsToUse;
            }
            maxCharsBox.setText(mixed);
        }
    }

    @Override
    public void checkboxClicked(final MouseEvent e) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final TristateCheckBox.State allowMultipleLinesState = (((TristateCheckBox) allowMultipleLinesBox).getState());
        final TristateCheckBox.State limitLengthState = (((TristateCheckBox) limitLengthBox).getState());

        for (final IWidget widget : widgets) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(
                    widgetsAndProperties.get(widget).getChildNodes());

            /* add field properties */
            final List<Element> fieldProperties = XMLUtils.getElementsFromNodeList(
                    objectProperties.get(0).getChildNodes());

            if (allowMultipleLinesState != TristateCheckBox.DONT_CARE) {
                final Element allowMultipleLines = fieldProperties.get(1);
                final String value;
                if (allowMultipleLinesState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                allowMultipleLines.getAttributeNode("value").setValue(value);
            }

            if (limitLengthState != TristateCheckBox.DONT_CARE) {
                final Element limitLength = fieldProperties.get(2);
                final String value;
                if (limitLengthState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                limitLength.getAttributeNode("value").setValue(value);
            }
        }
    }

}
