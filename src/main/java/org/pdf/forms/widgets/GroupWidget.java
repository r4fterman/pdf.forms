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
 * GroupWidget.java
 * ---------------
 */
package org.pdf.forms.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Special widget object that represents a group of widgets. Although this class
 * implements IWidget (as all widgets must do) it does not extend Widget
 */
public class GroupWidget implements IWidget {

    private List<IWidget> widgetsInGroup;
    private int type = IWidget.GROUP;
    private Icon icon;
    private static int nextWidgetNumber = 1;
    private String widgetName;

    private Document properties;
    private Element widgetsElement;

    private Element setupProperties() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            properties = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return XMLUtils.createAndAppendElement(properties, "widget", properties);
    }

    public GroupWidget() {
        icon = new ImageIcon(getClass().getResource("/org/pdf/forms/res/Group.gif"));

        String widgetName = "Group" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "GROUP", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        widgetsElement = XMLUtils.createAndAppendElement(properties, "widgets", rootElement);
    }

    public List<IWidget> getWidgetsInGroup() {
        return widgetsInGroup;
    }

    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
        this.widgetsInGroup = widgetsInGroup;

        List widgets = XMLUtils.getElementsFromNodeList(widgetsElement.getChildNodes());

        for (final Object widget1 : widgets) {
            Element widget = (Element) widget1;
            Node parent = widget.getParentNode();
            parent.removeChild(widget);
        }

        for (final IWidget widget : widgetsInGroup) {
            Document widgetProperties = widget.getProperties();

            Element widgetRoot = widgetProperties.getDocumentElement();

            widgetsElement.appendChild(properties.importNode(widgetRoot, true));
        }
    }

    public Rectangle getBounds() {
        return WidgetSelection.getMultipleWidgetBounds(new HashSet<>(widgetsInGroup));
    }

    public int getType() {
        return type;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setName(final String widgetName) {
        this.widgetName = widgetName;
    }

    public Document getProperties() {
        return properties;
    }

    ////////////////////////////////////////////////////////////////////////

    public void setAllProperties() {

    }

    public int getArrayNumber() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParagraphProperties(
            final Element paragraphProperties,
            final int currentlyEditing) {
    }

    public void setLayoutProperties(final Element paragraphProperties) {
    }

    public void setFontProperties(
            final Element parentElement,
            final int currentlyEditing) {
    }

    public void setCaptionProperties(Element captionProperties) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getWidget() {
        return null;
    }

    public void setPosition(
            final int x,
            final int y) {
    }

    public void setX(final int x) {
    }

    public void setY(final int y) {
    }

    public int getX() {
        return 0;
    }

    public int getY() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void setSize(
            final int width,
            final int height) {
    }

    public int getResizeTypeForSplitComponent(
            final int mouseX,
            final int mouseY) {
        return 0;
    }

    public boolean allowEditCaptionAndValue() {
        return false;
    }

    public boolean allowEditOfCaptionOnClick() {
        return false;
    }

    public Dimension getBoxSize() {
        return null;
    }

    public int getWidgetType() {
        return type;
    }

    public JComponent getValueComponent() {
        return null;
    }

    public PdfCaption getCaptionComponent() {
        return null;
    }

    public void setLastX(final int lastX) {
    }

    public void setLastY(final int lastY) {
    }

    public int getLastX() {
        return 0;
    }

    public int getLastY() {
        return 0;
    }

    public Point getAbsoluteLocationsOfCaption() {
        return null;
    }

    public Point getAbsoluteLocationsOfValue() {
        return null;
    }

    public boolean isComponentSplit() {
        return false;
    }

    public double getResizeHeightRatio() {
        return 0;
    }

    public double getResizeWidthRatio() {
        return 0;
    }

    public void setResizeHeightRatio(final double resizeHeightRatio) {
    }

    public void setResizeWidthRatio(final double resizeWidthRation) {
    }

    public double getResizeFromTopRatio() {
        return 0;
    }

    public double getResizeFromLeftRatio() {
        return 0;
    }

    public void setResizeFromTopRatio(final double resizeHeightRatio) {
    }

    public void setResizeFromLeftRatio(final double resizeWidthRation) {
    }

    public void setObjectProperties(final Element parentElement) {
    }

    public void setBorderAndBackgroundProperties(final Element borderProperties) {
    }

}
