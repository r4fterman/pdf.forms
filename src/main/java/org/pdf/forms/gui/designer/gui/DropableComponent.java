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
* DropableComponent.java
* ---------------
*/
package org.pdf.forms.gui.designer.gui;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DropableComponent implements DropTargetListener {
    private IDesigner designerPanel;
    private IWidget widget;

    public DropableComponent(IDesigner designer) {
        this.designerPanel = designer;
    }

    public void dragEnter(DropTargetDragEvent evt) {
        // Called when the user is dragging and enters this drop target.

//        System.out.println("entering code "+evt.getCurrentDataFlavorsAsList());
//
//        int widgetToAdd = designerPanel.getWidgetToAdd();
//
//        if (widgetToAdd == IWidget.NONE) { // Not a widget so must be something external
//            System.out.println("dragEnter widget to add == NONE");
//        } else { // adding a widget from the library
//
//
//
//            if (widgetToAdd == IWidget.RADIO_BUTTON) {
//                IMainFrame mainFrame = designerPanel.getMainFrame();
//                widget = WidgetFactory.createRadioButtonWidget(
//                        mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);
//
//            } else if (widgetToAdd == IWidget.CHECK_BOX) {
//                IMainFrame mainFrame = designerPanel.getMainFrame();
//                widget = WidgetFactory.createCheckBoxWidget(
//                        mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()), null);
//            } else {
//                widget = WidgetFactory.createWidget(widgetToAdd, (Rectangle) null);
//            }
//
//            Set list = new HashSet();
//            if (widget != null)
//                list.add(widget);
//
//            designerPanel.setSelectedWidgets(list);
//        }
    }

    public void dragOver(DropTargetDragEvent evt) {
        int widgetToAdd = designerPanel.getWidgetToAdd();

        //System.out.println("widgetToAdd = " + widgetToAdd);

//        if (widgetToAdd != IWidget.NONE) { // adding a widget from the library
//            System.out.println("accepting drag");
//            evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
//            designerPanel.setDragBoxLocation(evt.getLocation());
//            designerPanel.updateRulers(evt.getLocation());
//        } else {
//            System.out.println("dragOver widget to add == NONE");
//            evt.rejectDrag();
//        }
    }

    public void dragExit(DropTargetEvent evt) {
        // Called when the user is dragging and leaves this drop target.
//        designerPanel.setDragBoxLocation(null);
    }

    public void dropActionChanged(DropTargetDragEvent evt) {
        // Called when the user changes the drag action between copy or move.
    }

    public void drop(DropTargetDropEvent evt) {
        // Called when the user finishes or cancels the drag operation.

//        Dimension boxSize = widget.getBoxSize();
//        Point location = evt.getLocation();
//
//        widget.setX((location.x - boxSize.width / 2) + WidgetSelection.BOX_MARGIN);
//        widget.setY((location.y - boxSize.height / 2) + WidgetSelection.BOX_MARGIN);
//
//        designerPanel.setDragBoxLocation(null);
//        designerPanel.addWidget(widget);
//
//        designerPanel.setWidgetToAdd(IWidget.NONE);
//        designerPanel.resetPaletteButtons();
//
//        designerPanel.repaint();
//
//        Set set = new HashSet();
//        set.add(widget);
//
//        designerPanel.getMainFrame().setPropertiesCompound(set);
//        designerPanel.getMainFrame().setPropertiesToolBar(set);
    }
}
