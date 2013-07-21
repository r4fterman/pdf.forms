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
 * IMainFrame.java
 * ---------------
 */
package org.pdf.forms.gui;

import java.util.Set;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetArrays;

public interface IMainFrame {

    public static final int INSET = 15;

    public void resetPaletteButtons();

    public void setPropertiesCompound(Set widget);

    public void setPropertiesToolBar(Set widgets);

    public void addWidgetToHierarchy(IWidget widget);

    public void removeWidgetFromHierarchy(IWidget widget);

    public void displayPage(int page);

    public int getTotalNoOfPages();

    public int getCurrentPage();

    public void setFormsDocument(FormsDocument formsDocument);

    public IDesigner getDesigner();

    public void setCurrentDesignerFileName(String currentFileName);

    public String getCurrentDesignerFileName();

    public void setTitle(String title);

    public FormsDocument getFormsDocument();

    public void setCurrentPage(int currentPage);

    public void setPanelsState(boolean state);

    public void setTotalNoOfDisplayedPages(int totalNoOfDisplayedPages);

    public void addPageToHierarchyPanel(int pdfPage, Page newPage);

    public void updateHierarchyPanelUI();

    public void removePageFromHierarchyPanel(int index);

    // @scale
    // public void setScaling(double scaling);
    //
    // public double getScaling();

    public void updateHierarchy();

    public void setDockableVisible(String dockable, boolean visible);

    public void setDesignerCompoundContent(int content);

    public int getDesignerCompoundContent();

    public DesignerCompound getDesignerCompound();

    public double getCurrentSelectedScaling();

    public double getCurrentScaling();

    public void setCurrentSelectedScaling(double scaling);

    public void updateAvailiableFonts();

    public void addWidgetToPage(IWidget widget);

    public int getNextArrayNumberForName(String name, IWidget widget);

    public void handleArrayNumberOnWidgetDeletion(Set selectedWidgets);

    public WidgetArrays getWidgetArrays();

    public void renameWidget(String oldName, String name, IWidget widget);
}