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
* ParagraphPropertiesTab.java
* ---------------
*/
package org.pdf.forms.gui.properties.paragraph;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class ParagraphPropertiesTab extends JPanel implements Dockable {

    private final ParagraphPropertiesPanel paragraphPanel = new ParagraphPropertiesPanel();
    private final DockKey key = new DockKey("Paragraph");

    public ParagraphPropertiesTab(final IDesigner designer) {
        paragraphPanel.setDesignerPanel(designer);

        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || (widgets.iterator().next() instanceof Page)) {
            removeAll();
        } else {
            final Map<IWidget, Element> widgetsAndProperties = new HashMap<>();

            for (final IWidget widget : widgets) {
                if (widget.getType() == IWidget.IMAGE) {
                    removeAll();
                    return;
                }

                final Document properties = widget.getProperties();

                final Element layoutProperties = (Element) properties.getElementsByTagName("paragraph").item(0);

                widgetsAndProperties.put(widget, layoutProperties);
            }

            paragraphPanel.setProperties(widgetsAndProperties, 0);

            add(paragraphPanel);
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
