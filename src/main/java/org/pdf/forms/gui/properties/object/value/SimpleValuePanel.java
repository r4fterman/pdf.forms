package org.pdf.forms.gui.properties.object.value;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class SimpleValuePanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;

    private JTextField defaultTextBox;
    private JLabel typeLabel;
    private JLabel emptyMessageLabel;
    private JScrollPane scrollPane;
    private JComboBox<String> typeBox;

    public SimpleValuePanel(final int type) {
        initComponents();

        if (type == IWidget.TEXT_FIELD) {
            typeLabel.setVisible(true);
            typeBox.setVisible(true);
        } else {
            typeLabel.setVisible(false);
            typeBox.setVisible(false);
        }

        emptyMessageLabel.setVisible(false);
        scrollPane.setVisible(false);
    }

    private void initComponents() {
        typeLabel = new JLabel();
        typeLabel.setText("Type:");
        typeLabel.setEnabled(false);

        typeBox = new JComboBox<>();
        typeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "User Entered - Optional", "User Entered - Recommended", "User Entered - Required", "Read Only" }));
        typeBox.setEnabled(false);

        final JLabel defaultLabel = new JLabel();
        defaultLabel.setText("Default:");

        emptyMessageLabel = new JLabel();
        emptyMessageLabel.setText("Empty Message:");

        defaultTextBox = new JTextField();
        defaultTextBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateDefaultText(evt);
            }
        });

        final JTextArea emptyMessageBox = new JTextArea();
        emptyMessageBox.setColumns(20);
        emptyMessageBox.setRows(5);
        emptyMessageBox.setEnabled(false);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(emptyMessageBox);

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
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(emptyMessageLabel)
                                                        .add(layout.createSequentialGroup()
                                                                .add(defaultLabel)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(defaultTextBox, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                                                        .add(scrollPane, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))))
                                .add(172, 172, 172))
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
                                        .add(defaultTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(emptyMessageLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(scrollPane, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(178, Short.MAX_VALUE))
        );
    }

    private void updateDefaultText(final FocusEvent event) {
        final String defaultText = defaultTextBox.getText();

        for (final Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element widgetProperties = entry.getValue();

            if (defaultText != null && !defaultText.equals("mixed")) {
                final Element defaultTextElement = XMLUtils.getPropertyElement(widgetProperties, "Default").get();
                defaultTextElement.getAttributeNode("value").setValue(defaultText);
            }

            widget.setObjectProperties(widgetProperties);
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String defaultTextToUse = null;
        for (Element objectPropertiesElement : widgetsAndProperties.values()) {
            final Element valueProperties = (Element) objectPropertiesElement.getElementsByTagName("value").item(0);

            final String defaultText = XMLUtils.getAttributeFromChildElement(valueProperties, "Default").orElse("");
            if (defaultTextToUse == null) {
                defaultTextToUse = defaultText;
            } else {
                if (!defaultTextToUse.equals(defaultText)) {
                    defaultTextToUse = "mixed";
                }
            }

            final String mixed;
            if (defaultTextToUse.equals("mixed")) {
                mixed = "mixed";
            } else {
                mixed = defaultTextToUse;
            }
            defaultTextBox.setText(mixed);
        }
    }

}
