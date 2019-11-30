package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;

class RemovePageCommand implements Command {

    private final IMainFrame mainFrame;
    private final String version;

    RemovePageCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
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

        //System.out.println(mainFrame.getCurrentPage() +" "+ mainFrame.getTotalNoOfPages());

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
