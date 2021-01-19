package org.pdf.forms.gui.commands;

import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.Version;

public class ClosePdfCommand implements Command {

    private final IMainFrame mainFrame;
    private final Version version;

    ClosePdfCommand(
            final IMainFrame mainFrame,
            final Version version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        mainFrame.setFormsDocument(null);
        mainFrame.getDesigner().close();
        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version.getVersion());
        mainFrame.setPropertiesCompound(Set.of());
        mainFrame.setPropertiesToolBar(Set.of());
        mainFrame.setPanelsState(false);
        mainFrame.setCurrentPage(0);
    }

}
