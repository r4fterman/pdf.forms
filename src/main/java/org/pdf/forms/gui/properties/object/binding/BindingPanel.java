package org.pdf.forms.gui.properties.object.binding;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.widgets.IWidget;

public class BindingPanel extends JPanel {

    private final IMainFrame mainFrame;

    private Set<IWidget> widgets;
    private JTextField arrayField;
    private JTextField nameField;

    public BindingPanel(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initComponents();
    }

    private void initComponents() {
        final JLabel nameLabel = new JLabel("Name:");

        nameField = new JTextField();
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateName();
            }
        });

        arrayField = new JTextField();
        arrayField.setEnabled(false);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(nameLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(nameField, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(arrayField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(162, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(nameLabel)
                                        .add(nameField,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .add(arrayField,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(268, Short.MAX_VALUE))
        );
    }

    private void updateName() {
        if (widgets == null) {
            return;
        }

        final String name = nameField.getText();
        widgets.forEach(widget -> {
            saveNameToModel(name, widget);

            mainFrame.updateHierarchyPanelUI();
        });
    }

    private void saveNameToModel(
            final String name,
            final IWidget widget) {
        if (name == null || name.equals("mixed")) {
            return;
        }

        final String oldName = widget.getWidgetName();
        if (!oldName.equals(name)) {
            mainFrame.renameWidget(oldName, name, widget);
        }

        final BindingProperties bindingProperties = widget.getWidgetModel().getProperties().getObject().getBinding();
        bindingProperties.setName(name);

        final int arrayNumber = mainFrame.getNextArrayNumberForName(name, widget);
        bindingProperties.setArrayNumber(String.valueOf(arrayNumber));
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final boolean onlySingleWidgetSelected = widgets.size() == 1;
        nameField.setEnabled(onlySingleWidgetSelected);

        if (onlySingleWidgetSelected) {
            final IWidget widget = widgets.iterator().next();
            final BindingProperties bindingProperties = widget.getWidgetModel().getProperties().getObject()
                    .getBinding();

            nameField.setText(bindingProperties.getName().orElse(""));
            arrayField.setText(bindingProperties.getArrayNumber().orElse("0"));
        }
    }
}
