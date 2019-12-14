package org.pdf.forms.widgets.components;

import javax.swing.JCheckBox;

public class PdfCheckBox extends JCheckBox implements IPdfComponent {

    public PdfCheckBox() {
        setText(null);
    }

    @Override
    public void setUnderlineType(final int type) {
    }

    @Override
    public void setStrikethrough(final boolean isStrikethrough) {
    }
}
