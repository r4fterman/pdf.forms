package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.JDialog;
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

class FileBugReportCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(FileBugReportCommand.class);
    private final IMainFrame mainFrame;

    FileBugReportCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        bugReport();
    }

    private void bugReport() {
        //        final LinkedHashMap<String, Double> pdfFilesAndSizes = mainFrame.getFormsDocument().getPdfFilesUsed();

        final LinkedHashMap<String, Double> filesAndSizes = new LinkedHashMap<>();
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();

        try {
            final File designerFile = File.createTempFile("bugreport", ".des");
            designerFile.deleteOnExit();

            writeXML(documentProperties, designerFile.getAbsolutePath());

            final Double size = designerFile.length() / 1000d;
            filesAndSizes.put("Designer File", size);
        } catch (final IOException e) {
            logger.error("", e);
        }

        //        filesAndSizes.putAll(pdfFilesAndSizes);

        final JDialog dialog = new JDialog((Frame) mainFrame, "Bug report", true);

        final BugReportPanel bugReportPanel = new BugReportPanel(filesAndSizes, dialog);

        dialog.add(bugReportPanel);
        dialog.pack();
        dialog.setLocationRelativeTo((Component) mainFrame);
        dialog.setVisible(true);
    }

    private void writeXML(
            final Document documentProperties,
            final String fileName) {
        //        try {
        // InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
        //
        // TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        // transformer.transform(new DOMSource(documentProperties), new StreamResult(mainFrame.getCurrentDesignerFileName()));
        //        } catch (TransformerException e) {
        // e.printStackTrace();
        //        }

        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileName));
        } catch (final TransformerException e) {
            logger.error("Cannot write xml file " + fileName, e);
        }
    }
}
