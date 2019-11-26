/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
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

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class RadioButtonGroupOrganiser extends JPanel {

    private final List<ButtonGroup> radioButtonGroups;
    private final JDialog parentDialog;
    private final List<IWidget> widgetsOnPage;
    private final int type;

    private JList<String> buttonGroupsList;

    public RadioButtonGroupOrganiser(
            final JDialog parentDialog,
            final List<ButtonGroup> radioButtonGroups,
            final List<IWidget> widgetsOnPage,
            final int type) {
        this.parentDialog = parentDialog;
        this.radioButtonGroups = radioButtonGroups;
        this.widgetsOnPage = widgetsOnPage;
        this.type = type;

        initComponents();

        populateButtonGroupsList();
    }

    private void populateButtonGroupsList() {
        final DefaultListModel<String> model = (DefaultListModel) buttonGroupsList.getModel();
        model.removeAllElements();

        for (final ButtonGroup buttonGroup : radioButtonGroups) {
            model.addElement(buttonGroup.getName());
        }
    }

    private void initComponents() {
        final JLabel jLabel1 = new JLabel();
        final JScrollPane jScrollPane1 = new JScrollPane();
        buttonGroupsList = new JList<>();
        final JButton jButton1 = new JButton();
        final JButton jButton2 = new JButton();
        final JButton jButton3 = new JButton();
        final JButton jButton4 = new JButton();

        jLabel1.setText("Radio Button Groups");

        buttonGroupsList.setModel(new DefaultListModel<>());
        jScrollPane1.setViewportView(buttonGroupsList);

        jButton1.setText("Add ...");
        jButton1.addActionListener(this::addClicked);

        jButton2.setText("Remove");
        jButton2.addActionListener(this::removeClicked);

        jButton3.setText("Rename ...");
        jButton3.addActionListener(this::renameClicked);

        jButton4.setText("OK");
        jButton4.addActionListener(this::okClicked);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jScrollPane1, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                        .add(jLabel1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(GroupLayout.TRAILING, jButton4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jButton1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton2)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton3)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 154, Short.MAX_VALUE)
                                                .add(jButton4))
                                        .add(jScrollPane1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }

    private void okClicked(final java.awt.event.ActionEvent evt) {
        parentDialog.setVisible(false);
    }

    private void renameClicked(final java.awt.event.ActionEvent evt) {
        final String selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem != null) {
            final String newName = JOptionPane.showInputDialog(parentDialog, "Enter new name", "Rename", JOptionPane.QUESTION_MESSAGE);
            if (newName != null) {
                for (final ButtonGroup buttonGroup : radioButtonGroups) {
                    if (buttonGroup.getName().equals(newName)) {
                        // name already exists
                        JOptionPane.showMessageDialog(parentDialog, "A button group with name already exists");
                        return;
                    }
                }

                // if we are here then we are safe to rename

                for (final ButtonGroup buttonGroup : radioButtonGroups) {
                    if (buttonGroup.getName().equals(selectedItem)) {
                        buttonGroup.setName(newName);

                        for (final IWidget widget : widgetsOnPage) {
                            if (widget.getType() == IWidget.RADIO_BUTTON) {
                                final RadioButtonWidget rbw = (RadioButtonWidget) widget;
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

    private void removeClicked(final java.awt.event.ActionEvent evt) {
        final String selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem != null) {

            if (buttonGroupsList.getModel().getSize() == 1) {
                JOptionPane.showMessageDialog(parentDialog, "You must always have at least one button group per page");
                return;
            }

            for (final ButtonGroup buttonGroup : radioButtonGroups) {
                if (buttonGroup.getName().equals(selectedItem)) {
                    radioButtonGroups.remove(buttonGroup);

                    final ButtonGroup lastGroup = radioButtonGroups.get(radioButtonGroups.size() - 1);
                    for (final IWidget widget1 : widgetsOnPage) {
                        final RadioButtonWidget widget = (RadioButtonWidget) widget1;
                        if (widget.getRadioButtonGroupName().equals(selectedItem)) {
                            widget.setRadioButtonGroupName(lastGroup.getName());

                            final Element objectProperties = widget.getProperties().getDocumentElement();
                            final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();
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

    private void addClicked(final java.awt.event.ActionEvent evt) {
        final ButtonGroup newButtonGroup = new ButtonGroup(type);
        String newGroupName = newButtonGroup.getName();

        for (final ButtonGroup buttonGroup : radioButtonGroups) {
            if (buttonGroup.getName().equals(newGroupName)) {
                final int number = Integer.parseInt(newGroupName.charAt(newGroupName.length() - 1) + "");

                newGroupName = newGroupName.replaceAll(number + "", (number + 1) + "");
                newButtonGroup.setName(newGroupName);

                break;
            }
        }

        radioButtonGroups.add(newButtonGroup);

        populateButtonGroupsList();
    }

}
