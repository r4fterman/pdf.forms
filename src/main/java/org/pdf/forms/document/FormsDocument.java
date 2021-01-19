package org.pdf.forms.document;

import java.util.ArrayList;
import java.util.List;

import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.model.des.Version;

public class FormsDocument {

    private final List<Page> pages = new ArrayList<>();
    private final DesDocument desDocument;

    public FormsDocument(final Version version) {
        this.desDocument = new DesDocument(version);
    }

    public FormsDocument(final DesDocument desDocument) {
        this.desDocument = desDocument;
    }

    public void movePage(
            final int fromIndex,
            final int toIndex) {
        if (fromIndex > toIndex) {
            final Page objectToMove = pages.get(fromIndex);
            pages.add(toIndex, objectToMove);

            pages.remove(pages.lastIndexOf(objectToMove));
        } else {
            final Page objectToMove = pages.get(fromIndex);
            pages.add(toIndex + 1, objectToMove);

            pages.remove(objectToMove);
        }
    }

    public void addPage(
            final int index,
            final Page page) {
        pages.add(index - 1, page);
    }

    public void removePage(final int index) {
        pages.remove(index - 1);
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getPage(final int pageNumber) {
        return pages.get(pageNumber - 1);
    }

    public int getNoOfPages() {
        return pages.size();
    }

    public DesDocument getDesDocument() {
        return desDocument;
    }
}
