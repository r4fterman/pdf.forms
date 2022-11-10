package org.pdf.forms.widgets;

import java.awt.*;
import java.util.Optional;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.CaptionProperties;
import org.pdf.forms.model.des.FieldProperties;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.FontProperties;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.ParagraphProperties;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;

public class TextWidget extends Widget {

    private static int textNextWidgetNumber = 1;

    public TextWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Text.gif",
                fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Text" + textNextWidgetNumber;
        textNextWidgetNumber++;

        setWidgetName(widgetName);

        getWidgetModel().setType("TEXT");
        getWidgetModel().setName(widgetName);

        addProperties();
    }

    public TextWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Text.gif", fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        setWidgetName(getWidgetModel().getName().orElse(""));
        setAllProperties();
    }

    private void addProperties() {
        addFontProperties();
        addObjectProperties();
        addLayoutProperties();
        addBorderProperties();
        addParagraph();
        addCaptionProperties();

        getWidgetModel().setJavaScript(null);
    }

    private void addFontProperties() {
        final FontProperties fontProperties = new FontProperties();

        final FontCaption fontCaption = fontProperties.getFontCaption();
        fontCaption.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontCaption.setFontSize("11");
        fontCaption.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontCaption.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontCaption.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontCaption.setColor(String.valueOf(Color.BLACK.getRGB()));

        getWidgetModel().getProperties().setFont(fontProperties);
    }

    private void addObjectProperties() {
        final FieldProperties fieldProperties = new FieldProperties();
        fieldProperties.setPresence("Visible");
        getWidgetModel().getProperties().getObject().setField(fieldProperties);
    }

    private void addLayoutProperties() {
        final LayoutProperties layoutProperties = getWidgetModel().getProperties().getLayout();

        final SizeAndPosition sizeAndPosition = layoutProperties.getSizeAndPosition();
        sizeAndPosition.setX(1);
        sizeAndPosition.setY(1);
        sizeAndPosition.setWidth(1);
        sizeAndPosition.setHeight(1);
        sizeAndPosition.setXExpandToFit("false");
        sizeAndPosition.setYExpandToFit("false");
        sizeAndPosition.setAnchor("Top Left");
        sizeAndPosition.setRotation("0");

        final Margins margins = layoutProperties.getMargins();
        margins.setLeft("2");
        margins.setRight("4");
        margins.setTop("2");
        margins.setBottom("4");
    }

    private void addBorderProperties() {
        final BorderProperties borderProperties = new BorderProperties();

        final Borders borders = borderProperties.getBorders();
        borders.setBorderStyle("None");
        borders.setBorderWidth("1");
        borders.setBorderColor(String.valueOf(Color.BLACK.getRGB()));

        final BackgroundFill backgroundFill = borderProperties.getBackgroundFill();
        backgroundFill.setStyle("Solid");
        backgroundFill.setFillColor(String.valueOf(Color.WHITE.getRGB()));

        getWidgetModel().getProperties().setBorder(borderProperties);
    }

    private void addParagraph() {
        final ParagraphProperties paragraphProperties = new ParagraphProperties();

        final Optional<ParagraphCaption> paragraphCaption = paragraphProperties.getParagraphCaption();
        paragraphCaption.ifPresent(caption -> caption.setHorizontalAlignment("left"));
        paragraphCaption.ifPresent(caption -> caption.setVerticalAlignment("center"));
        getWidgetModel().getProperties().setParagraph(paragraphProperties);
    }

    private void addCaptionProperties() {
        final CaptionProperties captionProperties = new CaptionProperties();
        captionProperties.setTextValue("Text");
        getWidgetModel().getProperties().setCaptionProperties(captionProperties);
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final IPdfComponent baseComponent = (IPdfComponent) getBaseComponent();
        setParagraphProperties(baseComponent);
    }

    @Override
    public void setLayoutProperties() {
        setSizeAndPosition();
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final IPdfComponent baseComponent = (IPdfComponent) getBaseComponent();
        setFontProperties(baseComponent);
        setSize(getWidth(), getHeight());
    }

    @Override
    public void setCaptionProperties() {
        final String captionText = getWidgetModel().getProperties().getCaptionProperties().getTextValue().orElse("");
        getCaptionComponent().setText(captionText);
        setSize(getWidth(), getHeight());
    }

    @Override
    public PdfCaption getCaptionComponent() {
        return (PdfCaption) getBaseComponent();
    }

    @Override
    public Point getAbsoluteLocationsOfCaption() {
        return new Point(getX(), getY());
    }
}
