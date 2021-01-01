package org.pdf.forms.gui.commands;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.SwingWorker;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PDFImportWorker extends SwingWorker {

    private final Logger logger = LoggerFactory.getLogger(PDFImportWorker.class);

    private final IMainFrame mainFrame;
    private final String version;
    private final WidgetFactory widgetFactory;
    private final int importType;
    private final int pageCount;
    private final String pdfPath;
    private final PdfDecoder pdfDecoder;

    PDFImportWorker(
            final IMainFrame mainFrame,
            final String version,
            final WidgetFactory widgetFactory,
            final int importType,
            final int pageCount,
            final String pdfPath,
            final PdfDecoder pdfDecoder) {
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.importType = importType;
        this.pageCount = pageCount;
        this.pdfPath = pdfPath;
        this.pdfDecoder = pdfDecoder;
    }

    @Override
    public Object construct() {
        final ProgressMonitor progressDialog = createProgressMonitor();

        final List<Object[]> pages = new ArrayList<>();

        final int currentLastPage;
        if (importType == PDFImportChooser.IMPORT_NEW) {
            currentLastPage = importNew(progressDialog, pages, mainFrame.getTotalNoOfPages());
        } else if (importType == PDFImportChooser.IMPORT_EXISTING) {
            currentLastPage = importExisting(progressDialog, pages, mainFrame.getTotalNoOfPages());
        } else {
            currentLastPage = 1;
        }

        EventQueue.invokeLater(() -> {
            for (final Object[] properties: pages) {
                final int pageNumber = (Integer) properties[0];
                final Page newPage = (Page) properties[1];
                final List<IWidget> widgetsOnPage = (List<IWidget>) properties[2];

                mainFrame.setCurrentPage(pageNumber);

                addPage(pageNumber, newPage);

                for (final IWidget widget: widgetsOnPage) {
                    mainFrame.getDesigner().addWidget(widget);
                }
            }

            setTotalPages();

            mainFrame.setCurrentPage(currentLastPage);
            mainFrame.displayPage(mainFrame.getCurrentPage());

            progressDialog.close();

        });

        return null;
    }

    private int importNew(
            final ProgressMonitor progressDialog,
            final List<Object[]> pages,
            final int pageNumber) {
        int currentLastPage = pageNumber;
        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
            currentLastPage++;

            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

            final List<IWidget> widgetsOnPage = new ArrayList<>();
            final boolean isCancelled = decodePDFPage(pdfPath,
                    pdfDecoder,
                    progressDialog,
                    pdfPageNumber,
                    newPage,
                    widgetsOnPage);

            final Object[] properties = {
                    pdfPageNumber,
                    newPage,
                    widgetsOnPage};
            pages.add(properties);

            if (isCancelled) {
                break;
            }
        }

        mainFrame.setCurrentDesignerFileName("Untitled");
        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);
        return currentLastPage;
    }

    private int importExisting(
            final ProgressMonitor progressDialog,
            final List<Object[]> pages,
            final int pageNumber) {
        int currentLastPage = pageNumber;
        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
            currentLastPage++;

            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

            final List<IWidget> widgetsOnPage = new ArrayList<>();
            final boolean isCancelled = decodePDFPage(pdfPath,
                    pdfDecoder,
                    progressDialog,
                    pdfPageNumber,
                    newPage,
                    widgetsOnPage);

            final Object[] properties = {
                    currentLastPage,
                    newPage,
                    widgetsOnPage};
            pages.add(properties);

            if (isCancelled) {
                break;
            }
        }
        return currentLastPage;
    }

    private ProgressMonitor createProgressMonitor() {
        final ProgressMonitor progressDialog = new ProgressMonitor((Component) mainFrame, "", "", 0, pageCount);
        progressDialog.setMillisToDecideToPopup(0);
        progressDialog.setMillisToPopup(0);
        progressDialog.setNote("Importing page " + 1 + " of " + progressDialog.getMaximum());
        progressDialog.setProgress(0);

        return progressDialog;
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
            final PdfDecoder decoder = getDecoder(pdfPath);
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
         * when parsing a widget we don't want to be updating the display
         * until all widgets are parsed, so add all the widgets to a list
         * and after parsing this list can be iterated and the widgets added to the page.
         */
        new WidgetParser(widgetFactory, mainFrame).parseWidgets(pdfDecoder.getFormRenderer(),
                newPage,
                pageHeight,
                cropHeight,
                cropX,
                cropY,
                widgetsOnPage);

        pdfDecoder.closePdfFile();
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
}
