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
 * WidgetArrays.java
 * ---------------
 */
package org.pdf.forms.widgets.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class WidgetArrays {

    private Map widgetArrays = new HashMap();

    public boolean isWidgetArrayInList(String name) {
        return widgetArrays.containsKey(name);
    }

    public void addWidgetToArray(String name, IWidget widget) {
        List array = (List) widgetArrays.get(name);

        if (array == null) {
            array = new ArrayList();
            widgetArrays.put(name, array);
        }

        array.add(widget);
    }

    public void removeWidgetFromArray(IWidget widget, String name, IMainFrame mainFrame) {

        List array = (List) widgetArrays.get(name);
        if (array == null)
            return;

        array.remove(widget);

        if (array.size() == 1)
            widgetArrays.remove(name);
        else {
            for (int i = 0; i < array.size(); i++) {
                IWidget w = (IWidget) array.get(i);
                Element objectElement = (Element) w.getProperties().getElementsByTagName("object").item(0);
                Element biningElement = (Element) objectElement.getElementsByTagName("binding").item(0);

                Element arrayNumberElement = XMLUtils.getPropertyElement(biningElement, "Array Number");
                arrayNumberElement.getAttributeNode("value").setValue(i + "");

                w.setObjectProperties(objectElement);

                mainFrame.updateHierarchyPanelUI();
            }
        }
    }

    public int getNextArrayIndex(String name) {
        List array = (List) widgetArrays.get(name);
        return array.size() - 1;
    }
}
