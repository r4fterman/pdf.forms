package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RemovePageCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(RemovePageCommand.class);
    private final IMainFrame mainFrame;

    RemovePageCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        removePage();
    }

    private void removePage() {
        final int noOfPages = mainFrame.getTotalNoOfPages();
        if (noOfPages == 1) {
            JOptionPane.showMessageDialog((Component) mainFrame, "You cannot remove the last page", "Last Page", JOptionPane.ERROR_MESSAGE);

            return;
        }

        mainFrame.getFormsDocument().removePage(mainFrame.getCurrentPage());
        mainFrame.removePageFromHierarchyPanel(mainFrame.getCurrentPage());

        if (mainFrame.getCurrentPage() == noOfPages) {
            mainFrame.setCurrentPage(mainFrame.getCurrentPage() - 1);
        }

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }
}
