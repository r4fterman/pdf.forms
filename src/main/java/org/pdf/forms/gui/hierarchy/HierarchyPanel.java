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
* HierarchyPanel.java
* ---------------
*/
package org.pdf.forms.gui.hierarchy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.hierarchy.tree.CTree;
import org.pdf.forms.gui.hierarchy.tree.HierarchyTreeCellEditor;
import org.pdf.forms.gui.hierarchy.tree.HierarchyTreeCellRenderer;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class HierarchyPanel extends JPanel implements Dockable {
    private final DockKey key = new DockKey("Hierarchy");
    private final CTree tree;

    private final DefaultMutableTreeNode top;

    private final List<DefaultMutableTreeNode> pageNodes = new ArrayList<>();

    public HierarchyPanel(final IDesigner designer) {
        setLayout(new BorderLayout());

        top = new DefaultMutableTreeNode("Document Root");

        final DefaultTreeModel treeModel = new DefaultTreeModel(top);

        tree = new CTree(treeModel, designer);
        //tree.setEditable(true); // causes strange bugs whilst dragging and dropping

        final HierarchyTreeCellRenderer treeCellRenderer = new HierarchyTreeCellRenderer(designer.getMainFrame());
        tree.setCellRenderer(treeCellRenderer);

        final HierarchyTreeCellEditor treeCellEditor = new HierarchyTreeCellEditor(tree, treeCellRenderer);
        tree.setCellEditor(treeCellEditor);

        final JScrollPane treeScrollPane = new JScrollPane(tree);

        add(treeScrollPane);
    }

    public void addPage(
            final int currentPage,
            final Page page) {
        final DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page);

        pageNodes.add(currentPage - 1, pageNode);
        top.insert(pageNode, currentPage - 1);

        tree.updateUI();
    }

    public void removePage(final int index) {
        pageNodes.remove(index - 1);
        top.remove(index - 1);

        tree.updateUI();
    }

    public void updateHierarchy(final FormsDocument document) {
        /* remove all pages*/
        pageNodes.clear();
        top.removeAllChildren();

        for (int i = 1; i <= document.getNoOfPages(); i++) {
            final Page page = document.getPage(i);
            addPage(i, page);
            for (final IWidget widget : page.getWidgets()) {
                addWidgetToHierarchy(widget, i);
            }
        }
    }

    public void addWidgetToHierarchy(
            final IWidget widget,
            final int page) {
        final DefaultMutableTreeNode pageNode = pageNodes.get(page - 1);
        addWidgetsToParent(pageNode, widget);

        tree.updateUI();
    }

    public void removeWidgetFromHierarchy(
            final IWidget widget,
            final int page) {
        final DefaultMutableTreeNode pageNode = pageNodes.get(page - 1);

        final Enumeration<TreeNode> children = pageNode.children();
        final List<DefaultMutableTreeNode> nodesToRemove = new ArrayList<>();
        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        while (children.hasMoreElements()) {
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();

            if (widget == node.getUserObject()) {
                nodesToRemove.add(node);
            }
        }

        for (final DefaultMutableTreeNode node : nodesToRemove) {
            model.removeNodeFromParent(node);
        }

        tree.updateUI();
    }

    private void addWidgetsToParent(
            final DefaultMutableTreeNode parent,
            final IWidget widget) {
        final DefaultMutableTreeNode addedWidget = new DefaultMutableTreeNode(widget);
        parent.insert(addedWidget, 0);

        if (widget.getType() == IWidget.GROUP) {
            final List<IWidget> widgetsInGroup = widget.getWidgetsInGroup();
            for (final IWidget widgetInGroup : widgetsInGroup) {
                addWidgetsToParent(addedWidget, widgetInGroup);
            }
        }

        final TreePath treePath = new TreePath(addedWidget.getPath());
        tree.scrollPathToVisible(treePath);
        tree.expandPath(treePath);
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    public void setState(final boolean state) {
        if (state) {
            ((DefaultTreeModel) tree.getModel()).setRoot(top);
        } else {
            top.removeAllChildren();
            pageNodes.clear();

            ((DefaultTreeModel) tree.getModel()).setRoot(null);
        }
    }

    public void setHidden(final boolean state) {
        if (state) {
            ((DefaultTreeModel) tree.getModel()).setRoot(top);
        } else {
            ((DefaultTreeModel) tree.getModel()).setRoot(null);
        }
    }
}
