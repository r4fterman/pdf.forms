package org.pdf.forms.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XMLUtils {

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String ELEMENT_PROPERTY = "property";

    private XMLUtils() {
        // do nothing
    }

    public static Optional<Element> getPropertyElement(
            final Element parentElement,
            final String property) {
        final NodeList properties = parentElement.getElementsByTagName(ELEMENT_PROPERTY);
        for (int i = 0; i < properties.getLength(); i++) {
            final Node node = properties.item(i);
            if (node instanceof Element) {
                final Element element = (Element) node;
                final String name = element.getAttribute(ATTRIBUTE_NAME);
                if (name.equals(property)) {
                    return Optional.of(element);
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
        final NamedNodeMap attributes = element.getAttributes();
        final Node nameNode = attributes.getNamedItem(ATTRIBUTE_NAME);
        if (nameNode == null) {
            return Optional.empty();
        }

        final String nodeValue = nameNode.getNodeValue();
        if (nodeValue != null && nodeValue.equals(attributeName)) {
            final Node namedItem = attributes.getNamedItem(ATTRIBUTE_VALUE);
            if (namedItem != null) {
                return Optional.ofNullable(namedItem.getNodeValue());
            }
        }

        return Optional.empty();
    }

    public static Element createAndAppendElement(
            final Document document,
            final String elementName,
            final Node parent) {
        final Element element = document.createElement(elementName);
        parent.appendChild(element);
        return element;
    }

    public static void addBasicProperty(
            final Document document,
            final String name,
            final String value,
            final Element element) {
        final Element property = document.createElement(ELEMENT_PROPERTY);
        element.appendChild(property);
        property.setAttribute(ATTRIBUTE_NAME, name);
        property.setAttribute(ATTRIBUTE_VALUE, value);
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

        final Node item = attrs.getNamedItem(ATTRIBUTE_VALUE);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(item.getNodeValue());

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
        return createDocumentBuilder().parse(new ByteArrayInputStream(xmlContent.getBytes(UTF_8)));
    }

    public static Document readDocument(final File file) throws ParserConfigurationException, IOException, SAXException {
        return createDocumentBuilder().parse(file);
    }

    public static Document readDocumentFromUri(final String uri) throws ParserConfigurationException, IOException, SAXException {
        return createDocumentBuilder().parse(uri);
    }

    public static Document createNewDocument() throws ParserConfigurationException {
        return createDocumentBuilder().newDocument();
    }

    private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        return FACTORY.newDocumentBuilder();
    }
}
