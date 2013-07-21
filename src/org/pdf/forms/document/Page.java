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
 * Page.java
 * ---------------
 */
package org.pdf.forms.document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Page {

    private String pageName;

    private List widgets = new ArrayList();

    private List radioButtonGroups = new ArrayList();
    private List checkBoxGroups = new ArrayList();

    private int height;

    private int width;
    private String pdfFileLocation;

    private int pdfPageNumber;

    private Document pageProperties;

    private boolean widgetsAddedToDesigner = false;
    private int rotation = 0;

    public Page(String pageName, int width, int height) {
        this.width = width;
        this.height = height;

        this.pageName = pageName;
    }

    public Page(String pageName, String pdfFile, int pdfPage) {
        this.pdfFileLocation = pdfFile;
        this.pdfPageNumber = pdfPage;

        this.pageName = pageName;
    }

    public Document getPageProperties() {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            pageProperties = db.newDocument();

            Element rootElement = XMLUtils.createAndAppendElement(pageProperties, "page", pageProperties);

            boolean isPdfFile = pdfFileLocation != null;
            XMLUtils.addBasicProperty(pageProperties, "pagetype", isPdfFile ? "pdfpage" : "simplepage", rootElement);

            XMLUtils.addBasicProperty(pageProperties, "pagename", pageName, rootElement);

            Element pageDate = XMLUtils.createAndAppendElement(pageProperties, "pagedata", rootElement);

            if (isPdfFile) {
                XMLUtils.addBasicProperty(pageProperties, "pdffilelocation", pdfFileLocation, pageDate);
                XMLUtils.addBasicProperty(pageProperties, "pdfpagenumber", pdfPageNumber + "", pageDate);
            } else {
                XMLUtils.addBasicProperty(pageProperties, "width", width + "", pageDate);
                XMLUtils.addBasicProperty(pageProperties, "height", height + "", pageDate);
            }

            for (Iterator it = widgets.iterator(); it.hasNext(); ) {
                IWidget widget = (IWidget) it.next();

                Document widgetProperties = widget.getProperties();

                Element widgetRoot = widgetProperties.getDocumentElement();

                pageProperties.getDocumentElement().appendChild(pageProperties.importNode(widgetRoot, true));
            }

            /** add radio button groups to the page */
            Element radioButtonGroupsElement = XMLUtils.createAndAppendElement(pageProperties, "radiobuttongroups", rootElement);
            addButtonGroupsToPage(radioButtonGroupsElement, IWidget.RADIO_BUTTON);

            /** add check box groups to the page */
            Element checkBoxGroupsElement = XMLUtils.createAndAppendElement(pageProperties, "checkboxgroups", rootElement);
            addButtonGroupsToPage(checkBoxGroupsElement, IWidget.CHECK_BOX);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return pageProperties;
    }

    private void addButtonGroupsToPage(Element radioButtonGroupsElement, int type) {
        List buttonGroups = type == IWidget.RADIO_BUTTON ? radioButtonGroups : checkBoxGroups;

        for (Iterator it = buttonGroups.iterator(); it.hasNext(); ) {
            ButtonGroup buttonGroup = (ButtonGroup) it.next();
            XMLUtils.addBasicProperty(pageProperties, "buttongroupname", buttonGroup.getName(), radioButtonGroupsElement);
        }
    }

    public int getPdfPageNumber() {
        return pdfPageNumber;
    }

    public String getPdfFileLocation() {
        return pdfFileLocation;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public List getWidgets() {
        return widgets;
    }

    public List getRadioButtonGroups() {
        return radioButtonGroups;
    }

    public List getCheckBoxGroups() {
        return checkBoxGroups;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(Dimension size) {
        this.width = size.width;
        this.height = size.height;
    }

    public void setWidgets(List widgets) {
//    	System.out.println(pageName+" "+widgets);
        this.widgets = widgets;
    }

    public void setWidgetsAddedToDesigner(boolean widgetsAddedToDesigner) {
        this.widgetsAddedToDesigner = widgetsAddedToDesigner;
    }

    public boolean widgetsAddedToDesigner() {
        return widgetsAddedToDesigner;
    }

    public ButtonGroup getCheckBoxGroup(String groupName) {
        ButtonGroup returnedGroup = null;

        for (Iterator it = checkBoxGroups.iterator(); it.hasNext(); ) {
            ButtonGroup buttonGroup = (ButtonGroup) it.next();
            String bgName = buttonGroup.getName();
            if (bgName.equals(groupName))
                returnedGroup = buttonGroup;
        }

        return returnedGroup;
    }
}
