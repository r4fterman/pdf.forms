package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.util.Optional;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.model.des.Version;
import org.pdf.forms.writer.des.DesignerProjectFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SaveDesignerFileCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(SaveDesignerFileCommand.class);

    private final IMainFrame mainFrame;
    private final Version version;

    SaveDesignerFileCommand(
            final IMainFrame mainFrame,
            final Version version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        final DesDocument designerDocument = mainFrame.getFormsDocument().getDesDocument();

        final String currentDesignerFileName = mainFrame.getCurrentDesignerFileName();
        if (currentDesignerFileName.equals("Untitled")) {
            // saving for the first time
            saveDesignerFileAs(designerDocument);
        } else {
            // saving an already saved file
            writeXML(designerDocument, new File(currentDesignerFileName));
        }
    }

    private void saveDesignerFileAs(final DesDocument designerDocument) {
        getSelectedDesignerFile().ifPresent(file -> saveDesignerFile(file, designerDocument));
    }

    private void saveDesignerFile(
            final File file,
            final DesDocument designerDocument) {
        final String fileToSave = file.getAbsolutePath();

        mainFrame.setCurrentDesignerFileName(fileToSave);
        writeXML(designerDocument, file);
        mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version.getVersion());

    }

    private void writeXML(
            final DesDocument designerDocument,
            final File file) {
        final DesignerProjectFileWriter writer = new DesignerProjectFileWriter();
        writer.writeToFile(designerDocument, file);
    }

    private Optional<File> getSelectedDesignerFile() {
        final Optional<File> designerFile = chooseDesignerFile().map(this::ensureDesignerFileExtension);
        if (designerFile.isPresent()) {
            final File file = designerFile.get();
            final int value = JOptionPane.showConfirmDialog(
                    (Component) mainFrame,
                    "The file already exists, are you sure you wish to overwrite?",
                    "File already exists",
                    JOptionPane.YES_NO_OPTION);
            if (value == JOptionPane.NO_OPTION) {
                return getSelectedDesignerFile();
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
