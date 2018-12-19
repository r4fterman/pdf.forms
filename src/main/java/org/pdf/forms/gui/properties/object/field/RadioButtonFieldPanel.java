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
* RadioButtonFieldPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.field;

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.windows.RadioButtonGroupOrganiser;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class RadioButtonFieldPanel extends javax.swing.JPanel {
    private Map<IWidget, Element> widgetsAndProperties;

    private IDesigner designerPanel;
    private List<ButtonGroup> buttonGroups;
    private int type;

    /**
     * Creates new form RadioButtonFieldPanel
     */
    public RadioButtonFieldPanel() {
        initComponents();
    }

    public void setDesignerPanel(
            final IDesigner designerPanel,
            final int type) {
        this.designerPanel = designerPanel;
        this.type = type;
    }

    private void initComponents() {
        appearanceBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonGroupBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        appearanceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Underline", "Solid", "Sunken Box", "Custom..." }));
        appearanceBox.setEnabled(false);

        jLabel1.setText("Apperance:");
        jLabel1.setEnabled(false);

        jLabel2.setText("Radio Button Group:");

        buttonGroupBox.addActionListener(evt -> updateButtonGroup(evt));

        jButton1.setText("Organise");
        jButton1.addActionListener(evt -> organiseClicked(evt));

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
                                                .add(appearanceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jButton1)
                                                        .add(buttonGroupBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(appearanceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(buttonGroupBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton1)
                                .addContainerGap(214, Short.MAX_VALUE))
        );
    }

    private void organiseClicked(final java.awt.event.ActionEvent evt) {
        final JDialog dialog = new JDialog();
        final IMainFrame mainFrame = designerPanel.getMainFrame();
        final List<IWidget> widgets = mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()).getWidgets();

        final RadioButtonGroupOrganiser radioButtonGroupOrgansier = new RadioButtonGroupOrganiser(dialog, buttonGroups,
                widgets, type);

        dialog.getContentPane().add(radioButtonGroupOrgansier);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo((JFrame) mainFrame);
        dialog.setVisible(true);

        populateButtonGroups();

        setProperties(widgetsAndProperties);
    }

    private void updateButtonGroup(final java.awt.event.ActionEvent evt) {
        for (final Iterator it = widgetsAndProperties.keySet().iterator(); it.hasNext(); ) {

            final IWidget widget;
            if (type == IWidget.RADIO_BUTTON) {
                final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) it.next();
                radioButtonWidget.setRadioButtonGroupName((String) buttonGroupBox.getSelectedItem());

                widget = radioButtonWidget;
            } else {
                final CheckBoxWidget checkBoxWidget = (CheckBoxWidget) it.next();
                checkBoxWidget.setCheckBoxGroupName((String) buttonGroupBox.getSelectedItem());

                widget = checkBoxWidget;
            }

            final Element objectProperties = widget.getProperties().getDocumentElement();
            final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");
            defaultElement.getAttributeNode("value").setValue("Off");
            widget.setObjectProperties(objectProperties);
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String buttonGroupToUse = null;

        final IMainFrame mainFrame = designerPanel.getMainFrame();
        final Page page = mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage());

        switch (this.type) {
            case IWidget.RADIO_BUTTON:
                this.buttonGroups = page.getRadioButtonGroups();

                break;
            case IWidget.CHECK_BOX:
                this.buttonGroups = page.getCheckBoxGroups();

                break;
        }

        populateButtonGroups();

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectPropertiesElement = widgetsAndProperties.get(widget);

            /* add field propertes */
            final Element fieldProperties = (Element) objectPropertiesElement.getElementsByTagName("field").item(0);

            final String buttonGroup = XMLUtils.getAttributeFromChildElement(fieldProperties, "Group Name");

            if (buttonGroupToUse == null) { // this must be the first time round
                buttonGroupToUse = buttonGroup;
            } else { // check for subsequent widgets
                if (!buttonGroupToUse.equals(buttonGroup)) {
                    buttonGroupToUse = "mixed";
                }
            }

            setComboValue(buttonGroupBox, buttonGroupToUse.equals("mixed") ? null : buttonGroupToUse);
        }
    }

    private void populateButtonGroups() {
        final ActionListener listener = buttonGroupBox.getActionListeners()[0];
        buttonGroupBox.removeActionListener(listener);

        buttonGroupBox.removeAllItems();
        for (final ButtonGroup buttonGroup : buttonGroups) {
            buttonGroupBox.addItem(buttonGroup.getName());
        }

        buttonGroupBox.addActionListener(listener);
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

    private javax.swing.JComboBox<String> appearanceBox;
    private javax.swing.JComboBox<String> buttonGroupBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;

}
