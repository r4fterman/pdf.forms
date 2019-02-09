package org.pdf.forms.gui.commands;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InsertPageCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(InsertPageCommand.class);
    private final IMainFrame mainFrame;

    InsertPageCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        insertPage();
    }

    private void insertPage() {
        final Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", 595, 842);

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
