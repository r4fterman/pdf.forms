package org.pdf.forms.widgets;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.components.IPdfComponent;

public class ButtonWidget extends Widget {

    private static int nextWidgetNumber = 1;

    public ButtonWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Button.gif",
                fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);

        final String widgetName = "Button" + nextWidgetNumber;
        nextWidgetNumber++;

        setWidgetName(widgetName);
        getWidgetModel().setName(widgetName);
        getWidgetModel().setType("BUTTON");

        addProperties();
        addJavaScript();
    }

    public ButtonWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Button.gif", fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);

        final BindingProperties bindingProperties = getWidgetModel().getProperties().getObject().getBinding();
        setWidgetName(bindingProperties.getName().orElse(""));
        setArrayNumber(bindingProperties.getArrayNumber().map(Integer::parseInt).orElse(0));

        setAllProperties();
    }

    private void addProperties() {
        addFontProperties();
        addObjectProperties();
        addLayoutProperties();
        addBorderProperties();
        addParagraphProperties();
    }

    private void addFontProperties() {
        final FontCaption fontCaption = getWidgetModel().getProperties().getFont().getFontCaption();

        fontCaption.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontCaption.setFontSize("11");
        fontCaption.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontCaption.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontCaption.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontCaption.setColor(String.valueOf(Color.BLACK.getRGB()));
    }

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        objectProperties.getField().setPresence("Visible");
        objectProperties.getValue().setDefault("Button");

        final BindingProperties bindingProperties = objectProperties.getBinding();
        bindingProperties.setName(getWidgetName());
        bindingProperties.setArrayNumber("0");
    }

    private void addLayoutProperties() {
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
    }

    private void addBorderProperties() {
        final BorderProperties borderProperties = getWidgetModel().getProperties().getBorder();

        final Borders borders = borderProperties.getBorders();
        borders.setBorderStyle("None");
        borders.setBorderWidth("1");
        borders.setBorderColor(String.valueOf(Color.BLACK.getRGB()));

        final BackgroundFill backgroundFill = borderProperties.getBackgroundFill();
        backgroundFill.setStyle("Solid");
        backgroundFill.setFillColor(String.valueOf(getValueComponent().getBackground().getRGB()));
    }

    private void addParagraphProperties() {
        final ParagraphCaption paragraphCaption = getWidgetModel().getProperties().getParagraph().getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("center");
        paragraphCaption.setVerticalAlignment("center");
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final IPdfComponent button = (IPdfComponent) getBaseComponent();
        setParagraphProperties(button);
    }

    @Override
    public void setLayoutProperties() {
        setSizeAndPosition();
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final IPdfComponent button = (IPdfComponent) getBaseComponent();
        setFontProperties(button);

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties() {
        final JButton button = (JButton) getValueComponent();

        final String defaultText = getWidgetModel().getProperties().getObject().getValue().getDefault().orElse("");
        button.setText(defaultText);

        setSize(getWidth(), getHeight());

        setBindingProperties();
    }

    @Override
    public Point getAbsoluteLocationsOfValue() {
        return new Point(getX(), getY());
    }
}
