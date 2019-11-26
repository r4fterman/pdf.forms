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
* XMLUtils.java
* ---------------
*/
package org.pdf.forms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {

    public static Optional<Element> getPropertyElement(
            final Element parentElement,
            final String property) {
        final NodeList properties = parentElement.getElementsByTagName("property");

        for (int i = 0; i < properties.getLength(); i++) {
            final Node node = properties.item(i);

            if (node instanceof Element) {
                final Element e = (Element) node;
                final String attr = e.getAttribute("name");

                if (attr.equals(property)) {
                    return Optional.of(e);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<String> getAttributeByIndex(
            final Element element,
            final int index) {
        return getAttribute(getElementsFromNodeList(element.getChildNodes()), index);
    }

    public static Optional<String> getAttributeFromChildElement(
            final Element element,
            final String name) {
        return getAttributeByName(getElementsFromNodeList(element.getChildNodes()), name);
    }

    public static Optional<String> getAttributeFromElement(
            final Element element,
            final String attributeName) {
        final NamedNodeMap attrs = element.getAttributes();

        final Node nameNode = attrs.getNamedItem("name");
        if (nameNode != null) {
            final String nodeValue = nameNode.getNodeValue();
            if (nodeValue != null) {
                if (nodeValue.equals(attributeName)) {
                    final Node namedItem = attrs.getNamedItem("value");
                    if (namedItem != null) {
                        return Optional.ofNullable(namedItem.getNodeValue());
                    }
                }
            }
        }

        return Optional.empty();
    }

    public static Element createAndAppendElement(
            final Document document,
            final String elementName,
            final Node parent) throws DOMException {
        final Element element = document.createElement(elementName);
        parent.appendChild(element);

        return element;
    }

    public static void addBasicProperty(
            final Document document,
            final String name,
            final String value,
            final Element element) {
        final Element property = document.createElement("property");
        element.appendChild(property);
        property.setAttribute("name", name);
        property.setAttribute("value", value);
    }

    public static List<Element> getElementsFromNodeList(final NodeList nodeList) {
        final List<Element> elements = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }

        return elements;
    }

    private static Optional<String> getAttribute(
            final List<Element> elements,
            final int index) {
        final Element element = elements.get(index);
        final NamedNodeMap attrs = element.getAttributes();

        final Node item = attrs.getNamedItem("value");
        if (item != null) {
            return Optional.ofNullable(item.getNodeValue());
        }

        return Optional.empty();
    }

    private static Optional<String> getAttributeByName(
            final List<Element> elements,
            final String attributeName) {
        for (final Element element : elements) {
            final Optional<String> value = getAttributeFromElement(element, attributeName);
            if (value.isPresent()) {
                return value;
            }
        }

        return Optional.empty();
    }
}
