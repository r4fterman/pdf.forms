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
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class RadioButtonGroupOrganiser extends JPanel {

    private final List<ButtonGroup> radioButtonGroups;
    private final JDialog parentDialog;
    private final List<IWidget> widgetsOnPage;

    private JList<String> buttonGroupsList;

    public RadioButtonGroupOrganiser(
            final JDialog parentDialog,
            final List<ButtonGroup> radioButtonGroups,
            final List<IWidget> widgetsOnPage) {
        this.parentDialog = parentDialog;
        this.radioButtonGroups = radioButtonGroups;
        this.widgetsOnPage = widgetsOnPage;

        initComponents();

        populateButtonGroupsList();
    }

    private void initComponents() {
        final JLabel label = new JLabel("Radio Button Groups");

        buttonGroupsList = new JList<>();
        buttonGroupsList.setModel(new DefaultListModel<>());

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

        final boolean newNameAlreadyExists = radioButtonGroups.stream()
                .anyMatch(buttonGroup -> buttonGroup.getName().equals(newName));
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
        radioButtonGroups.stream()
                .filter(buttonGroup -> buttonGroup.getName().equals(oldName))
                .forEach(buttonGroup -> buttonGroup.setName(newName));

        setButtonGroupName(oldName, newName);
    }

    private void setButtonGroupName(
            final String oldButtonGroupName,
            final String newButtonGroupName) {
        widgetsOnPage.stream()
                .filter(widget -> widget instanceof RadioButtonWidget)
                .map(widget -> (RadioButtonWidget) widget)
                .filter(widget -> widget.getRadioButtonGroupName().equals(oldButtonGroupName))
                .forEach(widget -> widget.setRadioButtonGroupName(newButtonGroupName));
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
        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            if (buttonGroup.getName().equals(buttonGroupName)) {
                radioButtonGroups.remove(buttonGroup);

                final ButtonGroup lastGroup = radioButtonGroups.get(radioButtonGroups.size() - 1);
                switchButtonOff(buttonGroupName, lastGroup.getName());

                break;
            }
        }
    }

    private void switchButtonOff(
            final String buttonGroupName,
            final String buttonGroupToSelect) {
        widgetsOnPage.stream()
                .filter(widget -> widget instanceof RadioButtonWidget)
                .map(widget -> (RadioButtonWidget) widget)
                .filter(widget -> widget.getRadioButtonGroupName().equals(buttonGroupName))
                .forEach(widget -> {
                    widget.setRadioButtonGroupName(buttonGroupToSelect);

                    final Element objectProperties = widget.getProperties().getDocumentElement();
                    XMLUtils.getPropertyElement(objectProperties, "Default")
                            .ifPresent(defaultElement -> {
                                defaultElement.getAttributeNode("value").setValue("Off");
                                widget.setObjectProperties();
                            });
                });
    }

    private void addClicked(final ActionEvent evt) {
        final ButtonGroup newButtonGroup = new ButtonGroup(IWidget.RADIO_BUTTON);
        newButtonGroup.setName(createNonExistingButtonGroupName(newButtonGroup.getName()));
        radioButtonGroups.add(newButtonGroup);

        populateButtonGroupsList();
    }

    private String createNonExistingButtonGroupName(final String buttonGroupName) {
        for (final ButtonGroup buttonGroup: radioButtonGroups) {
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

        radioButtonGroups.forEach(buttonGroup -> model.addElement(buttonGroup.getName()));
    }
}
