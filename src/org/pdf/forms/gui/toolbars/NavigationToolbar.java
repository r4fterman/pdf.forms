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
 * NavigationToolbar.java
 * ---------------
 */
package org.pdf.forms.gui.toolbars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.vlsolutions.swing.toolbars.VLToolBar;

public abstract class NavigationToolbar extends VLToolBar {
    protected static final int FIRSTPAGE = 0;
    protected static final int FBACKPAGE = 1;
    protected static final int BACKPAGE = 2;
    protected static final int FORWARDPAGE = 3;
    protected static final int FFORWARDPAGE = 4;
    protected static final int LASTPAGE = 5;
    protected static final int SETPAGE = 6;

    protected JTextField currentPageBox = new JTextField(4);
    private JLabel totalNoOfPages = new JLabel();

    public NavigationToolbar() {

        totalNoOfPages.setText("of 1");
        currentPageBox.setText("1");

        add(Box.createHorizontalGlue());

        addButton("Rewind To Start", "/org/jpedal/examples/simpleviewer/res/start.gif", FIRSTPAGE);
        addButton("Back 10 Pages", "/org/jpedal/examples/simpleviewer/res/fback.gif", FBACKPAGE);
        addButton("Back", "/org/jpedal/examples/simpleviewer/res/back.gif", BACKPAGE);

        add(new JLabel("Page"));
        currentPageBox.setMaximumSize(new Dimension(5, 50));
        currentPageBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                executeCommand(SETPAGE);
            }
        });
        add(currentPageBox);
        add(totalNoOfPages);

        addButton("Forward", "/org/jpedal/examples/simpleviewer/res/forward.gif", FORWARDPAGE);
        addButton("Forward 10 Pages", "/org/jpedal/examples/simpleviewer/res/fforward.gif", FFORWARDPAGE);
        addButton("Fast Forward To End", "/org/jpedal/examples/simpleviewer/res/end.gif", LASTPAGE);

        add(Box.createHorizontalGlue());

    }

    public abstract void executeCommand(int type);

    public void setTotalNoOfPages(int noOfPages) {
        totalNoOfPages.setText("of " + noOfPages);
    }

    public void setCurrentPage(int currentPage) {
        currentPageBox.setText(currentPage + "");
    }

    public void close() {
        totalNoOfPages.setText("");
        currentPageBox.setText("");

        setComponentsEnabled(false);
    }

    public void open() {
        setComponentsEnabled(true);
    }

    private void setComponentsEnabled(boolean enabled) {
        Component[] components = getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = getComponents()[i];
            component.setEnabled(enabled);
        }
    }

    private void addButton(String tooltip, String url, final int type) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource(url)));
        button.setToolTipText(tooltip);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                executeCommand(type);
            }
        });

        add(button);
    }
}
