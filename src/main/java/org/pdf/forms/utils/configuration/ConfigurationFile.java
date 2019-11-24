package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public abstract class ConfigurationFile {

    private final Logger logger = LoggerFactory.getLogger(ConfigurationFile.class);

    private final File configFile;

    private Document doc;

    ConfigurationFile(final File configFile) {
        this.configFile = configFile;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();

            boolean needNewFile = false;

            if (configFile.exists() && configFile.canRead()) {
                try {
                    doc = db.parse(configFile);
                } catch (final Exception e) {
                    logger.error("Error parsing config file {}", configFile.getAbsolutePath(), e);
                    doc = db.newDocument();
                    needNewFile = true;
                }
            } else {
                final String userDir = System.getProperty("user.dir");
                final File configDir = new File(userDir, "configuration");

                configDir.mkdirs();
                doc = db.newDocument();
                needNewFile = true;
            }

            if (needNewFile) {
                writeDefaultConfiguration();
                writeDoc();
            }
        } catch (final Exception e) {
            logger.error("Error generating menu configuration file {}", configFile.getAbsolutePath(), e);
        }
    }

    private void writeDoc() throws Exception {
        final InputStream stylesheet = getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        transformer.transform(new DOMSource(doc), new StreamResult(configFile));
    }

    Document getDoc() {
        return doc;
    }

    void setDoc(final Document doc) {
        this.doc = doc;
    }

    protected abstract void writeDefaultConfiguration() throws Exception;
}
