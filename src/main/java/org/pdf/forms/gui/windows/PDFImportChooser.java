package org.pdf.forms.gui.windows;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

public class PDFImportChooser extends JDialog {

    public static final int IMPORT_NEW = 0;
    public static final int IMPORT_EXISTING = 1;

    private int importType;
    private JRadioButton importIntoExistingButton;
    private JRadioButton importIntoNewButton;

    public PDFImportChooser(final Component parent) {
        super((Frame) parent, "Import Type", true);

        setResizable(false);
        initComponents();
        pack();

        importIntoNewButton.setSelected(true);

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        final JLabel questionLabel = new JLabel();
        questionLabel.setText("How would you like to import the PDF document?");

        final JPanel importTypePanel = new JPanel();
        importTypePanel.setBorder(BorderFactory.createTitledBorder("Import Type"));

        importIntoExistingButton = new JRadioButton();
        importIntoExistingButton.setSelected(true);
        importIntoExistingButton.setText("Import PDF into existing document");
        importIntoExistingButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoExistingButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        importIntoNewButton = new JRadioButton();
        importIntoNewButton.setText("Import PDF into new document");
        importIntoNewButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoNewButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(importIntoExistingButton);
        buttonGroup.add(importIntoNewButton);

        final GroupLayout groupLayout = new GroupLayout(importTypePanel);
        importTypePanel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(importIntoExistingButton)
                                        .add(importIntoNewButton))
                                .addContainerGap(150, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(importIntoExistingButton)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(importIntoNewButton))
        );

        final JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelClicked);

        final JButton okButton = new JButton();
        okButton.setText("OK");
        okButton.addActionListener(this::okClicked);

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(okButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(cancelButton)
                                                .addContainerGap())
                                        .add(layout.createSequentialGroup()
                                                .add(questionLabel)
                                                .add(136, 136, 136))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(importTypePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(questionLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(importTypePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(cancelButton)
                                        .add(okButton))
                                .addContainerGap())
        );
    }

    private void cancelClicked(final ActionEvent event) {
        importType = 2;
        setVisible(false);
    }

    private void okClicked(final ActionEvent event) {
        if (importIntoExistingButton.isSelected()) {
            importType = IMPORT_EXISTING;
        } else {
            importType = IMPORT_NEW;
        }
        setVisible(false);
    }

    public int getImportType() {
        return importType;
    }

}
