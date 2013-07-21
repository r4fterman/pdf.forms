/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * WidgetParser.java
 * ---------------
 */
package org.pdf.forms.widgets.utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

//                                try {
//                                    StringWriter sw = new StringWriter();
//                                    InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
//                                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                                    Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
//                                    transformer.transform(new DOMSource(borderProperties), new StreamResult(sw));
//                                    System.out.println(sw.toString());
//                                } catch (TransformerException e) {
//                                    e.printStackTrace();
//                                }

public class WidgetParser {
    private WidgetParser() {
    }

    public static void parseWidgets(AcroRenderer currentFormRenderer, Page page, int pageHeight, int cropHeight, int cropX, int cropY, IMainFrame mainFrame, List widgetsOnPage) {
        Map rawFormData = currentFormRenderer.getRawFormData();

        //List widgetList = new ArrayList();

        for (Iterator it = rawFormData.keySet().iterator(); it.hasNext(); ) {
            String widgetName = (String) it.next();

            FormObject formObject = (FormObject) rawFormData.get(widgetName);

            int type = formObject.getType();
            switch (type) {

                case FormObject.FORMBUTTON: {
                    boolean[] flags = formObject.getFieldFlags();

                    boolean isPushButton = false, isRadio = false, hasNoToggleToOff = false, radioinUnison = false;
                    if (flags != null) {
                        isPushButton = flags[FormObject.PUSHBUTTON];
                        isRadio = flags[FormObject.RADIO];
                        hasNoToggleToOff = flags[FormObject.NOTOGGLETOOFF];
                        radioinUnison = flags[FormObject.RADIOINUNISON];
                    }

                    if (isPushButton) {
                        IWidget widget = getBasicWidget(formObject, IWidget.BUTTON, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        Document document = widget.getProperties();

                        String text = formObject.getNormalCaption();

                        Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
                        Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");
                        defaultTextElement.getAttributeNode("value").setValue(text);
                        widget.setObjectProperties(objectProperties);

                        handleBorder(formObject, widget);

                        handleVisibility(formObject, document);

                        widgetsOnPage.add(widget);

                    } else if (isRadio) {
//                    	System.out.println(">>>>>>>>>>>> RADIO");
                    } else {//checkBox
                        addCheckBox(formObject, page, null, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);
                    }

                    break;

                }
                case FormObject.FORMTEXT: {

//                	System.out.println(formObject.getBoundingRectangle());

                    IWidget widget = getBasicWidget(formObject, IWidget.TEXT_FIELD, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                    Document document = widget.getProperties();

                    Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
                    Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");

                    String text = formObject.getTextString();
                    defaultTextElement.getAttributeNode("value").setValue(text);

                    widget.setObjectProperties(objectProperties);

                    handleBorder(formObject, widget);

                    handleVisibility(formObject, document);

                    widgetsOnPage.add(widget);

                    break;
                }
                case FormObject.FORMCHOICE: {
                    boolean[] flags = formObject.getFieldFlags();

                    boolean isCombo = false, multiSelect = false, sort = false, isEditable = false, doNotSpellCheck = false, comminOnSelChange = false;
                    if (flags != null) {
                        isCombo = flags[FormObject.COMBO];
                        multiSelect = flags[FormObject.MULTISELECT];
                        sort = flags[FormObject.SORT];
                        isEditable = flags[FormObject.EDIT];
                        doNotSpellCheck = flags[FormObject.DONOTSPELLCHECK];
                        comminOnSelChange = flags[FormObject.COMMITONSELCHANGE];
                    }

                    if (isCombo) {
                        IWidget widget = getBasicWidget(formObject, IWidget.COMBO_BOX, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        handleChoiceField(formObject, widget);

                        widgetsOnPage.add(widget);
                    } else {//it is a list
                        IWidget widget = getBasicWidget(formObject, IWidget.LIST_BOX, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

                        handleChoiceField(formObject, widget);

                        widgetsOnPage.add(widget);
                    }

                    break;
                }
            }
        }

//        return widgetList;
    }

    private static int getNextArrayNumberForName(String name, IWidget widgetToTest, WidgetArrays widgetArrays, List widgetList) {
        if (widgetArrays.isWidgetArrayInList(name)) { // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        for (Iterator it = widgetList.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            String widgetName = widget.getWidgetName();
            if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
                /**
                 * another widget of this name already exists, and this is the first
                 * we've heard of it
                 */

                widgetArrays.addWidgetToArray(name, widget); // add the original widget
                widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget

                return widgetArrays.getNextArrayIndex(name);
            }
        }

//        List pages = formsDocument.getPages();
//		for (Iterator it = pages.iterator(); it.hasNext();) {
//			Page page = (Page) it.next();
//			List widgetsOnPage = page.getWidgets();
//
//			for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext();) {
//				IWidget widget = (IWidget) iter.next();
//				String widgetName = widget.getWidgetName();
//				if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
//					/**
//					 * another widget of this name already exists, and this is the first
//					 * we've heard of it
//					 */
//
//					widgetArrays.addWidgetToArray(name, widget); // add the original widget
//					widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
//
//					return widgetArrays.getNextArrayIndex(name);
//				}
//			}
//		}

        return 0;
    }

    private static void addCheckBox(FormObject formObject, Page page, String groupName, int pageHeight, int cropHeight, int cropX, int cropY, IMainFrame mainFrame, List widgetList) {
        if (formObject.getKidData() != null) { // this is a holder for more checkboxs
            Map kidData = formObject.getKidData();
            Iterator iter = kidData.keySet().iterator();

            String name = formObject.getFieldName();

            while (iter.hasNext()) {
                FormObject form = (FormObject) kidData.get(iter.next());

                addCheckBox(form, page, name, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetList);
            }
        } else {
            if (groupName == null)
                groupName = formObject.getFieldName();

            Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX, cropY);

            IWidget widget = WidgetFactory.createCheckBoxWidget(page, bounds, groupName);
            Document document = widget.getProperties();

            setGenericWidgetProperties(formObject, bounds, widget, document, mainFrame, widgetList);

            AbstractButton checkBox = (AbstractButton) widget.getValueComponent();

            checkBox.setSize(bounds.getSize());
            //System.out.println("bounds.getSize() = "+bounds.getSize());
            appearanceImages(formObject, checkBox, false, widget);

            //get default state
            boolean selected = false;
            String defaultState = formObject.getDefaultState();
            if (defaultState != null && defaultState.equals(formObject.getNormalOnState())) {
                selected = true;
            }

            // set the default state
            Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
            Element defaultElement = XMLUtils.getPropertyElement(objectProperties, "Default");
            defaultElement.getAttributeNode("value").setValue(selected ? "On" : "Off");
            widget.setObjectProperties(objectProperties);

            handleBorder(formObject, widget);

            handleVisibility(formObject, document);

            widgetList.add(widget);
        }
    }

    private static IWidget getBasicWidget(FormObject formObject, int type, int pageHeight, int cropHeight, int cropX, int cropY, IMainFrame mainFrame, List widgetList) {

        Rectangle bounds = getBounds(formObject, pageHeight, cropHeight, cropX,
                cropY);

        IWidget widget = WidgetFactory.createWidget(type, bounds);

        Document document = widget.getProperties();

        setGenericWidgetProperties(formObject, bounds, widget, document, mainFrame, widgetList);

        return widget;
    }

    private static void setGenericWidgetProperties(FormObject formObject, Rectangle bounds,
                                                   IWidget widget, Document document, IMainFrame mainFrame, List widgetList) {

        //System.out.println("bounds = " + bounds);

        widget.setX(bounds.x);
        widget.setY(bounds.y);

        Set set = new HashSet();
        set.add(widget);

        PropertyChanger.updateSizeAndPosition(set, new Integer[]{
                new Integer(bounds.x), new Integer(bounds.y),
                new Integer(bounds.width), new Integer(bounds.height)});

        /**
         * if the component is split, then because we are parsing it in from a
         * PDF file we know it has no caption, therefore set the caption
         * position to "None"
         */
        if (widget.isComponentSplit()) {
            Element layoutProperties = (Element) document.getElementsByTagName("layout").item(0);
            setProperty(layoutProperties, "Position", "None");

            widget.setLayoutProperties(layoutProperties);
        }

        /** set the widgets name */
        String widgetName = formObject.getFieldName();

        Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
        Element nameElement = XMLUtils.getPropertyElement(objectProperties, "Name");
        nameElement.getAttributeNode("value").setValue(widgetName);

        widget.setObjectProperties(objectProperties);

        int arrayNumber = getNextArrayNumberForName(widgetName, widget, mainFrame.getWidgetArrays(), widgetList);
        Element arrayNumberElement = XMLUtils.getPropertyElement(objectProperties, "Array Number");
        arrayNumberElement.getAttributeNode("value").setValue(arrayNumber + "");

        widget.setObjectProperties(objectProperties);

//        System.out.println(widget.getWidgetName()+" "+widget.getArrayNumber());
    }

    private static Rectangle getBounds(FormObject formObject, int pageHeight,
                                       int cropHeight, int cropX, int cropY) {

        /** get a clone of the bounding rectangle so not to affect the original */
        Rectangle bounds = (Rectangle) formObject.getBoundingRectangle().getBounds().clone();

        /** move the widget up the page to convert PDF y-cord to Java y-cord */
        bounds.y = pageHeight - (bounds.y + bounds.height);

        /** handle pages with a crop */
        bounds.y -= pageHeight - (cropY + cropHeight);
        bounds.x -= cropX;

        /** add on the page insets*/
        bounds.y += IMainFrame.INSET;
        bounds.x += IMainFrame.INSET;

        return bounds;
    }

    private static void handleChoiceField(FormObject formObject, IWidget widget) {
        Document document = widget.getProperties();

        //populate items array with list from Opt
        String[] items = formObject.getItemsList();
        Element objectProperties = (Element) document.getElementsByTagName("object").item(0);

        if (items != null) {
            Element itemsElement = (Element) objectProperties.getElementsByTagName("items").item(0);

            for (int i = 0; i < items.length; i++) {
                String value = items[i];

                if (value != null && !value.equals(""))
                    XMLUtils.addBasicProperty(document, "item", value, itemsElement);
            }
        }

        Element defaultTextElement = XMLUtils.getPropertyElement(objectProperties, "Default");

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

    private static void handleBorder(FormObject formObject, IWidget widget) {
        Map border = (Map) formObject.getBorder();
        if (border == null) {
            border = new HashMap();
            border.put("S", "/S");
            border.put("W", "1");
        }
        Document document = widget.getProperties();

        Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        Color borderColor = formObject.getBorderColor();
        Color backgroundColor = formObject.getBackgroundColor();

        if (borderColor == null) {
            setProperty(borderProperties, "Border Style", "None");
            return;
        }

        String width = (String) border.get("W");
        if (width != null) {
            setProperty(borderProperties, "Border Width", width);
        }

        String style = (String) border.get("S");
        if (style != null) {
            if (style.equals("/B"))
                style = "Beveled";

            setProperty(borderProperties, "Border Style", style);
        }

        if (borderColor != null) {
            String rgb = borderColor.getRGB() + "";
            setProperty(borderProperties, "Border Color", rgb);
        }

        if (backgroundColor != null) {
            setProperty(borderProperties, "Fill Color", backgroundColor.getRGB() + "");
        }

        widget.setBorderAndBackgroundProperties(borderProperties);
    }

    private static void setProperty(Element borderProperties, String attribute, String value) {
        Element leftEdgeWidthElement = XMLUtils.getPropertyElement(borderProperties, attribute);
        leftEdgeWidthElement.getAttributeNode("value").setValue(value);
    }

    private static void handleVisibility(FormObject formObject, Document document) {
        Element objectProperties;
        boolean[] characteristic = formObject.getCharacteristics();
        if (characteristic[0] || characteristic[1] || characteristic[5]) {
            objectProperties = (Element) document.getElementsByTagName("object").item(0);
            setProperty(objectProperties, "Presence", "Invisible");
        }
    }

    private static void appearanceImages(final FormObject form, AbstractButton comp, boolean showImages, IWidget widget) {

        CheckBoxWidget cbw = (CheckBoxWidget) widget;

        Image onImage = null, offImage = null;

        if (form.hasNormalOff()) {

            offImage = (form.getNormalOffImage());

//            comp.setText(null);
//            comp.setIcon(new FixImageIcon(form.getNormalOffImage()));
//            if (showImages)
//                ShowGUIMessage.showGUIMessage("normalAppOffImage", form.getNormalOffImage(), "normalAppOff");
        }
        if (form.hasNormalOn()) {
            onImage = (form.getNormalOnImage());
//            comp.setText(null);
//            comp.setSelectedIcon(new FixImageIcon(form.getNormalOnImage()));
//            BufferedImage normalOnImage = form.getNormalOnImage();
//            if (showImages) {
//				ShowGUIMessage.showGUIMessage("normalAppOnImage", normalOnImage, "normalAppOn");
//			}

//            comp.setSize(normalOnImage.getWidth(), normalOnImage.getHeight());
        }
        cbw.setOnOffImage(onImage, offImage);
    }
}
