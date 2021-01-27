package org.pdf.forms.gui.properties.object.value;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.widgets.IWidget;

public class SimpleValuePanel extends JPanel {

    private static final String[] TYPES = {
            "User Entered - Optional",
            "User Entered - Recommended",
            "User Entered - Required",
            "Read Only"
    };

    private Set<IWidget> widgets;

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
        typeLabel = new JLabel("Type:");
        typeLabel.setEnabled(false);

        typeBox = new JComboBox<>(TYPES);
        typeBox.setEnabled(false);

        final JLabel defaultLabel = new JLabel("Default:");

        emptyMessageLabel = new JLabel("Empty Message:");

        defaultTextBox = new JTextField();
        defaultTextBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateDefaultText();
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
                                                                .add(defaultTextBox,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        167,
                                                                        Short.MAX_VALUE))
                                                        .add(scrollPane,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                218,
                                                                GroupLayout.PREFERRED_SIZE))))
                                .add(172, 172, 172))
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
                                        .add(defaultTextBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(emptyMessageLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(scrollPane, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(178, Short.MAX_VALUE))
        );
    }

    private void updateDefaultText() {
        final String defaultText = defaultTextBox.getText();

        if (widgets != null
                && defaultText != null
                && !defaultText.equals("mixed")) {
            widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getValue()
                    .setDefault(defaultText));
        }
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final String defaultText = getDefaultText(widgets);
        defaultTextBox.setText(defaultText);
    }

    private String getDefaultText(final Set<IWidget> widgets) {
        final List<String> defaultValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getValue().getDefault().orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(defaultValues);
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final int numberOfIdenticalItems = Collections.frequency(values, values.get(0));
        final boolean listContainsOnlyEqualValues = numberOfIdenticalItems == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

}
