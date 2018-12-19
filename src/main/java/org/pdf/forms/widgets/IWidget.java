/**
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
 * IWidget.java
 * ---------------
 */
package org.pdf.forms.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IWidget {

    int NONE = -1;
    int TEXT_FIELD = 0;
    int TEXT = 1;
    int BUTTON = 2;
    int RADIO_BUTTON = 3;
    int CHECK_BOX = 4;
    int COMBO_BOX = 5;
    int LIST_BOX = 6;
    int IMAGE = 7;
    int GROUP = 8;

    int COMPONENT_BOTH = 0;
    int COMPONENT_CAPTION = 1;
    int COMPONENT_VALUE = 2;

    int UNDERLINE_NONE = 0;
    int UNDERLINE_SINGLE = 1;
    int UNDERLINE_DOUBLE = 2;
    int UNDERLINE_WORD_SINGLE = 3;
    int UNDERLINE_WORD_DOUBLE = 4;

    int STYLE_PLAIN = 0;
    int STYLE_BOLD = 1;
    int STYLE_ITALIC = 2;
    int STYLE_BOLDITALIC = 3;

    int STRIKETHROUGH_OFF = 0;
    int STRIKETHROUGH_ON = 1;

    JComponent getWidget();

    void setPosition(int x, int y);

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setSize(int width, int height);

    String toString();

    int getResizeTypeForSplitComponent(int mouseX, int mouseY);

    boolean allowEditCaptionAndValue();

    boolean allowEditOfCaptionOnClick();

    Dimension getBoxSize();

    int getWidgetType();

    Rectangle getBounds();

    JComponent getValueComponent();

    PdfCaption getCaptionComponent();

    void setLastX(int lastX);

    void setLastY(int lastY);

    int getLastX();

    int getLastY();

    Point getAbsoluteLocationsOfCaption();

    Point getAbsoluteLocationsOfValue();

    boolean isComponentSplit();

    double getResizeHeightRatio();

    double getResizeWidthRatio();

    void setResizeHeightRatio(double resizeHeightRatio);

    void setResizeWidthRatio(double resizeWidthRation);

    double getResizeFromTopRatio();

    double getResizeFromLeftRatio();

    void setResizeFromTopRatio(double resizeHeightRatio);

    void setResizeFromLeftRatio(double resizeWidthRation);

    List<IWidget> getWidgetsInGroup();

    void setWidgetsInGroup(List<IWidget> widgetsInGroup);

    int getType();

    String getWidgetName();

    Icon getIcon();

    Document getProperties();

    void setObjectProperties(Element parentElement);

    void setBorderAndBackgroundProperties(Element borderProperties);

    void setParagraphProperties(Element parentElement, int currentlyEditing);

    void setLayoutProperties(Element parentElement);

    void setFontProperties(Element parentElement, int currentlyEditing);

    void setCaptionProperties(Element captionProperties);

    void setAllProperties();

    int getArrayNumber();
}
