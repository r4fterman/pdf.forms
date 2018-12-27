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

    private final DragSource dragSource;
    private final IDesigner designerPanel;

    DragableComponent(
            final DragSource dragSource,
            final IDesigner designerPanel) {
        this.dragSource = dragSource;
        this.designerPanel = designerPanel;
    }

    @Override
    public void dragGestureRecognized(final DragGestureEvent dge) {
        //designerPanel.setWidgetToAdd(IWidget.TEXT);

        final Transferable t = new StringSelection("aString");
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

    @Override
    public void dragEnter(final DragSourceDragEvent dsde) {
        final int widgetToAdd = designerPanel.getWidgetToAdd();

        final IWidget widget;
        if (widgetToAdd == IWidget.RADIO_BUTTON) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            widget = WidgetFactory.createRadioButtonWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);

        } else if (widgetToAdd == IWidget.CHECK_BOX) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            widget = WidgetFactory.createCheckBoxWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);
        } else {
            widget = WidgetFactory.createWidget(widgetToAdd, (Rectangle) null);
        }

        final Set<IWidget> list = new HashSet<>();
        if (widget != null) {
            list.add(widget);
        }

        designerPanel.setSelectedWidgets(list);

        dsde.getDragSourceContext().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void dragOver(final DragSourceDragEvent dsde) {
        final Point point = SwingUtilities.convertPoint(null, dsde.getLocation(), (Component) designerPanel);
        designerPanel.setDragBoxLocation(point);
        designerPanel.updateRulers(point);
    }

    @Override
    public void dropActionChanged(final DragSourceDragEvent dsde) {
    }

    @Override
    public void dragDropEnd(final DragSourceDropEvent dsde) {
        final IWidget widget = designerPanel.getSelectedWidgets().iterator().next();

        final Dimension boxSize = widget.getBoxSize();
        final Point location = SwingUtilities.convertPoint(null, dsde.getLocation(), (Component) designerPanel);

        widget.setX((location.x - boxSize.width / 2) + WidgetSelection.BOX_MARGIN);
        widget.setY((location.y - boxSize.height / 2) + WidgetSelection.BOX_MARGIN);

        designerPanel.setDragBoxLocation(null);
        designerPanel.addWidget(widget);

        designerPanel.setWidgetToAdd(IWidget.NONE);
        designerPanel.resetPaletteButtons();

        designerPanel.repaint();

        final Set<IWidget> set = new HashSet<>();
        set.add(widget);

        designerPanel.getMainFrame().setPropertiesCompound(set);
        designerPanel.getMainFrame().setPropertiesToolBar(set);

        designerPanel.setWidgetToAdd(IWidget.NONE);
    }

    @Override
    public void dragExit(final DragSourceEvent dse) {
        dse.getDragSourceContext().setCursor(null);
        designerPanel.setDragBoxLocation(null);
    }
}
