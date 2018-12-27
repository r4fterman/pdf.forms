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
    private final int type = IWidget.GROUP;
    private final Icon icon;
    private static int nextWidgetNumber = 1;
    private String widgetName;

    private Document properties;
    private final Element widgetsElement;

    private Element setupProperties() {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            properties = db.newDocument();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }

        return XMLUtils.createAndAppendElement(properties, "widget", properties);
    }

    public GroupWidget() {
        icon = new ImageIcon(getClass().getResource("/org/pdf/forms/res/Group.gif"));

        final String widgetName = "Group" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widgetName = widgetName;

        final Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "GROUP", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        widgetsElement = XMLUtils.createAndAppendElement(properties, "widgets", rootElement);
    }

    @Override
    public List<IWidget> getWidgetsInGroup() {
        return widgetsInGroup;
    }

    @Override
    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
        this.widgetsInGroup = widgetsInGroup;

        final List widgets = XMLUtils.getElementsFromNodeList(widgetsElement.getChildNodes());

        for (final Object widget1 : widgets) {
            final Element widget = (Element) widget1;
            final Node parent = widget.getParentNode();
            parent.removeChild(widget);
        }

        for (final IWidget widget : widgetsInGroup) {
            final Document widgetProperties = widget.getProperties();

            final Element widgetRoot = widgetProperties.getDocumentElement();

            widgetsElement.appendChild(properties.importNode(widgetRoot, true));
        }
    }

    @Override
    public Rectangle getBounds() {
        return WidgetSelection.getMultipleWidgetBounds(new HashSet<>(widgetsInGroup));
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getWidgetName() {
        return widgetName;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    public void setName(final String widgetName) {
        this.widgetName = widgetName;
    }

    @Override
    public Document getProperties() {
        return properties;
    }

    ////////////////////////////////////////////////////////////////////////

    @Override
    public void setAllProperties() {

    }

    @Override
    public int getArrayNumber() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setParagraphProperties(
            final Element paragraphProperties,
            final int currentlyEditing) {
    }

    @Override
    public void setLayoutProperties(final Element paragraphProperties) {
    }

    @Override
    public void setFontProperties(
            final Element parentElement,
            final int currentlyEditing) {
    }

    @Override
    public void setCaptionProperties(final Element captionProperties) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JComponent getWidget() {
        return null;
    }

    @Override
    public void setPosition(
            final int x,
            final int y) {
    }

    @Override
    public void setX(final int x) {
    }

    @Override
    public void setY(final int y) {
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
    }

    @Override
    public int getResizeTypeForSplitComponent(
            final int mouseX,
            final int mouseY) {
        return 0;
    }

    @Override
    public boolean allowEditCaptionAndValue() {
        return false;
    }

    @Override
    public boolean allowEditOfCaptionOnClick() {
        return false;
    }

    @Override
    public Dimension getBoxSize() {
        return null;
    }

    @Override
    public int getWidgetType() {
        return type;
    }

    @Override
    public JComponent getValueComponent() {
        return null;
    }

    @Override
    public PdfCaption getCaptionComponent() {
        return null;
    }

    @Override
    public void setLastX(final int lastX) {
    }

    @Override
    public void setLastY(final int lastY) {
    }

    @Override
    public int getLastX() {
        return 0;
    }

    @Override
    public int getLastY() {
        return 0;
    }

    @Override
    public Point getAbsoluteLocationsOfCaption() {
        return null;
    }

    @Override
    public Point getAbsoluteLocationsOfValue() {
        return null;
    }

    @Override
    public boolean isComponentSplit() {
        return false;
    }

    @Override
    public double getResizeHeightRatio() {
        return 0;
    }

    @Override
    public double getResizeWidthRatio() {
        return 0;
    }

    @Override
    public void setResizeHeightRatio(final double resizeHeightRatio) {
    }

    @Override
    public void setResizeWidthRatio(final double resizeWidthRation) {
    }

    @Override
    public double getResizeFromTopRatio() {
        return 0;
    }

    @Override
    public double getResizeFromLeftRatio() {
        return 0;
    }

    @Override
    public void setResizeFromTopRatio(final double resizeHeightRatio) {
    }

    @Override
    public void setResizeFromLeftRatio(final double resizeWidthRation) {
    }

    @Override
    public void setObjectProperties(final Element parentElement) {
    }

    @Override
    public void setBorderAndBackgroundProperties(final Element borderProperties) {
    }

}
