package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Optional;

import javax.swing.*;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.model.des.Version;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportPdfCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ImportPdfCommand.class);

    private final IMainFrame mainFrame;
    private final Version version;
    private final WidgetFactory widgetFactory;
    private final DesignerPropertiesFile designerPropertiesFile;

    private final JMenuItem[] recentDesignerDocuments;

    public ImportPdfCommand(
            final IMainFrame mainFrame,
            final Version version,
            final WidgetFactory widgetFactory,
            final DesignerPropertiesFile designerPropertiesFile) {
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.designerPropertiesFile = designerPropertiesFile;

        final int numberOfRecentDocs = DesignerPropertiesFile.NO_OF_RECENT_DOCS;
        this.recentDesignerDocuments = new JMenuItem[numberOfRecentDocs];
    }

    @Override
    public void execute() {
        //todo: do not allow import of a PDF into a closed document
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
            worker.execute();

            designerPropertiesFile.addRecentPDFDocument(pdfPath);
            updateRecentDocuments(designerPropertiesFile.getRecentPDFDocuments());
        } catch (PdfException e) {
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

    private void updateRecentDocuments(final java.util.List<String> recentDocs) {
        if (recentDocs.isEmpty()) {
            return;
        }

        for (int i = 0; i < recentDocs.size(); i++) {
            updateRecentDocuments(recentDocs.get(i), recentDesignerDocuments, i);
        }
    }

    private void updateRecentDocuments(
            final String recentDoc,
            final JMenuItem[] recentDocuments,
            final int i) {
        if (recentDoc == null) {
            return;
        }

        final String shortenedFileName = FileUtil.getShortenedFileName(recentDoc, File.separator);
        if (recentDocuments[i] == null) {
            recentDocuments[i] = new JMenuItem();
        }
        final String label = (i + 1) + ": ";
        recentDocuments[i].setText(label + shortenedFileName);
        recentDocuments[i].setVisible(!recentDocuments[i].getText().equals(label));
        recentDocuments[i].setName(recentDoc);
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
