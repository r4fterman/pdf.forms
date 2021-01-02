package org.pdf.forms.widgets.components;

import javax.swing.*;

public class PdfCheckBox extends JCheckBox implements IPdfComponent {

    public PdfCheckBox() {
        setText(null);
    }

    @Override
    public void setUnderlineType(final int type) {
        // do nothing
    }

    @Override
    public void setStrikethrough(final boolean isStrikethrough) {
        // do nothing
    }
}
