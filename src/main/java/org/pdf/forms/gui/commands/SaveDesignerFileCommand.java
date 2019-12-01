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

import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

class SaveDesignerFileCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(SaveDesignerFileCommand.class);

    private final IMainFrame mainFrame;
    private final String version;

    SaveDesignerFileCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
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

    private void saveDesignerFileAs(final Document documentProperties) {
        boolean finished = false;
        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new DesFileFilter());
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            final int approved = chooser.showSaveDialog(null);
            if (approved != JFileChooser.APPROVE_OPTION) {
                return;
            }

            final File file = getSelectedFile(chooser);
            if (file.exists()) {
                final int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                        "File already exists", JOptionPane.YES_NO_OPTION);

                if (n == 1) {
                    continue;
                }
            }

            final String fileToSave = file.getAbsolutePath();
            mainFrame.setCurrentDesignerFileName(fileToSave);

            writeXML(documentProperties, mainFrame.getCurrentDesignerFileName());

            mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version);

            finished = true;
        }
    }

    private File getSelectedFile(final JFileChooser chooser) {
        final File file = chooser.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".des")) {
            return new File(file.getAbsolutePath() + ".des");
        }
        return file;
    }

    private void writeXML(
            final Document documentProperties,
            final String fileName) {
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //initialize StreamResult with File object to save to file
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileName));
        } catch (final TransformerException e) {
            logger.error("Error writing xml to file {}", fileName, e);
        }
    }
}
