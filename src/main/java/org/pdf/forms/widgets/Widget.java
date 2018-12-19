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
 * Widget.java
 * ---------------
 */
package org.pdf.forms.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jpedal.objects.acroforms.creation.JPedalBorderFactory;
import org.jpedal.utils.Strip;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.SplitComponent;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Widget {

    JComponent component;
    JComponent baseComponent;

    protected Document properties;

    protected boolean isComponentSplit, allowEditCaptionAndValue, allowEditOfCaptionOnClick;
    protected String widgetName;
    protected int arrayNumber;

    private Point position;
    private int type;
    private int lastX, lastY;
    private double resizeHeightRatio;
    private double resizeWidthRatio;
    private double resizeFromTopRatio;
    private double resizeWidthFromLeftRatio;
    private Icon icon;

    public Widget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final String iconLocation) {
        this.position = new Point(0, 0);

        this.type = type;
        this.component = component;
        this.baseComponent = baseComponent;

        icon = new ImageIcon(getClass().getResource(iconLocation));
    }

    public JComponent getWidget() {
        return component;
    }

    public int getWidgetType() {
        return type;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void setPosition(
            final int x,
            final int y) {
        position.x = x;
        position.y = y;
    }

    public void setX(final int x) {
        position.x = x;
    }

    public void setY(final int y) {
        position.y = y;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getWidth() {
        return component.getWidth();
    }

    public int getHeight() {
        return component.getHeight();
    }

    public void setSize(
            final int width,
            final int height) {
        component = WidgetFactory.createResizedComponent(baseComponent, width, height);
    }

    public boolean allowEditCaptionAndValue() {
        return allowEditCaptionAndValue;
    }

    public boolean allowEditOfCaptionOnClick() {
        return allowEditOfCaptionOnClick;
    }

    public Dimension getBoxSize() {
        return new Dimension(getWidth() + WidgetSelection.BOX_MARGIN * 2, getHeight() + WidgetSelection.BOX_MARGIN * 2);
    }

    public int getResizeTypeForSplitComponent(
            final int mouseX,
            final int mouseY) {
        SplitComponent sc = (SplitComponent) baseComponent;

        if (sc.getCaptionPosition() == SplitComponent.CAPTION_NONE) {
            return -1;
        }

        int widgetX = getX();
        int widgetY = getY();

        int resizeType = -1;

        int dividorLocation = sc.getDividerLocation();
        int orientation = sc.getOrientation();

        if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
            if (mouseY >= widgetY && mouseY <= widgetY + getHeight()
                    && mouseX > (widgetX + dividorLocation) - 3
                    && mouseX < (widgetX + dividorLocation + 3)) {

                resizeType = DesignerMouseMotionListener.RESIZE_SPLIT_HORIZONTAL_CURSOR;
            }
        } else {
            if (mouseX >= widgetX && mouseX <= widgetX + getWidth()
                    && mouseY > (widgetY + dividorLocation) - 3
                    && mouseY < (widgetY + dividorLocation + 3)) {

                resizeType = DesignerMouseMotionListener.RESIZE_SPLIT_VERTICAL_CURSOR;
            }
        }
        return resizeType;
    }

    public JComponent getValueComponent() {

        JComponent value;
        if (isComponentSplit) {
            value = ((SplitComponent) baseComponent).getValue();
        } else {
            value = baseComponent;
        }

        return value;
    }

    public PdfCaption getCaptionComponent() {
        PdfCaption caption = null;
        if (isComponentSplit) {
            caption = ((SplitComponent) baseComponent).getCaption();
        }

        return caption;
    }

    public void setLastX(final int lastX) {
        this.lastX = lastX;
    }

    public void setLastY(final int lastY) {
        this.lastY = lastY;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public Point getAbsoluteLocationsOfValue() {
        Point location = null;

        if (isComponentSplit) {

            int captionPosition = ((SplitComponent) baseComponent).getCaptionPosition();

            switch (captionPosition) {
                case SplitComponent.CAPTION_LEFT:
                    location = new Point(getX() + getCaptionComponent().getBounds().width + SplitComponent.DIVIDER_SIZE, getY());
                    break;

                case SplitComponent.CAPTION_TOP:
                    location = new Point(getX(), getY() + getCaptionComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);
                    break;

                case SplitComponent.CAPTION_RIGHT:
                    location = new Point(getX(), getY());

                    break;

                case SplitComponent.CAPTION_BOTTOM:
                    location = new Point(getX(), getY());

                    break;
                case SplitComponent.CAPTION_NONE:
                    location = new Point(getX(), getY());

                    break;
            }
        }

        return location;
    }

    public Point getAbsoluteLocationsOfCaption() {
        Point location = null;

        if (isComponentSplit) {

            int captionPosition = ((SplitComponent) baseComponent).getCaptionPosition();

            switch (captionPosition) {
                case SplitComponent.CAPTION_LEFT:
                    location = new Point(getX(), getY());
                    break;

                case SplitComponent.CAPTION_TOP:
                    location = new Point(getX(), getY());
                    break;

                case SplitComponent.CAPTION_RIGHT:
                    location = new Point(getX() + getValueComponent().getBounds().width + SplitComponent.DIVIDER_SIZE, getY());

                    break;

                case SplitComponent.CAPTION_BOTTOM:
                    location = new Point(getX(), getY() + getValueComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);

                    break;
            }
        }

        if (location == null) {
            location = new Point(getX(), getY());
        }

        return location;
    }

    public boolean isComponentSplit() {
        return isComponentSplit;
    }

    public void setResizeHeightRatio(final double resizeHeightRatio) {
        this.resizeHeightRatio = resizeHeightRatio;
    }

    public void setResizeWidthRatio(final double resizeWidthRatio) {
        this.resizeWidthRatio = resizeWidthRatio;
    }

    public double getResizeHeightRatio() {
        return resizeHeightRatio;
    }

    public double getResizeWidthRatio() {
        return resizeWidthRatio;
    }

    public void setResizeFromTopRatio(final double resizeFromTopRatio) {
        this.resizeFromTopRatio = resizeFromTopRatio;
    }

    public void setResizeFromLeftRatio(final double resizeWidthFromLeftRatio) {
        this.resizeWidthFromLeftRatio = resizeWidthFromLeftRatio;
    }

    public double getResizeFromTopRatio() {
        return resizeFromTopRatio;
    }

    public double getResizeFromLeftRatio() {
        return resizeWidthFromLeftRatio;
    }

    public List<IWidget> getWidgetsInGroup() {
        return null;
    }

    public void setWidgetsInGroup(final List widgetsInGroup) {
    }

    public int getType() {
        return type;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public int getArrayNumber() {
        return arrayNumber;
    }

    public Icon getIcon() {
        return icon;
    }

    public Document getProperties() {
        return properties;
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

    protected Element setupProperties() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            properties = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return XMLUtils.createAndAppendElement(properties, "widget", properties);
    }

    protected void setFontProperties(
            final Element properties,
            final IPdfComponent component) {
        String fontNameProperty = XMLUtils.getAttributeFromChildElement(properties, "Font Name");
        String fontSizeProperty = XMLUtils.getAttributeFromChildElement(properties, "Font Size");
        String fontStyleProperty = XMLUtils.getAttributeFromChildElement(properties, "Font Style");
        String underlineProperty = XMLUtils.getAttributeFromChildElement(properties, "Underline");
        String strikethroughProperty = XMLUtils.getAttributeFromChildElement(properties, "Strikethrough");
        String colorProperty = XMLUtils.getAttributeFromChildElement(properties, "Color");

        Font baseFont = FontHandler.getInstance().getFontFromName(fontNameProperty);

        final Map<TextAttribute, Float> fontAttrs = new HashMap<>();
        fontAttrs.put(TextAttribute.SIZE, Float.valueOf(fontSizeProperty));

        int fontStyle = Integer.parseInt(fontStyleProperty);

        switch (fontStyle) {
            case 0:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                break;
            case 1:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                break;
            case 2:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            case 3:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
        }

        Font fontToUse = baseFont.deriveFont(fontAttrs);

        component.setFont(fontToUse);

        int underline = Integer.parseInt(underlineProperty);
        component.setUnderlineType(underline);

        boolean isStrikethrough = Integer.parseInt(strikethroughProperty) == IWidget.STRIKETHROUGH_ON;
        component.setStikethrough(isStrikethrough);

        Color color = new Color(Integer.parseInt(colorProperty));
        component.setForeground(color);
    }

    void setSizeAndPosition(final Element layoutPropertiesElement) {
        Element sizeAndPositionElement = (Element) layoutPropertiesElement.getElementsByTagName("sizeandposition").item(0);

        int x = Integer.parseInt(XMLUtils.getAttributeFromChildElement(sizeAndPositionElement, "X"));
        int width = Integer.parseInt(XMLUtils.getAttributeFromChildElement(sizeAndPositionElement, "Width"));
        int y = Integer.parseInt(XMLUtils.getAttributeFromChildElement(sizeAndPositionElement, "Y"));
        int height = Integer.parseInt(XMLUtils.getAttributeFromChildElement(sizeAndPositionElement, "Height"));

        setPosition(x, y);
        setSize(width, height);
    }

    protected void setParagraphProperties(
            final Element captionPropertiesElement,
            final IPdfComponent component) {
        String horizontalAlignment = XMLUtils.getAttributeFromChildElement(captionPropertiesElement, "Horizontal Alignment");
        String verticallAlignment = XMLUtils.getAttributeFromChildElement(captionPropertiesElement, "Vertical Alignment");

        if (component instanceof PdfCaption) {
            String text = component.getText();
            text = Strip.stripXML(text).toString();
            text = "<html><p align=" + horizontalAlignment + ">" + text;
            component.setText(text);
        }

        try {
            if (horizontalAlignment.equals("justify")) {
                horizontalAlignment = "left";
            }

            Field field = SwingConstants.class.getDeclaredField(horizontalAlignment.toUpperCase());
            int alignment = field.getInt(this);
            component.setHorizontalAlignment(alignment);

            field = SwingConstants.class.getDeclaredField(verticallAlignment.toUpperCase());
            alignment = field.getInt(this);
            component.setVerticalAlignment(alignment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSize(getWidth(), getHeight());
    }

    public void setAllProperties() {

        Element root = properties.getDocumentElement();

        setParagraphProperties(root, IWidget.COMPONENT_BOTH);
        setLayoutProperties(root);
        setFontProperties(root, IWidget.COMPONENT_BOTH);
        setObjectProperties(root);
        setCaptionProperties(root);
    }

    void addJavaScript(final Element rootElement) {
        Element javaScriptElement = XMLUtils.createAndAppendElement(properties, "javascript", rootElement);

        Element mouseEnterElement = XMLUtils.createAndAppendElement(properties, "mouseEnter", javaScriptElement);
        mouseEnterElement.appendChild(properties.createTextNode(""));

        Element mouseExitElement = XMLUtils.createAndAppendElement(properties, "mouseExit", javaScriptElement);
        mouseExitElement.appendChild(properties.createTextNode(""));

        Element changeElement = XMLUtils.createAndAppendElement(properties, "change", javaScriptElement);
        changeElement.appendChild(properties.createTextNode(""));

        Element mouseUpElement = XMLUtils.createAndAppendElement(properties, "mouseUp", javaScriptElement);
        mouseUpElement.appendChild(properties.createTextNode(""));

        Element mouseDownElement = XMLUtils.createAndAppendElement(properties, "mouseDown", javaScriptElement);
        mouseDownElement.appendChild(properties.createTextNode(""));

        if (getType() == IWidget.TEXT_FIELD) {
            Element keystrokeElement = XMLUtils.createAndAppendElement(properties, "keystroke", javaScriptElement);
            keystrokeElement.appendChild(properties.createTextNode(""));
        }
    }

    public void setObjectProperties(final Element parentElement) {

    }

    void setBindingProperties(Element objectPropertiesElement) {
        Element bindingPropertiesElement = (Element) objectPropertiesElement.getElementsByTagName("binding").item(0);

        this.widgetName = XMLUtils.getAttributeFromChildElement(bindingPropertiesElement, "Name");
        this.arrayNumber = Integer.parseInt(XMLUtils.getAttributeFromChildElement(bindingPropertiesElement, "Array Number"));

        setSize(getWidth(), getHeight());
    }

    public void setCaptionProperties(final Element captionPropertiesElement) {
        if (isComponentSplit &&
                ((SplitComponent) baseComponent).getCaptionPosition() != SplitComponent.CAPTION_NONE) {

            Element captionProperties = (Element) properties.getElementsByTagName("caption_properties").item(0);

            String captionText = XMLUtils.getAttributeFromChildElement(captionProperties, "Text");
            getCaptionComponent().setText(captionText);

            String stringLocation = XMLUtils.getAttributeFromChildElement(captionProperties, "Divisor Location");
            if (!stringLocation.equals("")) {
                int divisorLocation = Integer.parseInt(stringLocation);
                SplitComponent ptf = (SplitComponent) baseComponent;
                ptf.setDividerLocation(divisorLocation);
            }

            setSize(getWidth(), getHeight());
        }
    }

    public void setBorderAndBackgroundProperties(final Element borderPropertiesElement) {
        JComponent component = getValueComponent();

        Element borderProperties = (Element) borderPropertiesElement.getElementsByTagName("borders").item(0);

        String borderStyle = XMLUtils.getAttributeFromChildElement(borderProperties, "Border Style");

        if (borderStyle.equals("None")) {
            component.setBorder(null);
        } else {
            String leftEdgeWidth = XMLUtils.getAttributeFromChildElement(borderProperties, "Border Width");
            String leftEdgeColor = XMLUtils.getAttributeFromChildElement(borderProperties, "Border Color");

            final Map<String, String> borderPropertiesMap = new HashMap<>();
            if (borderStyle.equals("Beveled")) {
                borderPropertiesMap.put("S", "/B");
            }
            if (borderStyle.equals("Solid")) {
                borderPropertiesMap.put("S", "/S");
            }
            if (borderStyle.equals("Dashed")) {
                borderPropertiesMap.put("S", "/D");
            }

            if (leftEdgeWidth != null && leftEdgeWidth.length() > 0) {
                borderPropertiesMap.put("W", leftEdgeWidth);
            }

            Color color = new Color(Integer.parseInt(leftEdgeColor));
            Border border = JPedalBorderFactory.createBorderStyle(borderPropertiesMap, color, color);

            component.setBorder(border);
        }

        Element backgroundFillProperties =
                (Element) borderPropertiesElement.getElementsByTagName("backgroundfill").item(0);

        String backgroundColor = XMLUtils.getAttributeFromChildElement(backgroundFillProperties, "Fill Color");
        component.setBackground(new Color(Integer.parseInt(backgroundColor)));

        setSize(getWidth(), getHeight());
    }
}
