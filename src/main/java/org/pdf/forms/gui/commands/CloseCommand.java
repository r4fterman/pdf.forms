package org.pdf.forms.gui.commands;

import java.util.HashSet;

import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(CloseCommand.class);
    private final IMainFrame mainFrame;
    private final Version version;

    CloseCommand(
            final IMainFrame mainFrame,
            final Version version) {
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
        mainFrame.setTitle("PDF Forms Designer Version " + version.getValue());

        mainFrame.setPropertiesCompound(new HashSet<>());
        mainFrame.setPropertiesToolBar(new HashSet<>());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }
}
