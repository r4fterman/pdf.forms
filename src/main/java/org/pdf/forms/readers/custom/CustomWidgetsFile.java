package org.pdf.forms.readers.custom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * holds values stored in XML file on disk.
 */
public final class CustomWidgetsFile {

    private static final int NO_OF_RECENT_DOCS = 6;

    public static CustomWidgetsFile getInstance(final File directory) {
        return new CustomWidgetsFile(new File(directory, ".custom_components.xml"));
    }

    private final Logger logger = LoggerFactory.getLogger(CustomWidgetsFile.class);
    private final File configFile;
    private Document doc;

    private CustomWidgetsFile(final File configFile) {
        this.configFile = configFile;
        try {
            this.doc = readConfigFile(configFile);

            final boolean modelUpdated = checkForModelUpdate(this.doc);
            if (modelUpdated) {
                //only write out if needed
                writeDoc();
            }
        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("Error generating properties file {}", configFile.getAbsolutePath(), e);
        }
    }

    public boolean checkForModelUpdate(final Document document) {
        boolean modelUpdated = false;

        if (isElementMissing(document, "custom_components")) {
            createNewModelInstance(document);
            updateElementInModel(document, "custom_components");
            modelUpdated = true;
        }

        return modelUpdated;
    }

    public boolean isNameTaken(final String name) {
        return XMLUtils.getElementsFromNodeList(getDocument().getElementsByTagName("custom_component")).stream()
                .map(element -> XMLUtils.getAttributeValueFromChildElement(element, "name"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(text -> text.equals(name));
    }

    public void addCustomWidget(
            final String name,
            final Set<IWidget> selectedWidgets) {

        final Element customComponentElement = XMLUtils.createAndAppendElement(getDocument(),
                "custom_component",
                getDocument().getDocumentElement());
        XMLUtils.addBasicProperty(getDocument(), "name", name, customComponentElement);

        for (final IWidget widget: selectedWidgets) {
            final Document widgetProperties = widget.getProperties();
            final Element widgetRoot = widgetProperties.getDocumentElement();
            customComponentElement.appendChild(getDocument().importNode(widgetRoot, true));
        }

        try {
            writeDoc();
        } catch (final TransformerException e) {
            logger.error("Error writing custom widget file", e);
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

        } catch (final Exception e) {
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
        } catch (final Exception e) {
            logger.error("Error setting value in properties file", e);
        }
    }

    public int getNumberRecentDocumentsToDisplay() {
        return NO_OF_RECENT_DOCS;
    }

    public Document getDocument() {
        return doc;
    }

    int getNumberOfRecentDocuments() {
        return NO_OF_RECENT_DOCS;
    }

    void updateElementInModel(
            final Document document,
            final String tagName) {
        final Element propertiesElement;
        final NodeList elements = document.getElementsByTagName("properties");
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
