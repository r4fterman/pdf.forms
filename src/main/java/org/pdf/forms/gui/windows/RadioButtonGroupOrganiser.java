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

        final String newName = JOptionPane.showInputDialog(parentDialog,
                "Enter new name",
                "Rename",
                JOptionPane.QUESTION_MESSAGE);
        if (newName == null) {
            return;
        }

        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            if (buttonGroup.getName().equals(newName)) {
                // name already exists
                JOptionPane.showMessageDialog(parentDialog, "A button group with name already exists");
                return;
            }
        }

        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            if (buttonGroup.getName().equals(selectedItem)) {
                buttonGroup.setName(newName);
                setButtonGroupName(selectedItem, newName);

                break;
            }
        }

        populateButtonGroupsList();
    }

    private void setButtonGroupName(
            final String oldButtonGroupName,
            final String newButtonGroupName) {
        for (final IWidget widget: widgetsOnPage) {
            final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) widget;
            if (radioButtonWidget.getRadioButtonGroupName().equals(oldButtonGroupName)) {
                radioButtonWidget.setRadioButtonGroupName(newButtonGroupName);
            }
        }
    }

    private void removeClicked(final ActionEvent event) {
        final String selectedItem = buttonGroupsList.getSelectedValue();
        if (selectedItem == null) {
            return;
        }

        if (buttonGroupsList.getModel().getSize() == 1) {
            JOptionPane.showMessageDialog(parentDialog, "You must always have at least one button group per page");
            return;
        }

        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            if (buttonGroup.getName().equals(selectedItem)) {
                radioButtonGroups.remove(buttonGroup);

                final ButtonGroup lastGroup = radioButtonGroups.get(radioButtonGroups.size() - 1);
                switchButtonOff(selectedItem, lastGroup);

                break;
            }
        }

        populateButtonGroupsList();
    }

    private void switchButtonOff(
            final String selectedItem,
            final ButtonGroup lastGroup) {
        for (final IWidget widgetOnPage: widgetsOnPage) {
            final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) widgetOnPage;
            if (radioButtonWidget.getRadioButtonGroupName().equals(selectedItem)) {
                radioButtonWidget.setRadioButtonGroupName(lastGroup.getName());

                final Element objectProperties = radioButtonWidget.getProperties().getDocumentElement();
                final Optional<Element> propertyElement = XMLUtils
                        .getPropertyElement(objectProperties, "Default");
                if (propertyElement.isPresent()) {
                    final Element defaultElement = propertyElement.get();
                    defaultElement.getAttributeNode("value").setValue("Off");
                    radioButtonWidget.setObjectProperties(objectProperties);
                }
            }
        }
    }

    private void addClicked(final ActionEvent evt) {
        final ButtonGroup newButtonGroup = new ButtonGroup(IWidget.RADIO_BUTTON);
        String newGroupName = newButtonGroup.getName();
        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            if (buttonGroup.getName().equals(newGroupName)) {
                final char c = newGroupName.charAt(newGroupName.length() - 1);
                final int number = Integer.parseInt(String.valueOf(c));

                newGroupName = newGroupName.replaceAll(String.valueOf(number), String.valueOf(number + 1));
                newButtonGroup.setName(newGroupName);

                break;
            }
        }

        radioButtonGroups.add(newButtonGroup);

        populateButtonGroupsList();
    }

    private void populateButtonGroupsList() {
        final DefaultListModel<String> model = (DefaultListModel<String>) buttonGroupsList.getModel();
        model.removeAllElements();

        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            model.addElement(buttonGroup.getName());
        }
    }
}
