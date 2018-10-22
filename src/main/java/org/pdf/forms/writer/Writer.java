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
* Writer.java
* ---------------
*/
package org.pdf.forms.writer;

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

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Color;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Writer {

    private PdfStamper globalStamper;
    private PdfWriter globalWriter;

    private Set fontSubstitutions;
    private IMainFrame mainFrame;

    public Writer(IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Set getFontSubstitutions() {
        return fontSubstitutions;
    }

    public void write(File fileToWriteTo, List[] widgets, org.w3c.dom.Document properties) {
        fontSubstitutions = new HashSet();

        List pages = XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("page"));

        Element rootElement = properties.getDocumentElement();
        List elementsFromNodeList = XMLUtils.getElementsFromNodeList(
                rootElement.getElementsByTagName("javascript"));

        Map eventsAndScripts = null;
        if (!elementsFromNodeList.isEmpty()) {
            Element javaScriptElement = (Element) elementsFromNodeList.get(0);

            eventsAndScripts = getEventAndScriptMap(javaScriptElement);
        }
        String documentJavaScript = getDocumentJavaScript(eventsAndScripts);

//        Set pdfFiles = getPdfFiles(pages);

        PdfDocumentLayout pdfDocumentLayout = getPdfDocumentLayout(pages);

        List pdfPages = pdfDocumentLayout.getPdfPages();
        
        if (pdfPages.isEmpty()) { // this is just a plain, hand made document
            Rectangle pageSize = getPageSize(pages, 1);

            Document document = new Document(pageSize);

            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileToWriteTo));
                document.open();

                globalWriter = writer;
                globalStamper = null;
                addWidgets(writer, widgets[0], pageSize, pageSize, 0);

                if (widgets[0].isEmpty())
                    writer.setPageEmpty(false);

                for (int i = 1; i < pages.size(); i++) {
                    int currentPage = i + 1;
                    pageSize = getPageSize(pages, currentPage);

                    document.setPageSize(pageSize);

                    document.newPage();

                    globalWriter = writer;
                    globalStamper = null;
                    addWidgets(writer, widgets[i], pageSize, pageSize, currentPage);

                    if (widgets[i].isEmpty())
                        writer.setPageEmpty(false);
                }

                if (!documentJavaScript.equals(""))
                    writer.addJavaScript(documentJavaScript);

            } catch (Exception e) {
                e.printStackTrace();
            }

            document.close();

        } else { // we've got pages imported from other PDF's

            try {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfCopyFields pdfCopyFields = new PdfCopyFields(baos);

                for (int i = 0; i < pages.size(); i++) {
                	int currentPage = i + 1;
                	Page page = pdfDocumentLayout.getPage(currentPage);
                	
                	if(page.isPdfPage()){
                		String fileName = page.getPdfPath();

                        List arrayList = new ArrayList();
                		arrayList.add(new Integer(page.getPdfPageNumber()));
                		
						pdfCopyFields.addDocument(new PdfReader(fileName), arrayList);
                	}
                }

                pdfCopyFields.close();

                PdfReader reader = new PdfReader(baos.toByteArray());

                AcroFields form = reader.getAcroFields();
                Map fields = form.getFields();
                System.out.println(fields.keySet());
                
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileToWriteTo));

                for (int i = 0; i < pages.size(); i++) {
                    Element page = (Element) pages.get(i);

                    int currentPage = i + 1;

                    if (isPdfPage(page)) { // this page has been imported
                        AcroFields acroFields = stamper.getAcroFields();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	private String getDocumentJavaScript(Map eventsAndScripts) {
        Collection set = eventsAndScripts.values();
        String script = "";
        if (!set.isEmpty()) {
            script = (String) set.iterator().next();
        }

        return script;
    }

    private boolean isPdfPage(Element page) {
        Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
        return fileLocationElement != null;
    }

    private String getPdfFileLocation(Element page) {
        Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
        if (fileLocationElement != null) {
            return fileLocationElement.getAttributeNode("value").getValue();
        }

        return null;
    }

    private int getPdfPageNumber(Element page) {
        Element pageNumber = XMLUtils.getPropertyElement(page, "pdfpagenumber");
        if (pageNumber != null) {
            return Integer.parseInt(pageNumber.getAttributeNode("value").getValue());
        }

        return -1;
    }

    private Rectangle getPageSize(List pages, int currentPage) {
        Element page = (Element) pages.get(currentPage - 1);
        Element pageDataElement = (Element) page.getElementsByTagName("pagedata").item(0);
        
        int width = Integer.parseInt(
        		XMLUtils.getAttributeFromChildElement(pageDataElement, "width"));
        int height = Integer.parseInt(
        		XMLUtils.getAttributeFromChildElement(pageDataElement, "height"));

        return new Rectangle(width, height);
    }

    private void handleButtonGroups(PdfWriter writer, List widgets, Rectangle pageSize, int currentPage) throws IOException, DocumentException {

    	Map radioButtonGroups = new HashMap();
    	Map checkBoxGroups = new HashMap();

    	for (Iterator it = widgets.iterator(); it.hasNext();) {
    		IWidget widget = (IWidget) it.next();

            if(widget.getType() != IWidget.RADIO_BUTTON && widget.getType() != IWidget.CHECK_BOX)
                continue;

            int widgetType = widget.getType();

    		Map buttonGroup;
    		String groupName;
    		if (widgetType == IWidget.RADIO_BUTTON) {
    			RadioButtonWidget rbw = (RadioButtonWidget) widget;
    			groupName = rbw.getRadioButtonGroupName();
    			buttonGroup = radioButtonGroups;
    		} else {
    			CheckBoxWidget cbw = (CheckBoxWidget) widget;
    			groupName = cbw.getCheckBoxGroupName();
    			buttonGroup = checkBoxGroups;
    		}

    		List buttonsInGroup = (List) buttonGroup.get(groupName);
    		if (buttonsInGroup == null) {
    			buttonsInGroup = new ArrayList();
    			buttonGroup.put(groupName, buttonsInGroup);
    		}
    		buttonsInGroup.add(widget);
    	}

        radioButtonGroups.putAll(checkBoxGroups);

    	for (Iterator iter = radioButtonGroups.keySet().iterator(); iter.hasNext();) {
    		String groupName = (String) iter.next();
    		List widgetsInGroup = (List) radioButtonGroups.get(groupName);

    		IWidget testWidget = (IWidget) widgetsInGroup.get(0);

    		int type = testWidget.getType();

            PdfFormField top;

            if (type == IWidget.RADIO_BUTTON) {
                top = PdfFormField.createRadioButton(writer, true);
            } else {
                top = PdfFormField.createCheckBox(writer);
            }

            top.setFieldName(groupName);
            top.setValueAsName(groupName);

            for (Iterator iterator = widgetsInGroup.iterator(); iterator.hasNext();) {
    			IWidget widget = (IWidget) iterator.next();

    			Element rootElement = widget.getProperties().getDocumentElement();

    			List elementsFromNodeList = XMLUtils.getElementsFromNodeList(rootElement.getElementsByTagName("javascript"));

    			Map eventsAndScripts = null;
    			if (!elementsFromNodeList.isEmpty()) {
    				Element javaScriptElement = (Element) elementsFromNodeList.get(0);

    				eventsAndScripts = getEventAndScriptMap(javaScriptElement);

    				writeOutCaption(widget, pageSize, currentPage);

    				AbstractButton value = (AbstractButton) widget.getValueComponent();

    				java.awt.Rectangle valueBounds;
    				if (type == IWidget.RADIO_BUTTON) {
    					valueBounds = new java.awt.Rectangle(13, 13);
    				} else {
    					java.awt.Rectangle iconBounds = new java.awt.Rectangle(13, 13);
    					Icon icon = value.getIcon();
    					if (icon != null) {
    						int iconWidth = icon.getIconWidth();
    						int iconHeight = icon.getIconHeight();

    						iconBounds = new java.awt.Rectangle(iconWidth, iconHeight);
    					}
    					valueBounds = iconBounds;
    				}

    				java.awt.Rectangle actualBounds = value.getBounds();
    				Point actualLocation = widget.getAbsoluteLocationsOfValue();

    				actualLocation.y += (actualBounds.height / 2d) - (valueBounds.height / 2d);

    				valueBounds.setLocation(actualLocation);
    				Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    RadioCheckField check = new RadioCheckField(writer, pdfValueBounds, null, getName(widget));
                    check.setChecked(value.isSelected());

                    addBorder(widget, check);

                    PdfFormField field;
                    if (type == IWidget.RADIO_BUTTON) {
                        check.setCheckType(RadioCheckField.TYPE_CIRCLE);
                        field = check.getRadioField();
                    } else {
                        check.setCheckType(RadioCheckField.TYPE_CROSS);
                        field = check.getCheckField();
                    }

                    top.addKid(field);

//    				PdfFormField field;
//    				RadioCheckField radioCheckField = new RadioCheckField(writer, pdfValueBounds, groupName, widget.getName());
//
//    				radioCheckField.setChecked(value.isSelected());
//
//    				if (type == IWidget.RADIO_BUTTON) {
//    					radioCheckField.setCheckType(RadioCheckField.TYPE_CIRCLE);
//    					radioCheckField.setBorderColor(Color.black);
//    					radioCheckField.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
//
//    					field = radioCheckField.getRadioField();
//
//    					if (top == null) { // first time round
//    						top = radioCheckField.getRadioGroup(true, false);
//    					}
//    				} else {
//    					radioCheckField.setCheckType(RadioCheckField.TYPE_CROSS);
//    					radioCheckField.setBorderColor(Color.black);
//    					radioCheckField.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
//
//    					field = radioCheckField.getCheckField();
//
//    					if (top == null) { // first time round
//    						top = radioCheckField.getRadioGroup(false, false);
//    					}
//    				}
//
//    				top.addKid(field);

    				addJavaScriptToFormField(eventsAndScripts, field, writer);
    			}
    		}

    		if (globalWriter != null)
    			globalWriter.addAnnotation(top);
    		else
    			globalStamper.addAnnotation(top, currentPage);
    	}
    }
    
    private void addWidgets(PdfWriter writer, List widgets, Rectangle pageSize, Rectangle cropBox, int currentPage) throws IOException, DocumentException {

        handleButtonGroups(writer, widgets, pageSize, currentPage);

        for (Iterator iter = widgets.iterator(); iter.hasNext();) {

            IWidget widget = (IWidget) iter.next();
            int type = widget.getWidgetType();

            Element rootElement = widget.getProperties().getDocumentElement();

            //Font valueFontToUse = getFont(rootElement, "value");

            List elementsFromNodeList = XMLUtils.getElementsFromNodeList(
                    rootElement.getElementsByTagName("javascript"));

            Map eventsAndScripts = null;
            if (!elementsFromNodeList.isEmpty()) {
                Element javaScriptElement = (Element) elementsFromNodeList.get(0);

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

                    PdfTextField value = (PdfTextField) widget.getValueComponent();
                    String valueText = value.getText();

                    java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());
                    Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    // write out textbox
                    //BaseFont bf = valueFontToUse.getCalculatedBaseFont(false);

                    java.awt.Font font = value.getFont();

                    BaseFont baseFont = getBaseFont(font);

                    TextField tf = new TextField(writer, pdfValueBounds, widget.getWidgetName());
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
                    /** these are handled separately at the beginning */
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

                    PdfComboBox value = (PdfComboBox) widget.getValueComponent();
                    java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    Element itemsElement = (Element) rootElement.getElementsByTagName("items").item(0);

                    List list = XMLUtils.getElementsFromNodeList(itemsElement.getChildNodes());
                    
                    String[] items = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        Element item = (Element) list.get(i);
                        items[i] = XMLUtils.getAttributeFromElement(item, "item");
                    }
                    
                    java.awt.Font font = value.getFont();

                    BaseFont baseFont = getBaseFont(font);

                    TextField combo = new TextField(writer, pdfValueBounds, widget.getWidgetName());
                    combo.setFont(baseFont);
                    combo.setFontSize(value.getFont().getSize());
                    combo.setTextColor(getBaseColor(widget.getValueComponent().getForeground()));
                    combo.setChoices(items);

                    Element editableElement = XMLUtils.getPropertyElement(rootElement, "Allow Custom Text Entry");
                    boolean editable = Boolean.valueOf(editableElement.getAttributes().getNamedItem("value").getNodeValue()).booleanValue();
                    if (editable)
                        combo.setOptions(BaseField.EDIT);

                    Element defaultElement = XMLUtils.getPropertyElement(rootElement, "Default");
                    String defaultText = defaultElement.getAttributes().getNamedItem("value").getNodeValue();
                    if (defaultText.equals("< None >"))
                        defaultText = "";

                    addBorder(widget, combo);

                    field = combo.getComboField();
                    field.setValueAsString(defaultText);

                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }
                case IWidget.LIST_BOX: {
                    //PdfCaption listBoxCaption = widget.getCaptionComponent();
                    writeOutCaption(widget, pageSize, currentPage);

                    JList value = ((PdfList) widget.getValueComponent()).getList();
                    java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    ListModel listModel = value.getModel();
                    int noOfItems = listModel.getSize();
                    String[] items = new String[noOfItems];
                    for (int i = 0; i < noOfItems; i++) {
                        items[i] = (String) value.getModel().getElementAt(i);
                    }

                    java.awt.Font font = value.getFont();

                    BaseFont baseFont = getBaseFont(font);

                    TextField list = new TextField(writer, pdfValueBounds, widget.getWidgetName());
                    list.setFont(baseFont);
                    list.setFontSize(value.getFont().getSize());
                    list.setChoices(items);

                    Element defaultElement = XMLUtils.getPropertyElement(rootElement, "Default");
                    String defaultText = defaultElement.getAttributes().getNamedItem("value").getNodeValue();

                    int index = 0;
                    if (!defaultText.equals("")) {
                        for (int i = 0; i < noOfItems; i++) {
                            String item = (String) value.getModel().getElementAt(i);
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

                    PdfButton value = (PdfButton) widget.getValueComponent();

                    java.awt.Rectangle valueBounds = value.getBounds();
                    valueBounds.setLocation(widget.getAbsoluteLocationsOfValue());

                    Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                    java.awt.Font font = value.getFont();

                    BaseFont baseFont = getBaseFont(font);

                    PushbuttonField pushbutton = new PushbuttonField(writer, pdfValueBounds, widget.getWidgetName());
                    pushbutton.setFont(baseFont);
                    pushbutton.setFontSize(font.getSize());
                    pushbutton.setText(value.getText());
                    pushbutton.setTextColor(getBaseColor(value.getForeground()));
                    pushbutton.setBackgroundColor(getBaseColor(value.getBackground()));

                    org.w3c.dom.Document document = widget.getProperties();
                    Element borderProperties = (Element) document.getElementsByTagName("border").item(0);
                    List borderElement = XMLUtils.getElementsFromNodeList(
                            borderProperties.getElementsByTagName("borders").item(0).getChildNodes());

                    addBorder(widget, pushbutton);

                    /** potential code for creating custom dash border */
                    //pushbutton.getField().setBorderStyle(new PdfBorderDictionary(Float.parseFloat(width),PdfBorderDictionary.STYLE_BEVELED,new PdfDashPattern(2)));

                    field = pushbutton.getField();
                    addJavaScriptToFormField(eventsAndScripts, field, writer);

                    break;
                }
                default:
                    break;
            }

            if (field == null)
                continue;

            if (globalWriter != null)
                globalWriter.addAnnotation(field);
            else
                globalStamper.addAnnotation(field, currentPage);
        }
    }

    private BaseColor getBaseColor(Color color) {
        return new GrayColor(color.getRGB());
    }

    private String getName(IWidget widget) {
        String widgetName = widget.getWidgetName();
        if (mainFrame.getWidgetArrays().isWidgetArrayInList(widgetName)) {
            int arrayNumber = widget.getArrayNumber();
            widgetName += "[" + arrayNumber + "]";
//				System.out.println("w = " + w + " arraynumber = " + arrayNumber);
        }

        return widgetName;
    }

    private void addBorder(IWidget widget, BaseField tf) {
        org.w3c.dom.Document document = widget.getProperties();
        Element borderProperties = (Element) document.getElementsByTagName("border").item(0);

        Element border = (Element) borderProperties.getElementsByTagName("borders").item(0);
        
        String style = XMLUtils.getAttributeFromChildElement(border, "Border Style");
        String width = XMLUtils.getAttributeFromChildElement(border, "Border Width");
        String color = XMLUtils.getAttributeFromChildElement(border, "Border Color");
        
        if (style.equals("Solid"))
            tf.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
        else if (style.equals("Dashed"))
            tf.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
        else if (style.equals("Beveled"))
            tf.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
        else if (style.equals("None"))
            return;

        tf.setBorderColor(new GrayColor(Integer.parseInt(color)));
        tf.setBorderWidth(Integer.parseInt(width));
    }

    private BaseFont getBaseFont(java.awt.Font font) throws IOException, DocumentException {
        String fontPath = FontHandler.getInstance().getAbsoluteFontPath(font);
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont(fontPath, "Cp1250", BaseFont.EMBEDDED);
        } catch (DocumentException e) {

            /**
             * A document exception has been thrown meaning that the font cannot be embedded
             * due to licensing restrictions so substitute with Helvetica
             */

            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);

            fontSubstitutions.add(font.getFontName());
        }
        return baseFont;
    }

    private void addJavaScriptToFormField(Map eventsAndScripts, PdfFormField formField, PdfWriter writer) {
        for (Iterator iter = eventsAndScripts.keySet().iterator(); iter.hasNext();) {
            PdfName pdfName = (PdfName) iter.next();
            String script = (String) eventsAndScripts.get(pdfName);

            if (!script.equals(""))
                formField.setAdditionalActions(pdfName, PdfAction.javaScript(script, writer));
        }
    }

//    private Font getFont(Element element, String type) {
//    	
//    	Element fontProperties = (Element) element.getElementsByTagName("font_" + type).item(0);
//        
//        /** get caption properties */
//        Element caption = (Element) fontProperties.getElementsByTagName("font_caption").item(0);
//        
//        String captionFontName = XMLUtils.getAttributeFromChildElement(caption, "Font Name");
//        String captionFontSize = XMLUtils.getAttributeFromChildElement(caption, "Font Size");
//        String captionFontStyle = XMLUtils.getAttributeFromChildElement(caption, "Font Style");
//        String captionUnderline = XMLUtils.getAttributeFromChildElement(caption, "Underline");
//        String captionStrikethrough = XMLUtils.getAttributeFromChildElement(caption, "Strikethrough");
//        String captionColor = XMLUtils.getAttributeFromChildElement(caption, "Color");
//    	
//    	
//        List elementsFromNodeList = XMLUtils.getElementsFromNodeList(
//                element.getElementsByTagName("font_" + type));
//
//        if (elementsFromNodeList.isEmpty())
//            return null;
//
//        Element fontElement = (Element) elementsFromNodeList.get(0);
//
//        List fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
//        String fontName = XMLUtils.getAttribute(fontList, 0);
//        int fontSize = Integer.parseInt(XMLUtils.getAttribute(fontList, 1));
//        int fontStyle = Integer.parseInt(XMLUtils.getAttribute(fontList, 2));
//        int underline = Integer.parseInt(XMLUtils.getAttribute(fontList, 3));
//        int strikethrough = Integer.parseInt(XMLUtils.getAttribute(fontList, 4));
//        int color = Integer.parseInt(XMLUtils.getAttribute(fontList, 5));
//
//        int fontNameToUse;
//
//        fontName = fontName.split(" ")[0];
//        if (fontName.equals("Courier"))
//            fontNameToUse = Font.COURIER;
//        else if (fontName.equals("Helvetica"))
//            fontNameToUse = Font.HELVETICA;
//        else if (fontName.equals("Times"))
//            fontNameToUse = Font.TIMES_ROMAN;
//        else if (fontName.equals("Symbol"))
//            fontNameToUse = Font.SYMBOL;
//        else
//            fontNameToUse = Font.ZAPFDINGBATS;
//
//
//        int styleToUse = Font.NORMAL;
//        switch (fontStyle) {
//            case IWidget.STYLE_BOLD:
//                styleToUse = Font.BOLD;
//                break;
//            case IWidget.STYLE_ITALIC:
//                styleToUse = Font.ITALIC;
//                break;
//            case IWidget.STYLE_BOLDITALIC:
//                styleToUse = Font.BOLDITALIC;
//                break;
//        }
//
//        int underlineToUse = underline == 0 ? 0 : Font.UNDERLINE;
//        int strikethroughToUse = strikethrough == 0 ? 0 : Font.STRIKETHRU;
//
//        styleToUse |= strikethroughToUse | underlineToUse;
//
//        return new Font(fontNameToUse, fontSize, styleToUse, new Color(color));
//    }

//    private Rectangle getSizeAndPosition(Element element) {
//        Element sizeAndPosition = (Element) XMLUtils.getElementsFromNodeList(
//                element.getElementsByTagName("sizeandposition")).get(0);
//
//        List sizeAndPositionList = XMLUtils.getElementsFromNodeList(sizeAndPosition.getChildNodes());
//
//        int x = Integer.parseInt(XMLUtils.getAttribute(sizeAndPositionList, 0));
//        int width = Integer.parseInt(XMLUtils.getAttribute(sizeAndPositionList, 1));
//        int height = Integer.parseInt(XMLUtils.getAttribute(sizeAndPositionList, 2));
//        int y = Integer.parseInt(XMLUtils.getAttribute(sizeAndPositionList, 3));
//
//        java.awt.Rectangle valueBounds = new java.awt.Rectangle(x, y, width + 100, height);
//
//        return convertJavaCoordsToPdfCoords(valueBounds, PageSize.A4);
//    }

    private PdfDocumentLayout getPdfDocumentLayout(List pages) {
		PdfDocumentLayout pdfDocumentLayout = new PdfDocumentLayout();

		for (Iterator it = pages.iterator(); it.hasNext();) {
			Element page = (Element) it.next();

			Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
			if (fileLocationElement == null) { // is a hand made page
				pdfDocumentLayout.addPage(false);
			} else { // its an imported page
				String fileLocation = fileLocationElement.getAttributeNode("value").getValue();

				Element pdfPageNumberElement = XMLUtils.getPropertyElement(page, "pdfpagenumber");
				String pdfPageNumber = pdfPageNumberElement.getAttributeNode("value").getValue();

				pdfDocumentLayout.addPage(true, fileLocation, Integer.parseInt(pdfPageNumber));
			}
		}

		return pdfDocumentLayout;
	}
    
// private Map getPdfFiles(List pages) {
//    	Map pdfFilesAndPages = new LinkedHashMap();
//    	int pageCount = 1;
//    	
//    	for (Iterator it = pages.iterator(); it.hasNext();) {
//            Element page = (Element) it.next();
//            
//            Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
//            if (fileLocationElement != null) {
//                String fileLocation = fileLocationElement.getAttributeNode("value").getValue();
//
//                List pageNumbers = (List) pdfFilesAndPages.get(fileLocation);
//                if(pageNumbers == null){
//                	pdfFilesAndPages.put(fileLocation, pageNumbers);
//                }
//                pageNumbers.add(new Integer(pageCount));
//            }
//            
//            pageCount++;
//    	}
//    	return pdfFilesAndPages;
//    	
////        Set pdfFiles = new LinkedHashSet();
////        for (Iterator it = pages.iterator(); it.hasNext();) {
////            Element page = (Element) it.next();
////
////            Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
////            if (fileLocationElement != null) {
////                String fileLocation = fileLocationElement.getAttributeNode("value").getValue();
////                pdfFiles.add(fileLocation);
////            }
////        }
////        return pdfFiles;
//    }

    private Map getEventAndScriptMap(Element javaScriptElement) {

        Map actionAndScriptMap = new HashMap();

        List javaScriptProperties = XMLUtils.getElementsFromNodeList(javaScriptElement.getChildNodes());

        for (Iterator iter = javaScriptProperties.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();

            String event = element.getNodeName();

            PdfName eventToUse = null;
            if (event.equals("mouseEnter")) {
                eventToUse = PdfName.E;
            } else if (event.equals("mouseExit")) {
                eventToUse = PdfName.X;
            } else if (event.equals("change")) {
                eventToUse = PdfName.F;
            } else if (event.equals("mouseUp")) {
                eventToUse = PdfName.U;
            } else if (event.equals("mouseDown")) {
                eventToUse = PdfName.D;
            } else if (event.equals("keystroke")) {
                eventToUse = PdfName.K;
            }

            NodeList childNodes = element.getChildNodes();
            if (childNodes.getLength() != 0) {
                Text textNode = (Text) childNodes.item(0);
                String nodeValue = textNode.getNodeValue();
                actionAndScriptMap.put(eventToUse, nodeValue);
            }
        }

        return actionAndScriptMap;
    }

//    private void writeOutCaption(PdfStamper stamper, IWidget widget, Rectangle pageSize, int currentPage) {
//        PdfCaption caption = widget.getCaptionComponent();
//        java.awt.Rectangle captionBounds = caption.getBounds();
//        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
//        Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);
//
//        PdfContentByte cb = stamper.getOverContent(currentPage);
//
//        PdfTemplate tp = cb.createTemplate(captionBounds.width, captionBounds.height);
//        Graphics2D g2 = tp.createGraphics(captionBounds.width, captionBounds.height);
//        caption.paint(g2);
//        g2.dispose();
//        cb.addTemplate(tp, pdfCaptionBounds.left(), pdfCaptionBounds.top() - captionBounds.height);
//    }

    private void writeOutCaption(IWidget widget, Rectangle pageSize, int currentPage) {

        PdfCaption caption = widget.getCaptionComponent();
        if (caption == null)
            return;

        if (widget.isComponentSplit()) {
            Element captionElement = (Element) XMLUtils.getElementsFromNodeList(
                    widget.getProperties().getElementsByTagName("layout")).get(0);

            String location = XMLUtils.getPropertyElement(captionElement, "Position").getAttributeNode("value").getValue();
            if (location.equals("None")) {
                return;
            }
        }

        java.awt.Rectangle captionBounds = caption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        PdfContentByte cb = globalStamper == null ?
                globalWriter.getDirectContent() : globalStamper.getOverContent(currentPage);

        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfCaptionBounds.getLeft(), pdfCaptionBounds.getTop() - captionBounds.height);

        java.awt.Font font = caption.getFont();
        String fontDirectory = FontHandler.getInstance().getFontDirectory(font);

        DefaultFontMapper mapper = new DefaultFontMapper();

        mapper.insertDirectory(fontDirectory);

        /**
         * we need to make this erroneous call to awtToPdf to see if an exception is thrown, if it is, it is
         * probably because the font cannot be embedded due to licensing restrictions, so substitute with Helvetica
         */
        try {
            mapper.awtToPdf(font);
        } catch (Exception e) {
            mapper = new DefaultFontMapper();
            fontSubstitutions.add(font.getFontName());
        }

        Graphics2D g2 = cb.createGraphics(captionBounds.width, captionBounds.height, mapper, true, .95f);

        //Graphics2D g2 = cb.createGraphicsShapes(captionBounds.width, captionBounds.height, true, 0.95f);

        caption.paint(g2);

        g2.dispose();
        cb.restoreState();
    }

    private void addImage(IWidget widget, Rectangle pageSize, int currentPage) {

        JLabel image = (JLabel) widget.getValueComponent();
        if (image == null)
            return;

        java.awt.Rectangle imageBounds = image.getBounds();
        imageBounds.setLocation(widget.getX(), widget.getY());
        Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(imageBounds, pageSize);

        // write out caption
        PdfContentByte cb = globalStamper == null ?
                globalWriter.getDirectContent() : globalStamper.getOverContent(currentPage);

        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, pdfValueBounds.getLeft(), pdfValueBounds.getTop() - imageBounds.height);

        Graphics2D g2 = cb.createGraphicsShapes(imageBounds.width, imageBounds.height);

        image.paint(g2);

        g2.dispose();
        cb.restoreState();

//        PdfCaption caption = widget.getCaptionComponent();
//        if (caption == null)
//            return;
//
//        java.awt.Rectangle captionBounds = caption.getBounds();
//        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
//        Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);
//
//        // write out caption
//        PdfContentByte cb = globalStamper == null ?
//                globalWriter.getDirectContent() : globalStamper.getOverContent(currentPage);
//
//        cb.saveState();
//        cb.concatCTM(1, 0, 0, 1, pdfCaptionBounds.left(), pdfCaptionBounds.top() - captionBounds.height);
//
//        String fontDirectory = FontHandler.getInstance().getFontDirectory(caption.getFont());
//
//        DefaultFontMapper mapper = new DefaultFontMapper();
//        mapper.insertDirectory(fontDirectory);
//
//        Graphics2D g2 = cb.createGraphics(captionBounds.width, captionBounds.height, mapper, true, .95f);
//
//        //Graphics2D g2 = cb.createGraphicsShapes(captionBounds.width, captionBounds.height, true, 0.95f);
//
//        caption.paint(g2);
//
//        g2.dispose();
//        cb.restoreState();
    }

    private Rectangle convertJavaCoordsToPdfCoords(java.awt.Rectangle bounds, Rectangle pageSize) {
        float javaX1 = bounds.x - IMainFrame.INSET;
        float javaY1 = bounds.y - IMainFrame.INSET;

        float javaX2 = javaX1 + bounds.width;

        float pdfY1 = pageSize.getHeight() - javaY1 - bounds.height;

        float pdfY2 = pdfY1 + bounds.height;

        return new Rectangle(javaX1, pdfY1, javaX2, pdfY2);
    }
}