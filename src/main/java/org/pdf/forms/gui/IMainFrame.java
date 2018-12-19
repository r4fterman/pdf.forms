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

    int INSET = 15;

    void resetPaletteButtons();

    void setPropertiesCompound(Set<IWidget> widget);

    void setPropertiesToolBar(Set<IWidget> widgets);

    void addWidgetToHierarchy(IWidget widget);

    void removeWidgetFromHierarchy(IWidget widget);

    void displayPage(int page);

    int getTotalNoOfPages();

    int getCurrentPage();

    void setFormsDocument(FormsDocument formsDocument);

    IDesigner getDesigner();

    void setCurrentDesignerFileName(String currentFileName);

    String getCurrentDesignerFileName();

    void setTitle(String title);

    FormsDocument getFormsDocument();

    void setCurrentPage(int currentPage);

    void setPanelsState(boolean state);

    void setTotalNoOfDisplayedPages(int totalNoOfDisplayedPages);

    void addPageToHierarchyPanel(int pdfPage, Page newPage);

    void updateHierarchyPanelUI();

    void removePageFromHierarchyPanel(int index);

    void updateHierarchy();

    void setDockableVisible(String dockable, boolean visible);

	void setDesignerCompoundContent(int content);
	
	int getDesignerCompoundContent();

	DesignerCompound getDesignerCompound();

	double getCurrentSelectedScaling();

	double getCurrentScaling();

	void setCurrentSelectedScaling(double scaling);

	void updateAvailiableFonts();

	void addWidgetToPage(IWidget widget);

    int getNextArrayNumberForName(String name, IWidget widget);

    void handleArrayNumberOnWidgetDeletion(Set<IWidget> selectedWidgets);

	WidgetArrays getWidgetArrays();

	void renameWidget(String oldName, String name, IWidget widget);
}
