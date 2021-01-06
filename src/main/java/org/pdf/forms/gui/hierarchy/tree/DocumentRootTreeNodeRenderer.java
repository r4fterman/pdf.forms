package org.pdf.forms.gui.hierarchy.tree;

import java.net.URL;

import javax.swing.*;

public class DocumentRootTreeNodeRenderer implements TreeNodeRenderer {

    private final String value;

    public DocumentRootTreeNodeRenderer(final String value) {
        this.value = value;
    }

    @Override
    public String getText() {
        return value;
    }

    @Override
    public Icon getIcon() {
        final URL resource = getClass().getResource("/org/pdf/forms/res/Form.gif");
        return new ImageIcon(resource);
    }
}
