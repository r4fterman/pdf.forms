package org.pdf.forms.gui.toolbars;

import org.pdf.forms.gui.designer.gui.DesignNavigatable;

public class DesignNavigationToolbar extends NavigationToolbar {

    private final DesignNavigatable designer;

    public DesignNavigationToolbar(final DesignNavigatable designer) {
        this.designer = designer;
    }

    @Override
    public void executeCommand(final int type) {
        if (type == FIRSTPAGE) {
            designer.displayDesignerPage(1);
        } else if (type == FBACKPAGE) {
            designer.displayDesignerPage(designer.getDesignerCurrentPage() - 10);
        } else if (type == BACKPAGE) {
            designer.displayDesignerPage(designer.getDesignerCurrentPage() - 1);
        } else if (type == FORWARDPAGE) {
            designer.displayDesignerPage(designer.getDesignerCurrentPage() + 1);
        } else if (type == FFORWARDPAGE) {
            designer.displayDesignerPage(designer.getDesignerCurrentPage() + 10);
        } else if (type == LASTPAGE) {
            designer.displayDesignerPage(designer.getTotalNoOfPages());
        } else if (type == SETPAGE) {
            final int page = Integer.parseInt(getCurrentPageBox().getText());

            if (page >= 1 && page <= designer.getTotalNoOfPages()) {
                designer.displayDesignerPage(page);
            } else {
                getCurrentPageBox().setText(designer.getDesignerCurrentPage() + "");
            }
        }
    }
}
