package org.pdf.forms.gui.hierarchy.tree;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.pdf.forms.gui.designer.IDesigner;

public class CTree extends JTree implements DragSourceListener, DragGestureListener, Autoscroll {


    private final Point draggedImageMouseClickPoint = new Point();

    private TreePath draggedPathSource;

    // The 'drag image'
    private BufferedImage draggedImage;


    public CTree(
            final DefaultTreeModel treeModel,
            final IDesigner designer) {
        super(treeModel);

        putClientProperty("JTree.lineStyle", "Angled");

        // Make this JTree a drag source
        final DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

        // Also, make this JTree a drag target
        final DropTarget dropTarget = new DropTarget(this, new CDropTargetListener(this, designer));
        dropTarget.setDefaultActions(DnDConstants.ACTION_MOVE);
    }

    @Override
    public void dragGestureRecognized(final DragGestureEvent event) {
        final Point ptDragOrigin = event.getDragOrigin();
        final TreePath path = getPathForLocation(ptDragOrigin.x, ptDragOrigin.y);
        if (path == null) {
            return;
        }
        if (isRootPath(path)) {
            // Ignore user trying to drag the root node
            return;
        }

        // Work out the offset of the drag point from the TreePath bounding rectangle origin
        final Rectangle raPath = getPathBounds(path);
        draggedImageMouseClickPoint.setLocation(ptDragOrigin.x - raPath.x, ptDragOrigin.y - raPath.y);

        // Get the cell renderer (which is a JLabel) for the path being dragged
        final JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(
                this,
                path.getLastPathComponent(),
                // isSelected (dont want a colored background)
                false,
                isExpanded(path),
                getModel().isLeaf(path.getLastPathComponent()),
                // row   (not important for rendering)
                0,
                // hasFocus  (dont want a focus rectangle)
                false
        );

        // The layout manager would normally do this
        lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight());

        // Get a buffered image of the selection for dragging a ghost image
        draggedImage = new BufferedImage((int) raPath.getWidth(), (int) raPath.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics2D g2 = draggedImage.createGraphics();

        // Ask the cell renderer to paint itself into the BufferedImage
        // Make the image ghostlike
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
        lbl.paint(g2);

        // Now paint a gradient UNDER the ghosted JLabel text (but not under the icon if any)
        // Note: this will need tweaking if your icon is not positioned to the left of the text
        final Icon icon = lbl.getIcon();
        final int nStartOfText;
        if (icon == null) {
            nStartOfText = 0;
        } else {
            nStartOfText = icon.getIconWidth() + lbl.getIconTextGap();
        }
        // Make the gradient ghostlike
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));
        g2.setPaint(new GradientPaint(nStartOfText, 0, SystemColor.controlShadow,
                getWidth(), 0, new Color(255, 255, 255, 0)));
        g2.fillRect(nStartOfText, 0, getWidth(), draggedImage.getHeight());

        g2.dispose();

        // Select this path in the tree
        setSelectionPath(path);

        // Wrap the path being transferred into a Transferable object
        final Transferable transferable = new CTransferableTreePath(path);

        // Remember the path being dragged (because if it is being moved, we will have to delete it later)
        draggedPathSource = path;

        // We pass our drag image just in case it IS supported by the platform
        event.startDrag(null, draggedImage, new Point(5, 5), transferable, this);
    }

    // Interface: DragSourceListener
    @Override
    public void dragDropEnd(final DragSourceDropEvent e) {
        if (e.getDropSuccess()) {
            final int nAction = e.getDropAction();
            if (nAction == DnDConstants.ACTION_MOVE) {
                // The dragged item (_pathSource) has been inserted at the target selected by the user.
                // Now it is time to delete it from its original location.
                //System.out.println("REMOVING: " + pathSource.getLastPathComponent());

                // .
                // .. ask your TreeModel to delete the node
                // .
                //((DefaultTreeModel)getModel()).removeNodeFromParent((MutableTreeNode) pathSource.getLastPathComponent());

                //((DefaultMutableTreeNode) pathSource.getLastPathComponent()).removeFromParent();

                draggedPathSource = null;
            }
        }
    }

    @Override
    public void dragEnter(final DragSourceDragEvent e) {
    }

    @Override
    public void dragOver(final DragSourceDragEvent e) {
    }

    @Override
    public void dragExit(final DragSourceEvent e) {
    }

    @Override
    public void dropActionChanged(final DragSourceDragEvent e) {
    }

    /**
     * Autoscroll Interface... The following code was borrowed from the book: Java Swing By Robert Eckstein, Marc Loy & Dave Wood Paperback - 1221 pages 1 Ed edition (September 1998) O'Reilly & Associates; ISBN: 156592455X
     * <p/>
     * The relevant chapter of which can be found at: http://www.oreilly.com/catalog/jswing/chapter/dnd.beta.pdf
     */

    private static final int AUTOSCROLL_MARGIN = 12;

    // Ok, we've been told to scroll because the mouse cursor is in our
    // scroll zone.
    @Override
    public void autoscroll(final Point pt) {
        // Figure out which row we're on.
        int nRow = getRowForLocation(pt.x, pt.y);

        // If we are not on a row then ignore this autoscroll request
        if (nRow < 0) {
            return;
        }

        final Rectangle raOuter = getBounds();
        // Now decide if the row is at the top of the screen or at the
        // bottom. We do this to make the previous row (or the next
        // row) visible as appropriate. If we're at the absolute top or
        // bottom, just return the first or last row respectively.

        // Is row at top of screen?
        if (pt.y + raOuter.y <= AUTOSCROLL_MARGIN) {
            // Yes, scroll up one row
            if (nRow <= 0) {
                nRow = 0;
            } else {
                nRow = nRow - 1;
            }
        } else {
            // No, scroll down one row
            if (nRow < getRowCount() - 1) {
                nRow = nRow + 1;
            }
        }

        scrollRowToVisible(nRow);
    }

    // Calculate the insets for the *JTREE*, not the viewport
    // the tree is in. This makes it a bit messy.
    @Override
    public Insets getAutoscrollInsets() {
        final Rectangle raOuter = getBounds();
        final Rectangle raInner = getParent().getBounds();
        return new Insets(
                raInner.y - raOuter.y + AUTOSCROLL_MARGIN, raInner.x - raOuter.x + AUTOSCROLL_MARGIN,
                raOuter.height - raInner.height - raInner.y + raOuter.y + AUTOSCROLL_MARGIN,
                raOuter.width - raInner.width - raInner.x + raOuter.x + AUTOSCROLL_MARGIN);
    }

    // More helpers...
    boolean isRootPath(final TreePath path) {
        return isRootVisible() && getRowForPath(path) == 0;
    }

    TreePath getPathSource() {
        return draggedPathSource;
    }

    BufferedImage getDraggedImage() {
        return draggedImage;
    }

    Point getDraggedImageMouseClickPoint() {
        return draggedImageMouseClickPoint;
    }
}

