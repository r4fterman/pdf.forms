/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
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
 * PreviewNavigationToolbar.java
 * ---------------
 */
package org.pdf.forms.gui.toolbars;

import org.pdf.forms.gui.designer.gui.PreviewNavigatable;

public class PreviewNavigationToolbar extends NavigationToolbar {

    private PreviewNavigatable designer;

    public PreviewNavigationToolbar(PreviewNavigatable designer) {
        this.designer = designer;
    }

    public void executeCommand(int type) {
        switch (type) {
            case FIRSTPAGE:
                designer.displayPreviewPage(1);
                break;
            case FBACKPAGE:
                designer.displayPreviewPage(designer.getPreviewCurrentPage() - 10);
                break;
            case BACKPAGE:
                designer.displayPreviewPage(designer.getPreviewCurrentPage() - 1);
                break;
            case FORWARDPAGE:
                designer.displayPreviewPage(designer.getPreviewCurrentPage() + 1);
                break;
            case FFORWARDPAGE:
                designer.displayPreviewPage(designer.getPreviewCurrentPage() + 10);
                break;
            case LASTPAGE:
                designer.displayPreviewPage(designer.getPreviewCurrentPage());
                break;
            case SETPAGE:
                int page = Integer.parseInt(currentPageBox.getText());

                if (page >= 1 && page <= designer.getTotalNoOfPages()) {
                    designer.displayPreviewPage(page);
                } else {
                    currentPageBox.setText(designer.getPreviewCurrentPage() + "");
                }
                break;
        }
    }
}
