package org.pdf.forms.writer;

import java.io.IOException;
import java.util.Set;

import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;

public interface PdfComponentWriter {

    Set<String> getFontSubstitutions();

    PdfFormField write(
            IWidget widget,
            Rectangle pageSize,
            int currentPage,
            final PdfWriter writer,
            final Element rootElement,
            final GlobalPdfWriter globalPdfWriter) throws IOException, DocumentException;
}
