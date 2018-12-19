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
* RadioButtonGroupOrganiser.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class RadioButtonGroupOrganiser extends javax.swing.JPanel {
    private List radioButtonGroups;
    private JDialog parentDialog;
    private List widgetsOnPage;
    private int type;

    /**
     * Creates new form RadioButtonGroupOrganiser
     */
    public RadioButtonGroupOrganiser(
            JDialog parentDialog,
            List radioButtonGroups,
            List widgetsOnPage,
            int type) {
        this.parentDialog = parentDialog;
        this.radioButtonGroups = radioButtonGroups;
        this.widgetsOnPage = widgetsOnPage;
        this.type = type;

        initComponents();

        populateButtonGroupsList();
    }

    private void populateButtonGroupsList() {
        DefaultListModel model = (DefaultListModel) buttonGroupsList.getModel();
        model.removeAllElements();

        for (Iterator it = radioButtonGroups.iterator(); it.hasNext(); ) {
            ButtonGroup buttonGroup = (ButtonGroup) it.next();
            model.addElement(buttonGroup.getName());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        buttonGroupsList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jLabel1.setText("Radio Button Groups");

        buttonGroupsList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(buttonGroupsList);

        jButton1.setText("Add ...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClicked(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeClicked(evt);
            }
        });

        jButton3.setText("Rename ...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameClicked(evt);
            }
        });

        jButton4.setText("OK");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                        .add(jLabel1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jButton1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton3)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 154, Short.MAX_VALUE)
                                                .add(jButton4))
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }

    private void okClicked(java.awt.event.ActionEvent evt) {
        parentDialog.setVisible(false);
    }

    private void renameClicked(java.awt.event.ActionEvent evt) {
        Object selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem != null) {
            String newName = JOptionPane.showInputDialog(parentDialog, "Enter new name", "Rename", JOptionPane.QUESTION_MESSAGE);
            if (newName != null) {
                for (Iterator it = radioButtonGroups.iterator(); it.hasNext(); ) {
                    ButtonGroup buttonGroup = (ButtonGroup) it.next();
                    if (buttonGroup.getName().equals(newName)) { // name already exists
                        JOptionPane.showMessageDialog(parentDialog, "A button group with name already exists");
                        return;
                    }
                }

                // if we are here then we are safe to rename

                for (Iterator it = radioButtonGroups.iterator(); it.hasNext(); ) {
                    ButtonGroup buttonGroup = (ButtonGroup) it.next();
                    if (buttonGroup.getName().equals(selectedItem)) {
                        buttonGroup.setName(newName);

                        for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext(); ) {
                            IWidget widget = (IWidget) iter.next();
                            if (widget.getType() == IWidget.RADIO_BUTTON) {
                                RadioButtonWidget rbw = (RadioButtonWidget) widget;
                                if (rbw.getRadioButtonGroupName().equals(selectedItem)) {
                                    rbw.setRadioButtonGroupName(newName);
                                }
                            }
                        }

                        break;
                    }
                }
            }

            populateButtonGroupsList();
        }
    }

    private void removeClicked(java.awt.event.ActionEvent evt) {
        Object selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem != null) {

            if (buttonGroupsList.getModel().getSize() == 1) {
                JOptionPane.showMessageDialog(parentDialog, "You must always have at least one button group per page");
                return;
            }

            for (Iterator it = radioButtonGroups.iterator(); it.hasNext(); ) {
                ButtonGroup buttonGroup = (ButtonGroup) it.next();
                if (buttonGroup.getName().equals(selectedItem)) {
                    radioButtonGroups.remove(buttonGroup);

                    ButtonGroup lastGroup = (ButtonGroup) radioButtonGroups.get(radioButtonGroups.size() - 1);
                    for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext(); ) {
                        RadioButtonWidget widget = (RadioButtonWidget) iter.next();
                        if (widget.getRadioButtonGroupName().equals(selectedItem)) {
                            widget.setRadioButtonGroupName(lastGroup.getName());

                            Element objectProperties = widget.getProperties().getDocumentElement();
                            Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");
                            defaultElement.getAttributeNode("value").setValue("Off");
                            widget.setObjectProperties(objectProperties);
                        }
                    }

                    break;
                }
            }

            populateButtonGroupsList();
        }
    }

    private void addClicked(java.awt.event.ActionEvent evt) {
        ButtonGroup newButtonGroup = new ButtonGroup(type);
        String newGroupName = newButtonGroup.getName();

        for (Iterator it = radioButtonGroups.iterator(); it.hasNext(); ) {
            ButtonGroup buttonGroup = (ButtonGroup) it.next();
            if (buttonGroup.getName().equals(newGroupName)) {
                int number = Integer.parseInt(newGroupName.charAt(newGroupName.length() - 1) + "");

                newGroupName = newGroupName.replaceAll(number + "", (number + 1) + "");
                newButtonGroup.setName(newGroupName);

                break;
            }
        }

        radioButtonGroups.add(newButtonGroup);

        populateButtonGroupsList();
    }

    private javax.swing.JList buttonGroupsList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;

}
