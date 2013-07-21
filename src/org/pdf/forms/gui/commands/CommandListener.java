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
 * CommandListener.java
 * ---------------
 */
package org.pdf.forms.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jpedal.examples.simpleviewer.gui.generic.GUIButton;
import org.jpedal.examples.simpleviewer.gui.swing.SwingCombo;
import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;

public class CommandListener implements ActionListener {
    private Commands commands;

    public CommandListener(Commands commands) {
        this.commands = commands;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        int ID = 0;
        if (source instanceof GUIButton)
            ID = ((GUIButton) source).getID();
        else if (source instanceof SwingMenuItem)
            ID = ((SwingMenuItem) source).getID();
        else if (source instanceof SwingCombo)
            ID = ((SwingCombo) source).getID();

        commands.executeCommand(ID);
    }
}
