package org.pdf.forms.gui.commands;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.DesignerPropertiesFile;

class FontManagementCommand implements Command {

    private final IMainFrame mainFrame;
    private final FontHandler fontHandler;
    private final DesignerPropertiesFile designerPropertiesFile;

    FontManagementCommand(
            final IMainFrame mainFrame,
            final FontHandler fontHandler,
            final DesignerPropertiesFile designerPropertiesFile) {
        this.mainFrame = mainFrame;
        this.fontHandler = fontHandler;
        this.designerPropertiesFile = designerPropertiesFile;
    }

    @Override
    public void execute() {
        final JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        dialog.add(new FontSelector(fontHandler, mainFrame, dialog, designerPropertiesFile));
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);

        mainFrame.updateAvailableFonts();
    }

}
