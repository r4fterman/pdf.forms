/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
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
* DesignerMouseMotionListener.java
* ---------------
*/
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

    private IDesigner designerPanel;

    private DesignerSelectionBox selectionBox;
    private WidgetSelection widgetSelection;

    public DesignerMouseMotionListener(IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        this.widgetSelection = designerPanel.getWidgetSelection();
        this.selectionBox = designerPanel.getSelectionBox();
    }

    public void mouseDragged(MouseEvent e) {
        designerPanel.updateRulers(new Point(e.getX(), e.getY()));

        if (designerPanel.getWidgetToAdd() == IWidget.NONE) {

            /*
             * nothing selected, so just drag out selection box
             */
            if (designerPanel.getSelectedWidgets().isEmpty()) {
                selectionBox.updateSize(e);
            } else { // A widget(s) is being resized 
                moveAndResizeSelectedWidget(e);

                if (!designerPanel.isResizing() && !designerPanel.isResizingSplitComponent())
                    designerPanel.setCurrentlyDraging(true);
            }
        } else {
            selectionBox.updateSize(e);
        }

        designerPanel.repaint();
    }


    public void mouseMoved(MouseEvent e) {
        designerPanel.updateRulers(new Point(e.getX(), e.getY()));

        Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

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
            }

            designerPanel.setCursor(new Cursor(resizeType));
        }
    }

    private void moveAndResizeSelectedWidget(MouseEvent e) {

        int resizeType = designerPanel.getResizeType();
        Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        //IWidget w= (IWidget) selectedWidgets.iterator().next();
        Set<IWidget> widgetsToUse = widgetSelection.getFlatternedWidgets(selectedWidgets); //w.getType() == IWidget.GROUP ? w.getWidgetsInGroup() : selectedWidgets;

        if (resizeType == DesignerMouseMotionListener.DEFAULT_CURSOR) // move a selection or single widget
            widgetSelection.moveWidgets(widgetsToUse, e.getX(), e.getY());
        else
            widgetSelection.resizeWidgets(widgetsToUse, designerPanel, e.getX(), e.getY(), resizeType);


        designerPanel.getMainFrame().setPropertiesCompound(widgetsToUse);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetsToUse);

    }
}
