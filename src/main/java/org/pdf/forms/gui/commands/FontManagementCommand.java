package org.pdf.forms.gui.commands;

import java.awt.Frame;

import javax.swing.JDialog;

import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;

class FontManagementCommand implements Command {

    private final IMainFrame mainFrame;

    FontManagementCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        fontManagement();
    }

    private void fontManagement() {
        final JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        final FontSelector fontSelector = new FontSelector(mainFrame, dialog);

        dialog.add(fontSelector);
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);

        mainFrame.updateAvailiableFonts();
    }
}
