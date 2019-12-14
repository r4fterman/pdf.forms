package org.pdf.forms.widgets.components;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.pdf.forms.fonts.FontHandler;

public class PdfList extends JScrollPane implements IPdfComponent {

    private final Lst list;

    public JList<String> getList() {
        return list;
    }

    public PdfList(final FontHandler fontHandler) {
        list = new Lst(new DefaultListModel<>(), fontHandler);

        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        setViewportView(list);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(final String text) {
    }

    @Override
    public void setUnderlineType(final int type) {
    }

    @Override
    public void setStrikethrough(final boolean isStrikethrough) {
    }

    @Override
    public void setHorizontalAlignment(final int alignment) {

    }

    @Override
    public void setVerticalAlignment(final int alignment) {
    }
}

class Lst extends JList<String> {

    private static final float FONT_SIZE = 11f;

    Lst(
            final DefaultListModel<String> defaultListModel,
            final FontHandler fontHandler) {
        super(defaultListModel);
        setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));
    }

}

