package org.pdf.forms.widgets;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.FieldProperties;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.FontProperties;
import org.pdf.forms.model.des.FontValue;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.ParagraphProperties;
import org.pdf.forms.model.des.ParagraphValue;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.model.des.ValueProperties;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.SplitComponent;

public class TextFieldWidget extends Widget {

    private static int nextWidgetNumber = 1;

    public TextFieldWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Text Field.gif",
                fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(true);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Text Field" + nextWidgetNumber;
        nextWidgetNumber++;

        setWidgetName(widgetName);
        getWidgetModel().setName(widgetName);
        getWidgetModel().setType("TEXT_FIELD");

        addProperties();
        addJavaScript();
    }

    public TextFieldWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Text Field.gif", fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(true);
        setAllowEditOfCaptionOnClick(true);

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
        addCaptionProperties();
    }

    private void addCaptionProperties() {
        getWidgetModel().getProperties().getCaptionProperties().setTextValue("Text Field");
    }

    private void addFontProperties() {
        final FontProperties fontProperties = getWidgetModel().getProperties().getFont();

        final FontCaption fontCaption = fontProperties.getFontCaption();
        fontCaption.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontCaption.setFontSize("11");
        fontCaption.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontCaption.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontCaption.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontCaption.setColor(String.valueOf(Color.BLACK.getRGB()));

        final FontValue fontValue = fontProperties.getFontValue();
        fontValue.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontValue.setFontSize("11");
        fontValue.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontValue.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontValue.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontValue.setColor(String.valueOf(Color.BLACK.getRGB()));
    }

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        final FieldProperties fieldProperties = objectProperties.getField();
        fieldProperties.setAppearance("Sunken Box");
        fieldProperties.allowMultipleLines(false);
        fieldProperties.setLimitLength(false);
        fieldProperties.setPresence("Visible");

        final ValueProperties valueProperties = objectProperties.getValue();
        valueProperties.setType("User Entered - Optional");
        valueProperties.setDefault("Text Field");

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
        margins.setLeft("4");
        margins.setLeft("2");
        margins.setLeft("4");

        final org.pdf.forms.model.des.Caption caption = layoutProperties.getCaption();
        caption.setPosition("Left");
        caption.setReserve("4");
    }

    private void addBorderProperties() {
        final BorderProperties border = getWidgetModel().getProperties().getBorder();

        final Borders borders = border.getBorders();
        borders.setBorderStyle("None");
        borders.setBorderWidth("1");
        borders.setBorderColor(String.valueOf(Color.BLACK.getRGB()));

        final BackgroundFill backgroundFill = border.getBackgroundFill();
        backgroundFill.setStyle("Solid");
        backgroundFill.setFillColor(String.valueOf(Color.WHITE.getRGB()));
    }

    private void addParagraphProperties() {
        final ParagraphProperties paragraphProperties = getWidgetModel().getProperties().getParagraph();

        final ParagraphCaption paragraphCaption = paragraphProperties.getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("left");
        paragraphCaption.setVerticalAlignment("center");

        final ParagraphValue paragraphValue = paragraphProperties.getParagraphValue();
        paragraphValue.setHorizontalAlignment("left");
        paragraphValue.setVerticalAlignment("center");
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final SplitComponent textField = (SplitComponent) getBaseComponent();

        if (currentlyEditing == IWidget.COMPONENT_BOTH) {
            setFontProperties(textField.getCaption());
            setFontProperties((IPdfComponent) textField.getValue());
        } else if (currentlyEditing == IWidget.COMPONENT_CAPTION) {
            setFontProperties(textField.getCaption());
        } else if (currentlyEditing == IWidget.COMPONENT_VALUE) {
            setFontProperties((IPdfComponent) textField.getValue());
        }

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setLayoutProperties() {
        setSizeAndPosition();
        setCaptionLocation();

        setSize(getWidth(), getHeight());
    }

    private void setCaptionLocation() {
        final int captionLocation = getWidgetModel().getProperties().getLayout().getCaption().getPosition()
                .map(caption -> new Caption(caption).getLocation())
                .orElse(Caption.DEFAULT_LOCATION);

        final SplitComponent textField = (SplitComponent) getBaseComponent();
        if (textField.getCaptionPosition() != captionLocation) {
            textField.setCaptionPosition(captionLocation);
        }
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final SplitComponent textField = (SplitComponent) getBaseComponent();

        if (currentlyEditing == IWidget.COMPONENT_BOTH) {
            setParagraphProperties(textField.getCaption());
            setParagraphProperties((IPdfComponent) textField.getValue());
        } else if (currentlyEditing == IWidget.COMPONENT_CAPTION) {
            setParagraphProperties(textField.getCaption());
        } else if (currentlyEditing == IWidget.COMPONENT_VALUE) {
            setParagraphProperties((IPdfComponent) textField.getValue());
        }

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties() {
        final String defaultText = getWidgetModel().getProperties().getObject().getValue().getDefault().orElse("");

        final JTextField textField = (JTextField) getValueComponent();
        textField.setText(defaultText);

        setBindingProperties();
    }

}
