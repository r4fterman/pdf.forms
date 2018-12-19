/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * CTransferableTreePath.java
 * ---------------
 */
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

    private final DataFlavor[] flavors = { TREEPATH_FLAVOR };

    /**
     * Constructs a transferrable tree path object for the specified path.
     */
    public CTransferableTreePath(final TreePath path) {
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
        if (flavor.isMimeTypeEqual(TREEPATH_FLAVOR.getMimeType())) // DataFlavor.javaJVMLocalObjectMimeType))
        {
            return path;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
	
