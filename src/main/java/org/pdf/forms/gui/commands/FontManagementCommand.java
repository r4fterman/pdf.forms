package org.pdf.forms.gui.commands;

import java.util.List;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.fonts.FontManagementDialog;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.properties.Font;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;

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
        final FontManagementDialog dialog = new FontManagementDialog(
                (JFrame) mainFrame,
                fontHandler.getFonts(),
                designerPropertiesFile.getCustomFonts());

        dialog.pack();
        dialog.setLocationRelativeTo((JFrame) mainFrame);
        dialog.setVisible(true);
        if (dialog.isCanceled()) {
            return;
        }

        final List<Font> customFonts = dialog.getCustomFonts();
        designerPropertiesFile.setCustomFonts(customFonts);
        fontHandler.updateFonts();
        mainFrame.updateAvailableFonts();
    }

}
