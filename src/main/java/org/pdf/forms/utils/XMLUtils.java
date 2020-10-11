package org.pdf.forms.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils {

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

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

    public static Document readDocument(final String xmlContent) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
    }

    public static Document readDocument(final File file) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(file);
    }

    public static Document readDocumentFromUri(final String uri) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(uri);
    }

    public static Document createNewDocument() throws ParserConfigurationException {
        final DocumentBuilder builder = createDocumentBuilder();
        return builder.newDocument();
    }

    private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        return FACTORY.newDocumentBuilder();
    }
}
