package org.pdf.forms.gui.designer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.util.List;
import java.util.Set;

import javax.swing.TransferHandler;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetSelection;

public interface IDesigner {

    Color BACKGROUND_COLOR = new Color(236, 233, 216);
    Color PAGE_COLOR = Color.white;

    int CLOSED = 0;
    int SIMPLEPAGE = 1;
    int PDFPAGE = 2;

    void displayPage(Page page);

    IWidget getWidgetAt(
            int x,
            int y);

    void addWidget(IWidget widget);

    void addWidget(
            int index,
            IWidget w);

    void removeSelectedWidgets();

    void removeWidget(
            IWidget widgetToRemove,
            List<IWidget> widgets);

    Set<IWidget> getSelectedWidgets();

    void setWidgetToAdd(int widgetToAdd);

    int getWidgetToAdd();

    void setSelectedWidgets(Set<IWidget> selectedWidgets);

    void setResizeType(int resizeType);

    int getResizeType();

    void setIsResizing(boolean isResizing);

    boolean isResizing();

    DesignerSelectionBox getSelectionBox();

    void resetPaletteButtons();

    void setProperties(Set<IWidget> widget);

    void updateRulers(Point point);

    void setDragBoxLocation(Point dragBoxLocation);

    void setCurrentlyDragging(boolean currentlyDraging);

    List<IWidget> getWidgets();

    void setIsResizingSplitComponent(boolean isResizingSplitComponentSplitComponent);

    boolean isResizingSplitComponent();

    WidgetSelection getWidgetSelection();

    CaptionChanger getCaptionChanger();

    IMainFrame getMainFrame();

    int getWidth();

    int getHeight();

    void repaint(
            int x,
            int y,
            int width,
            int height);

    Component add(Component component);

    void repaint();

    void validate();

    void grabFocus();

    void setCursor(Cursor cursor);

    void close();

    void updateUI();

    void setTransferHandler(TransferHandler newHandler);

}
