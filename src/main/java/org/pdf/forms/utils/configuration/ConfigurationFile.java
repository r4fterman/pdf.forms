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

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public abstract class ConfigurationFile {

    private final String separator = System.getProperty("file.separator");
    private final String userDir = System.getProperty("user.dir");

    private final String configDir = userDir + separator + "configuration";
    private String configFile = configDir + separator;

    private Document doc;

    ConfigurationFile(final String fileName) {
        configFile += fileName;

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();

            boolean needNewFile = false;

            if (new File(configFile).exists()) {
                try {
                    doc = db.parse(new File(configFile));
                } catch (final Exception e) {
                    doc = db.newDocument();
                    needNewFile = true;
                    //<start-full><start-demo>
                    e.printStackTrace();
                    //<end-demo><end-full>
                }
            } else {
                new File(configDir).mkdirs();
                doc = db.newDocument();
                needNewFile = true;
            }

            if (needNewFile) {
                writeDefaultConfiguration();
                writeDoc();
            }
        } catch (final Exception e) {
            LogWriter.writeLog("Exception " + e + " generating menu configuration file " + configFile);
            e.printStackTrace();
        }
    }

    private void writeDoc() throws Exception {
        final InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        transformer.transform(new DOMSource(doc), new StreamResult(configFile));
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(final Document doc) {
        this.doc = doc;
    }

    protected abstract void writeDefaultConfiguration() throws Exception;
}
