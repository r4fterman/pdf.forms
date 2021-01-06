package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.BugReportPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

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

            final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
            writeXML(documentProperties, designerFile.getAbsolutePath());

            final Double size = round(designerFile.length() / 1000d);
            filesAndSizes.put("Designer File", size);
        } catch (IOException e) {
            logger.error("Unable to create temporary bug report file", e);
        }

        return filesAndSizes;
    }

    private void writeXML(
            final Document documentProperties,
            final String fileName) {
        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            final Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileName));
        } catch (TransformerException e) {
            logger.error("Error writing xml to file {}", fileName, e);
        }
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
