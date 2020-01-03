package org.pdf.forms.gui.properties.object.value;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.w3c.dom.Element;

public class ValuePanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;

    private IDesigner designerPanel;
    private JComboBox<String> defaultBox;

    public ValuePanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent evt) {
                shown(evt);
            }
        });

        final JLabel typeLabel = new JLabel();
        typeLabel.setText("Type:");
        typeLabel.setEnabled(false);

        final JComboBox<String> typeBox = new JComboBox<>();
        typeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only" }));
        typeBox.setEnabled(false);

        final JLabel defaultLabel = new JLabel();
        defaultLabel.setText("Default:");

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
                                                .add(defaultBox, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(typeLabel)
                                        .add(typeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(defaultLabel)
                                        .add(defaultBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void shown(final ComponentEvent evt) {
        setProperties(widgetsAndProperties);
    }

    private void updateDefaultText(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final IWidget testWidget = widgets.iterator().next();

        final String selectedItem = String.valueOf(defaultBox.getSelectedItem());

        switch (testWidget.getType()) {
            case IWidget.CHECK_BOX:
                if (selectedItem.equals("null")) {
                    break;
                }

                for (final IWidget widget : widgetsAndProperties.keySet()) {
                    final Element objectProperties = widgetsAndProperties.get(widget);

                    final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

                    defaultElement.getAttributeNode("value").setValue(selectedItem);

                    widget.setObjectProperties(objectProperties);
                }

                break;
            case IWidget.RADIO_BUTTON:
                Element objectProperties = widgetsAndProperties.get(testWidget);

                Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

                if (selectedItem.equals("On")) {
                    final List<IWidget> allwidgets = designerPanel.getWidgets();
                    // todo flattern to account for groups
                    for (final IWidget widget : allwidgets) {
                        if (widget.getType() == IWidget.RADIO_BUTTON && widget != testWidget) {
                            final RadioButtonWidget rbw = (RadioButtonWidget) widget;
                            if (rbw.getRadioButtonGroupName().equals(((RadioButtonWidget) testWidget).getRadioButtonGroupName())) {
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
                objectProperties = widgetsAndProperties.get(testWidget);

                defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

                defaultElement.getAttributeNode("value").setValue(selectedItem);

                testWidget.setObjectProperties(objectProperties);

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

                    /* add field properties */

                    //                    if (widgetsAndProperties.size() == 1) { // only 1 widget is currently selected

                    //                    List objectProperties = XMLUtils.getElementsFromNodeList(
                    //                            ((Element) widgetsAndProperties.get(testWidget)).getChildNodes());

                    if (widgetsAndProperties.size() == 1) {
                        final Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);
                        final List<Element> items = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());
                        if (type == IWidget.COMBO_BOX) {
                            defaultBox.addItem("< None >");
                        }

                        for (final Element item : items) {
                            final String value = XMLUtils.getAttributeFromElement(item, "item").orElse("");
                            defaultBox.addItem(value);
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

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectProperties = widgetsAndProperties.get(widget);

            final String defaultState = getDefault(objectProperties);

            if (defaultStateToUse == null) { // this must be the first time round
                defaultStateToUse = defaultState;

            } else { // check for subsequent widgets

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

    private void setItemsEnabled(final boolean b) {
        defaultBox.setEnabled(b);
    }

}
