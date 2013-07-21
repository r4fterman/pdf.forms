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
 * ReportToolbar.java
 * ---------------
 */
package org.pdf.forms.gui.toolbars;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vlsolutions.swing.toolbars.VLToolBar;
import org.pdf.forms.gui.commands.CommandListener;

public class ReportToolbar extends VLToolBar {

    private List buttonsList = new ArrayList();

    private CommandListener commandListener;

    public ReportToolbar(CommandListener commandListener) {

        this.commandListener = commandListener;

//    	SwingButton button = new SwingButton("Email bug report");
//        button.init("/org/pdf/forms/res/email.png", Commands.BUGREPORT, "Email bug report");
//        button.addActionListener(commandListener);
//        add(button);

//        return button;

//    	JButton button = new JButton("Email bug report");
//    	button.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/simpleviewer/res/start.gif")));
//    	button.setToolTipText("Email bug report");
//    	button.addActionListener(new ActionListener() {
//    		public void actionPerformed(ActionEvent e) {
//    			WidgetAlignmentAndOrder.alignAndOrder(designer, type);
//    		}
//    	});

//    	buttonsList.add(button);

//    	add(button);
    }

    public void setState(boolean enabled) {
        for (Iterator it = buttonsList.iterator(); it.hasNext(); ) {
            JButton button = (JButton) it.next();
            button.setEnabled(enabled);
        }
    }
}
