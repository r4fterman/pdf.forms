package org.pdf.forms.widgets.utils;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.jpedal.objects.acroforms.formData.FormObject;
import org.jpedal.objects.acroforms.rendering.AcroRenderer;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        boolean multiSelect = false;
        boolean sort = false;
        boolean isEditable = false;
        boolean doNotSpellCheck = false;
        boolean comminOnSelChange = false;
        if (flags != null) {
            isCombo = flags[FormObject.COMBO];
            multiSelect = flags[FormObject.MULTISELECT];
            sort = flags[FormObject.SORT];
            isEditable = flags[FormObject.EDIT];
            doNotSpellCheck = flags[FormObject.DONOTSPELLCHECK];
            comminOnSelChange = flags[FormObject.COMMITONSELCHANGE];
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
        boolean isRadio = false;
        boolean hasNoToggleToOff = false;
        boolean radioinUnison = false;
        if (flags != null) {
            isPushButton = flags[FormObject.PUSHBUTTON];
            isRadio = flags[FormObject.RADIO];
            hasNoToggleToOff = flags[FormObject.NOTOGGLETOOFF];
            radioinUnison = flags[FormObject.RADIOINUNISON];
        }

        if (isPushButton) {
            addPushButton(pageHeight, cropHeight, cropX, cropY, widgetsOnPage, formObject);
            // } else if (isRadio) {
            // TODO: radio button
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

        final Document document = widget.getProperties();

        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
        final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

        final String text = formObject.getTextString();
        defaultTextElement.getAttributeNode("value").setValue(text);

        widget.setObjectProperties(objectProperties);

        handleBorder(formObject, widget);
        handleVisibility(formObject, document);

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

        final Document document = widget.getProperties();

        final String text = formObject.getNormalCaption();

        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
        final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default")
                .get();
        defaultTextElement.getAttributeNode("value").setValue(text);
        widget.setObjectProperties(objectProperties);

        handleBorder(formObject, widget);

        handleVisibility(formObject, document);

        widgetsOnPage.add(widget);
    }

    private int getNextArrayNumberForName(
            final String name,
            final IWidget widgetToTest,
            final WidgetArrays widgetArrays,
            final List<IWidget> widgetList) {
        if (widgetArrays.isWidgetArrayInList(name)) {
            // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest);
            // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        for (final IWidget widget: widgetList) {
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
            // this is a holder for more checkboxes
            final Map<?, FormObject> kidData = formObject.getKidData();
            final Iterator<?> iter = kidData.keySet().iterator();

            final String name = formObject.getFieldName();

            while (iter.hasNext()) {
                final FormObject form = kidData.get(iter.next());

                addCheckBox(form, page, name, pageHeight, cropHeight, cropX, cropY, widgetList);
            }
        } else {
            String group = groupName;
            if (groupName == null) {
                group = formObject.getFieldName();
            }

            final Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);

            final IWidget widget = widgetFactory.createCheckBoxWidget(page, bounds, group);
            final Document document = widget.getProperties();

            setGenericWidgetProperties(formObject, bounds, widget, document, widgetList);

            final AbstractButton checkBox = (AbstractButton) widget.getValueComponent();

            checkBox.setSize(bounds.getSize());
            appearanceImages(formObject, widget);

            //get default state
            boolean selected = false;
            final String defaultState = formObject.getDefaultState();
            if (defaultState != null && defaultState.equals(formObject.getNormalOnState())) {
                selected = true;
            }

            // set the default state
            final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
            final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

            final String value;
            if (selected) {
                value = "On";
            } else {
                value = "Off";
            }
            defaultElement.getAttributeNode("value").setValue(value);
            widget.setObjectProperties(objectProperties);

            handleBorder(formObject, widget);

            handleVisibility(formObject, document);

            widgetList.add(widget);
        }
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
        setGenericWidgetProperties(formObject, bounds, widget, widget.getProperties(), widgetList);
        return widget;
    }

    private void setGenericWidgetProperties(
            final FormObject formObject,
            final Rectangle bounds,
            final IWidget widget,
            final Document document,
            final List<IWidget> widgetList) {

        //System.out.println("bounds = " + bounds);

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
            final Element layoutProperties = (Element) document.getElementsByTagName("layout").item(0);
            setProperty(layoutProperties, "Position", "None");

            widget.setLayoutProperties(layoutProperties);
        }

        /* set the widgets name */
        final String widgetName = formObject.getFieldName();

        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
        final Element nameElement = XMLUtils.getPropertyElement(objectProperties, "Name").get();
        nameElement.getAttributeNode("value").setValue(widgetName);

        widget.setObjectProperties(objectProperties);

        final int arrayNumber = getNextArrayNumberForName(widgetName,
                widget,
                this.mainFrame.getWidgetArrays(),
                widgetList);
        final Element arrayNumberElement = XMLUtils.getPropertyElement(objectProperties, "Array Number").get();
        arrayNumberElement.getAttributeNode("value").setValue(arrayNumber + "");

        widget.setObjectProperties(objectProperties);
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
        final Document document = widget.getProperties();

        //populate items array with list from Opt
        final String[] items = formObject.getItemsList();
        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);

        if (items != null) {
            final Element itemsElement = (Element) objectProperties.getElementsByTagName("items").item(0);

            for (final String value: items) {
                if (value != null && !value.equals("")) {
                    XMLUtils.addBasicProperty(document, "item", value, itemsElement);
                }
            }
        }

        final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default").get();

        //get and set currently selected value
        String textValue = formObject.getSelectedItem();
        if (formObject.getValuesMap() != null) {
            textValue = formObject.getValuesMap().get(textValue).toString();
        }
        defaultTextElement.getAttributeNode("value").setValue(textValue);
        widget.setObjectProperties(objectProperties);
        handleBorder(formObject, widget);
        handleVisibility(formObject, document);
    }

    private void handleBorder(
            final FormObject formObject,
            final IWidget widget) {
        Map<String, String> border = (Map<String, String>) formObject.getBorder();
        if (border == null) {
            border = Map.of(
                    "S", "/S",
                    "W", "1"
            );
        }
        final Document document = widget.getProperties();

        final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        final Color borderColor = formObject.getBorderColor();
        final Color backgroundColor = formObject.getBackgroundColor();

        if (borderColor == null) {
            setProperty(borderProperties, "Border Style", "None");
            return;
        }

        final String width = border.get("W");
        if (width != null) {
            setProperty(borderProperties, "Border Width", width);
        }

        String style = border.get("S");
        if (style != null) {
            if (style.equals("/B")) {
                style = "Beveled";
            }

            setProperty(borderProperties, "Border Style", style);
        }

        final String rgb = borderColor.getRGB() + "";
        setProperty(borderProperties, "Border Color", rgb);

        if (backgroundColor != null) {
            setProperty(borderProperties, "Fill Color", backgroundColor.getRGB() + "");
        }

        widget.setBorderAndBackgroundProperties(borderProperties);
    }

    private void setProperty(
            final Element borderProperties,
            final String attribute,
            final String value) {
        final Element leftEdgeWidthElement = XMLUtils.getPropertyElement(borderProperties, attribute).get();
        leftEdgeWidthElement.getAttributeNode("value").setValue(value);
    }

    private void handleVisibility(
            final FormObject formObject,
            final Document document) {
        final Element objectProperties;
        final boolean[] characteristic = formObject.getCharacteristics();
        if (characteristic[0] || characteristic[1] || characteristic[5]) {
            objectProperties = (Element) document.getElementsByTagName("object").item(0);
            setProperty(objectProperties, "Presence", "Invisible");
        }
    }

    private void appearanceImages(
            final FormObject form,
            final IWidget widget) {
        final CheckBoxWidget checkBoxWidget = (CheckBoxWidget) widget;

        Image offImage = null;
        if (form.hasNormalOff()) {
            offImage = (form.getNormalOffImage());
        }

        Image onImage = null;
        if (form.hasNormalOn()) {
            onImage = (form.getNormalOnImage());
        }

        checkBoxWidget.setOnOffImage(onImage, offImage);
    }
}
