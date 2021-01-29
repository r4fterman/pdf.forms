package org.pdf.forms.gui;

import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class FileLinkParser {

    private final Logger logger = LoggerFactory.getLogger(FileLinkParser.class);

    private String textData;

    void parse(final String textData) {
        this.textData = textData;
    }

    Optional<String> getFileReference() {
        try {
            final Document doc = XMLUtils.readDocument(textData);
            final Element a = (Element) doc.getElementsByTagName("a").item(0);
            return getHrefAttribute(a);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            final String message = String.format("Unable to parse data [%s].", textData);
            logger.warn(message, e);
        }
        return Optional.empty();
    }

    /**
     * Returns the URL held in the href attribute from an element.
     *
     * @param element the element containing the href attribute
     * @return the URL held in the href attribute
     */
    private Optional<String> getHrefAttribute(final Element element) {
        final NamedNodeMap elementAttributes = element.getAttributes();
        return Optional.ofNullable(elementAttributes.getNamedItem("href"))
                .map(Node::getNodeValue);
    }
}
