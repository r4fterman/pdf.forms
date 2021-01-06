package org.pdf.forms.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class PropertiesFile {

    private static final int NO_OF_RECENT_DOCS = 6;

    private final Logger logger = LoggerFactory.getLogger(PropertiesFile.class);
    private final File configFile;

    private Document doc;

    PropertiesFile(final File configFile) {
        this.configFile = configFile;
        try {
            this.doc = readConfigFile(configFile);

            final boolean modelUpdated = checkForModelUpdate(doc);
            if (modelUpdated) {
                //only write out if needed
                writeDoc();
            }
        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("Error generating properties file {}", configFile.getAbsolutePath(), e);
        }
    }

    private Document readConfigFile(final File configFile) throws ParserConfigurationException {
        if (configFile.exists() && configFile.canRead()) {
            try {
                return XMLUtils.readDocument(configFile);
            } catch (SAXException | IOException e) {
                logger.error("Error parsing properties file {}", configFile.getAbsolutePath(), e);
            }
        }
        return XMLUtils.createNewDocument();
    }

    public abstract boolean checkForModelUpdate(Document document);

    void writeDoc() throws TransformerException {
        final InputStream stylesheet = getClass().getResourceAsStream(
                "/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));

        transformer.transform(new DOMSource(doc), new StreamResult(configFile));
    }

    void removeOldFiles(final Element recentElement) {
        final NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        while (allRecentDocs.getLength() > NO_OF_RECENT_DOCS) {
            recentElement.removeChild(allRecentDocs.item(0));
        }
    }

    void checkExists(
            final String file,
            final Element recentElement) {
        final NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        for (int i = 0; i < allRecentDocs.getLength(); i++) {
            final Node item = allRecentDocs.item(i);
            final NamedNodeMap attrs = item.getAttributes();
            final String value = attrs.getNamedItem("name").getNodeValue();

            if (value.equals(file)) {
                recentElement.removeChild(item);
            }
        }
    }

    public String getValue(final String elementName) {
        final NamedNodeMap attrs;
        try {
            final NodeList nl = doc.getElementsByTagName(elementName);
            final Element element = (Element) nl.item(0);
            if (element == null) {
                return "";
            }
            attrs = element.getAttributes();

        } catch (Exception e) {
            logger.error("Error generating properties file", e);
            return "";
        }

        return attrs.getNamedItem("value").getNodeValue();
    }

    public void setValue(
            final String elementName,
            final String newValue) {
        try {
            final NodeList nl = doc.getElementsByTagName(elementName);
            final Element element = (Element) nl.item(0);
            element.setAttribute("value", newValue);

            writeDoc();
        } catch (Exception e) {
            logger.error("Error setting value in properties file", e);
        }
    }

    public int getNumberRecentDocumentsToDisplay() {
        return NO_OF_RECENT_DOCS;
    }

    Document getDocument() {
        return doc;
    }

    int getNumberOfRecentDocuments() {
        return NO_OF_RECENT_DOCS;
    }

    void updateElementInModel(
            final Document document,
            final String tagName) {
        Element propertiesElement;
        NodeList elements = document.getElementsByTagName("properties");
        if (elements.getLength() == 0) {
            propertiesElement = addPropertiesElement(document);
        } else {
            propertiesElement = (Element) elements.item(0);
        }

        final Element element = document.createElement(tagName);
        propertiesElement.appendChild(element);
    }

    void createNewModelInstance(final Document document) {
        addPropertiesElement(document);
    }

    private Element addPropertiesElement(final Document document) {
        final Element propertiesElement = document.createElement("properties");
        document.appendChild(propertiesElement);
        return propertiesElement;
    }

    List<String> getElementNamesUnderRoot(final Document document) {
        final NodeList elements = document.getElementsByTagName("*");
        final List<String> elementsNames = new ArrayList<>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            elementsNames.add(elements.item(i).getNodeName());
        }
        return elementsNames;
    }

    boolean isElementMissing(
            final Document document,
            final String elementName) {
        return !getElementNamesUnderRoot(document).contains(elementName);
    }
}
