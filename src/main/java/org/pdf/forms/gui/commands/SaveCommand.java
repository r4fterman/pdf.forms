package org.pdf.forms.gui.commands;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

class SaveCommand extends SaveAsCommand {

    private final Logger logger = LoggerFactory.getLogger(SaveCommand.class);
    private final IMainFrame mainFrame;

    SaveCommand(
            final IMainFrame mainFrame,
            final Version version) {
        super(mainFrame, version);
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        saveDesignerFile();
    }

    private void saveDesignerFile() {
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();

        final String currentDesignerFileName = mainFrame.getCurrentDesignerFileName();
        if (currentDesignerFileName.equals("Untitled")) {
            // saving for the first time
            saveDesignerFileAs(documentProperties);
        } else {
            // saving an already saved file
            writeXML(documentProperties, currentDesignerFileName);
        }
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
