package org.pdf.forms.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetArrays;

public class MockMainFrame implements IMainFrame {

    private final List<IWidget> widgets = new ArrayList<>();

    private int pages = 0;

    private String currentFileName;
    private FormsDocument formsDocument;
    private int currentPage;
    private IDesigner designer;

    @Override
    public void resetPaletteButtons() {

    }

    @Override
    public void setPropertiesCompound(final Set<IWidget> widget) {

    }

    @Override
    public void setPropertiesToolBar(final Set<IWidget> widgets) {

    }

    @Override
    public void addWidgetToHierarchy(final IWidget widget) {

    }

    @Override
    public void removeWidgetFromHierarchy(final IWidget widget) {

    }

    @Override
    public void displayPage(final int page) {

    }

    @Override
    public int getTotalNoOfPages() {
        return pages;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setFormsDocument(final FormsDocument formsDocument) {
        this.formsDocument = formsDocument;
    }

    @Override
    public IDesigner getDesigner() {
        return designer;
    }

    @Override
    public void setCurrentDesignerFileName(final String currentFileName) {
        this.currentFileName = currentFileName;
    }

    @Override
    public String getCurrentDesignerFileName() {
        return currentFileName;
    }

    @Override
    public void setTitle(final String title) {

    }

    @Override
    public FormsDocument getFormsDocument() {
        return formsDocument;
    }

    @Override
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void setPanelsState(final boolean state) {

    }

    @Override
    public void setTotalNoOfDisplayedPages(final int totalNoOfDisplayedPages) {
        this.pages = totalNoOfDisplayedPages;
    }

    @Override
    public void addPageToHierarchyPanel(
            final int pdfPage,
            final Page newPage) {

    }

    @Override
    public void updateHierarchyPanelUI() {

    }

    @Override
    public void removePageFromHierarchyPanel(final int index) {

    }

    @Override
    public void updateHierarchy() {

    }

    @Override
    public void setDockableVisible(
            final String dockable,
            final boolean visible) {

    }

    @Override
    public void setDesignerCompoundContent(final int content) {

    }

    @Override
    public int getDesignerCompoundContent() {
        return 0;
    }

    @Override
    public DesignerCompound getDesignerCompound() {
        return null;
    }

    @Override
    public double getCurrentSelectedScaling() {
        return 0;
    }

    @Override
    public double getCurrentScaling() {
        return 0;
    }

    @Override
    public void setCurrentSelectedScaling(final double scaling) {

    }

    @Override
    public void updateAvailiableFonts() {

    }

    @Override
    public void addWidgetToPage(final IWidget widget) {
        widgets.add(widget);
    }

    @Override
    public int getNextArrayNumberForName(
            final String name,
            final IWidget widget) {
        return 0;
    }

    @Override
    public void handleArrayNumberOnWidgetDeletion(final Set<IWidget> selectedWidgets) {

    }

    @Override
    public WidgetArrays getWidgetArrays() {
        return new WidgetArrays();
    }

    @Override
    public void renameWidget(
            final String oldName,
            final String name,
            final IWidget widget) {

    }

    public void setDesigner(final IDesigner designer) {
        this.designer = designer;
    }
}
