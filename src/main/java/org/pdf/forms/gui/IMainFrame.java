package org.pdf.forms.gui;

import java.util.Set;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerPanel;
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

    void addPageToHierarchyPanel(
            int pdfPage,
            Page newPage);

    void updateHierarchyPanelUI();

    void removePageFromHierarchyPanel(int index);

    void updateHierarchy();

    void setDockableVisible(
            String dockable,
            boolean visible);

    void setDesignerCompoundContent(int content);

    int getDesignerCompoundContent();

    DesignerPanel getDesignerPanel();

    double getCurrentSelectedScaling();

    double getCurrentScaling();

    void setCurrentSelectedScaling(double scaling);

    void updateAvailableFonts();

    void addWidgetToPage(IWidget widget);

    int getNextArrayNumberForName(
            String name,
            IWidget widget);

    void handleArrayNumberOnWidgetDeletion(Set<IWidget> selectedWidgets);

    WidgetArrays getWidgetArrays();

    void renameWidget(
            String oldName,
            String name,
            IWidget widget);
}
