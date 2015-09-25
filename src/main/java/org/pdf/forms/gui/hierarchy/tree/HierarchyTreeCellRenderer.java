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
    private HierarchyTreeCellRenderer(){}

    public HierarchyTreeCellRenderer(IMainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public Icon getLeafIcon() {
        return icon;
    }

    public Dimension getPreferredSize() {
    	Font font = getFont();
    	if(font != null){
    		int width = SwingUtilities.computeStringWidth(getFontMetrics(getFont()), getText())
                    + getIcon().getIconWidth() + getIconTextGap();
    		return new Dimension(width, super.getPreferredSize().height);
    	}
    	return super.getPreferredSize();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {

        value = ((DefaultMutableTreeNode) value).getUserObject();

        String s = "";
        if (value instanceof IWidget) {
            IWidget w = (IWidget) value;

            s = w.getWidgetName();
            
            if (mainFrame.getWidgetArrays().isWidgetArrayInList(s)) {
                int arrayNumber = w.getArrayNumber();
                s += "[" + arrayNumber + "]";
//				System.out.println("w = " + w + " arraynumber = " + arrayNumber);
            }

            icon = w.getIcon();
        } else if (value instanceof Page) {
            Page page = (Page) value;

            s = page.getPageName();
            URL resource = getClass().getResource("/org/pdf/forms/res/Page.gif");
            icon = new ImageIcon(resource);

        } else if (value.equals("Document Root")) {
            s = value.toString();
            URL resource = getClass().getResource("/org/pdf/forms/res/Form.gif");
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
