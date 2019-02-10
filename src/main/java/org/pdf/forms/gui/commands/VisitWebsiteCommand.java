package org.pdf.forms.gui.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VisitWebsiteCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(VisitWebsiteCommand.class);

    @Override
    public void execute() {
        visitWebsite();
    }

    private void visitWebsite() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create("https://github.com/r4fterman/pdf.forms"));
            }
        } catch (final IOException e) {
            logger.error("Error loading webpage", e);
            JOptionPane.showMessageDialog(null, "Error loading webpage");
        }
    }

}
