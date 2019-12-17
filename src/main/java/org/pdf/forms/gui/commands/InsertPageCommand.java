package org.pdf.forms.gui.commands;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;

class InsertPageCommand implements Command {

    private final IMainFrame mainFrame;

    InsertPageCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", 595, 842);
        mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

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
