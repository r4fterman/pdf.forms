package org.pdf.forms.gui.hierarchy.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CDropTargetListener implements DropTargetListener {

    private final Logger logger = LoggerFactory.getLogger(CDropTargetListener.class);

    private TreePath pathLast = null;
    private final Rectangle2D raCueLine = new Rectangle2D.Float();
    private Rectangle2D raGhost = new Rectangle2D.Float();
    private final Color colorCueLine;
    private Point ptLast = new Point();
    private final Timer timerHover;
    private final CTree cTree;
    private final IDesigner designer;

    CDropTargetListener(
            final CTree cTree,
            final IDesigner designer) {
        this.cTree = cTree;
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
            if (this.cTree.isRootPath(pathLast)) {
                // Do nothing if we are hovering over the root node
                return;
            }
            if (!this.cTree.isExpanded(pathLast)) {
                this.cTree.expandPath(pathLast);
            }
        });
        // Set timer to one-shot mode
        timerHover.setRepeats(false);
    }

    @Override
    public void dragEnter(final DropTargetDragEvent e) {
        acceptOrRejectDrag(e, isDragNotAcceptable(e));
    }

    @Override
    public void dragExit(final DropTargetEvent e) {
        if (!DragSource.isDragImageSupported()) {
            cTree.repaint(raGhost.getBounds());
        }
    }

    @Override
    public void dragOver(final DropTargetDragEvent e) {
        // This is where the ghost image is drawn.
        // Even if the mouse is not moving, this method is still invoked 10 times per second
        final Point pt = e.getLocation();
        if (pt.equals(ptLast)) {
            return;
        }
        ptLast = pt;

        final Graphics2D g2 = (Graphics2D) cTree.getGraphics();

        // If a drag image is not supported by the platform, then draw my own drag image
        if (!DragSource.isDragImageSupported()) {
            // Rub out the last ghost image and cue line
            cTree.paintImmediately(raGhost.getBounds());
            // And remember where we are about to draw the new ghost image
            final int x = pt.x - cTree.getPtOffset().x;
            final int y = pt.y - cTree.getPtOffset().y;
            raGhost.setRect(x, y, cTree.getImgGhost().getWidth(), cTree.getImgGhost().getHeight());
            g2.drawImage(cTree.getImgGhost(), AffineTransform.getTranslateInstance(raGhost.getX(), raGhost.getY()), null);
        } else {
            // Just rub out the last cue line
            cTree.paintImmediately(raCueLine.getBounds());
        }

        final TreePath path = cTree.getClosestPathForLocation(pt.x, pt.y);
        if (path != pathLast) {
            pathLast = path;
            timerHover.restart();
        }

        // In any case draw (over the ghost image if necessary) a cue line indicating where a drop will occur
        final Rectangle raPath = Objects.requireNonNullElse(cTree.getPathBounds(path), new Rectangle(0, 0, 0, 0));

        final double y = raPath.y + raPath.getHeight();
        final Rectangle2D.Double rect = new Rectangle2D.Double(0, y, cTree.getWidth(), 2);
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
            dragOverPage(e, targetNode, sourceNode, flattenedTreeItems, targetUserObject);
        } else {
            dragOverWidget(e, targetNode, sourceNode, targetUserObject);
        }
    }

    private void dragOverWidget(
            final DropTargetDragEvent e,
            final DefaultMutableTreeNode targetNode,
            final DefaultMutableTreeNode sourceNode,
            final Object targetUserObject) {
        // we must be moving a widget
        if (targetUserObject instanceof Page) {
            // we are trying to drop a widget on a page which is allowed
            e.acceptDrag(e.getDropAction());
            return;
        }

        // we are not trying to drop a widget on a page, so we need to check if it is being dropped in a group
        final TreeNode targetParent = targetNode.getParent();
        if (targetParent == null) {
            e.rejectDrag();
            return;
        }

        int targetIndex = targetParent.getIndex(targetNode);
        final int sourceIndex = sourceNode.getParent().getIndex(sourceNode);

        if (sourceIndex > targetIndex && sourceNode.getParent() == targetNode.getParent()
                && (!(targetUserObject instanceof IWidget) || ((IWidget) targetUserObject).getType() != IWidget.GROUP)) {
            targetIndex++;
        }

        final boolean rejectDrag = targetIndex == sourceIndex && sourceNode.getParent() == targetNode.getParent();
        acceptOrRejectDrag(e, rejectDrag);
    }

    private void dragOverPage(
            final DropTargetDragEvent e,
            final DefaultMutableTreeNode targetNode,
            final DefaultMutableTreeNode sourceNode,
            final List<TreeNode> flattenedTreeItems,
            final Object targetUserObject) {
        // we are moving a page
        if (targetUserObject.equals("Document Root")) {
            // we are dropping it at the document root level which means it is definitely going to be dropped at index 0
            final int targetIndex = targetNode.getIndex(targetNode);
            final int sourceIndex = targetNode.getIndex(sourceNode);

            // looks like we are trying to drop the page in the same place it already is
            final boolean shouldRejectDrag = targetIndex == sourceIndex;
            acceptOrRejectDrag(e, shouldRejectDrag);
            return;
        }

        /*
         * it looks as though we are trying to drop a page inside another page,
         * and this would not be allowed, however,
         * if the targetUserObject is the last widget in its page,
         * then we want to drop the page below this widget
         */
        if (targetUserObject instanceof IWidget) {
            final int locationInFlattenedList = flattenedTreeItems.indexOf(targetNode);

            final int nextIndex = locationInFlattenedList + 1;
            if (nextIndex < flattenedTreeItems.size()) {
                /* get the item immediately below the this widgets */
                final DefaultMutableTreeNode nextItemInList = (DefaultMutableTreeNode) flattenedTreeItems.get(nextIndex);

                // we are trying to drop this page directly above another page, so this is allowed
                final boolean dropOfAPageOverAnotherPage = nextItemInList.getUserObject() instanceof Page;
                acceptOrRejectDrag(e, !dropOfAPageOverAnotherPage);
                return;
            }
        }
        e.rejectDrag();
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
        Arrays.stream(flavors)
                .filter(flavor -> flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
                .map(flavor -> dropTransferable(transferable, flavor, event))
                .filter(result -> !result)
                .findAny()
                .ifPresentOrElse(
                        falseResult -> event.dropComplete(false),
                        () -> event.dropComplete(true));
    }

    private boolean dropTransferable(
            final Transferable transferable,
            final DataFlavor flavor,
            final DropTargetDropEvent event) {
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
            if (sourceUserObject instanceof Page) {
                dropPage(mainFrame, sourceNode, targetNode, targetUserObject);
            } else {
                if (sourceUserObject instanceof IWidget
                        && targetUserObject instanceof IWidget) {
                    dropWidget(sourceNode, (IWidget) sourceUserObject, targetNode, (IWidget) targetUserObject);
                }
            }

            mainFrame.updateHierarchy();

            designer.repaint();
            return true;
        } catch (final UnsupportedFlavorException | IOException e) {
            logger.error("Error during drag and drop", e);
            return false;
        }
    }

    private void dropWidget(
            final DefaultMutableTreeNode sourceNode,
            final IWidget sourceUserObject,
            final DefaultMutableTreeNode targetNode,
            final IWidget targetUserObject) {
        // we must be moving a widget
        final Object[] sourceObjectPath = sourceNode.getUserObjectPath();
        final Object[] targetObjectPath = targetNode.getUserObjectPath();

        final List<IWidget> sourceWidgetList = getWidgetsList(sourceObjectPath, false);
        final List<IWidget> targetWidgetList = getWidgetsList(targetObjectPath, true);

        Collections.reverse(targetWidgetList);

        final int sourceIndex = sourceWidgetList.indexOf(sourceUserObject);
        final int targetIndex = getTargetIndex(targetUserObject, sourceWidgetList, targetWidgetList, sourceIndex);

        if (targetWidgetList == sourceWidgetList) {
            dragOverWidget(sourceIndex, targetIndex, sourceWidgetList);
            return;
        }

        sourceWidgetList.remove(sourceUserObject);
        targetWidgetList.add(targetIndex, sourceUserObject);

        /*
         * if a radio button widget is moving page, then we need to see if the page it is moving
         * to already has any ButtonGroup's, and if so add it to one, but if not create a
         * new ButtonGroup on that page, and add the widget to it.
         *
         * todo do this test for check boxes too
         */
        if (sourceUserObject != null
                && sourceUserObject.getType() == IWidget.RADIO_BUTTON) {
            final Object source = sourceObjectPath[1];
            final Object target = targetObjectPath[1];
            if (source instanceof Page
                    && target instanceof Page) {
                dropRadioButtonOverPage((RadioButtonWidget) sourceUserObject, (Page) source, (Page) target);
            }
        }

        removeEmptyGroupFromParent(sourceObjectPath, sourceWidgetList, targetWidgetList);
    }

    private void removeEmptyGroupFromParent(
            final Object[] sourceObjectPath,
            final List<IWidget> sourceWidgetList,
            final List<IWidget> targetWidgetList) {
        /*
         * check to see if we are moving the last widget out of a group, if we do we need to
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
                if (parentOfGroup instanceof Page) {
                    // remove the group from its parent page
                    ((Page) parentOfGroup).getWidgets().remove(groupWidget);
                } else {
                    // remove the group from its parent group widget
                    ((IWidget) parentOfGroup).getWidgetsInGroup().remove(groupWidget);
                }

                // set no widgets selected
                designer.setSelectedWidgets(Collections.emptySet());
            }
        }
        Collections.reverse(targetWidgetList);
    }

    private void dropRadioButtonOverPage(
            final RadioButtonWidget sourceUserObject,
            final Page sourcePage,
            final Page targetPage) {
        if (sourcePage != targetPage) {
            // we are dropping over a page
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
            sourceUserObject.setRadioButtonGroupName(rbg.getName());
        }
    }

    private int getTargetIndex(
            final IWidget targetUserObject,
            final List<IWidget> sourceWidgetList,
            final List<IWidget> targetWidgetList,
            final int sourceIndex) {
        // dropping at the start of the targetPage
        // OR dropping in the middle of other widgets
        if (targetUserObject instanceof Page
                || targetUserObject.getType() == IWidget.GROUP) {
            // dropping at start of group
            return 0;
        }

        final int targetIndex = targetWidgetList.indexOf(targetUserObject);
        if (targetWidgetList == sourceWidgetList) {
            if (targetIndex < sourceIndex) {
                return targetIndex + 1;
            }
        } else {
            return targetIndex + 1;
        }
        return targetIndex;
    }

    private void dropPage(
            final IMainFrame mainFrame,
            final DefaultMutableTreeNode sourceNode,
            final DefaultMutableTreeNode targetNode,
            final Object targetUserObject) {
        // we are moving a page
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
    }

    private List<IWidget> getWidgetsList(
            final Object[] objectPath,
            final boolean isTarget) {
        for (int i = objectPath.length - 1; i >= 0; i--) {
            final Object object = objectPath[i];
            if (object instanceof Page) {
                return ((Page) object).getWidgets();
            }

            final IWidget widget = (IWidget) object;
            if (widget.getType() == IWidget.GROUP && (isTarget || widget != objectPath[objectPath.length - 1])) {
                return widget.getWidgetsInGroup();
            }
        }

        return Collections.emptyList();
    }

    private void dragOverWidget(
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
        items.add(theNode);

        final Enumeration<? extends TreeNode> children = theNode.children();
        while (children.hasMoreElements()) {
            final TreeNode treeNode = children.nextElement();
            getFlattenedTreeNodes(treeNode, items);
        }
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent e) {
        acceptOrRejectDrag(e, isDragNotAcceptable(e));
    }

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

    private void acceptOrRejectDrag(
            final DropTargetDragEvent e,
            final boolean shouldRejectDrag) {
        if (shouldRejectDrag) {
            e.rejectDrag();
        } else {
            e.acceptDrag(e.getDropAction());
        }
    }
}
