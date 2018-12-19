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
* WidgetAlignmentAndOrderToolbar.java
* ---------------
*/
package org.pdf.forms.gui.toolbars;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class WidgetAlignmentAndOrderToolbar extends VLToolBar {

    private List<JButton> buttonsList = new ArrayList<>();

    public WidgetAlignmentAndOrderToolbar(final IDesigner designer) {

        String[] alignButtons = WidgetAlignmentAndOrder.getAlignButtons();
        String[] layoutButtons = WidgetAlignmentAndOrder.getOrderButtons();

        String[] buttons = concat(alignButtons, layoutButtons);

        for (final String url : buttons) {
            if (url.equals("Seperator")) {
                addSeparator();
            } else {
                String[] splitFilename = url.split("/");
                final String type = splitFilename[splitFilename.length - 1].split("\\.")[0];

                JButton button = new JButton();
                button.setIcon(new ImageIcon(getClass().getResource(url)));
                button.setToolTipText(type);
                button.addActionListener(e -> WidgetAlignmentAndOrder.alignAndOrder(designer, type));

                buttonsList.add(button);

                add(button);
            }
        }
    }

    private String[] concat(
            String[] array1,
            String[] array2) {

        String[] res = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, res, 0, array1.length);
        System.arraycopy(array2, 0, res, array1.length, array2.length);

        return res;
    }

    public void setState(boolean enabled) {
        for (final JButton button : buttonsList) {
            button.setEnabled(enabled);
        }
    }
}
