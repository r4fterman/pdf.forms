/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * FileFinder.java
 * ---------------
 */
package org.pdf.forms.gui.windows;

import java.awt.Component;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jpedal.examples.simpleviewer.utils.FileFilterer;

public class FileFinder extends javax.swing.JDialog {

    private String fileLocation;

    private JLabel fileLabel;
    private JTextField locationBox;

    /**
     * Creates new form FileFinderFrame.
     */
    public FileFinder(
            final Component parent,
            final String fileToFind) {
        setTitle("Unable to find file");
        setModal(true);

        initComponents();

        fileLabel.setText(new File(fileToFind).getName());
        locationBox.setText(fileToFind);

        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        final JPanel jPanel1 = new JPanel();
        final JLabel label = new JLabel();
        final JButton jButton1 = new JButton();
        final JButton jButton2 = new JButton();
        final JButton jButton3 = new JButton();
        locationBox = new JTextField();
        fileLabel = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        label.setText("Unable to locate the required PDF document:");

        jButton1.setText("OK");
        jButton1.addActionListener(this::okClicked);

        jButton2.setText("Skip");
        jButton2.addActionListener(this::skipClicked);

        jButton3.setText("Browse");
        jButton3.addActionListener(this::browseClicked);

        fileLabel.setText("jLabel1");

        final GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(label)
                                        .add(locationBox, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE)
                                        .add(fileLabel))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(GroupLayout.TRAILING, jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(GroupLayout.TRAILING, jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(GroupLayout.TRAILING, jButton3, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(jButton1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton2))
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(label)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(fileLabel)))
                                .add(39, 39, 39)
                                .add(jPanel1Layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jButton3)
                                        .add(locationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }

    private void skipClicked(final java.awt.event.ActionEvent evt) {
        fileLocation = null;
        setVisible(false);
    }

    private void browseClicked(final java.awt.event.ActionEvent evt) {
        final String path = locationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        final String[] pdf = new String[] {
                "pdf"
        };
        chooser.addChoosableFileFilter(new FileFilterer(pdf, "des (*.des)"));

        final int state = chooser.showOpenDialog(this);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            locationBox.setText(fileToOpen.getAbsolutePath());
        }
    }

    private void okClicked(final java.awt.event.ActionEvent evt) {
        final String newFile = locationBox.getText();

        if (!new File(newFile).exists()) {
            JOptionPane.showMessageDialog(this,
                    "The file you have entered does not exist, please select an existing file",
                    "String",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            fileLocation = locationBox.getText();
            setVisible(false);
        }
    }

    public String getFileLocation() {
        return fileLocation;
    }

}
