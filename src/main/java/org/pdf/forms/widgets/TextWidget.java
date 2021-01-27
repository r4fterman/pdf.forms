package org.pdf.forms.widgets;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;

public class TextWidget extends Widget {

    private static int nextWidgetNumber = 1;

    public TextWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(), type, baseComponent, component, "/org/pdf/forms/res/Text.gif", fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Text" + nextWidgetNumber;
        nextWidgetNumber++;

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
        final FontCaption fontCaption = getWidgetModel().getProperties().getFont().getFontCaption();

        fontCaption.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontCaption.setFontSize("11");
        fontCaption.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontCaption.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontCaption.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontCaption.setColor(String.valueOf(Color.BLACK.getRGB()));

        getWidgetModel().getProperties().getObject().getField().setPresence("Visible");

        final LayoutProperties layoutProperties = getWidgetModel().getProperties().getLayout();

        final SizeAndPosition sizeAndPosition = layoutProperties.getSizeAndPosition();
        sizeAndPosition.setXExpandToFit("false");
        sizeAndPosition.setYExpandToFit("false");
        sizeAndPosition.setAnchor("Top Left");
        sizeAndPosition.setRotation("0");

        final Margins margins = layoutProperties.getMargins();
        margins.setLeft("2");
        margins.setRight("4");
        margins.setTop("2");
        margins.setBottom("4");

        final Borders borders = getWidgetModel().getProperties().getBorder().getBorders();
        borders.setBorderStyle("None");
        borders.setBorderWidth("1");
        borders.setBorderColor(String.valueOf(Color.BLACK.getRGB()));

        final BackgroundFill backgroundFill = getWidgetModel().getProperties().getBorder().getBackgroundFill();
        backgroundFill.setStyle("Solid");
        backgroundFill.setFillColor(String.valueOf(Color.WHITE.getRGB()));

        final ParagraphCaption paragraphCaption = getWidgetModel().getProperties().getParagraph().getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("left");
        paragraphCaption.setVerticalAlignment("center");

        getWidgetModel().getProperties().getCaptionProperties().setTextValue("Text");
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final IPdfComponent text = (IPdfComponent) getBaseComponent();
        setParagraphProperties(text);
    }

    @Override
    public void setLayoutProperties() {
        setSizeAndPosition();
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final IPdfComponent text = (IPdfComponent) getBaseComponent();
        setFontProperties(text);
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
