package org.pdf.forms.writer;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.Caption;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class PdfTextFieldWriter implements PdfComponentWriter {

    private final Logger logger = LoggerFactory.getLogger(PdfTextFieldWriter.class);

    private final Set<String> fontSubstitutions = new HashSet<>();

    private final FontHandler fontHandler;

    public PdfTextFieldWriter(final FontHandler fontHandler) {
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
        //PdfCaption textFieldCaption = widget.getCaptionComponent();
        writeOutCaption(widget, pageSize, currentPage, globalPdfWriter);

        final PdfTextField value = (PdfTextField) widget.getValueComponent();
        final String valueText = value.getText();

        final java.awt.Rectangle valueBounds = value.getBounds();
        valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());
        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

        // write out textbox
        //BaseFont bf = valueFontToUse.getCalculatedBaseFont(false);

        final Font font = value.getFont();

        final BaseFont baseFont = getBaseFont(font);

        final TextField tf = new TextField(writer, pdfValueBounds, widget.getWidgetName());
        // tf.setBorderStyle(PdfBorderDictionary.STYLE_INSET);

        tf.setText(valueText);
        tf.setFont(baseFont);
        tf.setFontSize(value.getFont().getSize());
        tf.setTextColor(getBaseColor(widget.getValueComponent().getForeground()));
        tf.setBackgroundColor(getBaseColor(Color.white));
        addBorder(widget, tf);

        return tf.getTextField();
    }

    private void writeOutCaption(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) {
        final PdfCaption pdfCaption = widget.getCaptionComponent();
        if (pdfCaption == null) {
            return;
        }

        if (widget.isComponentSplit()) {
            final Caption caption = widget.getWidgetModel().getProperties().getLayout().getCaption();
            final String location = caption.getPosition().orElse("None");
            if (location.equals("None")) {
                return;
            }
        }

        final java.awt.Rectangle captionBounds = pdfCaption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        final Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        final PdfContentByte cb = globalPdfWriter.getContentByte(currentPage);
        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfCaptionBounds.getLeft(), pdfCaptionBounds.getTop() - captionBounds.height);

        final Font font = pdfCaption.getFont();
        final Optional<String> fontDirectory = fontHandler.getFontDirectory(font);

        DefaultFontMapper mapper = new DefaultFontMapper();
        fontDirectory.ifPresent(mapper::insertDirectory);

        /*
         * we need to make this erroneous call to awtToPdf to see if an exception is thrown, if it is, it is
         * probably because the font cannot be embedded due to licensing restrictions, so substitute with Helvetica
         */
        try {
            mapper.awtToPdf(font);
        } catch (final Exception e) {
            logger.error("Failed converting font from AWT to PDF for {}!", font.getName(), e);
            mapper = new DefaultFontMapper();
            fontSubstitutions.add(font.getFontName());
        }

        final Graphics2D graphics2D = cb.createGraphics(captionBounds.width, captionBounds.height, mapper, true, .95f);

        //Graphics2D graphics2D = cb.createGraphicsShapes(captionBounds.width, captionBounds.height, true, 0.95f);

        pdfCaption.paint(graphics2D);

        graphics2D.dispose();
        cb.restoreState();
    }

    private void addBorder(
            final IWidget widget,
            final BaseField baseField) {
        final Optional<BorderProperties> borderProperties = widget.getWidgetModel().getProperties().getBorder();
        if (borderProperties.isEmpty()) {
            return;
        }
        final Borders borders = borderProperties.get().getBorders();

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

        final int color = borders.getBorderColor().map(Integer::parseInt).orElse(Color.WHITE.getRGB());
        baseField.setBorderColor(new GrayColor(color));

        final int width = borders.getBorderWidth().map(Integer::parseInt).orElse(1);
        baseField.setBorderWidth(width);
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

    private void addJavaScriptToFormField(
            final Map<PdfName, String> eventsAndScripts,
            final PdfFormField formField,
            final PdfWriter writer) {
        for (final Map.Entry<PdfName, String> entry: eventsAndScripts.entrySet()) {
            final String script = entry.getValue();
            if (!script.equals("")) {
                formField.setAdditionalActions(entry.getKey(), PdfAction.javaScript(script, writer));
            }
        }
    }

    private BaseFont getBaseFont(final Font font) throws IOException, DocumentException {
        final Optional<String> absoluteFontPath = fontHandler.getAbsoluteFontPath(font);
        if (absoluteFontPath.isPresent()) {
            final String fontPath = absoluteFontPath.get();
            try {
                return BaseFont.createFont(fontPath, "Cp1250", BaseFont.EMBEDDED);
            } catch (final DocumentException e) {
                // A document exception has been thrown meaning that the font cannot be embedded
                // due to licensing restrictions
                logger.error("Error embedding font. So use Helvetica instead.", e);
            }
        }

        // substitute with Helvetica
        fontSubstitutions.add(font.getFontName());
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }
}
