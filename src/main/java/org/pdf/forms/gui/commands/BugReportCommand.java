package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.BugReportPanel;
import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.writer.des.DesignerProjectFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BugReportCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(BugReportCommand.class);
    private final IMainFrame mainFrame;

    BugReportCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final Map<String, Double> filesAndSizes = writeBugReportFile();

        final JDialog dialog = new JDialog((Frame) mainFrame, "Bug report", true);
        dialog.add(new BugReportPanel(filesAndSizes, dialog));
        dialog.pack();
        dialog.setLocationRelativeTo((Component) mainFrame);
        dialog.setVisible(true);
    }

    private Map<String, Double> writeBugReportFile() {
        final Map<String, Double> filesAndSizes = new LinkedHashMap<>();

        try {
            final File designerFile = File.createTempFile("bugreport", ".des");
            designerFile.deleteOnExit();

            final DesDocument designerDocument = mainFrame.getFormsDocument().getDesDocument();
            writeXML(designerDocument, designerFile);

            final Double size = round(designerFile.length() / 1000d);
            filesAndSizes.put("Designer File", size);
        } catch (final IOException e) {
            logger.error("Unable to create temporary bug report file", e);
        }

        return filesAndSizes;
    }

    private void writeXML(
            final DesDocument designerDocument,
            final File file) {
        final DesignerProjectFileWriter writer = new DesignerProjectFileWriter();
        writer.writeToFile(designerDocument, file);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 2);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }
}
