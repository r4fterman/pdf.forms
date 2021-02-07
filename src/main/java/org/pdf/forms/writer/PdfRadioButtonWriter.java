package org.pdf.forms.writer;

import java.util.Collections;
import java.util.Set;

import org.pdf.forms.widgets.IWidget;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfRadioButtonWriter implements PdfComponentWriter {

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
            final GlobalPdfWriter globalPdfWriter) {
        // do nothing
        return null;
    }
}
