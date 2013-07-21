/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
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
 * PropertiesFile.java
 * ---------------
 */
package org.pdf.forms.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * holds values stored in XML file on disk
 */
public abstract class PropertiesFile {

    protected String separator = System.getProperty("file.separator");
    protected String userDir = System.getProperty("user.dir");

    protected String configFile = userDir + separator;

    protected Document doc;

    protected int noOfRecentDocs = 6;

    public PropertiesFile(String fileName) {

        configFile += fileName;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            if (new File(configFile).exists()) {
                try {
                    doc = db.parse(new File(configFile));
                } catch (Exception e) {
                    doc = db.newDocument();
                    //<start-full><start-demo>
                    e.printStackTrace();
                    //<end-demo><end-full>
                }
            } else
                doc = db.newDocument();

            boolean hasAllElements = checkAllElementsPresent();

            //only write out if needed
            if (!hasAllElements)
                writeDoc();

        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " generating properties file");
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
        }
    }

    public abstract boolean checkAllElementsPresent() throws Exception;

    protected void writeDoc() throws Exception {
        InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        transformer.transform(new DOMSource(doc), new StreamResult(configFile));
    }

    protected void removeOldFiles(Element recentElement) throws Exception {
        NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        while (allRecentDocs.getLength() > noOfRecentDocs) {
            recentElement.removeChild(allRecentDocs.item(0));
        }
    }

    protected void checkExists(String file, Element recentElement) throws Exception {
        NodeList allRecentDocs = recentElement.getElementsByTagName("*");

        for (int i = 0; i < allRecentDocs.getLength(); i++) {
            Node item = allRecentDocs.item(i);
            NamedNodeMap attrs = item.getAttributes();
            String value = attrs.getNamedItem("name").getNodeValue();

            if (value.equals(file))
                recentElement.removeChild(item);
        }
    }

    public String getValue(String elementName) {
        NamedNodeMap attrs = null;
        try {
            NodeList nl = doc.getElementsByTagName(elementName);
            Element element = (Element) nl.item(0);
            if (element == null)
                return "";
            attrs = element.getAttributes();

        } catch (Exception e) {
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
            LogWriter.writeLog("Exception " + e + " generating properties file");
            return "";
        }

        return attrs.getNamedItem("value").getNodeValue();
    }

    /* (non-Javadoc)
     * @see org.jpedal.examples.simpleviewer.utils.PropertiesFile#setValue(java.lang.String, java.lang.String)
     */
    public void setValue(String elementName, String newValue) {
        try {
            NodeList nl = doc.getElementsByTagName(elementName);
            Element element = (Element) nl.item(0);
            element.setAttribute("value", newValue);

            writeDoc();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " setting value in properties file");
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
        }
    }

    public int getNoRecentDocumentsToDisplay() {
        return this.noOfRecentDocs;
    }
}
