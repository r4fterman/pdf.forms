package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Optional;

import javax.swing.*;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.pdf.forms.Configuration;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportPdfCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ImportPdfCommand.class);

    private final IMainFrame mainFrame;
    private final String version;
    private final WidgetFactory widgetFactory;
    private final Configuration configuration;

    private final JMenuItem[] recentDesignerDocuments;

    public ImportPdfCommand(
            final IMainFrame mainFrame,
            final String version,
            final WidgetFactory widgetFactory,
            final Configuration configuration) {
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.configuration = configuration;

        final int numberOfRecentDocs = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory())
                .getNumberRecentDocumentsToDisplay();
        this.recentDesignerDocuments = new JMenuItem[numberOfRecentDocs];
    }

    @Override
    public void execute() {
        //TODO: do not allow import of a PDF into a closed document
        final int importType = acquirePDFImportType();
        selectPdfImportFile()
                .ifPresent(file -> importPDF(importType, file.getAbsolutePath()));
    }

    private Optional<File> selectPdfImportFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new PdfFileFilter());

        final int state = chooser.showOpenDialog((Component) mainFrame);
        if (state == JFileChooser.APPROVE_OPTION) {
            return Optional.ofNullable(chooser.getSelectedFile());
        }
        return Optional.empty();
    }

    public void importPDF(final String pdfPath) {
        final int importType = acquirePDFImportType();
        importPDF(importType, pdfPath);
    }

    private void importPDF(
            final int importType,
            final String pdfPath) {
        if (importType == PDFImportChooser.IMPORT_NEW) {
            closePDF();
            setPanelsState(true);
            mainFrame.setFormsDocument(new FormsDocument(version));
        }

        try {
            final PdfDecoder pdfDecoder = getDecoder(pdfPath);
            final int pageCount = pdfDecoder.getPageCount();

            final PDFImportWorker worker = new PDFImportWorker(
                    mainFrame,
                    version,
                    widgetFactory,
                    importType,
                    pageCount,
                    pdfPath,
                    pdfDecoder);
            worker.start();

            final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration
                    .getConfigDirectory());
            properties.addRecentPDFDocument(pdfPath);
            updateRecentDocuments(properties.getRecentPDFDocuments());
        } catch (final PdfException e) {
            logger.error("Error importing PDF file {}", pdfPath, e);
        }
    }

    private int acquirePDFImportType() {
        final PDFImportChooser pdfImportChooser = new PDFImportChooser((Component) mainFrame);
        pdfImportChooser.setVisible(true);
        return pdfImportChooser.getImportType();
    }

    private PdfDecoder getDecoder(final String pdfPath) throws PdfException {
        final PdfDecoder decoder = new PdfDecoder();
        if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
            decoder.openPdfFileFromURL(pdfPath);
        } else {
            decoder.openPdfFile(pdfPath);
        }
        return decoder;
    }

    private void updateRecentDocuments(final String[] recentDocs) {
        if (recentDocs == null) {
            return;
        }

        final JMenuItem[] recentDocuments = recentDesignerDocuments;
        for (int i = 0; i < recentDocs.length; i++) {
            if (recentDocs[i] != null) {
                final String shortenedFileName = FileUtil.getShortenedFileName(recentDocs[i], File.separator);
                if (recentDocuments[i] == null) {
                    recentDocuments[i] = new JMenuItem();
                }
                recentDocuments[i].setText((i + 1) + ": " + shortenedFileName);
                recentDocuments[i].setVisible(!recentDocuments[i].getText().equals((i + 1) + ": "));

                recentDocuments[i].setName(recentDocs[i]);
            }
        }
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(new HashSet<>());
        mainFrame.setPropertiesToolBar(new HashSet<>());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }

}
