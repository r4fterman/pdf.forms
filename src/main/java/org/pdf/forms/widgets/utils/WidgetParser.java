/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * WidgetParser.java
 * ---------------
 */
package org.pdf.forms.widgets.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;

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

    private WidgetParser() {
    }

    public static void parseWidgets(
            final AcroRenderer currentFormRenderer,
            final Page page,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final IMainFrame mainFrame,
            final List<IWidget> widgetsOnPage) {
        final Map<String, FormObject> rawFormData = currentFormRenderer.getRawFormData();

        for (final String widgetName : rawFormData.keySet()) {
            final FormObject formObject = rawFormData.get(widgetName);

            final int type = formObject.getType();
            switch (type) {

                case FormObject.FORMBUTTON: {
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
                        final IWidget widget = getBasicWidget(formObject, IWidget.BUTTON, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        final Document document = widget.getProperties();

                        final String text = formObject.getNormalCaption();

                        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
                        final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");
                        defaultTextElement.getAttributeNode("value").setValue(text);
                        widget.setObjectProperties(objectProperties);

                        handleBorder(formObject, widget);

                        handleVisibility(formObject, document);

                        widgetsOnPage.add(widget);

                        //} else if (isRadio) {
                        // radio button
                    } else {
                        //checkBox
                        addCheckBox(formObject, page, null, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);
                    }

                    break;

                }
                case FormObject.FORMTEXT: {

                    //System.out.println(formObject.getBoundingRectangle());

                    final IWidget widget = getBasicWidget(formObject, IWidget.TEXT_FIELD, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                    final Document document = widget.getProperties();

                    final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
                    final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                    final String text = formObject.getTextString();
                    defaultTextElement.getAttributeNode("value").setValue(text);

                    widget.setObjectProperties(objectProperties);

                    handleBorder(formObject, widget);

                    handleVisibility(formObject, document);

                    widgetsOnPage.add(widget);

                    break;
                }
                case FormObject.FORMCHOICE: {
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
                        final IWidget widget = getBasicWidget(formObject, IWidget.COMBO_BOX, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        handleChoiceField(formObject, widget);

                        widgetsOnPage.add(widget);
                    } else {
                        //it is a list
                        final IWidget widget = getBasicWidget(formObject, IWidget.LIST_BOX, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        handleChoiceField(formObject, widget);

                        widgetsOnPage.add(widget);
                    }

                    break;
                }
                default:
                    break;
            }
        }
    }

    private static int getNextArrayNumberForName(
            final String name,
            final IWidget widgetToTest,
            final WidgetArrays widgetArrays,
            final List<IWidget> widgetList) {
        if (widgetArrays.isWidgetArrayInList(name)) { // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        for (final IWidget widget : widgetList) {
            final String widgetName = widget.getWidgetName();
            if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
                /*
                 * another widget of this name already exists, and this is the first
                 * we've heard of it
                 */

                widgetArrays.addWidgetToArray(name, widget); // add the original widget
                widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget

                return widgetArrays.getNextArrayIndex(name);
            }
        }

        return 0;
    }

    private static void addCheckBox(
            final FormObject formObject,
            final Page page,
            final String groupName,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final IMainFrame mainFrame,
            final List<IWidget> widgetList) {
        if (formObject.getKidData() != null) { // this is a holder for more checkboxes
            final Map kidData = formObject.getKidData();
            final Iterator iter = kidData.keySet().iterator();

            final String name = formObject.getFieldName();

            while (iter.hasNext()) {
                final FormObject form = (FormObject) kidData.get(iter.next());

                addCheckBox(form, page, name, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetList);
            }
        } else {
            String group = groupName;
            if (groupName == null) {
                group = formObject.getFieldName();
            }

            final Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);

            final IWidget widget = WidgetFactory.createCheckBoxWidget(page, bounds, group);
            final Document document = widget.getProperties();

            setGenericWidgetProperties(formObject, bounds, widget, document, mainFrame, widgetList);

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
            final Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");

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

    private static IWidget getBasicWidget(
            final FormObject formObject,
            final int type,
            final int pageHeight,
            final int cropHeight,
            final int cropX,
            final int cropY,
            final IMainFrame mainFrame,
            final List<IWidget> widgetList) {

        final Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);

        final IWidget widget = WidgetFactory.createWidget(type, bounds);

        final Document document = widget.getProperties();

        setGenericWidgetProperties(formObject, bounds, widget, document, mainFrame, widgetList);

        return widget;
    }

    private static void setGenericWidgetProperties(
            final FormObject formObject,
            final Rectangle bounds,
            final IWidget widget,
            final Document document,
            final IMainFrame mainFrame,
            final List<IWidget> widgetList) {

        //System.out.println("bounds = " + bounds);

        widget.setX(bounds.x);
        widget.setY(bounds.y);

        final Set<IWidget> set = new HashSet<>();
        set.add(widget);

        PropertyChanger.updateSizeAndPosition(set, new Integer[] {
                bounds.x, bounds.y,
                bounds.width, bounds.height });

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
        final Element nameElement = XMLUtils.getPropertyElement(objectProperties, "Name");
        nameElement.getAttributeNode("value").setValue(widgetName);

        widget.setObjectProperties(objectProperties);

        final int arrayNumber = getNextArrayNumberForName(widgetName, widget, mainFrame.getWidgetArrays(), widgetList);
        final Element arrayNumberElement = XMLUtils.getPropertyElement(objectProperties, "Array Number");
        arrayNumberElement.getAttributeNode("value").setValue(arrayNumber + "");

        widget.setObjectProperties(objectProperties);
    }

    private static Rectangle getBounds(
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

    private static void handleChoiceField(
            final FormObject formObject,
            final IWidget widget) {
        final Document document = widget.getProperties();

        //populate items array with list from Opt
        final String[] items = formObject.getItemsList();
        final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);

        if (items != null) {
            final Element itemsElement = (Element) objectProperties.getElementsByTagName("items").item(0);

            for (final String value : items) {
                if (value != null && !value.equals("")) {
                    XMLUtils.addBasicProperty(document, "item", value, itemsElement);
                }
            }
        }

        final Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");

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

    private static void handleBorder(
            final FormObject formObject,
            final IWidget widget) {
        Map<String, String> border = (Map) formObject.getBorder();
        if (border == null) {
            border = new HashMap<>();
            border.put("S", "/S");
            border.put("W", "1");
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

    private static void setProperty(
            final Element borderProperties,
            final String attribute,
            final String value) {
        final Element leftEdgeWidthElement = XMLUtils.getPropertyElement(borderProperties, attribute);
        leftEdgeWidthElement.getAttributeNode("value").setValue(value);
    }

    private static void handleVisibility(
            final FormObject formObject,
            final Document document) {
        final Element objectProperties;
        final boolean[] characteristic = formObject.getCharacteristics();
        if (characteristic[0] || characteristic[1] || characteristic[5]) {
            objectProperties = (Element) document.getElementsByTagName("object").item(0);
            setProperty(objectProperties, "Presence", "Invisible");
        }
    }

    private static void appearanceImages(
            final FormObject form,
            final IWidget widget) {

        final CheckBoxWidget cbw = (CheckBoxWidget) widget;

        Image onImage = null, offImage = null;

        if (form.hasNormalOff()) {
            offImage = (form.getNormalOffImage());
        }
        if (form.hasNormalOn()) {
            onImage = (form.getNormalOnImage());
        }
        cbw.setOnOffImage(onImage, offImage);
    }
}
