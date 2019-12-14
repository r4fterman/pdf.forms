package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.util.List;

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
        publishPDF();
    }

    private void publishPDF() {
        boolean finished = false;
        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new PdfFileFilter());
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            final int approved = chooser.showSaveDialog(null);
            if (approved != JFileChooser.APPROVE_OPTION) {
                return;
            }

            final File file = getSelectedFile(chooser);
            if (file.exists()) {
                final int value = JOptionPane.showConfirmDialog(
                        (Component) mainFrame,
                        "The file already exists, are you sure you wish to overwrite?",
                        "File already exists",
                        JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.NO_OPTION) {
                    continue;
                }
            }

            final Writer writer = new Writer(mainFrame, fontHandler);
            final int numberOfPages = mainFrame.getTotalNoOfPages();
            final FormsDocument documentProperties = mainFrame.getFormsDocument();

            final ImmutableMap.Builder<Integer, List<IWidget>> widgets = ImmutableMap.builder();
            for (int pageNumber = 0; pageNumber < numberOfPages; pageNumber++) {
                widgets.put(pageNumber, documentProperties.getPage(pageNumber + 1).getWidgets());
            }

            writer.write(file, widgets.build(), documentProperties.getDocumentProperties());
            finished = true;
        }
    }

    private File getSelectedFile(final JFileChooser chooser) {
        final File file = chooser.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".pdf")) {
            return new File(file.getAbsolutePath() + ".pdf");
        }
        return file;
    }
}
