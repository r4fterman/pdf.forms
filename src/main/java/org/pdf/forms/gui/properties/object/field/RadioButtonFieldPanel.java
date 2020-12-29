package org.pdf.forms.gui.properties.object.field;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
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

public class RadioButtonFieldPanel extends JPanel {

    private static final String[] APPEARANCES = {
            "None",
            "Underline",
            "Solid",
            "Sunken Box",
            "Custom..."
    };

    private Map<IWidget, Element> widgetsAndProperties;

    private IDesigner designerPanel;
    private List<ButtonGroup> buttonGroups;
    private int type;

    private JComboBox<String> buttonGroupBox;

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
        final JComboBox<String> appearanceBox = new JComboBox<>();
        appearanceBox.setModel(new DefaultComboBoxModel<>(APPEARANCES));
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
        final RadioButtonGroupOrganiser radioButtonGroupOrganiser = new RadioButtonGroupOrganiser(dialog,
                buttonGroups,
                widgets,
                type);

        dialog.getContentPane().add(radioButtonGroupOrganiser);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo((JFrame) mainFrame);
        dialog.setVisible(true);

        populateButtonGroups();

        setProperties(widgetsAndProperties);
    }

    private void updateButtonGroup(final ActionEvent event) {
        for (final IWidget widget: widgetsAndProperties.keySet()) {
            if (type == IWidget.RADIO_BUTTON) {
                final RadioButtonWidget radioButtonWidget = (RadioButtonWidget) widget;
                radioButtonWidget.setRadioButtonGroupName((String) buttonGroupBox.getSelectedItem());
            } else {
                final CheckBoxWidget checkBoxWidget = (CheckBoxWidget) widget;
                checkBoxWidget.setCheckBoxGroupName((String) buttonGroupBox.getSelectedItem());
            }

            final Element objectProperties = widget.getProperties().getDocumentElement();
            XMLUtils.getPropertyElement(objectProperties, "Default")
                    .ifPresent(element -> {
                        element.getAttributeNode("value").setValue("Off");
                        widget.setObjectProperties(objectProperties);
                    });
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final IMainFrame mainFrame = designerPanel.getMainFrame();
        final Page page = mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage());
        switch (type) {
            case IWidget.RADIO_BUTTON:
                this.buttonGroups = page.getRadioButtonGroups();
                break;
            case IWidget.CHECK_BOX:
                this.buttonGroups = page.getCheckBoxGroups();
                break;
            default:
                break;
        }

        populateButtonGroups();

        final String buttonGroupToUse = getButtonGroupToUse(widgetsAndProperties.values());
        if (buttonGroupToUse.equals("mixed")) {
            setComboValue(buttonGroupBox, null);
        } else {
            setComboValue(buttonGroupBox, buttonGroupToUse);
        }
    }

    private String getButtonGroupToUse(final Collection<Element> elementValues) {
        final List<String> buttonGroupValues = elementValues.stream()
                .map(element -> {
                    final Element fieldProperties = (Element) element.getElementsByTagName("field").item(0);
                    return XMLUtils.getAttributeFromChildElement(fieldProperties, "Group Name").orElse("");
                })
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(buttonGroupValues, buttonGroupValues.get(0)) == buttonGroupValues.size();
        if (listContainsOnlyEqualValues) {
            return buttonGroupValues.get(0);
        }
        return "mixed";
    }

    private void populateButtonGroups() {
        final ActionListener listener = buttonGroupBox.getActionListeners()[0];
        buttonGroupBox.removeActionListener(listener);

        buttonGroupBox.removeAllItems();
        for (final ButtonGroup buttonGroup: buttonGroups) {
            buttonGroupBox.addItem(buttonGroup.getName());
        }

        buttonGroupBox.addActionListener(listener);
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
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

}
