package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.Configuration;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.CustomWidgetsFile;

class AddSelectionToLibraryCommand implements Command {

    private final IMainFrame mainFrame;
    private final CustomWidgetsFile customWidgetsFile;

    AddSelectionToLibraryCommand(
            final IMainFrame mainFrame,
            final Configuration configuration) {
        this.mainFrame = mainFrame;
        this.customWidgetsFile = CustomWidgetsFile.getInstance(configuration.getConfigDirectory());
    }

    @Override
    public void execute() {
        addSelectionToLibrary();
    }

    private void addSelectionToLibrary() {
        String newComponentName = askForNewComponentName();
        while (canNotUseComponentName(newComponentName)) {
            newComponentName = askForOtherComponentName();
        }

        if (newComponentName != null) {
            addComponentWithName(newComponentName);
        }
    }

    private boolean canNotUseComponentName(final String newComponentName) {
        if (newComponentName == null) {
            return false;
        }

        return customWidgetsFile.isNameTaken(newComponentName);
    }

    private void addComponentWithName(final String newComponentName) {
        customWidgetsFile.addCustomWidget(newComponentName, mainFrame.getDesigner().getSelectedWidgets());
    }

    private String askForOtherComponentName() {
        return JOptionPane.showInputDialog((Component) mainFrame, "The name you have entered is already taken, please enter another name", "New component name",
                JOptionPane.WARNING_MESSAGE);
    }

    private String askForNewComponentName() {
        return JOptionPane.showInputDialog((Component) mainFrame, "Enter a name for the new component", "New component name",
                    JOptionPane.QUESTION_MESSAGE);
    }

}
