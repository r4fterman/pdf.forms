package org.pdf.forms.widgets;

import java.awt.*;
import java.util.List;

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
import org.pdf.forms.model.des.Item;
import org.pdf.forms.model.des.Items;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.ParagraphProperties;
import org.pdf.forms.model.des.ParagraphValue;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.model.des.ValueProperties;
import org.pdf.forms.widgets.components.PdfList;
import org.pdf.forms.widgets.components.SplitComponent;

public class ListBoxWidget extends Widget {

    private static int listBoxNextWidgetNumber = 1;

    private final FontHandler fontHandler;

    public ListBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/List Box.gif",
                fontHandler);
        this.fontHandler = fontHandler;

        setComponentSplit(true);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "List Box" + listBoxNextWidgetNumber;
        listBoxNextWidgetNumber++;

        setWidgetName(widgetName);

        getWidgetModel().setType("LIST_BOX");
        getWidgetModel().setName(widgetName);

        addProperties();
        addJavaScript();
    }

    public ListBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/List Box.gif", fontHandler);
        this.fontHandler = fontHandler;

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

    private void addFontProperties() {
        final FontProperties fontProperties = new FontProperties();

        final FontCaption fontCaption = fontProperties.getFontCaption();
        fontCaption.setFontName(fontHandler.getDefaultFont().getFontName());
        fontCaption.setFontSize("11");
        fontCaption.setFontStyle(String.valueOf(IWidget.STYLE_PLAIN));
        fontCaption.setUnderline(String.valueOf(IWidget.UNDERLINE_NONE));
        fontCaption.setStrikeThrough(String.valueOf(IWidget.STRIKETHROUGH_OFF));
        fontCaption.setColor(String.valueOf(Color.BLACK.getRGB()));

        final FontValue fontValue = fontProperties.getFontValue();
        fontValue.setFontName(fontHandler.getDefaultFont().getFontName());
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
        objectProperties.setField(fieldProperties);

        final ValueProperties valueProperties = new ValueProperties();
        valueProperties.setType("User Entered - Optional");
        valueProperties.setDefault("");
        objectProperties.setValue(valueProperties);

        final BindingProperties bindingProperties = new BindingProperties();
        bindingProperties.setName(getWidgetName());
        bindingProperties.setArrayNumber("0");
        objectProperties.setBinding(bindingProperties);

        objectProperties.setItems(new Items());
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

        final ParagraphCaption paragraphCaption = paragraphProperties.getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("left");
        paragraphCaption.setVerticalAlignment("center");

        final ParagraphValue paragraphValue = new ParagraphValue();
        paragraphValue.setHorizontalAlignment("left");
        paragraphValue.setVerticalAlignment("center");
        paragraphProperties.setParagraphValue(paragraphValue);

        getWidgetModel().getProperties().setParagraph(paragraphProperties);
    }

    private void addCaptionProperties() {
        final CaptionProperties captionProperties = new CaptionProperties();
        captionProperties.setTextValue("List");
        captionProperties.setDividerLocation("");
        getWidgetModel().getProperties().setCaptionProperties(captionProperties);
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        final SplitComponent listBox = (SplitComponent) getBaseComponent();
        setParagraphProperties(listBox.getCaption());

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

        final SplitComponent listBox = (SplitComponent) getBaseComponent();
        if (listBox.getCaptionPosition() != captionLocation) {
            listBox.setCaptionPosition(captionLocation);
        }
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        final SplitComponent listBox = (SplitComponent) getBaseComponent();
        setFontProperties(listBox.getCaption());

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties() {
        final PdfList pdfList = (PdfList) getValueComponent();

        final JList<String> list = pdfList.getList();

        /* add items to list box box list */
        final DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        model.removeAllElements();

        final List<Item> items = getWidgetModel().getProperties().getObject().getItems().getItem();
        items.forEach(item -> model.addElement(item.getItem()));

        String defaultText = getWidgetModel().getProperties().getObject().getValue().getDefault().orElse("< None >");
        if (defaultText.equals("< None >")) {
            defaultText = "";
        }
        list.setSelectedValue(defaultText, true);

        setBindingProperties();
        setSize(getWidth(), getHeight());
    }
}
