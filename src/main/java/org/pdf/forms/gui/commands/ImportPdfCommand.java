package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitor;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.SwingWorker;
import org.pdf.forms.Configuration;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetParser;
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

        final int numberOfRecentDocs = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory()).getNoRecentDocumentsToDisplay();
        this.recentDesignerDocuments = new JMenuItem[numberOfRecentDocs];
    }

    @Override
    public void execute() {
        // TODO: do not allow import of a pdf into a closed document
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
            final PdfDecoder pdfDecoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
                pdfDecoder.openPdfFileFromURL(pdfPath);
            } else {
                pdfDecoder.openPdfFile(pdfPath);
            }

            final int pageCount = pdfDecoder.getPageCount();

            final ProgressMonitor progressDialog = new ProgressMonitor((Component) mainFrame, "", "", 0, pageCount);
            progressDialog.setMillisToDecideToPopup(0);
            progressDialog.setMillisToPopup(0);
            progressDialog.setNote("Importing page " + 1 + " of " + progressDialog.getMaximum());
            progressDialog.setProgress(0);

            final SwingWorker worker = new SwingWorker() {
                @Override
                public Object construct() {

                    boolean isCancelled;

                    final List<Object[]> pages = new ArrayList<>();
                    int currentLastPage = mainFrame.getTotalNoOfPages();

                    if (importType == PDFImportChooser.IMPORT_NEW) {
                        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
                            currentLastPage++;

                            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

                            final List<IWidget> widgetsOnPage = new ArrayList<>();
                            isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);

                            final Object[] properties = {
                                    pdfPageNumber,
                                    newPage,
                                    widgetsOnPage };
                            pages.add(properties);

                            if (isCancelled) {
                                break;
                            }
                        }

                        mainFrame.setCurrentDesignerFileName("Untitled");
                        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);
                    } else if (importType == PDFImportChooser.IMPORT_EXISTING) {
                        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
                            currentLastPage++;

                            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

                            final List<IWidget> widgetsOnPage = new ArrayList<>();
                            isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);

                            final Object[] properties = {
                                    currentLastPage,
                                    newPage,
                                    widgetsOnPage };
                            pages.add(properties);

                            if (isCancelled) {
                                break;
                            }
                        }
                    }

                    final int finalCurrentLastPage = currentLastPage;

                    EventQueue.invokeLater(() -> {
                        for (final Object[] properties : pages) {
                            final int pageNumber = (Integer) properties[0];
                            final Page newPage = (Page) properties[1];
                            final List<IWidget> widgetsOnPage = (List<IWidget>) properties[2];

                            mainFrame.setCurrentPage(pageNumber);

                            addPage(pageNumber, newPage);

                            for (final IWidget widget : widgetsOnPage) {
                                mainFrame.getDesigner().addWidget(widget);
                            }
                        }

                        setTotalPages();

                        //    mainFrame.setCurrentPage(finalCurrentLastPage);
                        mainFrame.displayPage(mainFrame.getCurrentPage());

                        progressDialog.close();

                    });

                    return null;
                }
            };
            worker.start();

            final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory());
            properties.addRecentDocument(pdfPath, "recentpdffiles");
            updateRecentDocuments(properties.getRecentDocuments("recentpdffiles"));
        } catch (final PdfException e) {
            logger.error("Error importing PDF file {}", pdfPath, e);
        }
    }

    private int acquirePDFImportType() {
        final PDFImportChooser pdfImportChooser = new PDFImportChooser((Component) mainFrame);
        pdfImportChooser.setVisible(true);
        return pdfImportChooser.getImportType();
    }

    private boolean decodePDFPage(
            final String pdfPath,
            final PdfDecoder pdfDecoder,
            final ProgressMonitor progressDialog,
            final int pdfPageNumber,
            final Page newPage,
            final List<IWidget> widgetsOnPage) {
        decodePDFPage(pdfDecoder, pdfPath, pdfPageNumber, newPage, widgetsOnPage);

        if (progressDialog.isCanceled()) {
            return true;
        }

        progressDialog.setProgress(pdfPageNumber);
        progressDialog.setNote("Importing page " + pdfPageNumber + " of " + progressDialog.getMaximum());

        return false;
    }

    private void decodePDFPage(
            final PdfDecoder pdfDecoder,
            final String pdfPath,
            final int pdfPageNumber,
            final Page newPage,
            final List<IWidget> widgetsOnPage) {
        try {
            final PdfDecoder decoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
                decoder.openPdfFileFromURL(pdfPath);
            } else {
                decoder.openPdfFile(pdfPath);
            }

            decoder.decodePage(pdfPageNumber);
        } catch (final Exception e) {
            logger.error("Error decoding PDF page {} of file {}", pdfPageNumber, pdfPath, e);
        }

        final PdfPageData pdfPageData = pdfDecoder.getPdfPageData();

        final int pageHeight = pdfPageData.getMediaBoxHeight(pdfPageNumber);
        final int cropHeight = pdfPageData.getCropBoxHeight(pdfPageNumber);

        final int cropX = pdfPageData.getCropBoxX(pdfPageNumber);
        final int cropY = pdfPageData.getCropBoxY(pdfPageNumber);

        /*
         * when parsing a widget we don't want to be updating the display until all widgets are parsed, so add all the widgets to a list
         * so, after parsing, the list can be iterated and the widgets added to the page.
         */
        new WidgetParser(widgetFactory).parseWidgets(pdfDecoder.getFormRenderer(), newPage, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

        //newPage.setWidgets(widgetsList);

        pdfDecoder.closePdfFile();
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
                recentDocuments[i].setText(i + 1 + ": " + shortenedFileName);
                if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                    recentDocuments[i].setVisible(false);
                } else {
                    recentDocuments[i].setVisible(true);
                }

                recentDocuments[i].setName(recentDocs[i]);
            }
        }
    }

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
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
