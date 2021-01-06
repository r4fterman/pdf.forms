package org.pdf.forms.gui.hierarchy.tree;

import java.net.URL;

import javax.swing.*;

import org.pdf.forms.document.Page;

public class PageTreeNodeRenderer implements TreeNodeRenderer {

    private final Page page;

    public PageTreeNodeRenderer(final Page page) {
        this.page = page;
    }

    @Override
    public String getText() {
        return page.getPageName();
    }

    @Override
    public Icon getIcon() {
        final URL resource = getClass().getResource("/org/pdf/forms/res/Page.gif");
        return new ImageIcon(resource);
    }
}
