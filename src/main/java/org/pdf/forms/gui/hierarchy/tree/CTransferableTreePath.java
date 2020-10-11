package org.pdf.forms.gui.hierarchy.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.tree.TreePath;

/**
 * This represents a TreePath (a node in a JTree) that can be transferred between a drag source and a drop target.
 */
class CTransferableTreePath implements Transferable {
    // The type of DnD object being dragged...
    static final DataFlavor TREEPATH_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "TreePath");

    private final TreePath path;

    private final DataFlavor[] flavors = {
            TREEPATH_FLAVOR };

    /**
     * Constructs a transferable tree path object for the specified path.
     */
    CTransferableTreePath(final TreePath path) {
        this.path = path;
    }

    // Transferable interface methods...
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        return java.util.Arrays.asList(flavors).contains(flavor);
    }

    @Override
    public synchronized Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.isMimeTypeEqual(TREEPATH_FLAVOR.getMimeType())) {
            // DataFlavor.javaJVMLocalObjectMimeType))
            return path;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
