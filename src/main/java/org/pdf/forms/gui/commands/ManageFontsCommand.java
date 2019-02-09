package org.pdf.forms.gui.commands;

import java.awt.Frame;

import javax.swing.JDialog;

import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageFontsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ManageFontsCommand.class);
    private final IMainFrame mainFrame;

    ManageFontsCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
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
