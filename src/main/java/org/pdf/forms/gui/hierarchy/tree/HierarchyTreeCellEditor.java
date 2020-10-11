package org.pdf.forms.gui.hierarchy.tree;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

public class HierarchyTreeCellEditor extends DefaultTreeCellEditor implements TreeCellEditor {

    public HierarchyTreeCellEditor(
            final JTree tree,
            final DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    @Override
    public Component getTreeCellEditorComponent(
            final JTree tree,
            final Object value,
            final boolean isSelected,
            final boolean expanded,
            final boolean leaf,
            final int row) {
        final Container tf = (Container) super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
        ((JTextField) tf.getComponent(0)).setText(renderer.getText());

        return tf;
    }

}
