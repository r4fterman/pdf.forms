package org.pdf.forms.writer;

import java.util.Optional;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

class GlobalPdfWriter {

    private final Optional<PdfWriter> writer;
    private final Optional<PdfStamper> stamper;

    GlobalPdfWriter(final PdfWriter writer) {
        this.writer = Optional.of(writer);
        stamper = Optional.empty();
    }

    GlobalPdfWriter(final PdfStamper stamper) {
        writer = Optional.empty();
        this.stamper = Optional.of(stamper);
    }

    void addAnnotation(
            final PdfFormField pdfFormField,
            final int currentPage) {
        writer.ifPresentOrElse(w -> w.addAnnotation(pdfFormField),
                () -> stamper.ifPresent(s -> s.addAnnotation(pdfFormField, currentPage)));
    }

    PdfContentByte getContentByte(final int currentPage) {

        return writer.map(PdfWriter::getDirectContent)
                .orElseGet(() -> stamper.map(s -> s.getOverContent(currentPage))
                        .orElse(null));
    }
}
