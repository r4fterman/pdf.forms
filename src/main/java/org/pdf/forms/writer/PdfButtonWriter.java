package org.pdf.forms.writer;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

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
            final Element rootElement,
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

        final org.w3c.dom.Document document = widget.getProperties();
        final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);
        final List borderElement = XMLUtils.getElementsFromNodeList(borderProperties.getElementsByTagName("borders").item(0).getChildNodes());

        addBorder(widget, pushbutton);

        /* potential code for creating custom dash border */
        //pushbutton.getField().setBorderStyle(new PdfBorderDictionary(Float.parseFloat(width),PdfBorderDictionary.STYLE_BEVELED,new PdfDashPattern(2)));

        return pushbutton.getField();
    }

    private void addBorder(
            final IWidget widget,
            final BaseField tf) {
        final org.w3c.dom.Document document = widget.getProperties();
        final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        final Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);

        final String style = XMLUtils.getAttributeFromChildElement(border, "Border Style").orElse("None");
        final String width = XMLUtils.getAttributeFromChildElement(border, "Border Width").orElse("1");
        final String color = XMLUtils.getAttributeFromChildElement(border, "Border Color").orElse(String.valueOf(Color.WHITE.getRGB()));

        switch (style) {
            case "Solid":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
                break;
            case "Dashed":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
                break;
            case "Beveled":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
                break;
            case "None":
                return;
            default:
                return;
        }

        tf.setBorderColor(new GrayColor(Integer.parseInt(color)));
        tf.setBorderWidth(Integer.parseInt(width));
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

    private BaseFont getBaseFont(final Font font) throws IOException, DocumentException {
        final String fontPath = fontHandler.getAbsoluteFontPath(font);
        try {
            return BaseFont.createFont(fontPath, BaseFont.CP1250, BaseFont.EMBEDDED);
        } catch (final DocumentException e) {
            logger.error("Failed creating font from path {}!", fontPath, e);

            /*
             * A document exception has been thrown meaning that the font cannot be embedded
             * due to licensing restrictions so substitute with Helvetica
             */
            fontSubstitutions.add(font.getFontName());
            return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
        }
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }
}
