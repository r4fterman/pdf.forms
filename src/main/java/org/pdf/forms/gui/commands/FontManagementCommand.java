package org.pdf.forms.gui.commands;

import java.awt.Frame;

import javax.swing.JDialog;

import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;

class FontManagementCommand implements Command {

    private final IMainFrame mainFrame;
    private final FontHandler fontHandler;
    private final Configuration configuration;

    FontManagementCommand(
            final IMainFrame mainFrame,
            final FontHandler fontHandler,
            final Configuration configuration) {
        this.mainFrame = mainFrame;
        this.fontHandler = fontHandler;
        this.configuration = configuration;
    }

    @Override
    public void execute() {
        fontManagement();
    }

    private void fontManagement() {
        final JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        final FontSelector fontSelector = new FontSelector(fontHandler, mainFrame, dialog, configuration);

        dialog.add(fontSelector);
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);

        mainFrame.updateAvailiableFonts();
    }
}
