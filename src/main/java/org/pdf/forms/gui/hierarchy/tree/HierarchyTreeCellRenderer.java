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
* HierarchyTreeCellRenderer.java
* ---------------
*/
package org.pdf.forms.gui.hierarchy.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;

public class HierarchyTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon icon;

    private IMainFrame mainFrame;

    private HierarchyTreeCellRenderer() {
    }

    public HierarchyTreeCellRenderer(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public Icon getLeafIcon() {
        return icon;
    }

    @Override
    public Dimension getPreferredSize() {
        final Font font = getFont();
        if (font != null) {
            final int width = SwingUtilities.computeStringWidth(getFontMetrics(getFont()), getText())
                    + getIcon().getIconWidth() + getIconTextGap();
            return new Dimension(width, super.getPreferredSize().height);
        }
        return super.getPreferredSize();
    }

    @Override
    public Component getTreeCellRendererComponent(
            final JTree tree,
            final Object value,
            final boolean isSelected,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus) {

        final Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

        String s = "";
        if (userObject instanceof IWidget) {
            final IWidget w = (IWidget) userObject;

            s = w.getWidgetName();

            if (mainFrame.getWidgetArrays().isWidgetArrayInList(s)) {
                final int arrayNumber = w.getArrayNumber();
                s += "[" + arrayNumber + "]";
            }

            icon = w.getIcon();
        } else if (userObject instanceof Page) {
            final Page page = (Page) userObject;

            s = page.getPageName();
            final URL resource = getClass().getResource("/org/pdf/forms/res/Page.gif");
            icon = new ImageIcon(resource);

        } else if (userObject.equals("Document Root")) {
            s = userObject.toString();
            final URL resource = getClass().getResource("/org/pdf/forms/res/Form.gif");
            icon = new ImageIcon(resource);
        }

        setText(s);
        setIcon(icon);
        if (isSelected) {
            setBackground(new Color(236, 233, 216));
            setForeground(Color.BLACK);
        } else {
            setBackground(tree.getBackground());
            setForeground(tree.getForeground());
        }
        setEnabled(tree.isEnabled());
        setFont(tree.getFont());
        setOpaque(true);

        setPreferredSize(getPreferredSize());

        return this;
    }
}
