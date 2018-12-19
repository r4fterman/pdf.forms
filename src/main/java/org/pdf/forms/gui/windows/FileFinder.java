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
* FileFinder.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;

public class FileFinder extends javax.swing.JDialog {

    private String fileLocation;

    /**
     * Creates new form FileFinderFrame
     */
    public FileFinder(Component parent, String fileToFind) {
        setTitle("Unable to find file");
        setModal(true);

        initComponents();

        fileLabel.setText(new File(fileToFind).getName());
        locationBox.setText(fileToFind);

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        locationBox = new javax.swing.JTextField();
        fileLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        label.setText("Unable to locate the required PDF document:");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okClicked(evt);
            }
        });

        jButton2.setText("Skip");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipClicked(evt);
            }
        });

        jButton3.setText("Browse");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseClicked(evt);
            }
        });

        fileLabel.setText("jLabel1");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(label)
                                .add(locationBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 331, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(fileLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                        .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanel1Layout.createSequentialGroup()
                                        .add(jButton1)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButton2))
                                .add(jPanel1Layout.createSequentialGroup()
                                .add(label)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fileLabel)))
                        .add(39, 39, 39)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jButton3)
                                .add(locationBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }

    private void skipClicked(java.awt.event.ActionEvent evt) {
        fileLocation = null;
        setVisible(false);
    }

    private void browseClicked(java.awt.event.ActionEvent evt) {
        String path = locationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String[] pdf = new String[]{"pdf"};
        chooser.addChoosableFileFilter(new FileFilterer(pdf, "des (*.des)"));

        final int state = chooser.showOpenDialog(this);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            locationBox.setText(fileToOpen.getAbsolutePath());
        }
    }

    private void okClicked(java.awt.event.ActionEvent evt) {
        String newFile = locationBox.getText();

        if (!new File(newFile).exists()) {
            JOptionPane.showMessageDialog(this,
                    "The file you have entered does not exist, please select an existing file",
                    "String",
                    JOptionPane.OK_OPTION);
        } else {
            fileLocation = locationBox.getText();
            setVisible(false);
        }
    }

    public String getFileLocation() {
        return fileLocation;
    }

    private javax.swing.JLabel fileLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label;
    private javax.swing.JTextField locationBox;

}
