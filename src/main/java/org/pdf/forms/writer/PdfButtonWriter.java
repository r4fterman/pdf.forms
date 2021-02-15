package org.pdf.forms.writer;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;

public class PdfButtonWriter implements PdfComponentWriter {

    private final Logger logger = LoggerFactory.getLogger(PdfButtonWriter.class);

    private final Set<String> fontSubstitutions = new HashSet<>();

    private final FontHandler fontHandler;

    public PdfButtonWriter(final FontHandler fontHandler) {
        this.fontHandler = fontHandler;
    }

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
            final GlobalPdfWriter globalPdfWriter) throws IOException, DocumentException {
        final PdfButton value = (PdfButton) widget.getValueComponent();

        final java.awt.Rectangle valueBounds = value.getBounds();
        valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

        final Font font = value.getFont();
        final BaseFont baseFont = getBaseFont(font);

        final PushbuttonField pushbutton = new PushbuttonField(writer, pdfValueBounds, widget.getWidgetName());
        pushbutton.setFont(baseFont);
        pushbutton.setFontSize(font.getSize());
        pushbutton.setText(value.getText());
        pushbutton.setTextColor(getBaseColor(value.getForeground()));
        pushbutton.setBackgroundColor(getBaseColor(value.getBackground()));

        addBorder(widget, pushbutton);

        return pushbutton.getField();
    }

    private void addBorder(
            final IWidget widget,
            final BaseField baseField) {
        final Borders borders = widget.getWidgetModel().getProperties().getBorder().getBorders();

        final String style = borders.getBorderStyle().orElse("None");
        switch (style) {
            case "Solid":
                baseField.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
                break;
            case "Dashed":
                baseField.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
                break;
            case "Beveled":
                baseField.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
                break;
            default:
                return;
        }

        final int borderColor = borders.getBorderColor().map(Integer::parseInt).orElse(Color.WHITE.getRGB());
        baseField.setBorderColor(new GrayColor(borderColor));

        final int borderWidth = borders.getBorderWidth().map(Integer::parseInt).orElse(1);
        baseField.setBorderWidth(borderWidth);
    }

    private Rectangle convertJavaCoordsToPdfCoords(
            final java.awt.Rectangle bounds,
            final Rectangle pageSize) {
        final int javaX1 = bounds.x - IMainFrame.INSET;
        final int javaY1 = bounds.y - IMainFrame.INSET;

        final int javaX2 = javaX1 + bounds.width;

        final float pdfY1 = pageSize.getHeight() - javaY1 - bounds.height;
        final float pdfY2 = pdfY1 + bounds.height;

        return new Rectangle(javaX1, pdfY1, javaX2, pdfY2);
    }

    private BaseFont getBaseFont(final Font font) throws IOException, DocumentException {
        final Optional<String> absoluteFontPath = fontHandler.getAbsoluteFontPath(font);
        if (absoluteFontPath.isPresent()) {
            final String fontPath = absoluteFontPath.get();
            try {
                return BaseFont.createFont(fontPath, BaseFont.CP1250, BaseFont.EMBEDDED);
            } catch (final DocumentException e) {
                // A document exception has been thrown meaning that the font cannot be embedded
                // due to licensing restrictions
                logger.error("Failed creating font from path {}!", fontPath, e);
            }
        }

        // substitute font with Helvetica
        fontSubstitutions.add(font.getFontName());
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }
}
