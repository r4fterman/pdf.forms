package org.pdf.forms.gui.properties.object.field;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.windows.RadioButtonGroupOrganiser;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;

public class RadioButtonFieldPanel extends JPanel {

    private static final String[] APPEARANCES = {
            "None",
            "Underline",
            "Solid",
            "Sunken Box",
            "Custom..."
    };

    private final IDesigner designerPanel;

    private Set<IWidget> widgets;
    private List<ButtonGroup> buttonGroups;
    private JComboBox<String> buttonGroupBox;

    public RadioButtonFieldPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initComponents();
    }

    private void initComponents() {
        final JComboBox<String> appearanceBox = new JComboBox<>(APPEARANCES);
        appearanceBox.setEnabled(false);

        final JLabel appearanceLabel = new JLabel("Appearance:");
        appearanceLabel.setEnabled(false);

        final JLabel radioButtonGroupLabel = new JLabel("Radio Button Group:");

        buttonGroupBox = new JComboBox<>();
        buttonGroupBox.addActionListener(this::updateButtonGroup);

        final JButton organiseButton = new JButton("Organise");
        organiseButton.addActionListener(this::organiseClicked);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(appearanceLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(appearanceBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        190,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(radioButtonGroupLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(organiseButton)
                                                        .add(buttonGroupBox,
                                                                0,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(appearanceLabel)
                                        .add(appearanceBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(radioButtonGroupLabel)
                                        .add(buttonGroupBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(organiseButton)
                                .addContainerGap(214, Short.MAX_VALUE))
        );
    }

    private void organiseClicked(final ActionEvent event) {
        final IMainFrame mainFrame = designerPanel.getMainFrame();
        final List<IWidget> widgets = mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()).getWidgets();

        final JDialog dialog = new JDialog();
        final RadioButtonGroupOrganiser buttonGroupOrganiser = new RadioButtonGroupOrganiser(dialog,
                buttonGroups,
                widgets);

        dialog.getContentPane().add(buttonGroupOrganiser);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo((JFrame) mainFrame);
        dialog.setVisible(true);

        populateButtonGroups();

        setProperties(this.widgets);
    }

    private void updateButtonGroup(final ActionEvent event) {
        widgets.stream()
                .map(widget -> (RadioButtonWidget) widget)
                .forEach(radioButtonWidget -> {
                    radioButtonWidget.setRadioButtonGroupName((String) buttonGroupBox.getSelectedItem());
                    radioButtonWidget.getWidgetModel().getProperties().getObject().getValue().setDefault("Off");
                });

        designerPanel.repaint();
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final IMainFrame mainFrame = designerPanel.getMainFrame();
        final Page page = mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage());
        this.buttonGroups = page.getRadioButtonGroups();

        populateButtonGroups();

        final String buttonGroupToUse = getButtonGroupToUse(widgets);
        if (buttonGroupToUse.equals("mixed")) {
            setComboValue(buttonGroupBox, null);
        } else {
            setComboValue(buttonGroupBox, buttonGroupToUse);
        }
    }

    private String getButtonGroupToUse(final Set<IWidget> widgets) {
        final List<String> buttonGroupValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getField().getGroupName().orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(buttonGroupValues);
    }

    private void populateButtonGroups() {
        final ActionListener[] listeners = buttonGroupBox.getActionListeners();
        Arrays.stream(listeners).forEach(buttonGroupBox::removeActionListener);

        buttonGroupBox.removeAllItems();
        for (final ButtonGroup buttonGroup: buttonGroups) {
            buttonGroupBox.addItem(buttonGroup.getName());
        }

        Arrays.stream(listeners).forEach(buttonGroupBox::addActionListener);
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
            final Object value) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex((Integer) value);
        } else {
            comboBox.setSelectedItem(value);
        }

        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues = Collections
                .frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

}
