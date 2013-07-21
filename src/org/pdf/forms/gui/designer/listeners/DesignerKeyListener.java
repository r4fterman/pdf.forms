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
 * DesignerKeyListener.java
 * ---------------
 */
package org.pdf.forms.gui.designer.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;

public class DesignerKeyListener implements KeyListener {

    private IDesigner designerPanel;

    public DesignerKeyListener(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            Set selectedWidgets = designerPanel.getSelectedWidgets();
            designerPanel.getMainFrame().handleArrayNumberOnWidgetDeletion(selectedWidgets);

            designerPanel.removeSelectedWidgets();
            designerPanel.setSelectedWidgets(new HashSet());
            designerPanel.repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
