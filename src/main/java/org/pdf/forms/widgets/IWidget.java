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

    public static final int NONE = -1;
    public static final int TEXT_FIELD = 0;
    public static final int TEXT = 1;
    public static final int BUTTON = 2;
    public static final int RADIO_BUTTON = 3;
    public static final int CHECK_BOX = 4;
    public static final int COMBO_BOX = 5;
    public static final int LIST_BOX = 6;
    public static final int IMAGE = 7;
    public static final int GROUP = 8;

    public static final int COMPONENT_BOTH = 0;
    public static final int COMPONENT_CAPTION = 1;
    public static final int COMPONENT_VALUE = 2;

    public static final int UNDERLINE_NONE = 0;
    public static final int UNDERLINE_SINGLE = 1;
    public static final int UNDERLINE_DOUBLE = 2;
    public static final int UNDERLINE_WORD_SINGLE = 3;
    public static final int UNDERLINE_WORD_DOUBLE = 4;

    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_BOLDITALIC = 3;

    public static final int STRIKETHROUGH_OFF = 0;
    public static final int STRIKETHROUGH_ON = 1;

    public JComponent getWidget();

    public void setPosition(int x, int y);

    public void setX(int x);

    public void setY(int y);

    public int getX();

    public int getY();

    public int getWidth();

    public int getHeight();

    public void setSize(int width, int height);

    public String toString();

    public int getResizeTypeForSplitComponent(int mouseX, int mouseY);

    public boolean allowEditCaptionAndValue();

    public boolean allowEditOfCaptionOnClick();

    public Dimension getBoxSize();

    public int getWidgetType();

    public Rectangle getBounds();

    public JComponent getValueComponent();

    public PdfCaption getCaptionComponent();

    public void setLastX(int lastX);

    public void setLastY(int lastY);

    public int getLastX();

    public int getLastY();

    public Point getAbsoluteLocationsOfCaption();

    public Point getAbsoluteLocationsOfValue();

    public boolean isComponentSplit();

    public double getResizeHeightRatio();

    public double getResizeWidthRatio();

    public void setResizeHeightRatio(double resizeHeightRatio);

    public void setResizeWidthRatio(double resizeWidthRation);

    public double getResizeFromTopRatio();

    public double getResizeFromLeftRatio();

    public void setResizeFromTopRatio(double resizeHeightRatio);

    public void setResizeFromLeftRatio(double resizeWidthRation);

    public List getWidgetsInGroup();

    public void setWidgetsInGroup(List widgetsInGroup);

    public int getType();

    public String getWidgetName();

    public Icon getIcon();

    public Document getProperties();

    public void setObjectProperties(Element parentElement);

    public void setBorderAndBackgroundProperties(Element borderProperties);

    public void setParagraphProperties(Element parentElement, int currentlyEditing);

    public void setLayoutProperties(Element parentElement);

    public void setFontProperties(Element parentElement, int currentlyEditing);

    public void setCaptionProperties(Element captionProperties);

    public void setAllProperties();

    public int getArrayNumber();
}