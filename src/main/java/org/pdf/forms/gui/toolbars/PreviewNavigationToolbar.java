package org.pdf.forms.gui.toolbars;

import org.pdf.forms.gui.designer.gui.PreviewNavigatable;

public class PreviewNavigationToolbar extends NavigationToolbar {

    private final PreviewNavigatable designer;

    public PreviewNavigationToolbar(final PreviewNavigatable designer) {
        this.designer = designer;
    }

    @Override
    public void executeCommand(final int type) {
        if (type == FIRSTPAGE) {
            designer.displayPreviewPage(1);
        } else if (type == FBACKPAGE) {
            designer.displayPreviewPage(designer.getPreviewCurrentPage() - 10);
        } else if (type == BACKPAGE) {
            designer.displayPreviewPage(designer.getPreviewCurrentPage() - 1);
        } else if (type == FORWARDPAGE) {
            designer.displayPreviewPage(designer.getPreviewCurrentPage() + 1);
        } else if (type == FFORWARDPAGE) {
            designer.displayPreviewPage(designer.getPreviewCurrentPage() + 10);
        } else if (type == LASTPAGE) {
            designer.displayPreviewPage(designer.getPreviewCurrentPage());
        } else if (type == SETPAGE) {
            final int page = Integer.parseInt(getCurrentPageBox().getText());

            if (page >= 1 && page <= designer.getTotalNoOfPages()) {
                designer.displayPreviewPage(page);
            } else {
                getCurrentPageBox().setText(designer.getPreviewCurrentPage() + "");
            }
        }
    }
}
