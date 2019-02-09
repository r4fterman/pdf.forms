/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * PropertiesFile.java
 * ---------------
 */
package org.pdf.forms.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class PropertiesFile {

    private final Logger logger = LoggerFactory.getLogger(PropertiesFile.class);

    private final File configFile;

    private Document doc;

    private final int noOfRecentDocs = 6;

    PropertiesFile(final File configFile) {
        this.configFile = configFile;
        try {
            doc = readConfigFile(configFile);

            final boolean hasAllElements = checkAllElementsPresent();
            //only write out if needed
            if (!hasAllElements) {
                writeDoc();
            }
        } catch (final DOMException | ParserConfigurationException | TransformerException e) {
            logger.error("Error generating properties file " + configFile.getAbsolutePath(), e);
        }
    }

    private Document readConfigFile(final File configFile) throws ParserConfigurationException {
        final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        if (configFile.exists() && configFile.canRead()) {
            try {
                return documentBuilder.parse(configFile);
            } catch (final SAXException | IOException e) {
                logger.error("Error parsing properties file " + configFile.getAbsolutePath(), e);
            }
        }
        return documentBuilder.newDocument();
    }

    public abstract boolean checkAllElementsPresent() throws DOMException, ParserConfigurationException;

    void writeDoc() throws TransformerException {
        final InputStream stylesheet = getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));

        transformer.transform(new DOMSource(doc), new StreamResult(configFile));
    }

    void removeOldFiles(final Element recentElement) {
        final NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        while (allRecentDocs.getLength() > noOfRecentDocs) {
            recentElement.removeChild(allRecentDocs.item(0));
        }
    }

    void checkExists(
            final String filePath,
            final Element recentElement) {
        final NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        for (int i = 0; i < allRecentDocs.getLength(); i++) {
            final Node item = allRecentDocs.item(i);
            final NamedNodeMap attrs = item.getAttributes();
            final String value = attrs.getNamedItem("name").getNodeValue();

            if (value.equals(filePath)) {
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

    public int getNoRecentDocumentsToDisplay() {
        return noOfRecentDocs;
    }

    Document getDoc() {
        return doc;
    }

    void setDoc(final Document doc) {
        this.doc = doc;
    }

    int getNoOfRecentDocs() {
        return noOfRecentDocs;
    }

    abstract void destroy();
}
