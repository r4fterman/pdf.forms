package org.pdf.forms.widgets;

import java.awt.*;

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
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.model.des.ValueProperties;
import org.pdf.forms.widgets.components.CheckBoxIcon;
import org.pdf.forms.widgets.components.PdfCheckBox;
import org.pdf.forms.widgets.components.SplitComponent;

public class CheckBoxWidget extends Widget {

    private static int checkBoxNextWidgetNumber = 1;

    public CheckBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Check Box.gif",
                fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Check Box" + checkBoxNextWidgetNumber;
        checkBoxNextWidgetNumber++;

        setWidgetName(widgetName);

        getWidgetModel().setName(widgetName);
        getWidgetModel().setType("CHECK_BOX");

        addProperties();
        addJavaScript();
    }

    public CheckBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Check Box.gif", fontHandler);

        setComponentSplit(true);
        setAllowEditCaptionAndValue(false);
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
        final CaptionProperties captionProperties = new CaptionProperties();
        captionProperties.setTextValue("Check Box");
        captionProperties.setDividerLocation("");
        getWidgetModel().getProperties().setCaptionProperties(captionProperties);
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
    }

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        final FieldProperties fieldProperties = new FieldProperties();
        fieldProperties.setAppearance("Sunken Box");
        fieldProperties.setGroupName("");
        fieldProperties.setPresence("Visible");
        objectProperties.setField(fieldProperties);

        final ValueProperties valueProperties = new ValueProperties();
        valueProperties.setType("User Entered - Optional");
        valueProperties.setDefault("Off");
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
        caption.setPosition("Right");
        caption.setReserve("4");
        layoutProperties.setCaption(caption);
    }

    private void addBorderProperties() {
        final BorderProperties borderProperties = getWidgetModel().getProperties().getBorder();

        final Borders borders = borderProperties.getBorders();
        borders.setBorderStyle("None");
        borders.setBorderWidth("1");
        borders.setBorderColor(String.valueOf(Color.BLACK.getRGB()));

        final BackgroundFill backgroundFill = borderProperties.getBackgroundFill();
        backgroundFill.setStyle("Solid");
        backgroundFill.setFillColor(String.valueOf(Color.WHITE.getRGB()));
    }

    private void addParagraphProperties() {
        final ParagraphCaption paragraphCaption = getWidgetModel().getProperties().getParagraph().getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("left");
        paragraphCaption.setVerticalAlignment("center");
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final SplitComponent radioButton = (SplitComponent) getBaseComponent();
        setParagraphProperties(radioButton.getCaption());

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

        final SplitComponent radioButton = (SplitComponent) getBaseComponent();
        if (radioButton.getCaptionPosition() != captionLocation) {
            radioButton.setCaptionPosition(captionLocation);
        }
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final SplitComponent radioButton = (SplitComponent) getBaseComponent();
        setFontProperties(radioButton.getCaption());

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties() {
        final JCheckBox comboBox = (JCheckBox) getValueComponent();

        final String state = getWidgetModel().getProperties().getObject().getValue().getDefault().orElse("Off");
        comboBox.setSelected(state.equals("On"));

        setBindingProperties();

        setSize(getWidth(), getHeight());
    }

    public void setCheckBoxGroupName(final String name) {
        getWidgetModel().getProperties().getObject().getField().setGroupName(name);
    }

    public String getCheckBoxGroupName() {
        return getWidgetModel().getProperties().getObject().getField().getGroupName().orElse("");
    }

    public void setOnOffImage(
            final Image onImage,
            final Image offImage) {
        final PdfCheckBox checkBox = (PdfCheckBox) getValueComponent();
        final CheckBoxIcon checkBoxIcon = new CheckBoxIcon(onImage, offImage);
        checkBox.setIcon(checkBoxIcon);
    }
}
