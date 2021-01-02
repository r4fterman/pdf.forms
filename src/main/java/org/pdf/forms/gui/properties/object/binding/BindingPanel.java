package org.pdf.forms.gui.properties.object.binding;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class BindingPanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    private JTextField arrayField;
    private JTextField nameField;

    public BindingPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
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
        for (final IWidget widget: widgetsAndProperties.keySet()) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            final Element widgetProperties = widgetsAndProperties.get(widget);
            if (name != null && !name.equals("mixed")) {
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

            widget.setObjectProperties(widgetProperties);

            mainFrame.updateHierarchyPanelUI();
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        if (widgetsAndProperties.size() == 1) {
            nameField.setEnabled(true);

            final IWidget widget = widgetsAndProperties.keySet().iterator().next();

            final Element objectProperties = widgetsAndProperties.get(widget);

            /* add binding properties */
            final Element valueProperties = (Element) objectProperties.getElementsByTagName("binding").item(0);

            final String name = XMLUtils.getAttributeFromChildElement(valueProperties, "Name").orElse("");
            final String arrayNumber = XMLUtils.getAttributeFromChildElement(valueProperties, "Array Number")
                    .orElse("0");

            nameField.setText(name);
            arrayField.setText(arrayNumber);
        } else {
            nameField.setEnabled(false);
        }
    }

}
