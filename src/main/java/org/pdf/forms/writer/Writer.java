/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
*  This file is part of the PDF Forms Designer
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
* Writer.java
* ---------------
*/
package org.pdf.forms.writer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.pdf.forms.widgets.components.PdfButton;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.PdfComboBox;
import org.pdf.forms.widgets.components.PdfList;
import org.pdf.forms.widgets.components.PdfTextField;
import org.pdf.forms.writer.PdfDocumentLayout.Page;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.RadioCheckField;
import com.itextpdf.text.pdf.TextField;

public class Writer {

    private PdfStamper globalStamper;
    private PdfWriter globalWriter;

    private Set<String> fontSubstitutions;
    private final IMainFrame mainFrame;

    public Writer(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Set<String> getFontSubstitutions() {
        return fontSubstitutions;
    }

    public void write(
            final File fileToWriteTo,
            final List<IWidget>[] widgets,
            final org.w3c.dom.Document properties) {
        fontSubstitutions = new HashSet<>();

        final List<Element> pages = XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("page"));

        final Element rootElement = properties.getDocumentElement();
        final List<Element> elementsFromNodeList = XMLUtils.getElementsFromNodeList(rootElement.getElementsByTagName("javascript"));

        Map<PdfName, String> eventsAndScripts = null;
        if (!elementsFromNodeList.isEmpty()) {
            final Element javaScriptElement = elementsFromNodeList.get(0);
            eventsAndScripts = getEventAndScriptMap(javaScriptElement);
        }
        final String documentJavaScript = getDocumentJavaScript(eventsAndScripts);

        final PdfDocumentLayout pdfDocumentLayout = getPdfDocumentLayout(pages);

        final List pdfPages = pdfDocumentLayout.getPdfPages();

        if (pdfPages.isEmpty()) {
            // this is just a plain, hand made document
            Rectangle pageSize = getPageSize(pages, 1);

            final Document document = new Document(pageSize);

            try {
                final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileToWriteTo));
                document.open();

                globalWriter = writer;
                globalStamper = null;
                addWidgets(writer, widgets[0], pageSize, pageSize, 0);

                if (widgets[0].isEmpty()) {
                    writer.setPageEmpty(false);
                }

                for (int i = 1; i < pages.size(); i++) {
                    final int currentPage = i + 1;
                    pageSize = getPageSize(pages, currentPage);

                    document.setPageSize(pageSize);

                    document.newPage();

                    globalWriter = writer;
                    globalStamper = null;
                    addWidgets(writer, widgets[i], pageSize, pageSize, currentPage);

                    if (widgets[i].isEmpty()) {
                        writer.setPageEmpty(false);
                    }
                }

                if (!documentJavaScript.equals("")) {
                    writer.addJavaScript(documentJavaScript);
                }

            } catch (final Exception e) {
                e.printStackTrace();
            }

            document.close();

        } else {
            // we've got pages imported from other PDF's
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final PdfCopyFields pdfCopyFields = new PdfCopyFields(baos);

                for (int i = 0; i < pages.size(); i++) {
                    final int currentPage = i + 1;
                    final Page page = pdfDocumentLayout.getPage(currentPage);

                    if (page.isPdfPage()) {
                        final String fileName = page.getPdfPath();

                        final List<Integer> pageNumbers = new ArrayList<>();
                        pageNumbers.add(page.getPdfPageNumber());

                        pdfCopyFields.addDocument(new PdfReader(fileName), pageNumbers);
                    }
                }

                pdfCopyFields.close();

                final PdfReader reader = new PdfReader(baos.toByteArray());

                final AcroFields form = reader.getAcroFields();
                final Map fields = form.getFields();
                System.out.println(fields.keySet());

                final PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileToWriteTo));

                for (int i = 0; i < pages.size(); i++) {
                    final Element page = (Element) pages.get(i);

                    final int currentPage = i + 1;

                    if (isPdfPage(page)) { // this page has been imported
                        final AcroFields acroFields = stamper.getAcroFields();
                        acroFields.removeFieldsFromPage(currentPage);

                        globalWriter = null;
                        globalStamper = stamper;

                        //int pageHeight, int cropHeight, int cropX, int cropY
                        addWidgets(stamper.getWriter(), widgets[i], reader.getPageSizeWithRotation(currentPage), reader.getCropBox(currentPage), currentPage);
                    } else { // this is a brand new page
                        stamper.insertPage(currentPage, getPageSize(pages, currentPage));

                        globalWriter = null;
                        globalStamper = stamper;

                        addWidgets(stamper.getWriter(), widgets[i], getPageSize(pages, currentPage), reader.getCropBox(currentPage), currentPage);
                    }
                }

                stamper.addJavaScript(documentJavaScript);

                stamper.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getDocumentJavaScript(final Map<PdfName, String> eventsAndScripts) {
        final Collection<String> set = eventsAndScripts.values();
        String script = "";
        if (!set.isEmpty()) {
            script = set.iterator().next();
        }

        return script;
    }

    private boolean isPdfPage(final Element page) {
        final Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
        return fileLocationElement != null;
    }

    private String getPdfFileLocation(final Element page) {
        final Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
        if (fileLocationElement != null) {
            return fileLocationElement.getAttributeNode("value").getValue();
        }

        return null;
    }

    private int getPdfPageNumber(final Element page) {
        final Element pageNumber = XMLUtils.getPropertyElement(page, "pdfpagenumber");
        if (pageNumber != null) {
            return Integer.parseInt(pageNumber.getAttributeNode("value").getValue());
        }

        return -1;
    }

    private Rectangle getPageSize(
            final List pages,
            final int currentPage) {
        final Element page = (Element) pages.get(currentPage - 1);
        final Element pageDataElement = (Element) page.getElementsByTagName("pagedata").item(0);

        final int width = Integer.parseInt(
                XMLUtils.getAttributeFromChildElement(pageDataElement, "width"));
        final int height = Integer.parseInt(
                XMLUtils.getAttributeFromChildElement(pageDataElement, "height"));

        return new Rectangle(width, height);
    }

    private void handleButtonGroups(
            final PdfWriter writer,
            final List<IWidget> widgets,
            final Rectangle pageSize,
            final int currentPage) throws IOException, DocumentException {

        final Map<String, List<IWidget>> radioButtonGroups = new HashMap<>();
        final Map<String, List<IWidget>> checkBoxGroups = new HashMap<>();

        for (final IWidget widget : widgets) {
            if (widget.getType() != IWidget.RADIO_BUTTON && widget.getType() != IWidget.CHECK_BOX) {
                continue;
            }

            final int widgetType = widget.getType();

            final Map buttonGroup;
            final String groupName;
            if (widgetType == IWidget.RADIO_BUTTON) {
                final RadioButtonWidget rbw = (RadioButtonWidget) widget;
                groupName = rbw.getRadioButtonGroupName();
                buttonGroup = radioButtonGroups;
            } else {
                final CheckBoxWidget cbw = (CheckBoxWidget) widget;
                groupName = cbw.getCheckBoxGroupName();
                buttonGroup = checkBoxGroups;
            }

            List<IWidget> buttonsInGroup = (List) buttonGroup.get(groupName);
            if (buttonsInGroup == null) {
                buttonsInGroup = new ArrayList<>();
                buttonGroup.put(groupName, buttonsInGroup);
            }
            buttonsInGroup.add(widget);
        }

        radioButtonGroups.putAll(checkBoxGroups);

        for (final String groupName : radioButtonGroups.keySet()) {
            final List<IWidget> widgetsInGroup = radioButtonGroups.get(groupName);
            final IWidget testWidget = widgetsInGroup.get(0);

            final int type = testWidget.getType();

            final PdfFormField top;

            if (type == IWidget.RADIO_BUTTON) {
                top = PdfFormField.createRadioButton(writer, true);
            } else {
                top = PdfFormField.createCheckBox(writer);
            }

            top.setFieldName(groupName);
            top.setValueAsName(groupName);

            for (final IWidget widget : widgetsInGroup) {
                final Element rootElement = widget.getProperties().getDocumentElement();

                final List elementsFromNodeList = XMLUtils.getElementsFromNodeList(rootElement.getElementsByTagName("javascript"));

                Map<PdfName, String> eventsAndScripts = null;
                if (!elementsFromNodeList.isEmpty()) {
                    final Element javaScriptElement = (Element) elementsFromNodeList.get(0);

                    eventsAndScripts = getEventAndScriptMap(javaScriptElement);

                    writeOutCaption(widget, pageSize, currentPage);

                    final AbstractButton value = (AbstractButton) widget.getValueComponent();

                    final java.awt.Rectangle valueBounds;
                    if (type == IWidget.RADIO_BUTTON) {
                        valueBounds = new java.awt.Rectangle(13, 13);
                    } else {
                        java.awt.Rectangle iconBounds = new java.awt.Rectangle(13, 13);
                        final Icon icon = value.getIcon();
                        if (icon != null) {
                            final int iconWidth = icon.getIconWidth();
                            final int iconHeight = icon.getIconHeight();

                            iconBounds = new java.awt.Rectangle(iconWidth, iconHeight);
                        }
                        valueBounds = iconBounds;
                    }

                    final java.awt.Rectangle actualBounds = value.getBounds();
                    final Point actualLocation = widget.getAbsoluteLocationsOfValue();

                    actualLocation.y += (actualBounds.height / 2d) - (valueBounds.height / 2d);

                    valueBounds.setLocation(actualLocation);
                    final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    final RadioCheckField check = new RadioCheckField(writer, pdfValueBounds, null, getName(widget));
                    check.setChecked(value.isSelected());

                    addBorder(widget, check);

                    final PdfFormField field;
                    if (type == IWidget.RADIO_BUTTON) {
                        check.setCheckType(RadioCheckField.TYPE_CIRCLE);
                        field = check.getRadioField();
                    } else {
                        check.setCheckType(RadioCheckField.TYPE_CROSS);
                        field = check.getCheckField();
                    }

                    top.addKid(field);

                    addJavaScriptToFormField(eventsAndScripts, field, writer);
                }
            }

            if (globalWriter != null) {
                globalWriter.addAnnotation(top);
            } else {
                globalStamper.addAnnotation(top, currentPage);
            }
        }
    }

    private void addWidgets(
            final PdfWriter writer,
            final List<IWidget> widgets,
            final Rectangle pageSize,
            final Rectangle cropBox,
            final int currentPage) throws IOException, DocumentException {

        handleButtonGroups(writer, widgets, pageSize, currentPage);

        for (final IWidget widget : widgets) {
            final int type = widget.getWidgetType();

            final Element rootElement = widget.getProperties().getDocumentElement();

            //Font valueFontToUse = getFont(rootElement, "value");

            final List elementsFromNodeList = XMLUtils.getElementsFromNodeList(
                    rootElement.getElementsByTagName("javascript"));

            Map<PdfName, String> eventsAndScripts = null;
            if (!elementsFromNodeList.isEmpty()) {
                final Element javaScriptElement = (Element) elementsFromNodeList.get(0);

                eventsAndScripts = getEventAndScriptMap(javaScriptElement);
            }

            PdfFormField field = null;

            switch (type) {
                case IWidget.GROUP: {
                    addWidgets(writer, widget.getWidgetsInGroup(), pageSize, cropBox, currentPage);
                    break;
                }
                case IWidget.TEXT_FIELD: {

                    //PdfCaption textFieldCaption = widget.getCaptionComponent();
                    writeOutCaption(widget, pageSize, currentPage);

                    final PdfTextField value = (PdfTextField) widget.getValueComponent();
                    final String valueText = value.getText();

                    final java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());
                    final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    // write out textbox
                    //BaseFont bf = valueFontToUse.getCalculatedBaseFont(false);

                    final Font font = value.getFont();

                    final BaseFont baseFont = getBaseFont(font);

                    final TextField tf = new TextField(writer, pdfValueBounds, widget.getWidgetName());
                    // tf.setBorderStyle(PdfBorderDictionary.STYLE_INSET);

                    tf.setText(valueText);
                    tf.setFont(baseFont);
                    tf.setFontSize(value.getFont().getSize());
                    tf.setTextColor(getBaseColor(widget.getValueComponent().getForeground()));
                    tf.setBackgroundColor(getBaseColor(Color.white));
                    addBorder(widget, tf);

                    field = tf.getTextField();

                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }
                case IWidget.TEXT: {
                    writeOutCaption(widget, pageSize, currentPage);
                    break;
                }
                case IWidget.IMAGE: {
                    addImage(widget, pageSize, currentPage);
                    break;
                }
                case IWidget.RADIO_BUTTON: {
                    /* these are handled separately at the beginning */
                    break;
                }
                case IWidget.CHECK_BOX: {
                    //                    writeOutCaption(widget, pageSize, currentPage);
                    //
                    //                    PdfCheckBox value = (PdfCheckBox) widget.getValueComponent();
                    //
                    //                    // todo make it possible to change the size and style of the check box
                    //                    // http://forum.java.sun.com/thread.jspa?forumID=57&threadID=641479
                    //                    java.awt.Rectangle valueBounds = new java.awt.Rectangle(13, 13);
                    //
                    //                    java.awt.Rectangle actualBounds = value.getBounds();
                    //                    Point actualLocation = widget.getAbsoluteLocationsOfValue();
                    //
                    //                    actualLocation.y += (actualBounds.height / 2d) - (valueBounds.height / 2d);
                    //
                    //                    valueBounds.setLocation(actualLocation);
                    //                    Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);
                    //
                    //                    RadioCheckField check = new RadioCheckField(writer, pdfValueBounds, widget.getName(), "On");
                    //                    check.setCheckType(RadioCheckField.TYPE_CROSS);
                    //                    check.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
                    //                    check.setChecked(value.isSelected());
                    //
                    //                    addBorder(widget, check);
                    //
                    //                    field = check.getCheckField();
                    //
                    //                    addJavaScriptToFormField(eventsAndScripts, field, writer);
                    //
                    break;
                }
                case IWidget.COMBO_BOX: {
                    //PdfCaption comboBoxCaption = widget.getCaptionComponent();
                    writeOutCaption(widget, pageSize, currentPage);

                    final PdfComboBox value = (PdfComboBox) widget.getValueComponent();
                    final java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    final Element itemsElement = (Element) rootElement.getElementsByTagName("items").item(0);

                    final List list = XMLUtils.getElementsFromNodeList(itemsElement.getChildNodes());

                    final String[] items = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        final Element item = (Element) list.get(i);
                        items[i] = XMLUtils.getAttributeFromElement(item, "item");
                    }

                    final Font font = value.getFont();

                    final BaseFont baseFont = getBaseFont(font);

                    final TextField combo = new TextField(writer, pdfValueBounds, widget.getWidgetName());
                    combo.setFont(baseFont);
                    combo.setFontSize(value.getFont().getSize());
                    combo.setTextColor(getBaseColor(widget.getValueComponent().getForeground()));
                    combo.setChoices(items);

                    final Element editableElement = XMLUtils.getPropertyElement(rootElement, "Allow Custom Text Entry");
                    final boolean editable = Boolean.valueOf(editableElement.getAttributes().getNamedItem("value").getNodeValue());
                    if (editable) {
                        combo.setOptions(BaseField.EDIT);
                    }

                    final Element defaultElement = XMLUtils.getPropertyElement(rootElement, "Default");
                    String defaultText = defaultElement.getAttributes().getNamedItem("value").getNodeValue();
                    if (defaultText.equals("< None >")) {
                        defaultText = "";
                    }

                    addBorder(widget, combo);

                    field = combo.getComboField();
                    field.setValueAsString(defaultText);

                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }
                case IWidget.LIST_BOX: {
                    //PdfCaption listBoxCaption = widget.getCaptionComponent();
                    writeOutCaption(widget, pageSize, currentPage);

                    final JList value = ((PdfList) widget.getValueComponent()).getList();
                    final java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    final ListModel listModel = value.getModel();
                    final int noOfItems = listModel.getSize();
                    final String[] items = new String[noOfItems];
                    for (int i = 0; i < noOfItems; i++) {
                        items[i] = (String) value.getModel().getElementAt(i);
                    }

                    final Font font = value.getFont();

                    final BaseFont baseFont = getBaseFont(font);

                    final TextField list = new TextField(writer, pdfValueBounds, widget.getWidgetName());
                    list.setFont(baseFont);
                    list.setFontSize(value.getFont().getSize());
                    list.setChoices(items);

                    final Element defaultElement = XMLUtils.getPropertyElement(rootElement, "Default");
                    final String defaultText = defaultElement.getAttributes().getNamedItem("value").getNodeValue();

                    int index = 0;
                    if (!defaultText.equals("")) {
                        for (int i = 0; i < noOfItems; i++) {
                            final String item = (String) value.getModel().getElementAt(i);
                            if (item.equals(defaultText)) {
                                index = i;
                                break;
                            }
                        }
                    }

                    list.setChoiceSelection(index);

                    addBorder(widget, list);

                    field = list.getListField();

                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }

                case IWidget.BUTTON: {

                    final PdfButton value = (PdfButton) widget.getValueComponent();

                    final java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    final Font font = value.getFont();

                    final BaseFont baseFont = getBaseFont(font);

                    final PushbuttonField pushbutton = new PushbuttonField(writer, pdfValueBounds, widget.getWidgetName());
                    pushbutton.setFont(baseFont);
                    pushbutton.setFontSize(font.getSize());
                    pushbutton.setText(value.getText());
                    pushbutton.setTextColor(getBaseColor(value.getForeground()));
                    pushbutton.setBackgroundColor(getBaseColor(value.getBackground()));

                    final org.w3c.dom.Document document = widget.getProperties();
                    final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);
                    final List borderElement = XMLUtils.getElementsFromNodeList(
                            borderProperties.getElementsByTagName("borders").item(0).getChildNodes());

                    addBorder(widget, pushbutton);

                    /* potential code for creating custom dash border */
                    //pushbutton.getField().setBorderStyle(new PdfBorderDictionary(Float.parseFloat(width),PdfBorderDictionary.STYLE_BEVELED,new PdfDashPattern(2)));

                    field = pushbutton.getField();
                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }
                default:
                    break;
            }

            if (field == null) {
                continue;
            }

            if (globalWriter != null) {
                globalWriter.addAnnotation(field);
            } else {
                globalStamper.addAnnotation(field, currentPage);
            }
        }
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }

    private String getName(final IWidget widget) {
        String widgetName = widget.getWidgetName();
        if (mainFrame.getWidgetArrays().isWidgetArrayInList(widgetName)) {
            final int arrayNumber = widget.getArrayNumber();
            widgetName += "[" + arrayNumber + "]";
        }

        return widgetName;
    }

    private void addBorder(
            final IWidget widget,
            final BaseField tf) {
        final org.w3c.dom.Document document = widget.getProperties();
        final Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        final Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);

        final String style = XMLUtils.getAttributeFromChildElement(border, "Border Style");
        final String width = XMLUtils.getAttributeFromChildElement(border, "Border Width");
        final String color = XMLUtils.getAttributeFromChildElement(border, "Border Color");

        switch (style) {
            case "Solid":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
                break;
            case "Dashed":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
                break;
            case "Beveled":
                tf.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
                break;
            case "None":
                return;
            default:
                return;
        }

        tf.setBorderColor(new GrayColor(Integer.parseInt(color)));
        tf.setBorderWidth(Integer.parseInt(width));
    }

    private BaseFont getBaseFont(final java.awt.Font font) throws IOException, DocumentException {
        final String fontPath = FontHandler.getInstance().getAbsoluteFontPath(font);
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont(fontPath, "Cp1250", BaseFont.EMBEDDED);
        } catch (final DocumentException e) {

            /*
             * A document exception has been thrown meaning that the font cannot be embedded
             * due to licensing restrictions so substitute with Helvetica
             */

            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

            fontSubstitutions.add(font.getFontName());
        }
        return baseFont;
    }

    private void addJavaScriptToFormField(
            final Map<PdfName, String> eventsAndScripts,
            final PdfFormField formField,
            final PdfWriter writer) {
        for (final PdfName pdfName : eventsAndScripts.keySet()) {
            final String script = eventsAndScripts.get(pdfName);

            if (!script.equals("")) {
                formField.setAdditionalActions(pdfName, PdfAction.javaScript(script, writer));
            }
        }
    }

    private PdfDocumentLayout getPdfDocumentLayout(final List pages) {
        final PdfDocumentLayout pdfDocumentLayout = new PdfDocumentLayout();

        for (final Object page1 : pages) {
            final Element page = (Element) page1;

            final Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
            if (fileLocationElement == null) { // is a hand made page
                pdfDocumentLayout.addPage(false);
            } else { // its an imported page
                final String fileLocation = fileLocationElement.getAttributeNode("value").getValue();

                final Element pdfPageNumberElement = XMLUtils.getPropertyElement(page, "pdfpagenumber");
                final String pdfPageNumber = pdfPageNumberElement.getAttributeNode("value").getValue();

                pdfDocumentLayout.addPage(true, fileLocation, Integer.parseInt(pdfPageNumber));
            }
        }

        return pdfDocumentLayout;
    }

    private Map<PdfName, String> getEventAndScriptMap(final Element javaScriptElement) {

        final Map<PdfName, String> actionAndScriptMap = new HashMap<>();

        final List javaScriptProperties = XMLUtils.getElementsFromNodeList(javaScriptElement.getChildNodes());

        for (final Object property : javaScriptProperties) {
            final Element element = (Element) property;

            final String event = element.getNodeName();

            PdfName eventToUse = null;
            switch (event) {
                case "mouseEnter":
                    eventToUse = PdfName.E;
                    break;
                case "mouseExit":
                    eventToUse = PdfName.X;
                    break;
                case "change":
                    eventToUse = PdfName.F;
                    break;
                case "mouseUp":
                    eventToUse = PdfName.U;
                    break;
                case "mouseDown":
                    eventToUse = PdfName.D;
                    break;
                case "keystroke":
                    eventToUse = PdfName.K;
                    break;
                default:
                    break;
            }

            final NodeList childNodes = element.getChildNodes();
            if (childNodes.getLength() != 0) {
                final Text textNode = (Text) childNodes.item(0);
                final String nodeValue = textNode.getNodeValue();
                actionAndScriptMap.put(eventToUse, nodeValue);
            }
        }

        return actionAndScriptMap;
    }

    private void writeOutCaption(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage) {

        final PdfCaption caption = widget.getCaptionComponent();
        if (caption == null) {
            return;
        }

        if (widget.isComponentSplit()) {
            final Element captionElement = XMLUtils.getElementsFromNodeList(
                    widget.getProperties().getElementsByTagName("layout")).get(0);

            final String location = XMLUtils.getPropertyElement(captionElement, "Position").getAttributeNode("value").getValue();
            if (location.equals("None")) {
                return;
            }
        }

        final java.awt.Rectangle captionBounds = caption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        final Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        final PdfContentByte cb;
        if (globalStamper == null) {
            cb = globalWriter.getDirectContent();
        } else {
            cb = globalStamper.getOverContent(currentPage);
        }

        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfCaptionBounds.getLeft(), pdfCaptionBounds.getTop() - captionBounds.height);

        final java.awt.Font font = caption.getFont();
        final String fontDirectory = FontHandler.getInstance().getFontDirectory(font);

        DefaultFontMapper mapper = new DefaultFontMapper();

        mapper.insertDirectory(fontDirectory);

        /*
         * we need to make this erroneous call to awtToPdf to see if an exception is thrown, if it is, it is
         * probably because the font cannot be embedded due to licensing restrictions, so substitute with Helvetica
         */
        try {
            mapper.awtToPdf(font);
        } catch (final Exception e) {
            mapper = new DefaultFontMapper();
            fontSubstitutions.add(font.getFontName());
        }

        final Graphics2D g2 = cb.createGraphics(captionBounds.width, captionBounds.height, mapper, true, .95f);

        //Graphics2D g2 = cb.createGraphicsShapes(captionBounds.width, captionBounds.height, true, 0.95f);

        caption.paint(g2);

        g2.dispose();
        cb.restoreState();
    }

    private void addImage(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage) {

        final JLabel image = (JLabel) widget.getValueComponent();
        if (image == null) {
            return;
        }

        final java.awt.Rectangle imageBounds = image.getBounds();
        imageBounds.setLocation(widget.getX(), widget.getY());
        final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(imageBounds, pageSize);

        // write out caption
        final PdfContentByte cb;
        if (globalStamper == null) {
            cb = globalWriter.getDirectContent();
        } else {
            cb = globalStamper.getOverContent(currentPage);
        }

        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfValueBounds.getLeft(), pdfValueBounds.getTop() - imageBounds.height);

        final Graphics2D g2 = cb.createGraphicsShapes(imageBounds.width, imageBounds.height);

        image.paint(g2);

        g2.dispose();
        cb.restoreState();
    }

    private Rectangle convertJavaCoordsToPdfCoords(
            final java.awt.Rectangle bounds,
            final Rectangle pageSize) {
        final float javaX1 = bounds.x - IMainFrame.INSET;
        final float javaY1 = bounds.y - IMainFrame.INSET;

        final float javaX2 = javaX1 + bounds.width;

        final float pdfY1 = pageSize.getHeight() - javaY1 - bounds.height;

        final float pdfY2 = pdfY1 + bounds.height;

        return new Rectangle(javaX1, pdfY1, javaX2, pdfY2);
    }
}
