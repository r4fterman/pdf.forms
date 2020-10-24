package org.pdf.forms.gui.designer.listeners;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetSelection;

public class DesignerMouseMotionListener implements MouseMotionListener {

    public static final int DEFAULT_CURSOR = 0;
    public static final int SE_RESIZE_CURSOR = 1;
    public static final int NE_RESIZE_CURSOR = 2;
    public static final int SW_RESIZE_CURSOR = 3;
    public static final int NW_RESIZE_CURSOR = 4;
    public static final int RESIZE_SPLIT_HORIZONTAL_CURSOR = 5;
    public static final int RESIZE_SPLIT_VERTICAL_CURSOR = 6;

    private final IDesigner designerPanel;

    private final DesignerSelectionBox selectionBox;
    private final WidgetSelection widgetSelection;

    public DesignerMouseMotionListener(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        this.widgetSelection = designerPanel.getWidgetSelection();
        this.selectionBox = designerPanel.getSelectionBox();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        designerPanel.updateRulers(new Point(e.getX(), e.getY()));

        if (designerPanel.getWidgetToAdd() == IWidget.NONE) {

            /*
             * nothing selected, so just drag out selection box
             */
            if (designerPanel.getSelectedWidgets().isEmpty()) {
                selectionBox.updateSize(e);
            } else {
                // A widget(s) is being resized
                moveAndResizeSelectedWidget(e);

                if (!designerPanel.isResizing() && !designerPanel.isResizingSplitComponent()) {
                    designerPanel.setCurrentlyDragging(true);
                }
            }
        } else {
            selectionBox.updateSize(e);
        }

        designerPanel.repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        designerPanel.updateRulers(new Point(e.getX(), e.getY()));

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        /*
         * if we're resizing, or adding a new component, then dont change the cursor here,
         * otherwise, set the cusor to whatever it needs to be
         */
        if (!(designerPanel.isResizing() || designerPanel.getWidgetToAdd() != IWidget.NONE)) {

            //            int resizeType = widgetSelection.getResizeType((int) (e.getX() / scale), (int) (e.getY() / scale), selectedWidgets); //@scale
            int resizeType = widgetSelection.getResizeType(e.getX(), e.getY(), selectedWidgets);

            designerPanel.setResizeType(resizeType);

            switch (resizeType) {
                case DesignerMouseMotionListener.DEFAULT_CURSOR: {
                    resizeType = Cursor.DEFAULT_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.SE_RESIZE_CURSOR: {
                    resizeType = Cursor.SE_RESIZE_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.NE_RESIZE_CURSOR: {
                    resizeType = Cursor.NE_RESIZE_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.SW_RESIZE_CURSOR: {
                    resizeType = Cursor.SW_RESIZE_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.NW_RESIZE_CURSOR: {
                    resizeType = Cursor.NW_RESIZE_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.RESIZE_SPLIT_HORIZONTAL_CURSOR: {
                    resizeType = Cursor.W_RESIZE_CURSOR;
                    break;
                }
                case DesignerMouseMotionListener.RESIZE_SPLIT_VERTICAL_CURSOR: {
                    resizeType = Cursor.S_RESIZE_CURSOR;
                    break;
                }
                default:
                    break;
            }

            designerPanel.setCursor(new Cursor(resizeType));
        }
    }

    private void moveAndResizeSelectedWidget(final MouseEvent e) {

        final int resizeType = designerPanel.getResizeType();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        //IWidget w= (IWidget) selectedWidgets.iterator().next();
        final Set<IWidget> widgetsToUse = widgetSelection.getFlatternedWidgets(selectedWidgets); //w.getType() == IWidget.GROUP ? w.getWidgetsInGroup() : selectedWidgets;

        // move a selection or single widget
        if (resizeType == DesignerMouseMotionListener.DEFAULT_CURSOR) {
            widgetSelection.moveWidgets(widgetsToUse, e.getX(), e.getY());
        } else {
            widgetSelection.resizeWidgets(widgetsToUse, designerPanel, e.getX(), e.getY(), resizeType);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgetsToUse);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetsToUse);
    }

}
