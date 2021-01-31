package org.pdf.forms.widgets;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.model.des.Draw;
import org.pdf.forms.model.des.FieldProperties;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Margins;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.utils.WidgetFactory;

public class ImageWidget extends Widget {

    private static int imageNextWidgetNumber = 1;

    private static final int STRETCH = 0;
    private static final int FULL_SIZE = 1;

    private Image currentImage;
    private int sizing;

    public ImageWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(new org.pdf.forms.model.des.Widget(),
                type,
                baseComponent,
                component,
                "/org/pdf/forms/res/Image.gif",
                fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(false);

        final String widgetName = "Image " + imageNextWidgetNumber;
        imageNextWidgetNumber++;

        setWidgetName(widgetName);
        getWidgetModel().setName(widgetName);
        getWidgetModel().setType("IMAGE");

        addProperties();
    }

    public ImageWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final org.pdf.forms.model.des.Widget widget,
            final FontHandler fontHandler) {
        super(widget, type, baseComponent, component, "/org/pdf/forms/res/Image.gif", fontHandler);

        setComponentSplit(false);
        setAllowEditCaptionAndValue(false);

        setWidgetName(widget.getName().orElse(""));

        addProperties();
    }

    private void addProperties() {
        addObjectProperties();
        addLayoutProperties();

        getWidgetModel().getProperties().setFont(null);
        getWidgetModel().getProperties().setBorder(null);
        getWidgetModel().getProperties().setParagraph(null);
        getWidgetModel().setJavaScript(null);

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

    private void addObjectProperties() {
        final ObjectProperties objectProperties = getWidgetModel().getProperties().getObject();

        FieldProperties fieldProperties = new FieldProperties();
        fieldProperties.setPresence("Visible");
        objectProperties.setField(fieldProperties);

        final Draw draw = new Draw();
        draw.setLocation("");
        draw.setSizing("Stretch Image To Fit");
        objectProperties.setDraw(draw);
    }

    @Override
    public void setLayoutProperties() {
        setSizeAndPosition();
    }

    @Override
    public void setObjectProperties() {
        final Draw draw = getWidgetModel().getProperties().getObject().getDraw();

        final String sizingPropertyValue = draw.getSizing().orElse("Stretch Image To Fit");
        if (sizingPropertyValue.equals("Stretch Image To Fit")) {
            this.sizing = STRETCH;
        } else if (sizingPropertyValue.equals("Use Image Size")) {
            this.sizing = FULL_SIZE;
        }

        final String location = draw.getLocation().orElse("");
        if (new File(location).exists()) {
            this.currentImage = Toolkit.getDefaultToolkit().createImage(location);
        } else {
            this.currentImage = null;
        }

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
        final JLabel imageLabel = (JLabel) getBaseComponent();

        if (currentImage == null) {
            imageLabel.setIcon(null);
        } else {
            if (sizing == STRETCH) {
                imageLabel.setIcon(new ImageIcon(currentImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            } else if (sizing == FULL_SIZE) {
                imageLabel.setIcon(new ImageIcon(currentImage));
            }
        }

        setComponent(WidgetFactory.createResizedComponent(getBaseComponent(), width, height));
    }
}
