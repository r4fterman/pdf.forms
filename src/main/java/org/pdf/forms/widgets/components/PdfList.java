package org.pdf.forms.widgets.components;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;

public class PdfList extends JScrollPane implements IPdfComponent {

    private static final float FONT_SIZE = 11f;

    private final JList<String> list;

    public PdfList(final FontHandler fontHandler) {
        list = new JList<>(new DefaultListModel<>());
        list.setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));

        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        setViewportView(list);
    }

    public JList<String> getList() {
        return list;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(final String text) {
        // do nothing
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
}
