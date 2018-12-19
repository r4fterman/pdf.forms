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
 * DesignerKeyListener.java
 * ---------------
 */
package org.pdf.forms.gui.designer.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DesignerKeyListener implements KeyListener {

    private final IDesigner designerPanel;

    public DesignerKeyListener(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
            designerPanel.getMainFrame().handleArrayNumberOnWidgetDeletion(selectedWidgets);

            designerPanel.removeSelectedWidgets();
            designerPanel.setSelectedWidgets(new HashSet<>());
            designerPanel.repaint();
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }
}
