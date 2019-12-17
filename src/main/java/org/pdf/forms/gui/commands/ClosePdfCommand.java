package org.pdf.forms.gui.commands;

import java.util.Set;

import org.pdf.forms.gui.IMainFrame;

public class ClosePdfCommand implements Command {

    private final IMainFrame mainFrame;
    private final String version;

    ClosePdfCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        mainFrame.setFormsDocument(null);
        mainFrame.getDesigner().close();
        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);
        mainFrame.setPropertiesCompound(Set.of());
        mainFrame.setPropertiesToolBar(Set.of());
        mainFrame.setPanelsState(false);
        mainFrame.setCurrentPage(0);
    }

}
