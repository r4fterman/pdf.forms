package org.pdf.forms.writer;

import java.util.ArrayList;
import java.util.List;

public class PdfDocumentLayout {

    private final List<Page> document = new ArrayList<>();

    public void addPage(final boolean isPdfPage) {
        document.add(new Page(isPdfPage));
    }

    public void addPage(
            final boolean isPdfPage,
            final String pdfPath,
            final int pdfPageNumber) {
        document.add(new Page(isPdfPage, pdfPath, pdfPageNumber));
    }

    public Page getPage(final int pageNumber) {
        return document.get(pageNumber - 1);
    }

    public List<Page> getPdfPages() {
        final List<Page> pdfPages = new ArrayList<>();

        for (final Page page : document) {
            if (page.isPdfPage) {
                pdfPages.add(page);
            }
        }

        return pdfPages;
    }

    public static class Page {
        private final boolean isPdfPage;
        private String pdfPath;
        private int pdfPageNumber;

        Page(final boolean isPdfPage) {
            this.isPdfPage = isPdfPage;
        }

        Page(
                final boolean isPdfPage,
                final String pdfPath,
                final int pdfPageNumber) {
            this(isPdfPage);
            this.pdfPath = pdfPath;
            this.pdfPageNumber = pdfPageNumber;
        }

        boolean isPdfPage() {
            return isPdfPage;
        }

        String getPdfPath() {
            return pdfPath;
        }

        int getPdfPageNumber() {
            return pdfPageNumber;
        }
    }
}
