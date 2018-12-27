/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
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

    // private final int units = (int) (Rule.DPI * 2.54);

    private JTextField heightBox;
    private JRadioButton landscapeButton;
    private JComboBox<String> paperTypeBox;
    private JRadioButton portraitButton;
    private JTextField widthBox;

    /**
     * Creates new form PagePanel.
     */
    public PagePanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initComponents();

        portraitButton.setEnabled(false);
        landscapeButton.setEnabled(false);
    }

    private void initComponents() {

        final ButtonGroup buttonGroup1 = new ButtonGroup();
        final JLabel jLabel1 = new JLabel();
        paperTypeBox = new JComboBox<>();
        final JLabel jLabel2 = new JLabel();
        heightBox = new JTextField();
        final JLabel jLabel3 = new JLabel();
        widthBox = new JTextField();
        final JLabel jLabel4 = new JLabel();
        portraitButton = new JRadioButton();
        landscapeButton = new JRadioButton();

        jLabel1.setText("Paper Type:");

        paperTypeBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "A4", "A5", "Letter", "Custom" }));
        paperTypeBox.addActionListener(this::updatePaperType);

        jLabel2.setText("Height:");

        heightBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSize(evt);
            }
        });

        jLabel3.setText("Width:");

        widthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSize(evt);
            }
        });

        jLabel4.setText("Orientation:");

        buttonGroup1.add(portraitButton);
        portraitButton.setText("Portrait");
        portraitButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        portraitButton.setMargin(new Insets(0, 0, 0, 0));
        portraitButton.addActionListener(this::orientationClicked);

        buttonGroup1.add(landscapeButton);
        landscapeButton.setText("Landscape");
        landscapeButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        landscapeButton.setMargin(new Insets(0, 0, 0, 0));
        landscapeButton.addActionListener(this::orientationClicked);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(paperTypeBox, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel2)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(heightBox, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                                                .add(18, 18, 18)
                                                                .add(jLabel3)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(widthBox, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel4)
                                                                .addPreferredGap(LayoutStyle.UNRELATED)
                                                                .add(portraitButton)
                                                                .add(18, 18, 18)
                                                                .add(landscapeButton)))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(paperTypeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(heightBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel3)
                                        .add(widthBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .add(11, 11, 11)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel4)
                                        .add(portraitButton)
                                        .add(landscapeButton))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void updateSize(final FocusEvent evt) {
        handleCustomPaper();

        designerPanel.repaint();
    }

    private void updatePaperType(final ActionEvent evt) {
        final Object size = paperTypeBox.getSelectedItem();
        if ("A4".equals(size)) {
            paperTypeBox.setSelectedItem("A4");
            setSize(A4, false);

            page.setSize(A4);

        } else if ("A5".equals(size)) {
            paperTypeBox.setSelectedItem("A5");
            setSize(A5, false);

            page.setSize(A5);

        } else if ("Letter".equals(size)) {
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

    private void orientationClicked(final ActionEvent evt) {
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

        //     int rotation = page.getRotation();
        //     if(rotation == 0)
        //      portraitButton.setSelected(true);
        //     else
        //      landscapeButton.setSelected(true);

    }

    private void setItemQuietly(
            final JComboBox<String> comboBox,
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
        height = round((height / Rule.DPI) * 2.54);
        heightBox.setText(height + " cm");

        double width = size.width;
        width = round((width / Rule.DPI) * 2.54);
        widthBox.setText(width + " cm");
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 1);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }

}
