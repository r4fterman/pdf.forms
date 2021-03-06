package org.pdf.forms.widgets.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.model.des.Widget;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.ButtonWidget;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.ComboBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.ImageWidget;
import org.pdf.forms.widgets.ListBoxWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.pdf.forms.widgets.TextFieldWidget;
import org.pdf.forms.widgets.TextWidget;
import org.pdf.forms.widgets.components.PdfButton;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfCheckBox;
import org.pdf.forms.widgets.components.PdfComboBox;
import org.pdf.forms.widgets.components.PdfList;
import org.pdf.forms.widgets.components.PdfRadioButton;
import org.pdf.forms.widgets.components.PdfTextField;
import org.pdf.forms.widgets.components.SplitComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetFactory {

    private static final JFrame FRAME = new JFrame();

    public static JLabel createResizedComponent(
            final JComponent component,
            final int width,
            final int height) {
        return getComponentAsLabel(component, new Dimension(width, height));
    }

    private static JLabel getComponentAsLabel(
            final JComponent component,
            final Dimension size) {
        component.setSize(size);

        FRAME.getContentPane().setLayout(null);
        FRAME.getContentPane().add(component);
        FRAME.pack();

        final JLabel labelWithIcon = new JLabel(new ImageIcon(createImage(component)));
        labelWithIcon.setSize(size);
        return labelWithIcon;
    }

    private static BufferedImage createImage(final JComponent component) {
        final Dimension dimension = component.getSize();
        final Rectangle region = new Rectangle(0, 0, dimension.width, dimension.height);
        component.setOpaque(true);

        final BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.setClip(region);
        component.paint(graphics2D);
        graphics2D.dispose();

        return image;
    }

    private final Logger logger = LoggerFactory.getLogger(WidgetFactory.class);
    private final Map<Integer, BiFunction<Widget, Rectangle, IWidget>> factoryMethods;

    private final FontHandler fontHandler;

    public WidgetFactory(final FontHandler fontHandler) {
        this.fontHandler = fontHandler;
        factoryMethods = Map.of(
                IWidget.TEXT_FIELD, this::createTextField,
                IWidget.TEXT, this::createLabel,
                IWidget.BUTTON, this::createButton,
                IWidget.RADIO_BUTTON, this::createRadioButton,
                IWidget.CHECK_BOX, this::createCheckBox,
                IWidget.COMBO_BOX, this::createComboBox,
                IWidget.LIST_BOX, this::createListBox,
                IWidget.IMAGE, this::createImageWidget
        );
    }

    public IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds) {
        return createCheckBoxWidget(page, bounds, null);
    }

    IWidget createCheckBoxWidget(
            final Page page,
            final Rectangle bounds,
            final String groupName) {
        final CheckBoxWidget widget = createCheckBox(null, bounds);
        final ButtonGroup buttonGroup = createButtonGroup(page, groupName);
        widget.setCheckBoxGroupName(buttonGroup.getName());
        return widget;
    }

    public IWidget createRadioButtonWidget(
            final Page page,
            final Rectangle bounds) {
        final RadioButtonWidget widget = createRadioButton(null, bounds);
        final List<ButtonGroup> radioButtonGroups = page.getRadioButtonGroups();

        final ButtonGroup buttonGroup;
        if (radioButtonGroups.isEmpty()) {
            buttonGroup = new ButtonGroup(IWidget.RADIO_BUTTON);
            radioButtonGroups.add(buttonGroup);
        } else {
            buttonGroup = radioButtonGroups.get(radioButtonGroups.size() - 1);
        }

        widget.setRadioButtonGroupName(buttonGroup.getName());
        return widget;
    }

    public IWidget createWidget(
            final int widgetToAdd,
            final Rectangle bounds) {
        if (bounds != null) {
            if (bounds.height < 0) {
                bounds.height = -bounds.height;
                bounds.y = bounds.y - bounds.height;
            }

            if (bounds.width < 0) {
                bounds.width = -bounds.width;
                bounds.x = bounds.x - bounds.width;
            }
        }

        return createWidgetWithBounds(widgetToAdd, null, bounds);
    }

    public IWidget createWidget(
            final int widgetToAdd,
            final Widget widget) {
        return createWidgetWithBounds(widgetToAdd, widget, null);
    }

    private IWidget createWidgetWithBounds(
            final int widgetToAdd,
            final Widget widget,
            final Rectangle bounds) {
        return factoryMethods.getOrDefault(widgetToAdd, (r, b) -> {
            logger.warn("Manual exit because of impossible situation in WidgetFactory, trying to add widget type {}",
                    widgetToAdd);
            return null;
        }).apply(widget, bounds);
    }

    private IWidget createImageWidget(
            final Widget widget,
            final Rectangle bounds) {
        final JLabel image = new JLabel();
        image.setBackground(Color.WHITE);

        final JLabel label = getComponentAsLabel(image, getSize(150, 150, bounds));
        if (widget == null) {
            return new ImageWidget(IWidget.IMAGE, image, label, fontHandler);
        }
        return new ImageWidget(IWidget.IMAGE, image, label, widget, fontHandler);
    }

    private IWidget createTextField(
            final Widget widget,
            final Rectangle bounds) {
        final SplitComponent tf = new SplitComponent("Text Field",
                new PdfTextField("Text Field", fontHandler),
                SplitComponent.CAPTION_LEFT,
                fontHandler);

        final JLabel label = getComponentAsLabel(tf, getSize(150, 20, bounds));
        if (widget == null) {
            return new TextFieldWidget(IWidget.TEXT_FIELD, tf, label, fontHandler);
        }
        return new TextFieldWidget(IWidget.TEXT_FIELD, tf, label, widget, fontHandler);
    }

    private IWidget createLabel(
            final Widget widget,
            final Rectangle bounds) {
        final PdfCaption label = new PdfCaption("Text", fontHandler);
        label.setBackground(IDesigner.PAGE_COLOR);

        final JLabel componentAsLabel = getComponentAsLabel(label, getSize(100, 20, bounds));
        if (widget == null) {
            return new TextWidget(IWidget.TEXT, label, componentAsLabel, fontHandler);
        }
        return new TextWidget(IWidget.TEXT, label, componentAsLabel, widget, fontHandler);
    }

    private CheckBoxWidget createCheckBox(
            final Widget widget,
            final Rectangle bounds) {
        final PdfCheckBox cb = new PdfCheckBox();
        cb.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent checkBox = new SplitComponent("Check Box", cb, SplitComponent.CAPTION_RIGHT, fontHandler);
        final JLabel label = getComponentAsLabel(checkBox, getSize(100, 20, bounds));
        if (widget == null) {
            return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, label, fontHandler);
        }
        return new CheckBoxWidget(IWidget.CHECK_BOX, checkBox, label, widget, fontHandler);
    }

    private IWidget createComboBox(
            final Widget widget,
            final Rectangle bounds) {
        final PdfComboBox pdfComboBox = new PdfComboBox(fontHandler);

        final SplitComponent combo = new SplitComponent("Drop-down list",
                pdfComboBox,
                SplitComponent.CAPTION_LEFT,
                fontHandler);
        final JLabel label = getComponentAsLabel(combo, getSize(200, 20, bounds));

        if (widget == null) {
            return new ComboBoxWidget(IWidget.COMBO_BOX, combo, label, fontHandler);
        }
        return new ComboBoxWidget(IWidget.COMBO_BOX, combo, label, widget, fontHandler);
    }

    private IWidget createListBox(
            final Widget widget,
            final Rectangle bounds) {
        final PdfList pdfList = new PdfList(fontHandler);

        final SplitComponent scroll = new SplitComponent("List", pdfList, SplitComponent.CAPTION_TOP, fontHandler);
        final JLabel label = getComponentAsLabel(scroll, getSize(100, 100, bounds));

        if (widget == null) {
            return new ListBoxWidget(IWidget.LIST_BOX, scroll, label, fontHandler);
        }
        return new ListBoxWidget(IWidget.LIST_BOX, scroll, label, widget, fontHandler);
    }

    private IWidget createButton(
            final Widget widget,
            final Rectangle bounds) {
        final PdfButton pdfButton = new PdfButton("Button", fontHandler);

        final Dimension size = getSize(100, 50, bounds);
        final JLabel label = getComponentAsLabel(pdfButton, size);
        if (widget == null) {
            return new ButtonWidget(IWidget.BUTTON, pdfButton, label, fontHandler);
        }
        return new ButtonWidget(IWidget.BUTTON, pdfButton, label, widget, fontHandler);
    }

    private RadioButtonWidget createRadioButton(
            final Widget widget,
            final Rectangle bounds) {
        final PdfRadioButton pdfRadioButton = new PdfRadioButton();
        pdfRadioButton.setBackground(IDesigner.PAGE_COLOR);

        final SplitComponent radioButton = new SplitComponent("Radio Button",
                pdfRadioButton,
                SplitComponent.CAPTION_RIGHT,
                fontHandler);
        final JLabel label = getComponentAsLabel(radioButton, getSize(200, 20, bounds));
        if (widget == null) {
            return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, label, fontHandler);
        }
        return new RadioButtonWidget(IWidget.RADIO_BUTTON, radioButton, label, widget, fontHandler);
    }

    private ButtonGroup createButtonGroup(
            final Page page,
            final String groupName) {
        if (groupName == null) {
            final ButtonGroup buttonGroup = new ButtonGroup(IWidget.CHECK_BOX);
            page.getCheckBoxGroups().add(buttonGroup);
            return buttonGroup;
        }

        return page.getCheckBoxGroup(groupName)
                .orElseGet(() -> {
                    final ButtonGroup buttonGroup = new ButtonGroup(groupName);
                    page.getCheckBoxGroups().add(buttonGroup);
                    return buttonGroup;
                });
    }

    private Dimension getSize(
            final int defaultWidth,
            final int defaultHeight,
            final Rectangle bounds) {
        return Optional.ofNullable(bounds)
                .map(b -> convertToDimension(defaultWidth, defaultHeight, b))
                .orElseGet(() -> new Dimension(defaultWidth, defaultHeight));
    }

    private Dimension convertToDimension(
            final int defaultWidth,
            final int defaultHeight,
            final Rectangle bounds) {
        final int width = getWidth(bounds, defaultWidth);
        final int height = getHeight(bounds, defaultHeight);
        return new Dimension(width, height);
    }

    private int getWidth(
            final Rectangle bounds,
            final int defaultWidth) {
        if (bounds.width != 0) {
            return bounds.width;
        }
        return defaultWidth;
    }

    private int getHeight(
            final Rectangle bounds,
            final int defaultHeight) {
        if (bounds.height != 0) {
            return bounds.height;
        }
        return defaultHeight;
    }

}
