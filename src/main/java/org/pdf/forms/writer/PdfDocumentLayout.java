/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* PdfDocumentLayout.java
* ---------------
*/
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
