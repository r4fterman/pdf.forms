package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.CustomWidgetsFile;

class AddSelectionToLibraryCommand implements Command {
    private final IMainFrame mainFrame;

    AddSelectionToLibraryCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        addSelectionToLibrary();
    }

    private void addSelectionToLibrary() {
        final File configDir = new File(System.getProperty("user.dir"));
        final CustomWidgetsFile customWidgetsFile = CustomWidgetsFile.getInstance(configDir);
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
