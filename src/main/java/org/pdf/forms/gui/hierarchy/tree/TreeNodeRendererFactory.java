package org.pdf.forms.gui.hierarchy.tree;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;

public final class TreeNodeRendererFactory {

    public static TreeNodeRenderer getInstance(
            final Object userObject,
            final IMainFrame mainFrame) {
        if (userObject instanceof IWidget) {
            return new WidgetTreeNodeRenderer((IWidget) userObject, mainFrame);
        } else if (userObject instanceof Page) {
            return new PageTreeNodeRenderer((Page) userObject);
        } else if (userObject.equals("Document Root")) {
            return new DocumentRootTreeNodeRenderer(userObject.toString());
        }
        return new DefaultTreeNodeRenderer();
    }

    private TreeNodeRendererFactory() {
        // do nothing
    }
}
