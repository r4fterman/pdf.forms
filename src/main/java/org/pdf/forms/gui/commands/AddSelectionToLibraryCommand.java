package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.Configuration;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.CustomWidgetsFile;

class AddSelectionToLibraryCommand implements Command {

    private final IMainFrame mainFrame;
    private final Configuration configuration;

    AddSelectionToLibraryCommand(
            final IMainFrame mainFrame,
            final Configuration configuration) {
        this.mainFrame = mainFrame;
        this.configuration = configuration;
    }

    @Override
    public void execute() {
        addSelectionToLibrary();
    }

    private void addSelectionToLibrary() {
        final CustomWidgetsFile customWidgetsFile = CustomWidgetsFile.getInstance(configuration.getConfigDirectory());
        boolean finished = false;

        String name = JOptionPane.showInputDialog((Component) mainFrame, "Enter a name for the new component", "New component name",
                JOptionPane.QUESTION_MESSAGE);

        while (!finished) {
            if (name == null) {
                return;
            }

            if (customWidgetsFile.isNameTaken(name)) {
                name = JOptionPane.showInputDialog((Component) mainFrame, "The name you have entered is already taken, please enter another name", "New component name",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                finished = true;
            }
        }

        customWidgetsFile.addCustomWidget(name, mainFrame.getDesigner().getSelectedWidgets());
    }

}
