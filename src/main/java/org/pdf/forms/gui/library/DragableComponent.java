/**
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
* DragableComponent.java
* ---------------
*/
package org.pdf.forms.gui.library;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetSelection;

public class DragableComponent implements DragGestureListener, DragSourceListener {

    private DragSource dragSource;
    private IDesigner designerPanel;

    public DragableComponent(DragSource dragSource, IDesigner designerPanel) {
        this.dragSource = dragSource;
        this.designerPanel = designerPanel;
    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        //designerPanel.setWidgetToAdd(IWidget.TEXT);

        Transferable t = new StringSelection("aString");
        dragSource.startDrag(dge, null, t, this);

//        Transferable t = new StringSelection("aString");
//        dragSource.startDrag(dge, null, new Transferable(){
//
//            public DataFlavor[] getTransferDataFlavors() {
//                return new DataFlavor[]{
//                        new DataFlavor(IWidget.class, "X-designer/IWidget; class=<org.pdf.forms.widgets.IWidget>")
//                };
//            }
//
//            public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
//                return true;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
//                return designerPanel.getSelectedWidgets();
//            }
//        }, this);
    }

    public void dragEnter(DragSourceDragEvent dsde) {

        int widgetToAdd = designerPanel.getWidgetToAdd();

        IWidget widget;
        if (widgetToAdd == IWidget.RADIO_BUTTON) {
            IMainFrame mainFrame = designerPanel.getMainFrame();
            widget = WidgetFactory.createRadioButtonWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);

        } else if (widgetToAdd == IWidget.CHECK_BOX) {
            IMainFrame mainFrame = designerPanel.getMainFrame();
            widget = WidgetFactory.createCheckBoxWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);
        } else {
            widget = WidgetFactory.createWidget(widgetToAdd, (Rectangle) null);
        }

        Set list = new HashSet();
        if (widget != null)
            list.add(widget);

        designerPanel.setSelectedWidgets(list);

        dsde.getDragSourceContext().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void dragOver(DragSourceDragEvent dsde) {
        Point point = SwingUtilities.convertPoint(null, dsde.getLocation(), (Component) designerPanel);
        designerPanel.setDragBoxLocation(point);
        designerPanel.updateRulers(point);
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
//        System.out.println("dropActionChanged");
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
        //System.out.println("dragDropEnd");

        IWidget widget = (IWidget) designerPanel.getSelectedWidgets().iterator().next();

        Dimension boxSize = widget.getBoxSize();
        Point location = SwingUtilities.convertPoint(null, dsde.getLocation(), (Component) designerPanel);

        widget.setX((location.x - boxSize.width / 2) + WidgetSelection.BOX_MARGIN);
        widget.setY((location.y - boxSize.height / 2) + WidgetSelection.BOX_MARGIN);

        designerPanel.setDragBoxLocation(null);
        designerPanel.addWidget(widget);

        designerPanel.setWidgetToAdd(IWidget.NONE);
        designerPanel.resetPaletteButtons();

        designerPanel.repaint();

        Set set = new HashSet();
        set.add(widget);

        designerPanel.getMainFrame().setPropertiesCompound(set);
        designerPanel.getMainFrame().setPropertiesToolBar(set);

        designerPanel.setWidgetToAdd(IWidget.NONE);
    }

    public void dragExit(DragSourceEvent dse) {
//        System.out.println("dragExit");
        dse.getDragSourceContext().setCursor(null);
        designerPanel.setDragBoxLocation(null);
    }
}
