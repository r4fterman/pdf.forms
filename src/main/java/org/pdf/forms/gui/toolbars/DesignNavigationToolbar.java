package org.pdf.forms.gui.toolbars;

import org.pdf.forms.gui.designer.gui.DesignNavigatable;

public class DesignNavigationToolbar extends NavigationToolbar {

    private final DesignNavigatable designer;

    public DesignNavigationToolbar(final DesignNavigatable designer) {
        this.designer = designer;
    }

    @Override
    public void executeCommand(final int type) {
        switch (type) {
            case FIRSTPAGE:
                designer.displayDesignerPage(1);
                break;
            case FBACKPAGE:
                designer.displayDesignerPage(designer.getDesignerCurrentPage() - 10);
                break;
            case BACKPAGE:
                designer.displayDesignerPage(designer.getDesignerCurrentPage() - 1);
                break;
            case FORWARDPAGE:
                designer.displayDesignerPage(designer.getDesignerCurrentPage() + 1);
                break;
            case FFORWARDPAGE:
                designer.displayDesignerPage(designer.getDesignerCurrentPage() + 10);
                break;
            case LASTPAGE:
                designer.displayDesignerPage(designer.getTotalNoOfPages());
                break;
            case SETPAGE:
                final int page = Integer.parseInt(getCurrentPageBox().getText());

                if (page >= 1 && page <= designer.getTotalNoOfPages()) {
                    designer.displayDesignerPage(page);
                } else {
                    getCurrentPageBox().setText(designer.getDesignerCurrentPage() + "");
                }
                break;
            default:
                break;
        }
    }
}
