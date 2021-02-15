package org.pdf.forms.writer;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class PdfComboBoxWriter implements PdfComponentWriter {

    private final Logger logger = LoggerFactory.getLogger(PdfComboBoxWriter.class);

    private final Set<String> fontSubstitutions = new HashSet<>();
    private final FontHandler fontHandler;

    public PdfComboBoxWriter(final FontHandler fontHandler) {
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
        //PdfCaption comboBoxCaption = widget.getCaptionComponent();
        writeOutCaption(widget, pageSize, currentPage, globalPdfWriter);

        final PdfComboBox value = (PdfComboBox) widget.getValueComponent();
        final java.awt.Rectangle valueBounds = value.getBounds();
        valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

        final ObjectProperties objectProperties = widget.getWidgetModel().getProperties().getObject();

        final Font font = value.getFont();

        final BaseFont baseFont = getBaseFont(font);

        final TextField combo = new TextField(writer, pdfValueBounds, widget.getWidgetName());
        combo.setFont(baseFont);
        combo.setFontSize(value.getFont().getSize());
        combo.setTextColor(getBaseColor(widget.getValueComponent().getForeground()));
        combo.setChoices(objectProperties.getItems().getItemValues().toArray(new String[0]));

        final boolean editable = objectProperties.getField().allowCustomTextEntry();
        if (editable) {
            combo.setOptions(BaseField.EDIT);
        }

        final String defaultText = objectProperties.getValue().getDefault()
                .map(this::convertDefaultValue)
                .orElse("");

        addBorder(widget, combo);

        final PdfFormField field = combo.getComboField();
        field.setValueAsString(defaultText);
        return field;
    }

    private String convertDefaultValue(final String defaultValue) {
        if (defaultValue.equals("< None >")) {
            return "";
        }
        return defaultValue;
    }

    private void writeOutCaption(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) {
        final PdfCaption caption = widget.getCaptionComponent();
        if (caption == null) {
            return;
        }

        if (widget.isComponentSplit()) {
            final LayoutProperties layoutProperties = widget.getWidgetModel().getProperties().getLayout();
            final String location = layoutProperties.getCaption().getPosition().orElse("None");
            if (location.equals("None")) {
                return;
            }
        }

        final java.awt.Rectangle captionBounds = caption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        final Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        final PdfContentByte cb = globalPdfWriter.getContentByte(currentPage);
        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfCaptionBounds.getLeft(), pdfCaptionBounds.getTop() - captionBounds.height);

        final java.awt.Font font = caption.getFont();
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

        caption.paint(graphics2D);

        graphics2D.dispose();
        cb.restoreState();
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

        // substitute with Helvetica
        fontSubstitutions.add(font.getFontName());
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }
}
