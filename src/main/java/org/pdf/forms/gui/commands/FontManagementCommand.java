package org.pdf.forms.gui.commands;

import java.awt.Frame;

import javax.swing.JDialog;

import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;

class FontManagementCommand implements Command {

    private final IMainFrame mainFrame;
    private final String version;

    FontManagementCommand(
            final IMainFrame mainFrame,
            final String version) {

        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        fontManagement();
    }

    private void fontManagement() {
        final JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        final FontSelector fs = new FontSelector(mainFrame, dialog);

        dialog.add(fs);
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);

        mainFrame.updateAvailiableFonts();
    }
}
