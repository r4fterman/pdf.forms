package org.pdf.forms.widgets.components;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;

public class PdfComboBox extends JComboBox<String> implements IPdfComponent {

    private static final float FONT_SIZE = 11f;

    public PdfComboBox(final FontHandler fontHandler) {
        setEditable(true);
        setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));
    }

    @Override
    public void setUnderlineType(final int type) {
        // do nothing
    }

    @Override
    public void setStrikethrough(final boolean isStrikethrough) {
        // do nothing
    }

    @Override
    public void setHorizontalAlignment(final int alignment) {
        // do nothing
    }

    @Override
    public void setVerticalAlignment(final int alignment) {
        // do nothing
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(final String text) {
        // do nothing
    }
}
