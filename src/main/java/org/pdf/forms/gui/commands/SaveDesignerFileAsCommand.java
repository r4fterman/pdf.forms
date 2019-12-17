package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.util.Optional;

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

class SaveDesignerFileAsCommand implements Command {

    private Logger logger = LoggerFactory.getLogger(SaveDesignerFileAsCommand.class);

    private final IMainFrame mainFrame;
    private final String version;

    SaveDesignerFileAsCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
        getSelectedDesignerFile().ifPresent(file -> saveDesignerFile(file, documentProperties));
    }

    private void saveDesignerFile(
            final File file,
            final Document documentProperties) {
        final String fileToSave = file.getAbsolutePath();

        mainFrame.setCurrentDesignerFileName(fileToSave);
        writeDesignerFile(documentProperties, file);
        mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version);
    }

    private void writeDesignerFile(
            final Document documentProperties,
            final File file) {
        final String fileToSave = file.getAbsolutePath();
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //initialize StreamResult with File object to save to file
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileToSave));
        } catch (final TransformerException e) {
            logger.error("Error writing xml to file {}", fileToSave, e);
        }
    }

    private Optional<File> getSelectedDesignerFile() {
        final Optional<File> designerFile = chooseDesignerFile().map(this::ensureDesignerFileExtension);
        if (designerFile.isPresent()) {
            final File file = designerFile.get();
            if (file.exists()) {
                final int value = JOptionPane.showConfirmDialog(
                        (Component) mainFrame,
                        "The file already exists, are you sure you wish to overwrite?",
                        "File already exists",
                        JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.NO_OPTION) {
                    return getSelectedDesignerFile();
                }
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    private Optional<File> chooseDesignerFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new DesFileFilter());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        final int state = chooser.showSaveDialog(null);
        if (state != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }
        return Optional.ofNullable(chooser.getSelectedFile());
    }

    private File ensureDesignerFileExtension(final File file) {
        if (!file.getAbsolutePath().endsWith(".des")) {
            return new File(file.getAbsolutePath() + ".des");
        }
        return file;
    }
}
