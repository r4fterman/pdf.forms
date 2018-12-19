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
 * FontSelector.java
 * ---------------
 */
package org.pdf.forms.fonts;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.DesignerPropertiesFile;

public class FontSelector extends javax.swing.JPanel {

    private IMainFrame mainFrame;
    private JDialog parent;

    /**
     * Creates new form FontSelector
     * @param parentDialog
     */
    public FontSelector(
            IMainFrame mainFrame,
            JDialog parentDialog) {
        initComponents();

        this.parent = parentDialog;
        this.mainFrame = mainFrame;

        populateFontsAvailiable();
    }

    private void populateFontsAvailiable() {
        Map<Font, String> fonts = FontHandler.getInstance().getFontFileMap();

        DefaultListModel model = new DefaultListModel();
        fontsList.setModel(model);

        for (Font font : fonts.keySet()) {
            model.addElement(font.getFontName() + " -> " + fonts.get(font));
        }
    }

    private void initComponents() {

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fontsList = new javax.swing.JList<>();
        javax.swing.JButton addFontButton = new javax.swing.JButton();
        javax.swing.JButton okButton = new javax.swing.JButton();

        jLabel1.setText("Availiable Fonts:");

        jScrollPane1.setViewportView(fontsList);

        addFontButton.setText("Add Font");
        addFontButton.addActionListener(evt -> addFontClicked(evt));

        okButton.setText("OK");
        okButton.addActionListener(evt -> okClicked(evt));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel1)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, okButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, addFontButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(addFontButton)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 327, Short.MAX_VALUE)
                                                .add(okButton))
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }

    private void okClicked(java.awt.event.ActionEvent evt) {
        parent.setVisible(false);
    }

    private void addFontClicked(java.awt.event.ActionEvent evt) {
        final JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String[] ttf = new String[] { "ttf" };
        chooser.addChoosableFileFilter(new FileFilterer(ttf, "ttf (*.ttf)"));

        final int state = chooser.showOpenDialog((Component) mainFrame);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            String name = FontHandler.getInstance().registerFont(fileToOpen);
            DesignerPropertiesFile.getInstance().addCustomFont(name, fileToOpen.getAbsolutePath());

            populateFontsAvailiable();
        }

    }

    private javax.swing.JList<String> fontsList;
    private javax.swing.JScrollPane jScrollPane1;

}
