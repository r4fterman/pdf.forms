package org.pdf.forms.gui.properties.object.value;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;

public class ValuePanel extends JPanel {

    private static final String[] TYPES = {
            "User Entered - Optional",
            "User Entered - Recommended",
            "User Entered - Required",
            "Read Only"
    };

    private final IDesigner designerPanel;

    private Set<IWidget> widgets;
    private JComboBox<String> defaultBox;

    public ValuePanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initComponents();
    }

    private void initComponents() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent evt) {
                shown();
            }
        });

        final JLabel typeLabel = new JLabel("Type:");
        typeLabel.setEnabled(false);

        final JComboBox<String> typeBox = new JComboBox<>(TYPES);
        typeBox.setEnabled(false);

        final JLabel defaultLabel = new JLabel("Default:");

        defaultBox = new JComboBox<>();
        defaultBox.addActionListener(this::updateDefaultText);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(typeLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(typeBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(defaultLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(defaultBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        175,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(typeLabel)
                                        .add(typeBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(defaultLabel)
                                        .add(defaultBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void shown() {
        setProperties(widgets);
    }

    private void updateDefaultText(final ActionEvent evt) {
        final String value = String.valueOf(defaultBox.getSelectedItem());

        final IWidget widget = widgets.iterator().next();
        final int widgetType = widget.getType();
        if (widgetType == IWidget.CHECK_BOX) {
            updateCheckBoxValue(value);
        } else if (widgetType == IWidget.RADIO_BUTTON) {
            updateRadioButtonValue(value, widget);
        } else {
            updateElementDefaultValue(widget, value);
        }

        designerPanel.repaint();
    }

    private void updateCheckBoxValue(final String value) {
        if (value.equals("null")) {
            return;
        }

        widgets.forEach(widget -> updateElementDefaultValue(widget, value));
    }

    private void updateRadioButtonValue(
            final String value,
            final IWidget firstWidget) {
        updateElementDefaultValue(firstWidget, value);

        if (value.equals("On")) {
            updateRadioButtonWidgets(firstWidget);
        }
    }

    private void updateRadioButtonWidgets(final IWidget firstWidget) {
        final String radioButtonGroupName = ((RadioButtonWidget) firstWidget).getRadioButtonGroupName();

        designerPanel.getWidgets().stream()
                .filter(widget -> widget.getType() == IWidget.RADIO_BUTTON && widget != firstWidget)
                .filter(widget -> ((RadioButtonWidget) widget).getRadioButtonGroupName().equals(radioButtonGroupName))
                .forEach(widget -> updateElementDefaultValue(widget, "Off"));
    }

    private void updateElementDefaultValue(
            final IWidget widget,
            final String value) {
        widget.getWidgetModel().getProperties().getObject().getValue().setDefault(value);
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final ActionListener[] listeners = defaultBox.getActionListeners();
        Arrays.stream(listeners).forEach(defaultBox::removeActionListener);

        defaultBox.removeAllItems();
        applyProperties(widgets);

        Arrays.stream(listeners).forEach(defaultBox::addActionListener);
    }

    private void applyProperties(final Set<IWidget> widgets) {
        final IWidget widget = widgets.iterator().next();
        final int widgetType = widget.getType();
        if (widgetType == IWidget.CHECK_BOX) {
            setupToggleWidget();
        } else if (widgetType == IWidget.RADIO_BUTTON) {
            applyRadioButtonProperties(widgets);
        } else {
            applyProperties(widgets, widget, widgetType);
        }
    }

    private void applyProperties(
            final Set<IWidget> widgets,
            final IWidget widget,
            final int widgetType) {
        if (widgets.size() == 1) {
            // only 1 widget selected
            setItemsEnabled(true);

            final ObjectProperties objectProperties = widget.getWidgetModel().getProperties().getObject();
            fillComboBox(widgetType, objectProperties);

            final String defaultValue = objectProperties.getValue().getDefault().orElse("");
            defaultBox.setSelectedItem(defaultValue);
        } else {
            setItemsEnabled(false);
        }
    }

    private void fillComboBox(
            final int widgetType,
            final ObjectProperties objectProperties) {
        if (widgetType == IWidget.COMBO_BOX) {
            defaultBox.addItem("< None >");
        }

        objectProperties.getItems().getItem().forEach(item -> {
            defaultBox.addItem(item.getValue());
        });
    }

    private void applyRadioButtonProperties(final Set<IWidget> widgetsAndProperties) {
        if (widgetsAndProperties.size() == 1) {
            setItemsEnabled(true);
            setupToggleWidget();
        } else {
            setItemsEnabled(false);
        }
    }

    private void setupToggleWidget() {
        defaultBox.addItem("On");
        defaultBox.addItem("Off");

        final String defaultState = getDefaultState(widgets);
        if (defaultState.equals("mixed")) {
            defaultBox.setSelectedItem(null);
        } else {
            defaultBox.setSelectedItem(defaultState);
        }
    }

    private String getDefaultState(final Set<IWidget> widgets) {
        final List<String> values = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getValue().getDefault().orElse(""))
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections.frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

    private void setItemsEnabled(final boolean enabled) {
        defaultBox.setEnabled(enabled);
    }

}
