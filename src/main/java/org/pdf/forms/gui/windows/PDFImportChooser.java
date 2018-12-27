/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* PDFImportChooser.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdesktop.layout.GroupLayout;

public class PDFImportChooser extends javax.swing.JDialog {

    public static final int IMPORT_NEW = 0;
    public static final int IMPORT_EXISTING = 1;

    private int importType;
    private JRadioButton importIntoExistingButton;
    private JRadioButton importIntoNewButton;

    /**
     * Creates new form PDFImportChooser.
     */
    public PDFImportChooser(final Component parent) {
        super((Frame) parent, "Import Type", true);

        initComponents();

        importIntoNewButton.setSelected(true);

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        final ButtonGroup buttonGroup1 = new ButtonGroup();
        final JLabel jLabel1 = new JLabel();
        final JPanel jPanel1 = new JPanel();
        importIntoExistingButton = new JRadioButton();
        importIntoNewButton = new JRadioButton();
        final JButton cancelButton = new JButton();
        final JButton okButton = new JButton();

        setResizable(false);
        jLabel1.setText("How would you like to import the PDF document?");

        jPanel1.setBorder(BorderFactory.createTitledBorder("Import Type"));
        buttonGroup1.add(importIntoExistingButton);
        importIntoExistingButton.setSelected(true);
        importIntoExistingButton.setText("Import PDF into existing document");
        importIntoExistingButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoExistingButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(importIntoNewButton);
        importIntoNewButton.setText("Import PDF into new document");
        importIntoNewButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoNewButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(importIntoExistingButton)
                                        .add(importIntoNewButton))
                                .addContainerGap(150, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(importIntoExistingButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(importIntoNewButton))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelClicked);

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
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(cancelButton)
                                                .addContainerGap())
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .add(136, 136, 136))
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(cancelButton)
                                        .add(okButton))
                                .addContainerGap())
        );
        pack();
    }

    private void cancelClicked(final ActionEvent evt) {
        this.importType = 2;
        setVisible(false);
    }

    private void okClicked(final ActionEvent evt) {
        if (importIntoExistingButton.isSelected()) {
            this.importType = IMPORT_EXISTING;
        } else {
            this.importType = IMPORT_NEW;
        }
        setVisible(false);
    }

    public int getImportType() {
        return importType;
    }

}
