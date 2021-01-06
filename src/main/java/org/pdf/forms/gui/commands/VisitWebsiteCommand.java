package org.pdf.forms.gui.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitWebsiteCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitWebsiteCommand.class);

    public static final String GITHUB_PROJECT_PAGE = "https://github.com/r4fterman/pdf.forms";
    private static final String ORIGINAL_PROJECT_PAGE = "http://pdfformsdesigne.sourceforge.net";

    public static boolean openWebpage(final String httpAddress) {
        final Desktop desktop = getDesktop();
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                final URI uri = URI.create(httpAddress);
                desktop.browse(uri);
                return true;
            } catch (IOException e) {
                LOGGER.error("Error opening web browser for address {}", httpAddress, e);
            }
        }
        return false;
    }

    private static Desktop getDesktop() {
        if (Desktop.isDesktopSupported()) {
            return Desktop.getDesktop();
        }
        return null;
    }

    VisitWebsiteCommand() {
    }

    @Override
    public void execute() {
        visitWebsite();
    }

    private void visitWebsite() {
        if (!openWebpage(GITHUB_PROJECT_PAGE)) {
            JOptionPane.showMessageDialog(null, "Error loading web page " + GITHUB_PROJECT_PAGE);
            LOGGER.error("Error loading web page {} in browser", GITHUB_PROJECT_PAGE);
        }
    }
}
