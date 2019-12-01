package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;

class RemovePageCommand implements Command {

    private final IMainFrame mainFrame;

    RemovePageCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        removePage();
    }

    private void removePage() {
        final int numberOfPages = mainFrame.getTotalNoOfPages();
        if (numberOfPages == 1) {
            JOptionPane.showMessageDialog((Component) mainFrame, "You cannot remove the last page", "Last Page", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainFrame.getFormsDocument().removePage(mainFrame.getCurrentPage());
        mainFrame.removePageFromHierarchyPanel(mainFrame.getCurrentPage());
        if (mainFrame.getCurrentPage() == numberOfPages) {
            mainFrame.setCurrentPage(mainFrame.getCurrentPage() - 1);
        }
        mainFrame.displayPage(mainFrame.getCurrentPage());
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

}
