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
* ColorCellRenderer.java
* ---------------
*/
package org.pdf.forms.gui.properties.customcomponents.colorcombobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorCellRenderer implements ListCellRenderer<Object> {

    private final Logger logger = LoggerFactory.getLogger(ColorCellRenderer.class);

    // width doesn't matter as combobox will size
    private final Dimension preferredSize = new Dimension(0, 20);

    private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(
            final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        final JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Color) {
            renderer.setBackground((Color) value);
            renderer.setText("");
        } else {
            logger.info("Unexpected list cell renderer value {}", value);
        }

        renderer.setPreferredSize(preferredSize);
        return renderer;
    }
}
