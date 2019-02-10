package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

class SaveAsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(SaveAsCommand.class);
    private final IMainFrame mainFrame;
    private final Version version;

    SaveAsCommand(
            final IMainFrame mainFrame,
            final Version version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        saveDesignerFileAs();
    }

    private void saveDesignerFileAs() {
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
        saveDesignerFileAs(documentProperties);
    }

    void saveDesignerFileAs(final Document documentProperties) {
        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[] {
                    "des"
            },
                    "des (*.des)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            final int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".des")) {
                    fileToSave += ".des";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    final int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1) {
                        continue;
                    }
                }

                mainFrame.setCurrentDesignerFileName(fileToSave);

                writeXML(documentProperties, mainFrame.getCurrentDesignerFileName());

                mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version.getValue());

                finished = true;
            } else {
                return;
            }
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
        // logger.error("writeXml", e);
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
