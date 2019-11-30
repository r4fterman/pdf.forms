package org.pdf.forms.gui.commands;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;

class InsertPageCommand implements Command{

    private final IMainFrame mainFrame;
    private final String version;

    InsertPageCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        insertPage(595, 842);
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

}
