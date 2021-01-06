package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.pdf.forms.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class ConfigurationFile {

    private final Logger logger = LoggerFactory.getLogger(ConfigurationFile.class);

    private final File configFile;
    private final Document document;

    ConfigurationFile(
            final File configFile,
            final File directory) {
        this.configFile = configFile;
        this.document = createConfigurationDocument(configFile, directory);
    }

    private Document createConfigurationDocument(
            final File configFile,
            final File directory) {
        try {
            if (configFile.exists() && configFile.canRead()) {
                return XMLUtils.readDocument(configFile);
            }

            return createNewConfigurationFile(directory);
        } catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
            logger.error("Unable to build configuration file.", e);
            throw new RuntimeException("Unable to build configuration file.", e);
        }
    }

    private Document createNewConfigurationFile(final File directory) throws ParserConfigurationException, TransformerException {
        final File configDir = new File(directory, "configuration");
        configDir.mkdirs();

        final Document configDocument = XMLUtils.createNewDocument();
        writeToDefaultConfiguration(configDocument);
        writeContentToDocument(configDocument);
        return configDocument;
    }

    private void writeContentToDocument(final Document document) throws TransformerException {
        final InputStream stylesheet = getClass().getResourceAsStream(
                "/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        transformer.transform(new DOMSource(document), new StreamResult(configFile));
    }

    Document getDocument() {
        return document;
    }

    protected abstract void writeToDefaultConfiguration(Document document);
}
