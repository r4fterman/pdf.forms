/*
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
* PagePanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.page;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JComboBox;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;

import com.itextpdf.text.PageSize;

public class PagePanel extends javax.swing.JPanel {

    private final Dimension A4 = new Dimension(
            (int) PageSize.A4.getWidth(), (int) PageSize.A4.getHeight());

    private final Dimension A5 = new Dimension(
            (int) PageSize.A5.getWidth(), (int) PageSize.A5.getHeight());

    private final Dimension LETTER = new Dimension(
            (int) PageSize.LETTER.getWidth(), (int) PageSize.LETTER.getHeight());

    private Page page;

    private final IDesigner designerPanel;

    //	private final int units = (int) (Rule.DPI * 2.54);

    /**
     * Creates new form PagePanel
     */
    public PagePanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initComponents();

        portraitButton.setEnabled(false);
        landscapeButton.setEnabled(false);
    }

    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        paperTypeBox = new javax.swing.JComboBox();
        final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        heightBox = new javax.swing.JTextField();
        final javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        widthBox = new javax.swing.JTextField();
        final javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        portraitButton = new javax.swing.JRadioButton();
        landscapeButton = new javax.swing.JRadioButton();

        jLabel1.setText("Paper Type:");

        paperTypeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A4", "A5", "Letter", "Custom" }));
        paperTypeBox.addActionListener(evt -> updatePaperType(evt));

        jLabel2.setText("Height:");

        heightBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final java.awt.event.FocusEvent evt) {
                updateSize(evt);
            }
        });

        jLabel3.setText("Width:");

        widthBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final java.awt.event.FocusEvent evt) {
                updateSize(evt);
            }
        });

        jLabel4.setText("Orientation:");

        buttonGroup1.add(portraitButton);
        portraitButton.setText("Portrait");
        portraitButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        portraitButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        portraitButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                orientationClicked(evt);
            }
        });

        buttonGroup1.add(landscapeButton);
        landscapeButton.setText("Landscape");
        landscapeButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        landscapeButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        landscapeButton.addActionListener(evt -> orientationClicked(evt));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(paperTypeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel2)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(heightBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .add(18, 18, 18)
                                                                .add(jLabel3)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(widthBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel4)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                                .add(portraitButton)
                                                                .add(18, 18, 18)
                                                                .add(landscapeButton)))))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(paperTypeBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(heightBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel3)
                                        .add(widthBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(11, 11, 11)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel4)
                                        .add(portraitButton)
                                        .add(landscapeButton))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void updateSize(final java.awt.event.FocusEvent evt) {
        handleCustomPaper();

        designerPanel.repaint();
    }

    private void updatePaperType(final java.awt.event.ActionEvent evt) {
        final Object size = paperTypeBox.getSelectedItem();
        if (size.equals("A4")) {
            paperTypeBox.setSelectedItem("A4");
            setSize(A4, false);

            page.setSize(A4);

        } else if (size.equals("A5")) {
            paperTypeBox.setSelectedItem("A5");
            setSize(A5, false);

            page.setSize(A5);

        } else if (size.equals("Letter")) {
            paperTypeBox.setSelectedItem("Letter");
            setSize(LETTER, false);

            page.setSize(LETTER);

        } else { // custom
            paperTypeBox.setSelectedItem("Custom");
            heightBox.setEnabled(true);
            widthBox.setEnabled(true);

            handleCustomPaper();
        }

        designerPanel.repaint();

    }

    private void handleCustomPaper() {
        /* get height */
        final String heightText = heightBox.getText().replaceAll("cm", "");

        double height = page.getHeight();
        try {
            height = Double.parseDouble(heightText);
        } catch (final NumberFormatException e) {
            //e.printStackTrace();
        }
        heightBox.setText(height + " cm");

        height = (height / 2.54) * Rule.DPI;

        /* get width */
        final String widthText = widthBox.getText().replaceAll("cm", "");

        double width = page.getWidth();
        try {
            width = Double.parseDouble(widthText);
        } catch (final NumberFormatException e) {
            //e.printStackTrace();
        }
        widthBox.setText(width + " cm");

        width = (width / 2.54) * Rule.DPI;

        page.setSize(new Dimension((int) Math.round(width), (int) Math.round(height)));
    }

    private void orientationClicked(final java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public void setProperties(final Set widgets) {
        page = (Page) widgets.iterator().next();

        final Dimension size = page.getSize();
        if (size.equals(A4)) {
            setItemQuietly(paperTypeBox, "A4");
            setSize(size, false);
        } else if (size.equals(A5)) {
            setItemQuietly(paperTypeBox, "A5");
            setSize(size, false);
        } else if (size.equals(LETTER)) {
            setItemQuietly(paperTypeBox, "Letter");
            setSize(size, false);
        } else { // custom
            setItemQuietly(paperTypeBox, "Custom");
            setSize(size, true);
        }

        //    	int rotation = page.getRotation();
        //    	if(rotation == 0)
        //    		portraitButton.setSelected(true);
        //    	else
        //    		landscapeButton.setSelected(true);

    }

    private void setItemQuietly(
            final JComboBox comboBox,
            final Object item) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(item);
        comboBox.addActionListener(listener);
    }

    private void setSize(
            final Dimension size,
            final boolean enabled) {
        heightBox.setEnabled(enabled);
        widthBox.setEnabled(enabled);

        double height = size.height;
        height = round((height / Rule.DPI) * 2.54, 1);
        heightBox.setText(height + " cm");

        double width = size.width;
        width = round((width / Rule.DPI) * 2.54, 1);
        widthBox.setText(width + " cm");
    }

    private double round(
            double number,
            final int decPlaces) {
        final double exponential = Math.pow(10, decPlaces);

        number *= exponential;
        number = Math.round(number);
        number /= exponential;

        return number;
    }

    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField heightBox;
    private javax.swing.JRadioButton landscapeButton;
    private javax.swing.JComboBox paperTypeBox;
    private javax.swing.JRadioButton portraitButton;
    private javax.swing.JTextField widthBox;

}
