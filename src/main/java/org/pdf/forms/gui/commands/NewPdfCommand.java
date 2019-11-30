package org.pdf.forms.gui.commands;

import java.util.HashSet;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;

class NewPdfCommand implements Command {

    private final IMainFrame mainFrame;
    private final String version;

    NewPdfCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        newPDF(595, 842);
    }

    private void newPDF(
            final int width,
            final int height) {
        closePDF();

        mainFrame.setCurrentDesignerFileName("Untitled");
        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

        setPanelsState(true);

        mainFrame.setFormsDocument(new FormsDocument(version));

        insertPage(width, height);
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(new HashSet<>());
        mainFrame.setPropertiesToolBar(new HashSet<>());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private void insertPage(
            final int width,
            final int height) {
        final Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", width, height);

        mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

        addPage(mainFrame.getCurrentPage(), newPage);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }
}
