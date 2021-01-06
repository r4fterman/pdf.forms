package org.pdf.forms.gui.toolbars;

import org.pdf.forms.gui.designer.gui.PreviewNavigatable;

public class PreviewNavigationToolbar extends NavigationToolbar {

    private final PreviewNavigatable designer;

    public PreviewNavigationToolbar(final PreviewNavigatable designer) {
        this.designer = designer;
    }

    @Override
    public void executeCommand(final int type) {
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
                final int page = Integer.parseInt(getCurrentPageBox().getText());

                if (page >= 1 && page <= designer.getTotalNoOfPages()) {
                    designer.displayPreviewPage(page);
                } else {
                    getCurrentPageBox().setText(String.valueOf(designer.getPreviewCurrentPage()));
                }
                break;
            default:
                break;
        }
    }
}
