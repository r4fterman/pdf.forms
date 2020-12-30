package org.pdf.forms.gui.properties.object.value;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class ValuePanel extends JPanel {

    private static final String[] TYPES = {
            "User Entered - Optional",
            "User Entered - Recommended",
            "User Entered - Required",
            "Read Only"
    };

    private final IDesigner designerPanel;

    private Map<IWidget, Element> widgetsAndProperties;
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
        setProperties(widgetsAndProperties);
    }

    private void updateDefaultText(final ActionEvent evt) {
        final String selectedItem = String.valueOf(defaultBox.getSelectedItem());

        final IWidget firstWidget = widgetsAndProperties.keySet().iterator().next();
        switch (firstWidget.getType()) {
            case IWidget.CHECK_BOX:
                if (selectedItem.equals("null")) {
                    break;
                }

                for (final IWidget widget: widgetsAndProperties.keySet()) {
                    final Element objectProperties = widgetsAndProperties.get(widget);
                    final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();
                    defaultElement.getAttributeNode("value").setValue(selectedItem);
                    widget.setObjectProperties(objectProperties);
                }

                break;
            case IWidget.RADIO_BUTTON:
                Element objectProperties = widgetsAndProperties.get(firstWidget);
                Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();
                defaultElement.getAttributeNode("value").setValue(selectedItem);
                firstWidget.setObjectProperties(objectProperties);

                if (selectedItem.equals("On")) {
                    final List<IWidget> allwidgets = designerPanel.getWidgets();
                    // todo flattern to account for groups
                    for (final IWidget widget: allwidgets) {
                        if (widget.getType() == IWidget.RADIO_BUTTON && widget != firstWidget) {
                            final RadioButtonWidget rbw = (RadioButtonWidget) widget;
                            if (rbw.getRadioButtonGroupName().equals(((RadioButtonWidget) firstWidget)
                                    .getRadioButtonGroupName())) {
                                objectProperties = rbw.getProperties().getDocumentElement();
                                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();
                                defaultElement.getAttributeNode("value").setValue("Off");
                                rbw.setObjectProperties(objectProperties);
                            }
                        }
                    }
                }

                break;
            default:
                objectProperties = widgetsAndProperties.get(firstWidget);
                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();
                defaultElement.getAttributeNode("value").setValue(selectedItem);
                firstWidget.setObjectProperties(objectProperties);

                break;
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final IWidget testWidget = widgetsAndProperties.keySet().iterator().next();

        final ActionListener listener = defaultBox.getActionListeners()[0];
        defaultBox.removeActionListener(listener);

        defaultBox.removeAllItems();

        final int type = testWidget.getType();
        switch (type) {
            case IWidget.CHECK_BOX:
                setupToggleWidget();
                break;
            case IWidget.RADIO_BUTTON:
                if (widgetsAndProperties.size() == 1) {
                    setItemsEnabled(true);
                    setupToggleWidget();
                } else {
                    setItemsEnabled(false);
                }

                break;
            default:
                if (widgetsAndProperties.size() == 1) {
                    // only 1 widget selected
                    setItemsEnabled(true);

                    final Element objectProperties = widgetsAndProperties.get(testWidget);
                    if (widgetsAndProperties.size() == 1) {
                        if (type == IWidget.COMBO_BOX) {
                            defaultBox.addItem("< None >");
                        }

                        final Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);
                        if (itemElement != null) {
                            final List<Element> items = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());
                            for (final Element item: items) {
                                final String value = XMLUtils.getAttributeFromElement(item, "item").orElse("");
                                defaultBox.addItem(value);
                            }
                        }
                    }

                    final String defaultText = getDefault(objectProperties);
                    defaultBox.setSelectedItem(defaultText);
                } else {
                    setItemsEnabled(false);
                }
                break;
        }

        defaultBox.addActionListener(listener);
    }

    private void setupToggleWidget() {
        String defaultStateToUse = null;

        defaultBox.addItem("On");
        defaultBox.addItem("Off");

        for (final Element objectProperties: widgetsAndProperties.values()) {
            final String defaultState = getDefault(objectProperties);
            if (defaultStateToUse == null) {
                // this must be the first time round
                defaultStateToUse = defaultState;
            } else {
                // check for subsequent widgets
                if (!defaultStateToUse.equals(defaultState)) {
                    defaultStateToUse = "mixed";
                }
            }
        }

        final Object selectedItem;
        if ("mixed".equals(defaultStateToUse)) {
            selectedItem = null;
        } else {
            selectedItem = defaultStateToUse;
        }
        defaultBox.setSelectedItem(selectedItem);
    }

    private String getDefault(final Element objectProperties) {
        final Element valueProperties = (Element) objectProperties.getElementsByTagName("value").item(0);
        return XMLUtils.getAttributeFromChildElement(valueProperties, "Default").orElse("");
    }

    private void setItemsEnabled(final boolean enabled) {
        defaultBox.setEnabled(enabled);
    }

}
