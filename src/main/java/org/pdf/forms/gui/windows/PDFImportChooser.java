/**
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
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

public class PDFImportChooser extends javax.swing.JDialog {

    public static int IMPORT_NEW = 0;
    public static int IMPORT_EXISTING = 1;
    public static int IMPORT_CANCELED = 2;

    private int importType;

    /**
     * Creates new form PDFImportChooser
     */
    public PDFImportChooser(Component parent) {
		super((Frame) parent, "Import Type", true);

        initComponents();

        importIntoNewButton.setSelected(true);
        
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        importIntoExistingButton = new javax.swing.JRadioButton();
        importIntoNewButton = new javax.swing.JRadioButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setResizable(false);
        jLabel1.setText("How would you like to import the PDF document?");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Import Type"));
        buttonGroup1.add(importIntoExistingButton);
        importIntoExistingButton.setSelected(true);
        importIntoExistingButton.setText("Import PDF into existing document");
        importIntoExistingButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoExistingButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(importIntoNewButton);
        importIntoNewButton.setText("Import PDF into new document");
        importIntoNewButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        importIntoNewButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(importIntoExistingButton)
                                .add(importIntoNewButton))
                        .addContainerGap(150, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                        .add(importIntoExistingButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(importIntoNewButton))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelClicked(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(cancelButton)
                                .addContainerGap())
                        .add(layout.createSequentialGroup()
                                .add(jLabel1)
                                .add(136, 136, 136))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 17, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(cancelButton)
                                .add(okButton))
                        .addContainerGap())
        );
        pack();
    }

    private void cancelClicked(java.awt.event.ActionEvent evt) {
        importType = IMPORT_CANCELED;
        setVisible(false);
    }

    private void okClicked(java.awt.event.ActionEvent evt) {
        importType = importIntoExistingButton.isSelected() ? IMPORT_EXISTING : IMPORT_NEW;
        setVisible(false);
    }

    public int getImportType() {
        return importType;
    }

    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton importIntoExistingButton;
    private javax.swing.JRadioButton importIntoNewButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;

}
