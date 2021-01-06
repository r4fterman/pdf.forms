package org.pdf.forms.gui.designer.listeners;

import java.awt.*;
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
    public void mouseDragged(final MouseEvent event) {
        designerPanel.updateRulers(new Point(event.getX(), event.getY()));

        if (designerPanel.getWidgetToAdd() == IWidget.NONE) {
            dragOutSelectionBox(event);
        } else {
            selectionBox.updateSize(event);
        }
        designerPanel.repaint();
    }

    private void dragOutSelectionBox(final MouseEvent event) {
        //nothing selected, so just drag out selection box
        if (designerPanel.getSelectedWidgets().isEmpty()) {
            selectionBox.updateSize(event);
            return;
        }

        // A widget(s) is being resized
        moveAndResizeSelectedWidget(event);
        if (!designerPanel.isResizing() && !designerPanel.isResizingSplitComponent()) {
            designerPanel.setCurrentlyDragging(true);
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        designerPanel.updateRulers(new Point(e.getX(), e.getY()));


        /*
         * if we're resizing, or adding a new component, then dont change the cursor here,
         * otherwise, set the cusor to whatever it needs to be
         */
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        if (!(designerPanel.isResizing() || designerPanel.getWidgetToAdd() != IWidget.NONE)) {

            //int resizeType = widgetSelection.getResizeType((int) (e.getX() / scale), (int) (e.getY() / scale), selectedWidgets); //@scale
            final int resizeType = widgetSelection.getResizeType(e.getX(), e.getY(), selectedWidgets);
            designerPanel.setResizeType(resizeType);

            final int cursorType = getCursorType(resizeType);
            designerPanel.setCursor(new Cursor(cursorType));
        }
    }

    private int getCursorType(final int resizeType) {
        switch (resizeType) {
            case DesignerMouseMotionListener.DEFAULT_CURSOR:
                return Cursor.DEFAULT_CURSOR;
            case DesignerMouseMotionListener.SE_RESIZE_CURSOR:
                return Cursor.SE_RESIZE_CURSOR;
            case DesignerMouseMotionListener.NE_RESIZE_CURSOR:
                return Cursor.NE_RESIZE_CURSOR;
            case DesignerMouseMotionListener.SW_RESIZE_CURSOR:
                return Cursor.SW_RESIZE_CURSOR;
            case DesignerMouseMotionListener.NW_RESIZE_CURSOR:
                return Cursor.NW_RESIZE_CURSOR;
            case DesignerMouseMotionListener.RESIZE_SPLIT_HORIZONTAL_CURSOR:
                return Cursor.W_RESIZE_CURSOR;
            case DesignerMouseMotionListener.RESIZE_SPLIT_VERTICAL_CURSOR:
                return Cursor.S_RESIZE_CURSOR;
            default:
                return resizeType;
        }
    }

    private void moveAndResizeSelectedWidget(final MouseEvent mouseEvent) {
        final int resizeType = designerPanel.getResizeType();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        //IWidget w= (IWidget) selectedWidgets.iterator().next();
        final Set<IWidget> widgetsToUse = widgetSelection.getFlattenedWidgets(selectedWidgets);
        //w.getType() == IWidget.GROUP ? w.getWidgetsInGroup() : selectedWidgets;

        // move a selection or single widget
        if (resizeType == DesignerMouseMotionListener.DEFAULT_CURSOR) {
            widgetSelection.moveWidgets(widgetsToUse, mouseEvent.getX(), mouseEvent.getY());
        } else {
            widgetSelection.resizeWidgets(widgetsToUse,
                    designerPanel,
                    mouseEvent.getX(),
                    mouseEvent.getY(),
                    resizeType);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgetsToUse);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetsToUse);
    }

}
