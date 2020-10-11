package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import javax.swing.*;

public class MarginPanel extends JPanel {

    public MarginPanel() {
        initializePanel();
    }

    private void initializePanel() {
        final org.jdesktop.layout.GroupLayout groupLayout = new org.jdesktop.layout.GroupLayout(this);
        setLayout(groupLayout);
        setBorder(BorderFactory.createTitledBorder("Margins"));

        final JLabel leftLabel = createDisabledLabel("Left:");
        final JLabel rightLabel = createDisabledLabel("Right");
        final JLabel topLabel = createDisabledLabel("Top:");
        final JLabel bottomLabel = createDisabledLabel("Bottom:");

        final JTextField leftMarginBox = createDisabledTextField();
        final JTextField rightMarginBox = createDisabledTextField();
        final JTextField topMarginBox = createDisabledTextField();
        final JTextField bottomMarginBox = createDisabledTextField();

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(leftLabel)
                                        .add(rightLabel))
                                .add(26, 26, 26)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(leftMarginBox, PREFERRED_SIZE, 55, PREFERRED_SIZE)
                                        .add(rightMarginBox, PREFERRED_SIZE, 55, PREFERRED_SIZE))
                                .add(29, 29, 29)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(topLabel)
                                        .add(bottomLabel))
                                .add(19, 19, 19)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(topMarginBox, PREFERRED_SIZE, 55, PREFERRED_SIZE)
                                        .add(bottomMarginBox, PREFERRED_SIZE, 55, PREFERRED_SIZE)))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(BASELINE)
                                        .add(leftLabel)
                                        .add(leftMarginBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .add(topLabel)
                                        .add(topMarginBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addPreferredGap(RELATED)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(groupLayout.createParallelGroup(BASELINE)
                                                .add(bottomLabel)
                                                .add(bottomMarginBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                        .add(rightLabel)
                                        .add(rightMarginBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addContainerGap())
        );
    }

    private JLabel createDisabledLabel(final String labelText) {
        final JLabel label = new JLabel();
        label.setText(labelText);
        label.setEnabled(false);
        return label;
    }

    private JTextField createDisabledTextField() {
        final JTextField textField = new JTextField();
        textField.setEnabled(false);
        return textField;
    }
}
