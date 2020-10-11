package org.pdf.forms.gui.designer.gui;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DropableComponent implements DropTargetListener {
    private final IDesigner designerPanel;
    private IWidget widget;

    public DropableComponent(final IDesigner designer) {
        this.designerPanel = designer;
    }

    @Override
    public void dragEnter(final DropTargetDragEvent evt) {
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

    @Override
    public void dragOver(final DropTargetDragEvent evt) {
        final int widgetToAdd = designerPanel.getWidgetToAdd();

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

    @Override
    public void dragExit(final DropTargetEvent evt) {
        // Called when the user is dragging and leaves this drop target.
        //        designerPanel.setDragBoxLocation(null);
    }

    @Override
    public void dropActionChanged(final DropTargetDragEvent evt) {
        // Called when the user changes the drag action between copy or move.
    }

    @Override
    public void drop(final DropTargetDropEvent evt) {
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
