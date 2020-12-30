package org.pdf.forms.gui.designer;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetSelection;

public class DesignerMock implements IDesigner {

    private IWidget widget;

    @Override
    public void displayPage(final Page page) {
        // do nothing
    }

    @Override
    public IWidget getWidgetAt(
            final int x,
            final int y) {
        return widget;
    }

    @Override
    public void addWidget(final IWidget widget) {
        this.widget = widget;
    }

    @Override
    public void addWidget(
            final int index,
            final IWidget widget) {
        this.widget = widget;
    }

    @Override
    public void removeSelectedWidgets() {
        // do nothing
    }

    @Override
    public void removeWidget(
            final IWidget widgetToRemove,
            final List<IWidget> widgets) {
        // do nothing
    }

    @Override
    public Set<IWidget> getSelectedWidgets() {
        return Set.of();
    }

    @Override
    public void setWidgetToAdd(final int widgetToAdd) {
        // do nothing
    }

    @Override
    public int getWidgetToAdd() {
        return 0;
    }

    @Override
    public void setSelectedWidgets(final Set<IWidget> selectedWidgets) {
        // do nothing
    }

    @Override
    public void setResizeType(final int resizeType) {
        // do nothing
    }

    @Override
    public int getResizeType() {
        return 0;
    }

    @Override
    public void setIsResizing(final boolean isResizing) {
        // do nothing
    }

    @Override
    public boolean isResizing() {
        return false;
    }

    @Override
    public DesignerSelectionBox getSelectionBox() {
        return null;
    }

    @Override
    public void resetPaletteButtons() {
        // do nothing
    }

    @Override
    public void setProperties(final Set<IWidget> widget) {
        // do nothing
    }

    @Override
    public void updateRulers(final Point point) {
        // do nothing
    }

    @Override
    public void setDragBoxLocation(final Point dragBoxLocation) {
        // do nothing
    }

    @Override
    public void setCurrentlyDragging(final boolean currentlyDraging) {
        // do nothing
    }

    @Override
    public List<IWidget> getWidgets() {
        return Collections.emptyList();
    }

    @Override
    public void setIsResizingSplitComponent(final boolean isResizingSplitComponentSplitComponent) {
        // do nothing
    }

    @Override
    public boolean isResizingSplitComponent() {
        return false;
    }

    @Override
    public WidgetSelection getWidgetSelection() {
        return null;
    }

    @Override
    public CaptionChanger getCaptionChanger() {
        return null;
    }

    @Override
    public IMainFrame getMainFrame() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void repaint(
            final int x,
            final int y,
            final int width,
            final int height) {
        // do nothing
    }

    @Override
    public Component add(final Component component) {
        return null;
    }

    @Override
    public void repaint() {
        // do nothing
    }

    @Override
    public void validate() {
        // do nothing
    }

    @Override
    public void grabFocus() {
        // do nothing
    }

    @Override
    public void setCursor(final Cursor cursor) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public void updateUI() {
        // do nothing
    }

    @Override
    public void setTransferHandler(final TransferHandler newHandler) {
        // do nothing
    }
}
