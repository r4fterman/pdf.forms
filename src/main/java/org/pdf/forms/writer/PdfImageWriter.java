package org.pdf.forms.writer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfImageWriter implements PdfComponentWriter {

    private final Set<String> fontSubstitutions = new HashSet<>();

    @Override
    public Set<String> getFontSubstitutions() {
        return Set.copyOf(fontSubstitutions);
    }

    @Override
    public PdfFormField write(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final PdfWriter writer,
            final GlobalPdfWriter globalPdfWriter) {
        addImage(widget, pageSize, currentPage, globalPdfWriter);
        return null;
    }

    private void addImage(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) {
        final JLabel image = (JLabel) widget.getValueComponent();
        if (image == null) {
            return;
        }

        final java.awt.Rectangle imageBounds = image.getBounds();
        imageBounds.setLocation(widget.getX(), widget.getY());
        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(imageBounds, pageSize);

        // write out caption
        final PdfContentByte cb = globalPdfWriter.getContentByte(currentPage);
        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfValueBounds.getLeft(), pdfValueBounds.getTop() - imageBounds.height);

        final Graphics2D g2 = cb.createGraphicsShapes(imageBounds.width, imageBounds.height);

        image.paint(g2);

        g2.dispose();
        cb.restoreState();
    }

    private Rectangle convertJavaCoordsToPdfCoords(
            final java.awt.Rectangle bounds,
            final Rectangle pageSize) {
        final float javaX1 = bounds.x - IMainFrame.INSET;
        final float javaY1 = bounds.y - IMainFrame.INSET;

        final float javaX2 = javaX1 + bounds.width;

        final float pdfY1 = pageSize.getHeight() - javaY1 - bounds.height;

        final float pdfY2 = pdfY1 + bounds.height;

        return new Rectangle(javaX1, pdfY1, javaX2, pdfY2);
    }
}
