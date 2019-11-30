package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.writer.Writer;

import com.google.common.collect.ImmutableMap;

class PublishPdfCommand implements Command {

    private final IMainFrame mainFrame;
    private final String version;

    PublishPdfCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;
    }

    @Override
    public void execute() {
        publishPDF();
    }


    private void publishPDF() {
        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[] {
                    "pdf"
            },
                    "pdf (*.pdf)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            final int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".pdf")) {
                    fileToSave += ".pdf";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    final int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1) {
                        continue;
                    }
                }

                final Writer writer = new Writer(mainFrame);

                final int noOfPages = mainFrame.getTotalNoOfPages();

                final FormsDocument documentProperties = mainFrame.getFormsDocument();

                final ImmutableMap.Builder<Integer, List<IWidget>> widgets = ImmutableMap.builder();
                for (int pageNumber = 0; pageNumber < noOfPages; pageNumber++) {
                    widgets.put(pageNumber, documentProperties.getPage(pageNumber + 1).getWidgets());
                }

                writer.write(file, widgets.build(), documentProperties.getDocumentProperties());

                finished = true;
            } else {
                return;
            }
        }
    }
}
