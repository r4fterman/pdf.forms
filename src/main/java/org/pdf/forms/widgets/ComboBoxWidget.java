package org.pdf.forms.widgets;

import java.awt.*;
import java.util.Optional;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.CaptionProperties;
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
import org.pdf.forms.widgets.components.SplitComponent;

public class ComboBoxWidget extends Widget {

    private static int comboBoxNextWidgetNumber = 1;

    public ComboBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Drop-down List.gif",
                fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(true);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Drop-down List" + comboBoxNextWidgetNumber;
        comboBoxNextWidgetNumber++;

        setWidgetName(widgetName);

        getWidgetModel().setName(widgetName);
        getWidgetModel().setType("COMBO_BOX");

        addProperties();
        addJavaScript();
    }

    public ComboBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Drop-down List.gif", fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(true);
        setAllowEditOfCaptionOnClick(true);

        final BindingProperties bindingProperties = widget.getProperties().getObject().getBinding();
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
        final CaptionProperties captionProperties = new CaptionProperties();
        captionProperties.setTextValue("Drop-down List");
        captionProperties.setDividerLocation("");
        getWidgetModel().getProperties().setCaptionProperties(captionProperties);
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

        final FontValue fontValue = fontProperties.getFontValue();
        fontValue.setFontName(getFontHandler().getDefaultFont().getFontName());
        fontValue.setFontSize("11");
        fontValue.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontValue.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontValue.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontValue.setColor(String.valueOf(Color.BLACK.getRGB()));

        getWidgetModel().getProperties().setFont(fontProperties);
    }

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        final FieldProperties fieldProperties = new FieldProperties();
        fieldProperties.setAppearance("Sunken Box");
        fieldProperties.setPresence("Visible");
        fieldProperties.allowCustomTextEntry(false);
        objectProperties.setField(fieldProperties);

        final ValueProperties valueProperties = new ValueProperties();
        valueProperties.setType("User Entered - Optional");
        valueProperties.setDefault("< None >");
        objectProperties.setValue(valueProperties);

        final BindingProperties bindingProperties = new BindingProperties();
        bindingProperties.setName(getWidgetName());
        bindingProperties.setArrayNumber("0");
        objectProperties.setBinding(bindingProperties);
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

        final org.pdf.forms.model.des.Caption caption = new org.pdf.forms.model.des.Caption();
        caption.setPosition("Left");
        caption.setReserve("4");
        layoutProperties.setCaption(caption);
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

    private void addParagraphProperties() {
        final ParagraphProperties paragraphProperties = new ParagraphProperties();

        final Optional<ParagraphCaption> paragraphCaption = paragraphProperties.getParagraphCaption();
        paragraphCaption.ifPresent(caption -> caption.setHorizontalAlignment("left"));
        paragraphCaption.ifPresent(caption -> caption.setVerticalAlignment("center"));

        final ParagraphValue paragraphValue = new ParagraphValue();
        paragraphValue.setHorizontalAlignment("left");
        paragraphValue.setVerticalAlignment("center");
        paragraphProperties.setParagraphValue(paragraphValue);

        getWidgetModel().getProperties().setParagraph(paragraphProperties);
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final SplitComponent comboBox = (SplitComponent) getBaseComponent();
        setParagraphProperties(comboBox.getCaption());

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

        final SplitComponent comboBox = (SplitComponent) getBaseComponent();
        if (comboBox.getCaptionPosition() != captionLocation) {
            comboBox.setCaptionPosition(captionLocation);
        }
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final SplitComponent comboBox = (SplitComponent) getBaseComponent();
        setFontProperties(comboBox.getCaption());

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties() {
        /* add items to combo box list */
        final JComboBox<String> comboBox = (JComboBox<String>) getValueComponent();
        final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
        model.removeAllElements();

        getWidgetModel().getProperties().getObject().getItems()
                .ifPresent(items -> items.getItem().forEach(item -> model.addElement(item.getItem())));

        /* set default value for combo box */
        String defaultText = getWidgetModel().getProperties().getObject().getValue().getDefault().orElse("< None >");
        if (defaultText.equals("< None >")) {
            defaultText = "";
        }
        comboBox.setSelectedItem(defaultText);
        setBindingProperties();

        setSize(getWidth(), getHeight());
    }
}
