package org.pdf.forms.gui.properties.layout;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetSelection;

public class TestDesginer implements IDesigner {
    @Override
    public void displayPage(final Page page) {

    }

    @Override
    public IWidget getWidgetAt(
            final int x,
            final int y) {
        return null;
    }

    @Override
    public void addWidget(final IWidget widget) {

    }

    @Override
    public void addWidget(
            final int index,
            final IWidget w) {

    }

    @Override
    public void removeSelectedWidgets() {

    }

    @Override
    public void removeWidget(
            final IWidget widgetToRemove,
            final List<IWidget> widgets) {

    }

    @Override
    public Set<IWidget> getSelectedWidgets() {
        return Collections.emptySet();
    }

    @Override
    public void setWidgetToAdd(final int widgetToAdd) {

    }

    @Override
    public int getWidgetToAdd() {
        return 0;
    }

    @Override
    public void setSelectedWidgets(final Set<IWidget> selectedWidgets) {

    }

    @Override
    public void setResizeType(final int resizeType) {

    }

    @Override
    public int getResizeType() {
        return 0;
    }

    @Override
    public void setIsResizing(final boolean isResizing) {

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

    }

    @Override
    public void setProperties(final Set<IWidget> widget) {

    }

    @Override
    public void updateRulers(final Point point) {

    }

    @Override
    public void setDragBoxLocation(final Point dragBoxLocation) {

    }

    @Override
    public void setCurrentlyDraging(final boolean currentlyDraging) {

    }

    @Override
    public List<IWidget> getWidgets() {
        return Collections.emptyList();
    }

    @Override
    public void setIsResizingSplitComponent(final boolean isResizingSplitComponentSplitComponent) {

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

    }

    @Override
    public Component add(final Component component) {
        return null;
    }

    @Override
    public void repaint() {

    }

    @Override
    public void validate() {

    }

    @Override
    public void grabFocus() {

    }

    @Override
    public void setCursor(final Cursor cursor) {

    }

    @Override
    public void close() {

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void setTransferHandler(final TransferHandler newHandler) {

    }
}
