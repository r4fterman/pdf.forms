package org.pdf.forms.writer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfRadioButtonWriter implements PdfComponentWriter {

    private final Set<String> fontSubstitutions = new HashSet<>();

    @Override
    public Set<String> getFontSubstitutions() {
        return Collections.emptySet();
    }

    @Override
    public PdfFormField write(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final PdfWriter writer,
            final Element rootElement,
            final GlobalPdfWriter globalPdfWriter) {
        // do nothing
        return null;
    }
}
