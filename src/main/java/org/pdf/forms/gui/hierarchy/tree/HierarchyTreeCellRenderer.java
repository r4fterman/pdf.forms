package org.pdf.forms.gui.hierarchy.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.pdf.forms.gui.IMainFrame;

public class HierarchyTreeCellRenderer extends DefaultTreeCellRenderer {

    private final IMainFrame mainFrame;

    private Icon icon;

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
        if (font == null) {
            return super.getPreferredSize();
        }

        final FontMetrics fontMetrics = getFontMetrics(font);
        final int textWidth = SwingUtilities.computeStringWidth(fontMetrics, getText());
        final int width = textWidth + getIcon().getIconWidth() + getIconTextGap();
        return new Dimension(width, super.getPreferredSize().height);
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
        final TreeNodeRenderer treeNodeRenderer = TreeNodeRendererFactory.getInstance(userObject, mainFrame);
        this.icon = treeNodeRenderer.getIcon();

        setText(treeNodeRenderer.getText());
        setIcon(icon);

        setBackground(getBackgroundColor(isSelected, tree.getBackground()));
        setForeground(getForegroundColor(isSelected, tree.getForeground()));

        setEnabled(tree.isEnabled());
        setFont(tree.getFont());

        setOpaque(true);

        setPreferredSize(getPreferredSize());

        return this;
    }

    private Color getForegroundColor(
            final boolean isSelected,
            final Color defaultColor) {
        if (isSelected) {
            return Color.BLACK;
        }
        return defaultColor;
    }

    private Color getBackgroundColor(
            final boolean isSelected,
            final Color defaultColor) {
        if (isSelected) {
            return new Color(236, 233, 216);
        }
        return defaultColor;
    }
}
