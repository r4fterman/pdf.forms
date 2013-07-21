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
 * HierarchyPanel.java
 * ---------------
 */
package org.pdf.forms.gui.hierarchy;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.hierarchy.tree.CTree;
import org.pdf.forms.gui.hierarchy.tree.HierarchyTreeCellEditor;
import org.pdf.forms.gui.hierarchy.tree.HierarchyTreeCellRenderer;
import org.pdf.forms.widgets.IWidget;

public class HierarchyPanel extends JPanel implements Dockable {
    private DockKey key = new DockKey("Hierarchy");
    private CTree tree;

    private DefaultMutableTreeNode top;

    private List pageNodes = new ArrayList();

    public HierarchyPanel(IDesigner designer) {

        setLayout(new BorderLayout());

        top = new DefaultMutableTreeNode("Document Root");

        DefaultTreeModel treeModel = new DefaultTreeModel(top);

        tree = new CTree(treeModel, designer);
        //tree.setEditable(true); // causes strange bugs whilst dragging and dropping

        HierarchyTreeCellRenderer treeCellRenderer = new HierarchyTreeCellRenderer(designer.getMainFrame());
        tree.setCellRenderer(treeCellRenderer);

        HierarchyTreeCellEditor treeCellEditor = new HierarchyTreeCellEditor(tree, treeCellRenderer);
        tree.setCellEditor(treeCellEditor);

        JScrollPane treeScrollPane = new JScrollPane(tree);

        add(treeScrollPane);
    }

    public void addPage(int currentPage, Page page) {
        DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page);

        pageNodes.add(currentPage - 1, pageNode);
        top.insert(pageNode, currentPage - 1);

        tree.updateUI();
    }

    public void removePage(int index) {
        pageNodes.remove(index - 1);
        top.remove(index - 1);

        tree.updateUI();
    }

    public void updateHierarchy(FormsDocument document) {
        /** remove all pages*/
        pageNodes.clear();
        top.removeAllChildren();

        for (int i = 1; i <= document.getNoOfPages(); i++) {
            Page page = document.getPage(i);
            addPage(i, page);
            for (Iterator it = page.getWidgets().iterator(); it.hasNext(); ) {
                IWidget widget = (IWidget) it.next();
                addWidgetToHierarchy(widget, i);
            }
        }
    }

    public void addWidgetToHierarchy(IWidget widget, int page) {
        DefaultMutableTreeNode pageNode = (DefaultMutableTreeNode) pageNodes.get(page - 1);
        addWidgetsToParent(pageNode, widget);

        tree.updateUI();
    }

    public void removeWidgetFromHierarchy(IWidget widget, int page) {
        DefaultMutableTreeNode pageNode = (DefaultMutableTreeNode) pageNodes.get(page - 1);

        Enumeration children = pageNode.children();
        List nodesToRemove = new ArrayList();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        while (children.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();

            if (widget == node.getUserObject()) {
                nodesToRemove.add(node);
            }
        }

        for (Iterator it = nodesToRemove.iterator(); it.hasNext(); ) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) it.next();
            model.removeNodeFromParent(node);
        }

        tree.updateUI();
    }

    private void addWidgetsToParent(DefaultMutableTreeNode parent, IWidget widget) {
        DefaultMutableTreeNode addedWidget = new DefaultMutableTreeNode(widget);
        parent.insert(addedWidget, 0);

        if (widget.getType() == IWidget.GROUP) {
            List widgetsInGroup = widget.getWidgetsInGroup();
            for (Iterator it = widgetsInGroup.iterator(); it.hasNext(); ) {
                addWidgetsToParent(addedWidget, (IWidget) it.next());
            }
        }

        TreePath treePath = new TreePath(addedWidget.getPath());
        tree.scrollPathToVisible(treePath);
        tree.expandPath(treePath);
    }

    public DockKey getDockKey() {
        return key;
    }

    public Component getComponent() {
        return this;
    }

    public void setState(boolean state) {
        if (state) {
            ((DefaultTreeModel) tree.getModel()).setRoot(top);
        } else {
            top.removeAllChildren();
            pageNodes.clear();

            ((DefaultTreeModel) tree.getModel()).setRoot(null);
        }
    }

    public void setHidden(boolean state) {
        if (state) {
            ((DefaultTreeModel) tree.getModel()).setRoot(top);
        } else {
            ((DefaultTreeModel) tree.getModel()).setRoot(null);
        }
    }
}
