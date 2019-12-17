package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.writer.Writer;

import com.google.common.collect.ImmutableMap;

class PublishPdfCommand implements Command {

    private final IMainFrame mainFrame;
    private final FontHandler fontHandler;

    PublishPdfCommand(
            final IMainFrame mainFrame,
            final FontHandler fontHandler) {
        this.mainFrame = mainFrame;
        this.fontHandler = fontHandler;
    }

    @Override
    public void execute() {
        getSelectedPdfFile().ifPresent(this::writePdfFile);
    }

    private Optional<File> getSelectedPdfFile() {
        final Optional<File> pdfFile = choosePdfFile().map(this::ensurePdfFileExtension);
        if (pdfFile.isPresent()) {
            final File file = pdfFile.get();
            if (file.exists()) {
                final int value = JOptionPane.showConfirmDialog(
                        (Component) mainFrame,
                        "The file already exists, are you sure you wish to overwrite?",
                        "File already exists",
                        JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.NO_OPTION) {
                    return getSelectedPdfFile();
                }
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    private void writePdfFile(final File file) {
        final Writer writer = new Writer(mainFrame, fontHandler);
        final int numberOfPages = mainFrame.getTotalNoOfPages();
        final FormsDocument documentProperties = mainFrame.getFormsDocument();

        final ImmutableMap.Builder<Integer, List<IWidget>> widgets = ImmutableMap.builder();
        for (int pageNumber = 0; pageNumber < numberOfPages; pageNumber++) {
            widgets.put(pageNumber, documentProperties.getPage(pageNumber + 1).getWidgets());
        }

        writer.write(file, widgets.build(), documentProperties.getDocumentProperties());
    }

    private Optional<File> choosePdfFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new PdfFileFilter());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        final int state = chooser.showSaveDialog(null);
        if (state != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }
        return Optional.ofNullable(chooser.getSelectedFile());
    }

    private File ensurePdfFileExtension(final File file) {
        if (!file.getAbsolutePath().endsWith(".pdf")) {
            return new File(file.getAbsolutePath() + ".pdf");
        }
        return file;
    }

}
