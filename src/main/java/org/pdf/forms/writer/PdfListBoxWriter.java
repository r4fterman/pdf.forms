package org.pdf.forms.writer;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.itextpdf.awt.DefaultFontMapper;
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

public class PdfListBoxWriter implements PdfComponentWriter {

    private final Logger logger = LoggerFactory.getLogger(PdfListBoxWriter.class);

    private final Set<String> fontSubstitutions = new HashSet<>();

    private final FontHandler fontHandler;

    public PdfListBoxWriter(final FontHandler fontHandler) {
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
        //PdfCaption listBoxCaption = widget.getCaptionComponent();
        writeOutCaption(widget, pageSize, currentPage, globalPdfWriter);

        final JList<String> value = ((PdfList) widget.getValueComponent()).getList();
        final java.awt.Rectangle valueBounds = value.getBounds();
        valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

        final ListModel<String> listModel = value.getModel();
        final List<String> items = new ArrayList<>();
        for (int i = 0; i < listModel.getSize(); i++) {
            items.add(listModel.getElementAt(i));
        }

        final Font font = value.getFont();

        final BaseFont baseFont = getBaseFont(font);

        final TextField list = new TextField(writer, pdfValueBounds, widget.getWidgetName());
        list.setFont(baseFont);
        list.setFontSize(value.getFont().getSize());
        list.setChoices(items.toArray(String[]::new));

        final String defaultText = XMLUtils.getPropertyElement(rootElement, "Default")
                .map(defaultElement -> defaultElement.getAttributes().getNamedItem("value").getNodeValue())
                .orElse("");

        int index = 0;
        if (!defaultText.equals("")) {
            for (int i = 0; i < listModel.getSize(); i++) {
                final String item = listModel.getElementAt(i);
                if (item.equals(defaultText)) {
                    index = i;
                    break;
                }
            }
        }

        list.setChoiceSelection(index);

        addBorder(widget, list);

        return list.getListField();
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
            final Element captionElement = XMLUtils.getElementsFromNodeList(
                    widget.getProperties().getElementsByTagName("layout")).get(0);

            final String location = XMLUtils.getPropertyElement(captionElement, "Position")
                    .map(positionElement -> positionElement.getAttributeNode("value").getValue())
                    .orElse("None");
            if (location.equals("None")) {
                return;
            }
        }

        final java.awt.Rectangle captionBounds = caption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        final Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        final PdfContentByte pdfContentByte = globalPdfWriter.getContentByte(currentPage);
        pdfContentByte.saveState();
        pdfContentByte.concatCTM(1, 0, 0, 1, pdfCaptionBounds.getLeft(), pdfCaptionBounds.getTop() - captionBounds.height);

        final Font font = caption.getFont();
        final String fontDirectory = fontHandler.getFontDirectory(font);


        /*
         * we need to make this erroneous call to awtToPdf to see if an exception is thrown, if it is, it is
         * probably because the font cannot be embedded due to licensing restrictions, so substitute with Helvetica
         */
        DefaultFontMapper mapper;
        try {
            mapper = new DefaultFontMapper();
            mapper.insertDirectory(fontDirectory);
            mapper.awtToPdf(font);
        } catch (Exception e) {
            logger.error("Failed converting font from AWT to PDF for {}!", font.getName(), e);
            mapper = new DefaultFontMapper();
            fontSubstitutions.add(font.getFontName());
        }

        final Graphics2D graphics2D = pdfContentByte.createGraphics(captionBounds.width, captionBounds.height, mapper, true, .95f);

        //Graphics2D graphics2D = cb.createGraphicsShapes(captionBounds.width, captionBounds.height, true, 0.95f);

        caption.paint(graphics2D);

        graphics2D.dispose();
        pdfContentByte.restoreState();
    }

    private void addBorder(
            final IWidget widget,
            final BaseField baseField) {
        final Document document = widget.getProperties();
        final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        final Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);

        final String style = XMLUtils.getAttributeValueFromChildElement(border, "Border Style").orElse("None");
        final String width = XMLUtils.getAttributeValueFromChildElement(border, "Border Width").orElse("1");
        final String color = XMLUtils.getAttributeValueFromChildElement(border, "Border Color").orElse(String
                .valueOf(Color.WHITE.getRGB()));

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

        baseField.setBorderColor(new GrayColor(Integer.parseInt(color)));
        baseField.setBorderWidth(Integer.parseInt(width));
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
        final String fontPath = fontHandler.getAbsoluteFontPath(font);
        try {
            return BaseFont.createFont(fontPath, "Cp1250", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            logger.error("Error embedding font. So use Helvetica instead.", e);
            /*
             * A document exception has been thrown meaning that the font cannot be embedded
             * due to licensing restrictions so substitute with Helvetica
             */
            final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
            fontSubstitutions.add(font.getFontName());
            return baseFont;
        }
    }

}
