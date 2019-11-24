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
* CDropTargetListener.java
* ---------------
*/
package org.pdf.forms.gui.hierarchy.tree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// DropTargetListener interface object...
class CDropTargetListener implements DropTargetListener {

    private final Logger logger = LoggerFactory.getLogger(CDropTargetListener.class);

    // Fields...
    private TreePath pathLast = null;
    private final Rectangle2D raCueLine = new Rectangle2D.Float();
    private Rectangle2D raGhost = new Rectangle2D.Float();
    private final Color colorCueLine;
    private Point ptLast = new Point();
    private final Timer timerHover;
    private final CTree cTree;
    private final IDesigner designer;

    // Constructor...
    CDropTargetListener(
            final CTree cTreeRef,
            final IDesigner designer) {
        this.cTree = cTreeRef;
        this.designer = designer;

        colorCueLine = new Color(
                SystemColor.controlShadow.getRed(),
                SystemColor.controlShadow.getGreen(),
                SystemColor.controlShadow.getBlue(),
                64
        );

        // Set up a hover timer, so that a node will be automatically expanded or collapsed
        // if the user lingers on it for more than a short time
        timerHover = new Timer(500, e -> {
            //_nLeftRight = 0; // Reset left/right movement trend
            if (cTree.isRootPath(pathLast)) {
                return;
                // Do nothing if we are hovering over the root node
            }
            if (!cTree.isExpanded(pathLast)) {
                cTree.expandPath(pathLast);
            }
        });
        timerHover.setRepeats(false);
        // Set timer to one-shot mode
    }

    // DropTargetListener interface
    @Override
    public void dragEnter(final DropTargetDragEvent e) {
        if (isDragNotAcceptable(e)) {
            e.rejectDrag();
        } else {
            e.acceptDrag(e.getDropAction());
        }
    }

    @Override
    public void dragExit(final DropTargetEvent e) {
        if (!DragSource.isDragImageSupported()) {
            cTree.repaint(raGhost.getBounds());
        }
    }

    /**
     * This is where the ghost image is drawn.
     */
    @Override
    public void dragOver(final DropTargetDragEvent e) {
        // Even if the mouse is not moving, this method is still invoked 10 times per second
        final Point pt = e.getLocation();
        if (pt.equals(ptLast)) {
            return;
        }

        ptLast = pt;

        final Graphics2D g2 = (Graphics2D) cTree.getGraphics();

        // If a drag image is not supported by the platform, then draw my own drag image
        if (!DragSource.isDragImageSupported()) {
            cTree.paintImmediately(raGhost.getBounds());    // Rub out the last ghost image and cue line
            // And remember where we are about to draw the new ghost image
            raGhost.setRect(pt.x - cTree.getPtOffset().x, pt.y - cTree.getPtOffset().y, cTree.getImgGhost().getWidth(), cTree.getImgGhost().getHeight());
            g2.drawImage(cTree.getImgGhost(), AffineTransform.getTranslateInstance(raGhost.getX(), raGhost.getY()), null);
        } else {
            // Just rub out the last cue line
            cTree.paintImmediately(raCueLine.getBounds());
        }

        final TreePath path = cTree.getClosestPathForLocation(pt.x, pt.y);
        if (!(path == pathLast)) {
            pathLast = path;
            timerHover.restart();
        }

        // In any case draw (over the ghost image if necessary) a cue line indicating where a drop will occur
        final Rectangle raPath = cTree.getPathBounds(path);

        final Rectangle2D.Double rect = new Rectangle2D.Double(0, raPath.y + (int) raPath.getHeight(), cTree.getWidth(), 2);
        raCueLine.setRect(rect);

        g2.setColor(colorCueLine);
        g2.fill(raCueLine);

        // And include the cue line in the area to be rubbed out next time
        raGhost = raGhost.createUnion(raCueLine);

        final DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        final DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode) cTree.getPathSource().getLastPathComponent();

        final List<TreeNode> flattenedTreeItems = new ArrayList<>();
        getFlattenedTreeNodes((TreeNode) cTree.getModel().getRoot(), flattenedTreeItems);

        final Object targetUserObject = targetNode.getUserObject();
        final Object sourceUserObject = sourceNode.getUserObject();

        // check to see if a page is being moved
        if (sourceUserObject instanceof Page) {
            // we are moving a page
            if (targetUserObject.equals("Document Root")) {
                // we are dropping it at the document root level which means it is definitely going to be dropped at index 0
                final int targetIndex = targetNode.getIndex(targetNode);
                final int sourceIndex = targetNode.getIndex(sourceNode);

                if (targetIndex == sourceIndex) {
                    // looks like we are trying to drop the page in the same place it already is
                    e.rejectDrag();
                } else {
                    e.acceptDrag(e.getDropAction());
                }
            } else {
                /*
                 * it looks as though we are trying to drop a page inside another page, and this would not be allowed, however, if the targetUserObject
                 * is the last widget in its page, then we want to drop the page below this widget
                 */
                if (targetUserObject instanceof IWidget) {
                    final int locationInFlattenedList = flattenedTreeItems.indexOf(targetNode);

                    final int nextIndex = locationInFlattenedList + 1;
                    if (nextIndex < flattenedTreeItems.size()) {
                        /* get the item imediately below the this widgets */
                        final DefaultMutableTreeNode nextItemInList = (DefaultMutableTreeNode) flattenedTreeItems.get(nextIndex);

                        if (nextItemInList.getUserObject() instanceof Page) {
                            // we are trying to drop this page directly above another page, so this is allowed
                            e.acceptDrag(e.getDropAction());
                        } else {
                            e.rejectDrag();
                        }
                    } else {
                        e.rejectDrag();
                    }
                } else {
                    e.rejectDrag();
                }
            }
        } else {
            // we must be moving a widget
            if (targetUserObject instanceof Page) {
                // we are trying to drop a widget on a page which is allowed
                e.acceptDrag(e.getDropAction());
            } else {
                // we are not trying to drop a widget on a page, so we need to check if it is being dropped in a group
                final TreeNode targetParent = targetNode.getParent();
                if (targetParent != null) {
                    int targetIndex = targetParent.getIndex(targetNode);
                    final int sourceIndex = sourceNode.getParent().getIndex(sourceNode);

                    if (sourceIndex > targetIndex && sourceNode.getParent() == targetNode.getParent()
                            && (!(targetUserObject instanceof IWidget) || ((IWidget) targetUserObject).getType() != IWidget.GROUP)) {

                        targetIndex++;
                    }

                    if (targetIndex == sourceIndex && sourceNode.getParent() == targetNode.getParent()) {
                        // looks like we are trying to drop the widget in the same place it already is
                        e.rejectDrag();
                    } else {
                        e.acceptDrag(e.getDropAction());
                    }
                } else {
                    e.rejectDrag();
                }
            }
        }
    }

    @Override
    public void drop(final DropTargetDropEvent event) {
        // Prevent hover timer from doing an unwanted expandPath or collapsePath
        timerHover.stop();

        if (!isDropAcceptable(event)) {
            event.rejectDrop();
            return;
        }

        event.acceptDrop(event.getDropAction());

        final Transferable transferable = event.getTransferable();

        final DataFlavor[] flavors = transferable.getTransferDataFlavors();
        for (final DataFlavor flavor : flavors) {
            if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
                try {
                    final Point pt = event.getLocation();
                    final TreePath pathTarget = cTree.getClosestPathForLocation(pt.x, pt.y);
                    final TreePath pathSource = (TreePath) transferable.getTransferData(flavor);

                    /* clear the ghost image */
                    cTree.repaint(raGhost.getBounds());

                    final IMainFrame mainFrame = designer.getMainFrame();

                    final DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode) pathSource.getLastPathComponent();
                    final DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) pathTarget.getLastPathComponent();

                    final Object sourceUserObject = sourceNode.getUserObject();
                    final Object targetUserObject = targetNode.getUserObject();

                    // check to see if a page is being moved
                    if (sourceUserObject instanceof Page) { // we are moving a page
                        final DefaultMutableTreeNode sourceParent = (DefaultMutableTreeNode) sourceNode.getParent();
                        final int sourceIndex = sourceParent.getIndex(sourceNode);
                        int targeteIndex = 0;

                        if (!targetUserObject.equals("Document Root")) {
                            final List<TreeNode> flattenedTreeItems = new ArrayList<>();
                            getFlattenedTreeNodes((TreeNode) cTree.getModel().getRoot(), flattenedTreeItems);

                            final int locationInFlattenedList = flattenedTreeItems.indexOf(targetNode);

                            final int nextIndex = locationInFlattenedList + 1;
                            if (nextIndex < flattenedTreeItems.size()) {
                                /* get the item imediately below the this widgets */
                                final DefaultMutableTreeNode nextItemInList = (DefaultMutableTreeNode) flattenedTreeItems.get(nextIndex);

                                targeteIndex = nextItemInList.getParent().getIndex(nextItemInList);

                                if (targeteIndex > sourceIndex) {
                                    targeteIndex--;
                                }
                            }
                        }

                        mainFrame.getFormsDocument().movePage(sourceIndex, targeteIndex);

                        mainFrame.setCurrentPage(targeteIndex + 1);
                        mainFrame.displayPage(mainFrame.getCurrentPage());

                    } else { // we must be moving a widget
                        final Object[] sourceObjectPath = sourceNode.getUserObjectPath();
                        final Object[] targetObjectPath = targetNode.getUserObjectPath();

                        final List<IWidget> sourceWidgetList = getWidgetsList(sourceObjectPath, false);
                        final List<IWidget> targetWidgetList = getWidgetsList(targetObjectPath, true);

                        Collections.reverse(targetWidgetList);

                        final int sourceIndex = sourceWidgetList.indexOf(sourceUserObject);
                        int targetIndex;

                        if (targetUserObject instanceof Page) {
                            // dropping at the start of the targetPage
                            targetIndex = 0;
                        } else {
                            final IWidget targetWidget = (IWidget) targetUserObject;

                            if (targetWidget.getType() == IWidget.GROUP) {
                                // dropping at start of group
                                targetIndex = 0;
                            } else {
                                // dropping in the middle of other widgets
                                targetIndex = targetWidgetList.indexOf(targetUserObject);
                                if (targetWidgetList == sourceWidgetList) {
                                    if (targetIndex < sourceIndex) {
                                        targetIndex++;
                                    }
                                } else {
                                    targetIndex++;
                                }
                            }
                        }

                        if (targetWidgetList == sourceWidgetList) {
                            moveWidget(sourceIndex, targetIndex, sourceWidgetList);
                        } else {
                            sourceWidgetList.remove(sourceUserObject);
                            targetWidgetList.add(targetIndex, (IWidget) sourceUserObject);

                            /*
                             * if a radio button widget is moving page, then we need to see if the page it is moving
                             * to already has any ButtonGroup's, and if so add it to one, but if not create a
                             * new ButtonGroup on that page, and add the widget to it.
                             *
                             * todo do this test for check boxs too
                             */
                            if (sourceUserObject instanceof IWidget
                                    && ((IWidget) sourceUserObject).getType() == IWidget.RADIO_BUTTON) {
                                // we are moving a radio button

                                final Page sourcePage = (Page) sourceObjectPath[1];
                                final Page targetPage = (Page) targetObjectPath[1];

                                if (sourcePage != targetPage) { // we are moving page
                                    final List<ButtonGroup> radioButtonGroups = targetPage.getRadioButtonGroups();
                                    final ButtonGroup rbg;
                                    if (radioButtonGroups.isEmpty()) {
                                        /*
                                         * there are no radio button groups currently on this page, so we need to
                                         * create a new on
                                         */
                                        rbg = new ButtonGroup(IWidget.RADIO_BUTTON);
                                        radioButtonGroups.add(rbg);
                                    } else {
                                        /* add the radio button to the last ButtonGroup in the list */
                                        rbg = radioButtonGroups.get(radioButtonGroups.size() - 1);
                                    }

                                    /* set the radio buttons new group */
                                    ((RadioButtonWidget) sourceUserObject).setRadioButtonGroupName(rbg.getName());
                                }
                            }

                            /*
                             * check to see if we are moving the last widget out of a group, if we are we need to
                             * remove the empty group from its parent
                             */
                            final Object parent = sourceObjectPath[sourceObjectPath.length - 2];
                            if (sourceWidgetList.isEmpty() && parent instanceof IWidget) {

                                /*
                                 * the widget list is empty, and the parent of the widget moved is a group widget,
                                 * therefore we need to remove this group widget from its parent
                                 */
                                final IWidget groupWidget = (IWidget) parent;
                                if (groupWidget.getType() == IWidget.GROUP) {

                                    /* go back up the tree to find teh parent of the group widget */
                                    final Object parentOfGroup = sourceObjectPath[sourceObjectPath.length - 3];
                                    if (parentOfGroup instanceof Page) { // remove the group from its parent page
                                        ((Page) parentOfGroup).getWidgets().remove(groupWidget);
                                    } else { // remove the group from its parent group widget
                                        ((IWidget) parentOfGroup).getWidgetsInGroup().remove(groupWidget);
                                    }

                                    /* set no widgets selected */
                                    designer.setSelectedWidgets(new HashSet<>());
                                }
                            }
                        }

                        Collections.reverse(targetWidgetList);
                    }

                    mainFrame.updateHierarchy();

                    designer.repaint();

                    break; // No need to check remaining flavors
                } catch (final UnsupportedFlavorException | IOException e) {
                    logger.error("Error during drag and drop", e);
                    event.dropComplete(false);
                    return;
                }
            }
        }

        event.dropComplete(true);
    }

    private List<IWidget> getWidgetsList(
            final Object[] objectPath,
            final boolean isTarget) {
        for (int j = objectPath.length - 1; j >= 0; j--) {
            final Object object = objectPath[j];
            if (object instanceof Page) {
                return ((Page) object).getWidgets();
            }

            final IWidget widget = (IWidget) object;
            if (widget.getType() == IWidget.GROUP && (isTarget || widget != objectPath[objectPath.length - 1])) {
                return widget.getWidgetsInGroup();
            }
        }

        return null;
    }

    private void moveWidget(
            final int fromIndex,
            final int toIndex,
            final List<IWidget> page) {
        if (fromIndex > toIndex) {
            final IWidget objectToMove = page.get(fromIndex);
            page.add(toIndex, objectToMove);

            page.remove(page.lastIndexOf(objectToMove));
        } else {
            final int idx = toIndex + 1;
            final IWidget objectToMove = page.get(fromIndex);
            page.add(idx, objectToMove);

            page.remove(objectToMove);
        }
    }

    private void getFlattenedTreeNodes(
            final TreeNode theNode,
            final List<TreeNode> items) {
        // add the item
        items.add(theNode);

        // recursion
        for (final Enumeration theChildren = theNode.children(); theChildren.hasMoreElements(); ) {
            getFlattenedTreeNodes((TreeNode) theChildren.nextElement(), items);
        }
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent e) {
        if (isDragNotAcceptable(e)) {
            e.rejectDrag();
        } else {
            e.acceptDrag(e.getDropAction());
        }
    }

    // Helpers...
    private boolean isDragNotAcceptable(final DropTargetDragEvent e) {
        // Only accept COPY or MOVE gestures (ie LINK is not supported)
        if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            return true;
        }

        // Only accept this particular flavor
        return !e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR);

    }

    private boolean isDropAcceptable(final DropTargetDropEvent e) {
        // Only accept COPY or MOVE gestures (ie LINK is not supported)
        if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            return false;
        }

        // Only accept this particular flavor
        return e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR);
    }
}
