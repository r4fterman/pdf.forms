package org.pdf.forms.widgets.utils;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.jpedal.objects.acroforms.formData.FormObject;
import org.jpedal.objects.acroforms.rendering.AcroRenderer;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.Item;
import org.pdf.forms.model.des.ObjectProperties;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.IWidget;

public final class WidgetParser {

    private final WidgetFactory widgetFactory;
    private final IMainFrame mainFrame;

    public WidgetParser(
            final WidgetFactory widgetFactory,
            final IMainFrame mainFrame) {
        this.widgetFactory = widgetFactory;
        this.mainFrame = mainFrame;
    }

    public void parseWidgets(
            final AcroRenderer currentFormRenderer,
            final Page page,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage) {
        final Map<String, FormObject> rawFormData = currentFormRenderer.getRawFormData();

        for (final Map.Entry<String, FormObject> entry: rawFormData.entrySet()) {
            final FormObject formObject = entry.getValue();

            final int type = formObject.getType();
            if (type == FormObject.FORMBUTTON) {
                addFormButton(page, pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
            } else if (type == FormObject.FORMTEXT) {
                addTextField(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
            } else if (type == FormObject.FORMCHOICE) {
                addChoice(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
            }
        }
    }

    private void addChoice(
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        final boolean[] flags = formObject.getFieldFlags();

        boolean isCombo = false;
        if (flags != null) {
            isCombo = flags[FormObject.COMBO];
        }

        if (isCombo) {
            addComboBox(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
        } else {
            addListBox(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
        }
    }

    private void addFormButton(
            final Page page,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        final boolean[] flags = formObject.getFieldFlags();

        boolean isPushButton = false;
        if (flags != null) {
            isPushButton = flags[FormObject.PUSHBUTTON];
        }

        if (isPushButton) {
            addPushButton(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
        } else {
            //checkBox
            addCheckBox(formObject,
                    page,
                    null,
                    pageHeight,
                    cropHeight,
                    cropX,
                    cropY,
                    widgetsOnPage);
        }
    }

    private void addListBox(
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        //it is a list
        final IWidget widget = getBasicWidget(formObject,
                IWidget.LIST_BOX,
                pageHeight,
                cropHeight,
                cropX,
                cropY,
                widgetsOnPage);

        handleChoiceField(formObject, widget);

        widgetsOnPage.add(widget);
    }

    private void addComboBox(
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        final IWidget widget = getBasicWidget(formObject,
                IWidget.COMBO_BOX,
                pageHeight,
                cropHeight,
                cropX,
                cropY,
                widgetsOnPage);

        handleChoiceField(formObject, widget);

        widgetsOnPage.add(widget);
    }

    private void addTextField(
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        final IWidget widget = getBasicWidget(formObject,
                IWidget.TEXT_FIELD,
                pageHeight,
                cropHeight,
                cropX,
                cropY,
                widgetsOnPage);
        final String text = formObject.getTextString();

        widget.getWidgetModel().getProperties().getObject().getValue().setDefault(text);
        widget.setObjectProperties();

        handleBorder(formObject, widget);
        handleVisibility(formObject, widget);

        widgetsOnPage.add(widget);
    }

    private void addPushButton(
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetsOnPage,
            final FormObject formObject) {
        final IWidget widget = getBasicWidget(formObject,
                IWidget.BUTTON,
                pageHeight,
                cropHeight,
                cropX,
                cropY,
                widgetsOnPage);

        final String text = formObject.getNormalCaption();

        widget.getWidgetModel().getProperties().getObject().getValue().setDefault(text);
        widget.setObjectProperties();

        handleBorder(formObject, widget);
        handleVisibility(formObject, widget);

        widgetsOnPage.add(widget);
    }

    private int getNextArrayNumberForName(
            final String name,
            final IWidget widgetToTest,
            final WidgetArrays widgetArrays,
            final List<IWidget> widgets) {
        if (widgetArrays.isWidgetArrayInList(name)) {
            // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest);
            // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        for (final IWidget widget: widgets) {
            final String widgetName = widget.getWidgetName();
            if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
                // another widget of this name already exists, and this is the first
                // we've heard of it
                widgetArrays.addWidgetToArray(name, widget);
                // add the original widget
                widgetArrays.addWidgetToArray(name, widgetToTest);
                // add the new widget

                return widgetArrays.getNextArrayIndex(name);
            }
        }

        return 0;
    }

    private void addCheckBox(
            final FormObject formObject,
            final Page page,
            final String groupName,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetList) {
        if (formObject.getKidData() != null) {
            final String name = formObject.getFieldName();

            // this is a holder for more checkboxes
            final Map<?, FormObject> kidData = formObject.getKidData();
            for (final Map.Entry<?, FormObject> entry: kidData.entrySet()) {
                final FormObject form = entry.getValue();
                addCheckBox(form, page, name, pageHeight, cropHeight, cropX, cropY, widgetList);
            }
            return;
        }

        String group = groupName;
        if (groupName == null) {
            group = formObject.getFieldName();
        }

        final Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);

        final IWidget widget = widgetFactory.createCheckBoxWidget(page, bounds, group);
        setGenericWidgetProperties(formObject, bounds, widget, widgetList);

        final AbstractButton checkBox = (AbstractButton) widget.getValueComponent();

        checkBox.setSize(bounds.getSize());
        appearanceImages(formObject, widget);

        //get default state
        String value = "Off";
        final String defaultState = formObject.getDefaultState();
        if (defaultState != null && defaultState.equals(formObject.getNormalOnState())) {
            value = "On";
        }

        widget.getWidgetModel().getProperties().getObject().getValue().setDefault(value);
        widget.setObjectProperties();

        handleBorder(formObject, widget);
        handleVisibility(formObject, widget);

        widgetList.add(widget);
    }

    private IWidget getBasicWidget(
            final FormObject formObject,
            final int type,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final List<IWidget> widgetList) {
        final Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);
        final IWidget widget = widgetFactory.createWidget(type, bounds);
        setGenericWidgetProperties(formObject, bounds, widget, widgetList);
        return widget;
    }

    private void setGenericWidgetProperties(
            final FormObject formObject,
            final Rectangle bounds,
            final IWidget widget,
            final List<IWidget> widgetList) {
        widget.setX(bounds.x);
        widget.setY(bounds.y);

        final Set<IWidget> set = new HashSet<>();
        set.add(widget);

        PropertyChanger.updateSizeAndPosition(
                set,
                new Point(bounds.x, bounds.y),
                new Dimension(bounds.width, bounds.height)
        );

        /*
         * if the component is split, then because we are parsing it in from a
         * PDF file we know it has no caption, therefore set the caption
         * position to "None"
         */
        if (widget.isComponentSplit()) {
            widget.getWidgetModel().getProperties().getLayout().getCaption().setPosition("None");
            widget.setLayoutProperties();
        }

        final String widgetName = formObject.getFieldName();
        final int arrayNumber = getNextArrayNumberForName(widgetName,
                widget,
                this.mainFrame.getWidgetArrays(),
                widgetList);

        final BindingProperties bindingProperties = widget.getWidgetModel().getProperties().getObject().getBinding();
        bindingProperties.setName(widgetName);
        bindingProperties.setArrayNumber(String.valueOf(arrayNumber));
        widget.setObjectProperties();
    }

    private Rectangle getBounds(
            final FormObject formObject,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY) {
        /* get a clone of the bounding rectangle so not to affect the original */
        final Rectangle bounds = (Rectangle) formObject.getBoundingRectangle().getBounds().clone();

        /* move the widget up the page to convert PDF y-cord to Java y-cord */
        bounds.y = pageHeight - (bounds.y + bounds.height);

        /* handle pages with a crop */
        bounds.y -= pageHeight - (cropY + cropHeight);
        bounds.x -= cropX;

        /* add on the page insets*/
        bounds.y += IMainFrame.INSET;
        bounds.x += IMainFrame.INSET;

        return bounds;
    }

    private void handleChoiceField(
            final FormObject formObject,
            final IWidget widget) {
        final ObjectProperties objectProperties = widget.getWidgetModel().getProperties().getObject();

        //populate items array with list from Opt
        final String[] items = formObject.getItemsList();
        if (items != null) {
            final List<Item> itemList = objectProperties.getItems().getItem();
            for (final String value: items) {
                if (value != null && !value.equals("")) {
                    itemList.add(new Item(value));
                }
            }
        }

        //get and set currently selected value
        String textValue = formObject.getSelectedItem();
        if (formObject.getValuesMap() != null) {
            textValue = formObject.getValuesMap().get(textValue).toString();
        }

        objectProperties.getValue().setDefault(textValue);
        widget.setObjectProperties();

        handleBorder(formObject, widget);
        handleVisibility(formObject, widget);
    }

    private void handleBorder(
            final FormObject formObject,
            final IWidget widget) {
        final Map<String, String> border = Optional.ofNullable(formObject.getBorder())
                .map(map -> (Map<String, String>) map)
                .orElseGet(() -> Map.of(
                        "S", "/S",
                        "W", "1"
                ));

        final BorderProperties borderProperties = widget.getWidgetModel().getProperties().getBorder();
        final Borders borders = borderProperties.getBorders();

        final Color borderColor = formObject.getBorderColor();
        if (borderColor == null) {
            borders.setBorderStyle("None");
            return;
        }

        final String width = border.get("W");
        if (width != null) {
            borders.setBorderWidth(width);
        }

        String style = border.get("S");
        if (style != null) {
            if (style.equals("/B")) {
                style = "Beveled";
            }

            borders.setBorderStyle(style);
        }

        borders.setBorderColor(String.valueOf(borderColor.getRGB()));

        final Color backgroundColor = formObject.getBackgroundColor();
        if (backgroundColor != null) {
            borderProperties.getBackgroundFill().setFillColor(String.valueOf(backgroundColor.getRGB()));
        }

        widget.setBorderAndBackgroundProperties();
    }

    private void handleVisibility(
            final FormObject formObject,
            final IWidget widget) {
        final boolean[] characteristic = formObject.getCharacteristics();
        //todo: WTF???
        if (characteristic[0] || characteristic[1] || characteristic[5]) {
            widget.getWidgetModel().getProperties().getObject().getField().setPresence("Invisible");
        }
    }

    private void appearanceImages(
            final FormObject formObject,
            final IWidget widget) {
        final CheckBoxWidget checkBoxWidget = (CheckBoxWidget) widget;

        Image offImage = null;
        if (formObject.hasNormalOff()) {
            offImage = (formObject.getNormalOffImage());
        }

        Image onImage = null;
        if (formObject.hasNormalOn()) {
            onImage = (formObject.getNormalOnImage());
        }

        checkBoxWidget.setOnOffImage(onImage, offImage);
    }
}
