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
* LibraryPanel.java
* ---------------
*/
package org.pdf.forms.gui.library;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.utils.WidgetFactory;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class LibraryPanel extends JPanel implements Dockable {

    private final DockKey key = new DockKey("Library");
    private final JList<String> list;
    private final ListSelectionListener listener;

    public LibraryPanel(
            final IDesigner designer,
            final WidgetFactory widgetFactory) {
        setLayout(new BorderLayout());

        list = new JList<>(new String[] {
                "Text Field",
                "Text",
                "Button",
                "Radio Button",
                "Check Box",
                "Drop-down List",
                "List Box",
                "Image" });

        final LibraryPanelCellRenderer libraryPanelCellRenderer = new LibraryPanelCellRenderer();
        list.setCellRenderer(libraryPanelCellRenderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        listener = e -> {
            designer.setWidgetToAdd(list.getSelectedIndex());
            designer.getCaptionChanger().closeCaptionChanger();
        };

        list.addListSelectionListener(listener);

        final DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_COPY_OR_MOVE, new DragableComponent(dragSource, designer, widgetFactory));

        final JScrollPane listScroller = new JScrollPane(list);

        add(listScroller);
    }

    public void setState(final boolean state) {
        list.setEnabled(state);
    }

    class LibraryPanelCellRenderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {

            final String s = value.toString();
            setText(s);
            final URL resource = getClass().getResource("/org/pdf/forms/res/" + s + ".gif");
            final ImageIcon longIcon = new ImageIcon(resource);
            setIcon(longIcon);
            if (isSelected) {
                setBackground(new Color(236, 233, 216));
                setForeground(Color.BLACK);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    public void resetButtons() {
        list.removeListSelectionListener(listener);
        list.clearSelection();
        list.addListSelectionListener(listener);
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
