package org.pdf.forms.gui.commands;

import java.util.HashSet;

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
        closePDF();
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);
        mainFrame.getDesigner().close();
        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);
        mainFrame.setPropertiesCompound(new HashSet<>());
        mainFrame.setPropertiesToolBar(new HashSet<>());
        mainFrame.setPanelsState(false);
        mainFrame.setCurrentPage(0);
    }

}
