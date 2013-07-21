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
 * XMLUtils.java
 * ---------------
 */
package org.pdf.forms.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {
    public static Element getPropertyElement(Element parentElement, String property) {
        NodeList properties = parentElement.getElementsByTagName("property");

        for (int i = 0; i < properties.getLength(); i++) {
            Node node = properties.item(i);

            if (node instanceof Element) {
                Element e = (Element) node;
                String attr = e.getAttribute("name");

                if (attr.equals(property))
                    return e;
            }
        }

        return null;
    }

    public static String getAttributeByIndex(Element element, int index) {
        return getAttribute(getElementsFromNodeList(element.getChildNodes()), index);
    }

    public static String getAttributeFromChildElement(Element element, String name) {
        return getAttributeByName(getElementsFromNodeList(element.getChildNodes()), name);
    }

    public static String getAttributeFromElement(Element element, String attributeName) {
        NamedNodeMap attrs = element.getAttributes();

        Node nameNode = attrs.getNamedItem("name");
        if (nameNode != null) {
            String nodeValue = nameNode.getNodeValue();
            if (nodeValue != null) {
                if (nodeValue.equals(attributeName))
                    return attrs.getNamedItem("value").getNodeValue();
            }
        }

        return null;
    }

    public static Element createAndAppendElement(Document document, String elementName, Node parent) throws DOMException {
        Element element = document.createElement(elementName);
        parent.appendChild(element);

        return element;
    }

    public static void addBasicProperty(Document document, String value1, String value2, Element element) {
        Element property = document.createElement("property");
        element.appendChild(property);
        property.setAttribute("name", value1);
        property.setAttribute("value", value2);

        //return property;
    }

    public static List getElementsFromNodeList(NodeList nodeList) {
        List elements = new ArrayList();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                elements.add(node);
            }
        }

        return elements;
    }

    private static String getAttribute(List list, int index) {
        Element element = (Element) list.get(index);
        NamedNodeMap attrs = element.getAttributes();

        Node item = attrs.getNamedItem("value");
        return item.getNodeValue();
    }

    private static String getAttributeByName(List list, String attributeName) {
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();

            String value = getAttributeFromElement(element, attributeName);
            if (value != null)
                return value;
        }

        return null;
    }
}
