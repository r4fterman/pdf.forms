package org.pdf.forms.gui.properties.object.binding;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BindingPanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;
    private final IMainFrame mainFrame;

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
        if (widgetsAndProperties == null) {
            return;
        }

        final String name = nameField.getText();
        for (final Map.Entry<IWidget, Element> entry: widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element widgetProperties = entry.getValue();

            saveNameToModel(name, widget, widgetProperties);
            widget.setObjectProperties(widgetProperties);

            mainFrame.updateHierarchyPanelUI();
        }
    }

    private void saveNameToModel(
            final String name,
            final IWidget widget,
            final Element widgetProperties) {
        if (name == null || name.equals("mixed")) {
            return;
        }

        final String oldName = widget.getWidgetName();
        if (!oldName.equals(name)) {
            mainFrame.renameWidget(oldName, name, widget);
        }

        XMLUtils.getPropertyElement(widgetProperties, "Name")
                .ifPresent(nameElement -> nameElement.getAttributeNode("value").setValue(name));

        final int arrayNumber = mainFrame.getNextArrayNumberForName(name, widget);
        XMLUtils.getPropertyElement(widgetProperties, "Array Number")
                .ifPresent(arrayNumberElement -> arrayNumberElement.getAttributeNode("value")
                        .setValue(String.valueOf(arrayNumber)));
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final boolean onlySingleWidgetSelected = widgetsAndProperties.size() == 1;
        nameField.setEnabled(onlySingleWidgetSelected);

        if (onlySingleWidgetSelected) {
            final Element objectProperties = widgetsAndProperties.entrySet().iterator().next().getValue();
            final Element valueProperties = (Element) objectProperties.getElementsByTagName("binding").item(0);
            final String name = XMLUtils.getAttributeValueFromChildElement(valueProperties, "Name")
                    .orElse("");
            final String arrayNumber = XMLUtils.getAttributeValueFromChildElement(valueProperties, "Array Number")
                    .orElse("0");

            nameField.setText(name);
            arrayField.setText(arrayNumber);
        }
    }

}
