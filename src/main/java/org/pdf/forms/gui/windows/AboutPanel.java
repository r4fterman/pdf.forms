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
 * AboutPanel.java
 * ---------------
 */
package org.pdf.forms.gui.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;
import org.jpedal.utils.BrowserLauncher;

public class AboutPanel extends javax.swing.JPanel {

    /**
     * Creates new form AboutPanel.
     */
    public AboutPanel() {
        initComponents();
    }

    private void initComponents() {

        final JLabel jLabel2 = getURLLabel(this, "http://pdfformsdesigne.sourceforge.net");
        final JLabel jLabel4 = new JLabel();
        final JLabel jLabel5 = new JLabel();
        final JLabel jLabel6 = new JLabel();
        final JLabel jLabel7 = new JLabel();
        final JLabel jLabel8 = new JLabel();
        final JLabel jLabel9 = new JLabel();
        final JLabel jLabel10 = getURLLabel(this, "http://www.jpedal.org");
        final JLabel jLabel11 = getURLLabel(this, "http://www.vlsolutions.com/en/products/docking/");
        final JLabel jLabel12 = getURLLabel(this, "http://www.lowagie.com/iText/");
        final JLabel jLabel13 = getURLLabel(this, "https://swing-layout.dev.java.net/");

        jLabel2.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel4.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("PDF Forms Designer");

        jLabel5.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel5.setText("Used Libraries:");

        jLabel6.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel6.setText("JPedal - ");

        jLabel7.setFont(new java.awt.Font("Serif", 0, 12));
        jLabel7.setText("VLDocking - ");

        jLabel8.setFont(new java.awt.Font("Serif", 0, 12));
        jLabel8.setText("iText - ");

        jLabel9.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N
        jLabel9.setText("Swing Layout -");

        jLabel10.setFont(new java.awt.Font("Serif", 0, 12));

        jLabel11.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Serif", 0, 12)); // NOI18N

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jLabel2, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .add(jLabel4, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .add(jLabel5)
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel7)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(jLabel11))
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel6)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(jLabel10))
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel8)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(jLabel12))
                                                        .add(layout.createSequentialGroup()
                                                                .add(jLabel9)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(jLabel13)))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel6)
                                        .add(jLabel10))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel7)
                                        .add(jLabel11))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel8)
                                        .add(jLabel12))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel9)
                                        .add(jLabel13))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.getContentPane().add(new AboutPanel());
        frame.pack();
        frame.setVisible(true);
    }

    private JLabel getURLLabel(
            final JPanel details,
            final String website) {
        final JLabel url = new JLabel("<html><center> " + website);
        url.setForeground(Color.blue);

        url.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                details.setCursor(new Cursor(Cursor.HAND_CURSOR));
                url.setText("<html><center><a href=" + website + ">" + website + "</a></center>");
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                details.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                url.setText("<html><center>" + website);
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    BrowserLauncher.openURL(website);
                } catch (final IOException e1) {
                    JOptionPane.showMessageDialog(null, "Error loading webpage");
                    //<start-full><start-demo>
                    e1.printStackTrace();
                    //<end-demo><end-full>
                }
            }

            @Override
            public void mousePressed(final MouseEvent e) {
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
            }
        });
        return url;
    }

}
