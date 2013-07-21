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
 * DesignerPropertiesFile.java
 * ---------------
 */
package org.pdf.forms.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * holds values stored in XML file on disk
 */
public class DesignerPropertiesFile extends PropertiesFile {

    private static DesignerPropertiesFile instance;

    private DesignerPropertiesFile(String fileName) {
        super(fileName);
    }

    public static DesignerPropertiesFile getInstance() {
        if (instance == null)
            // it's ok, we can call this constructor
            instance = new DesignerPropertiesFile(".properties.xml");

        return instance;
    }

    public String[] getRecentDocuments(String type) {
        String[] recentDocuments = new String[noOfRecentDocs];

        try {
            NodeList nl = doc.getElementsByTagName(type);
            List fileNames = new ArrayList();

            if (nl != null && nl.getLength() > 0) {
                NodeList allRecentDocs = ((Element) nl.item(0)).getElementsByTagName("*");

                for (int i = 0; i < allRecentDocs.getLength(); i++) {
                    Node item = allRecentDocs.item(i);
                    NamedNodeMap attrs = item.getAttributes();
                    fileNames.add(attrs.getNamedItem("name").getNodeValue());
                }
            }

            //prune unwanted entries
            while (fileNames.size() > noOfRecentDocs) {
                fileNames.remove(0);
            }

            Collections.reverse(fileNames);

            recentDocuments = (String[]) fileNames.toArray(new String[noOfRecentDocs]);
        } catch (Exception e) {
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
            LogWriter.writeLog("Exception " + e + " getting recent documents");
            return null;
        }

        return recentDocuments;
    }

    public void addRecentDocument(String file, String type) {
        try {
            Element recentElement = (Element) doc.getElementsByTagName(type).item(0);

            checkExists(file, recentElement);

            Element elementToAdd = doc.createElement("file");
            elementToAdd.setAttribute("name", file);

            recentElement.appendChild(elementToAdd);

            removeOldFiles(recentElement);

            writeDoc();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " adding recent document to properties file");
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
        }
    }

    public void addCustomFont(String name, String path) {
        try {
            Element customFontsElement = (Element) doc.getElementsByTagName("customfonts").item(0);

            checkExists(name, customFontsElement);

            Element elementToAdd = doc.createElement("file");
            elementToAdd.setAttribute("name", name);
            elementToAdd.setAttribute("path", path);

            customFontsElement.appendChild(elementToAdd);

            writeDoc();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " adding custom font to properties file");
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
        }
    }

    public Map getCustomFonts() {
        Map customFonts = new HashMap();

        try {
            NodeList nl = doc.getElementsByTagName("customfonts");

            if (nl != null && nl.getLength() > 0) {
                NodeList allCustomFonts = ((Element) nl.item(0)).getElementsByTagName("*");

                for (int i = 0; i < allCustomFonts.getLength(); i++) {
                    Node item = allCustomFonts.item(i);
                    NamedNodeMap attrs = item.getAttributes();

                    String name = attrs.getNamedItem("name").getNodeValue();
                    String path = attrs.getNamedItem("path").getNodeValue();

                    customFonts.put(name, path);
                }
            }
        } catch (Exception e) {
            //<start-full><start-demo>
            e.printStackTrace();
            //<end-demo><end-full>
            LogWriter.writeLog("Exception " + e + " getting custom fonts");
            return null;
        }

        return customFonts;
    }

    public boolean checkAllElementsPresent() throws Exception {

        //assume true and set to false if wrong
        boolean hasAllElements = true;

        NodeList allElements = doc.getElementsByTagName("*");
        List elementsInTree = new ArrayList(allElements.getLength());

        for (int i = 0; i < allElements.getLength(); i++)
            elementsInTree.add(allElements.item(i).getNodeName());

        Element propertiesElement = null;

        if (elementsInTree.contains("properties")) {
            propertiesElement = (Element) doc.getElementsByTagName("properties").item(0);
        } else {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.newDocument();

            propertiesElement = doc.createElement("properties");
            doc.appendChild(propertiesElement);

            allElements = doc.getElementsByTagName("*");
            elementsInTree = new ArrayList(allElements.getLength());

            for (int i = 0; i < allElements.getLength(); i++)
                elementsInTree.add(allElements.item(i).getNodeName());

            hasAllElements = false;
        }

        if (!elementsInTree.contains("recentdesfiles")) {
            Element recentDes = doc.createElement("recentdesfiles");
            propertiesElement.appendChild(recentDes);

            hasAllElements = false;
        }

        if (!elementsInTree.contains("recentpdffiles")) {
            Element recentPDF = doc.createElement("recentpdffiles");
            propertiesElement.appendChild(recentPDF);

            hasAllElements = false;
        }

        if (!elementsInTree.contains("customfonts")) {
            Element customFonts = doc.createElement("customfonts");
            propertiesElement.appendChild(customFonts);

            hasAllElements = false;
        }

        return hasAllElements;
    }
}
