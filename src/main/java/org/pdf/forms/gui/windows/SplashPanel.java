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
* SplashPanel.java
* ---------------
*/
package org.pdf.forms.gui.windows;

public class SplashPanel extends javax.swing.JPanel {
    
    /** Creates new form SpashPanel */
    public SplashPanel(final String version) {
        initComponents(version);
    }
    
    private void initComponents(final String version) {

        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setLayout(null);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Version: " + version);
        add(jLabel2);
        jLabel2.setBounds(600, 230, 100, 14);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/headerout.png"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(612, 332));
        add(jLabel1);
        jLabel1.setBounds(0, 0, 730, 255);
        add(progressBar);
        progressBar.setBounds(0, 255, 717, 19);
    }
    
    public void setStatusMaximum(int max){
        progressBar.setMaximum(max);
    }
    
    public void setProgress(int progress, String text){
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        progressBar.setString(text);
    }
    
    private javax.swing.JProgressBar progressBar;

}
