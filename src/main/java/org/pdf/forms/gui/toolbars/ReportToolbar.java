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
 * ReportToolbar.java
 * ---------------
 */
package org.pdf.forms.gui.toolbars;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.pdf.forms.gui.commands.CommandListener;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class ReportToolbar extends VLToolBar {

    private final List<JButton> buttonsList = new ArrayList<>();

    public ReportToolbar(final CommandListener commandListener) {

        //     SwingButton button = new SwingButton("Email bug report");
        //        button.init("/org/pdf/forms/res/email.png", Commands.BUGREPORT, "Email bug report");
        //        button.addActionListener(commandListener);
        //        add(button);

        //        return button;

        //     JButton button = new JButton("Email bug report");
        //     button.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/simpleviewer/res/start.gif")));
        //     button.setToolTipText("Email bug report");
        //     button.addActionListener(new ActionListener() {
        //      public void actionPerformed(ActionEvent e) {
        //       WidgetAlignmentAndOrder.alignAndOrder(designer, type);
        //      }
        //     });

        //     buttonsList.add(button);

        //     add(button);
    }

    public void setState(final boolean enabled) {
        for (final JButton button : buttonsList) {
            button.setEnabled(enabled);
        }
    }
}
