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
* IDesigner.java
* ---------------
*/
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

    void setCurrentlyDraging(boolean currentlyDraging);

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
