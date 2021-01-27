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

    private static int nextWidgetNumber = 1;

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

        final String widgetName = "List Box" + nextWidgetNumber;
        nextWidgetNumber++;

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

    private void addCaptionProperties() {
        final CaptionProperties captionProperties = getWidgetModel().getProperties().getCaptionProperties();
        captionProperties.setTextValue("List");
    }

    private void addFontProperties() {
        final FontProperties fontProperties = getWidgetModel().getProperties().getFont();

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
    }

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        final FieldProperties fieldProperties = objectProperties.getField();
        fieldProperties.setAppearance("Sunken Box");
        fieldProperties.setPresence("Visible");

        final ValueProperties valueProperties = objectProperties.getValue();
        valueProperties.setType("User Entered - Optional");

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

        final org.pdf.forms.model.des.Caption caption = layoutProperties.getCaption();
        caption.setPosition("Left");
        caption.setReserve("4");
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
        final ParagraphProperties paragraphProperties = getWidgetModel().getProperties().getParagraph();

        final ParagraphCaption paragraphCaption = paragraphProperties.getParagraphCaption();
        paragraphCaption.setHorizontalAlignment("left");
        paragraphCaption.setVerticalAlignment("center");

        final ParagraphValue paragraphValue = paragraphProperties.getParagraphValue();
        paragraphValue.setHorizontalAlignment("left");
        paragraphValue.setVerticalAlignment("center");
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
