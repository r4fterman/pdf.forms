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
* PdfImportProgressDialog.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class PdfImportProgressDialog extends JDialog {

    private int noOfPages;
    private boolean isCancelled = false;

    private javax.swing.JLabel infoText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton stopButton;

    public PdfImportProgressDialog(final JFrame frame) {
        super(frame);
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        infoText = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        stopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/animation.gif"))); // NOI18N

        infoText.setText("Importing PDF page 1 of 10:");

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonstopClicked(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 424, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                                .addGap(10, 10, 10)
                                                .addComponent(stopButton))
                                        .addComponent(infoText, GroupLayout.Alignment.LEADING))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 179, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(infoText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(stopButton))
                                .addContainerGap())
        );

        pack();
    }

    private void stopButtonstopClicked(java.awt.event.ActionEvent evt) {
        stopButton.setEnabled(false);
        isCancelled = true;
    }

    public void setStatusMaximum(int max) {
        this.noOfPages = max;
        progressBar.setMaximum(max);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
        infoText.setText("Importing PDF page " + (progress + 1) + " of " + noOfPages + ":");
    }

    public boolean isCancelled() {
        return isCancelled;
    }

}
