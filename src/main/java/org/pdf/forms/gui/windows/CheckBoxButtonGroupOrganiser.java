package org.pdf.forms.gui.windows;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.TRAILING;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;

public class CheckBoxButtonGroupOrganiser extends JPanel {

    private final List<ButtonGroup> checkBoxButtonGroups;
    private final JDialog parentDialog;
    private final List<IWidget> widgetsOnPage;

    private JList<String> buttonGroupsList;

    public CheckBoxButtonGroupOrganiser(
            final JDialog parentDialog,
            final List<ButtonGroup> checkBoxButtonGroups,
            final List<IWidget> widgetsOnPage) {
        this.parentDialog = parentDialog;
        this.checkBoxButtonGroups = checkBoxButtonGroups;
        this.widgetsOnPage = widgetsOnPage;

        initComponents();

        populateButtonGroupsList();
    }

    private void initComponents() {
        final JLabel label = new JLabel("CheckBox Button Groups");

        buttonGroupsList = new JList<>(new DefaultListModel<>());

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(buttonGroupsList);

        final JButton addButton = new JButton("Add ...");
        addButton.addActionListener(this::addClicked);

        final JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(this::removeClicked);

        final JButton renameButton = new JButton("Rename ...");
        renameButton.addActionListener(this::renameClicked);

        final JButton okButton = new JButton("OK");
        okButton.addActionListener(this::okClicked);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        final GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(LEADING)
                .add(scrollPane, DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .add(label);
        final GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(LEADING, false)
                .add(TRAILING, okButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                .add(renameButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                .add(removeButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                .add(addButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE);
        final GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup)
                .addPreferredGap(RELATED)
                .add(parallelGroup1)
                .addContainerGap();
        layout.setHorizontalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup));
        final GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup()
                .add(addButton)
                .addPreferredGap(RELATED)
                .add(removeButton)
                .addPreferredGap(RELATED)
                .add(renameButton)
                .addPreferredGap(RELATED, 154, Short.MAX_VALUE)
                .add(okButton);
        final GroupLayout.ParallelGroup parallelGroup3 = layout.createParallelGroup(LEADING)
                .add(sequentialGroup2)
                .add(scrollPane, DEFAULT_SIZE, 258, Short.MAX_VALUE);
        final GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup()
                .addContainerGap()
                .add(label)
                .addPreferredGap(RELATED)
                .add(parallelGroup3)
                .addContainerGap();
        final GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(LEADING).add(sequentialGroup1);
        layout.setVerticalGroup(parallelGroup2);
    }

    private void okClicked(final ActionEvent event) {
        parentDialog.setVisible(false);
    }

    private void renameClicked(final ActionEvent event) {
        final String selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem == null) {
            return;
        }

        askForNewButtonGroupName()
                .ifPresent(newName -> {
                    updateButtonGroupName(selectedItem, newName);
                    populateButtonGroupsList();
                });
    }

    private Optional<String> askForNewButtonGroupName() {
        final String newName = JOptionPane.showInputDialog(parentDialog,
                "Enter new name",
                "Rename",
                JOptionPane.QUESTION_MESSAGE);
        if (newName == null) {
            return Optional.empty();
        }

        final boolean newNameAlreadyExists = checkBoxButtonGroups.stream().anyMatch(buttonGroup -> buttonGroup.getName()
                .equals(newName));
        if (newNameAlreadyExists) {
            // name already exists
            JOptionPane.showMessageDialog(parentDialog, "A button group with this name already exists.");
            return Optional.empty();
        }
        return Optional.of(newName);
    }

    private void updateButtonGroupName(
            final String oldName,
            final String newName) {
        checkBoxButtonGroups.stream()
                .filter(buttonGroup -> buttonGroup.getName().equals(oldName))
                .forEach(buttonGroup -> buttonGroup.setName(newName));
    }

    private void removeClicked(final ActionEvent event) {
        final String selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem == null) {
            return;
        }

        if (buttonGroupsList.getModel().getSize() == 1) {
            JOptionPane.showMessageDialog(parentDialog, "You must always have at least one button group per page.");
            return;
        }

        removeButtonGroup(selectedItem);

        populateButtonGroupsList();
    }

    private void removeButtonGroup(final String buttonGroupName) {
        for (final ButtonGroup buttonGroup: checkBoxButtonGroups) {
            if (buttonGroup.getName().equals(buttonGroupName)) {
                checkBoxButtonGroups.remove(buttonGroup);

                final ButtonGroup lastGroup = checkBoxButtonGroups.get(checkBoxButtonGroups.size() - 1);
                switchButtonOff(buttonGroupName, lastGroup);

                break;
            }
        }
    }

    private void switchButtonOff(
            final String buttonGroupName,
            final ButtonGroup lastGroup) {
        widgetsOnPage.stream()
                .filter(widget -> widget instanceof RadioButtonWidget)
                .map(widget -> (RadioButtonWidget) widget)
                .filter(radioButtonWidget -> radioButtonWidget.getRadioButtonGroupName().equals(buttonGroupName))
                .forEach(radioButtonWidget -> switchRadioButtonOff(radioButtonWidget, lastGroup.getName()));
    }

    private void switchRadioButtonOff(
            final RadioButtonWidget radioButtonWidget,
            final String radioButtonGroupName) {
        radioButtonWidget.setRadioButtonGroupName(radioButtonGroupName);
        radioButtonWidget.getWidgetModel().getProperties().getObject().getValue().setDefault("off");
    }

    private void addClicked(final ActionEvent evt) {
        final ButtonGroup newButtonGroup = new ButtonGroup(IWidget.CHECK_BOX);
        newButtonGroup.setName(createNonExistingButtonGroupName(newButtonGroup.getName()));
        checkBoxButtonGroups.add(newButtonGroup);

        populateButtonGroupsList();
    }

    private String createNonExistingButtonGroupName(final String buttonGroupName) {
        for (final ButtonGroup buttonGroup: checkBoxButtonGroups) {
            if (buttonGroup.getName().equals(buttonGroupName)) {
                final char c = buttonGroupName.charAt(buttonGroupName.length() - 1);
                final int number = Integer.parseInt(String.valueOf(c));
                return buttonGroupName.replaceAll(String.valueOf(number), String.valueOf(number + 1));
            }
        }
        return buttonGroupName;
    }

    private void populateButtonGroupsList() {
        final DefaultListModel<String> model = (DefaultListModel<String>) buttonGroupsList.getModel();
        model.removeAllElements();

        checkBoxButtonGroups.forEach(buttonGroup -> model.addElement(buttonGroup.getName()));
    }
}
