/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
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
* ObjectPropertiesTab.java
* ---------------
*/
package org.pdf.forms.gui.properties.object;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class ObjectPropertiesTab extends JPanel implements Dockable {

    private final ObjectPropertiesPanel objectPanel = new ObjectPropertiesPanel();
    private final DockKey key = new DockKey("Object");

    public ObjectPropertiesTab(final IDesigner designer) {

        objectPanel.setDesignerPanel(designer);

        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        removeAll();

        if (!widgets.isEmpty()) {
            boolean mixed = false;
            int type = -1;
            for (final IWidget widget : widgets) {
                if (type == -1) {
                    type = widget.getType();
                } else {
                    if (type != widget.getType()) {
                        mixed = true;
                        break;
                    }
                }

            }

            if (mixed) {
                add(new JLabel("Mixed components"));
            } else {
                objectPanel.setProperties(widgets, type);
                add(objectPanel);
            }
        }

        updateUI();
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
