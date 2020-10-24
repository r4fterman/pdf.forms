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
        final DefaultListModel<String> model = (DefaultListModel<String>) buttonGroupsList.getModel();
        model.removeAllElements();

        for (final ButtonGroup buttonGroup: radioButtonGroups) {
            model.addElement(buttonGroup.getName());
        }
    }

    private void initComponents() {
        final JButton addButton = new JButton();
        final JButton removeButton = new JButton();
        final JButton renameButton = new JButton();
        final JButton okButton = new JButton();

        final JLabel label = new JLabel();
        label.setText("Radio Button Groups");

        buttonGroupsList = new JList<>();
        buttonGroupsList.setModel(new DefaultListModel<>());

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(buttonGroupsList);

        addButton.setText("Add ...");
        addButton.addActionListener(this::addClicked);

        removeButton.setText("Remove");
        removeButton.addActionListener(this::removeClicked);

        renameButton.setText("Rename ...");
        renameButton.addActionListener(this::renameClicked);

        okButton.setText("OK");
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
                setRadioButtonGroupName(selectedItem, newName);

                break;
            }
        }

        populateButtonGroupsList();
    }

    private void setRadioButtonGroupName(
            final String oldRadioButtonGroupName,
            final String newRadioButtonGroupName) {
        for (final IWidget widget: widgetsOnPage) {
            if (widget.getType() == IWidget.RADIO_BUTTON) {
                final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) widget;
                if (radioButtonWidget.getRadioButtonGroupName().equals(oldRadioButtonGroupName)) {
                    radioButtonWidget.setRadioButtonGroupName(newRadioButtonGroupName);
                }
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
                final Optional<Element> propertyElement = XMLUtils.getPropertyElement(objectProperties,
                        "Default");
                if (propertyElement.isPresent()) {
                    final Element defaultElement = propertyElement.get();
                    defaultElement.getAttributeNode("value").setValue("Off");
                    radioButtonWidget.setObjectProperties(objectProperties);
                }
            }
        }
    }

    private void addClicked(final ActionEvent evt) {
        final ButtonGroup newButtonGroup = new ButtonGroup(type);
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

}
