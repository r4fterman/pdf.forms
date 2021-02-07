package org.pdf.forms.writer;

import java.io.IOException;
import java.util.Set;

import org.pdf.forms.widgets.IWidget;

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
            PdfWriter writer,
            GlobalPdfWriter globalPdfWriter) throws IOException, DocumentException;
}
