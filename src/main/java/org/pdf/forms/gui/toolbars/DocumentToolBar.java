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
* DocumentToolBar.java
* ---------------
*/
package org.pdf.forms.gui.toolbars;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.jpedal.examples.simpleviewer.gui.swing.SwingButton;
import org.jpedal.examples.simpleviewer.gui.swing.SwingCombo;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class DocumentToolBar extends VLToolBar {

    private final CommandListener commandListener;

    private final JButton saveButton;

    private final List<JComponent> zoomComponents = new ArrayList<>();
    private final List<JComponent> documentComponents = new ArrayList<>();

    private final SwingCombo zoomBox;

    public DocumentToolBar(final CommandListener commandListener) {

        this.commandListener = commandListener;

        documentComponents.add(addToolBarButton("/org/pdf/forms/res/New.gif", Commands.NEW, "New"));
        documentComponents.add(addToolBarButton("/org/pdf/forms/res/open.gif", Commands.OPEN, "Open"));

        saveButton = addToolBarButton("/org/pdf/forms/res/save.gif", Commands.SAVE_FILE, "Save");

        addSeparator();

        final JButton zoomIn = addToolBarButton("/org/pdf/forms/res/plus.gif", Commands.ZOOM_IN, "Zoom In");
        zoomIn.setEnabled(false);
        zoomComponents.add(zoomIn);

        zoomBox = new SwingCombo(new String[] { "100%", "75%", "50%", "25%" });
        zoomBox.setEditable(true);
        zoomBox.setID(Commands.ZOOM);
        zoomBox.addActionListener(commandListener);
        zoomBox.setPreferredSize(new Dimension(80, 24));
        add(zoomBox);
        zoomBox.setEnabled(false);
        zoomComponents.add(zoomBox);

        final JButton zoomOut = addToolBarButton("/org/pdf/forms/res/minus.gif", Commands.ZOOM_OUT, "Zoom Out");
        zoomOut.setEnabled(false);
        zoomComponents.add(zoomOut);
    }

    private JButton addToolBarButton(
            final String url,
            final int command,
            final String toolTip) {
        final SwingButton button = new SwingButton();
        button.init(url, command, toolTip);
        button.addActionListener(commandListener);
        add(button);

        return button;
    }

    public void setSaveState(final boolean state) {
        saveButton.setEnabled(state);
    }

    public void setZoomState(final boolean state) {
        for (final JComponent component : zoomComponents) {
            component.setEnabled(state);
        }
    }

    public void setState(final boolean state) {
        setSaveState(state);

        for (final JComponent component : documentComponents) {
            component.setEnabled(state);
        }
    }

    public double getCurrentSelectedScaling() {
        return Double.parseDouble(((String) zoomBox.getSelectedItem()).replaceAll("%", ""));
    }

    public void setCurrentlySelectedScaling(final double scaling) {
        zoomBox.removeActionListener(commandListener);
        zoomBox.setSelectedItem(scaling + "%");
        zoomBox.addActionListener(commandListener);
    }
}
