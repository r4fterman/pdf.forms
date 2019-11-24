package org.pdf.forms.writer;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

class GlobalPdfWriterTest extends EasyMockSupport {

    private PdfWriter writer;
    private PdfStamper stamper;

    @BeforeEach
    void setUp() {
        writer = createMock(PdfWriter.class);
        stamper = createMock(PdfStamper.class);
    }

    @Test
    void addAnnotation_on_pdfWriter_should_add_annotation_on_Writer() {
        final PdfFormField pdfFormField = createMock(PdfFormField.class);
        writer.addAnnotation(pdfFormField);

        replayAll();
        final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(writer);
        globalPdfWriter.addAnnotation(pdfFormField, 1);
        verifyAll();
    }

    @Test
    void addAnnotation_on_pdfStamper_should_add_annotation_on_stamper() {
        final PdfFormField pdfFormField = createMock(PdfFormField.class);
        stamper.addAnnotation(pdfFormField, 1);

        replayAll();
        final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(stamper);
        globalPdfWriter.addAnnotation(pdfFormField, 1);
        verifyAll();
    }

    @Test
    void getContentByte_on_pdfWriter_should_return_content_byte_from_writer() {
        final PdfContentByte pdfContentByte = createMock(PdfContentByte.class);
        expect(writer.getDirectContent()).andReturn(pdfContentByte);

        replayAll();
        final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(writer);
        final PdfContentByte contentByte = globalPdfWriter.getContentByte(1);
        verifyAll();

        assertThat(contentByte, is(pdfContentByte));
    }

    @Test
    void getContentByte_on_pdfStamper_should_return_content_byte_from_stamper() {
        final PdfContentByte pdfContentByte = createMock(PdfContentByte.class);
        expect(stamper.getOverContent(1)).andReturn(pdfContentByte);

        replayAll();
        final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(stamper);
        final PdfContentByte contentByte = globalPdfWriter.getContentByte(1);
        verifyAll();

        assertThat(contentByte, is(pdfContentByte));
    }
}
