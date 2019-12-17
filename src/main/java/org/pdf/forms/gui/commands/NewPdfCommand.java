package org.pdf.forms.gui.commands;

import java.util.Set;

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
        closePDF();

        mainFrame.setCurrentDesignerFileName("Untitled");
        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);
        mainFrame.setPanelsState(true);
        mainFrame.setFormsDocument(new FormsDocument(version));

        insertPage();
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);
        mainFrame.getDesigner().close();
        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);
        mainFrame.setPropertiesCompound(Set.of());
        mainFrame.setPropertiesToolBar(Set.of());
        mainFrame.setPanelsState(false);
        mainFrame.setCurrentPage(0);
    }

    private void insertPage() {
        mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

        final Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", 595, 842);
        addPage(mainFrame.getCurrentPage(), newPage);

        mainFrame.displayPage(mainFrame.getCurrentPage());
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

}
