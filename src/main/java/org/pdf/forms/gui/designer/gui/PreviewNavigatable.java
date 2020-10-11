package org.pdf.forms.gui.designer.gui;

public interface PreviewNavigatable {
    void displayPreviewPage(int page);

    int getPreviewCurrentPage();

    int getTotalNoOfPages();
}
