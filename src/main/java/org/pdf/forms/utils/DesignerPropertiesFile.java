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
 * DesignerPropertiesFile.java
 * ---------------
 */
package org.pdf.forms.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * holds values stored in XML file on disk.
 */
public final class DesignerPropertiesFile extends PropertiesFile {

    private static DesignerPropertiesFile instance;

    private DesignerPropertiesFile(final String fileName) {
        super(fileName);
    }

    public static DesignerPropertiesFile getInstance() {
        if (instance == null) {
            // it's ok, we can call this constructor
            instance = new DesignerPropertiesFile(".properties.xml");
        }

        return instance;
    }

    public String[] getRecentDocuments(final String type) {
        String[] recentDocuments;

        try {
            final NodeList nl = getDoc().getElementsByTagName(type);
            final List<String> fileNames = new ArrayList<>();

            if (nl != null && nl.getLength() > 0) {
                final NodeList allRecentDocs = ((Element) nl.item(0)).getElementsByTagName("*");

                for (int i = 0; i < allRecentDocs.getLength(); i++) {
                    final Node item = allRecentDocs.item(i);
                    final NamedNodeMap attrs = item.getAttributes();
                    fileNames.add(attrs.getNamedItem("name").getNodeValue());
                }
            }

            //prune unwanted entries
            while (fileNames.size() > getNoOfRecentDocs()) {
                fileNames.remove(0);
            }

            Collections.reverse(fileNames);

            recentDocuments = fileNames.toArray(new String[getNoOfRecentDocs()]);
        } catch (final Exception e) {
            e.printStackTrace();
            LogWriter.writeLog("Exception " + e + " getting recent documents");
            return null;
        }

        return recentDocuments;
    }

    public void addRecentDocument(
            final String file,
            final String type) {
        try {
            final Element recentElement = (Element) getDoc().getElementsByTagName(type).item(0);

            checkExists(file, recentElement);

            final Element elementToAdd = getDoc().createElement("file");
            elementToAdd.setAttribute("name", file);

            recentElement.appendChild(elementToAdd);

            removeOldFiles(recentElement);

            writeDoc();
        } catch (final Exception e) {
            LogWriter.writeLog("Exception " + e + " adding recent document to properties file");
            e.printStackTrace();
        }
    }

    public void addCustomFont(
            final String name,
            final String path) {
        try {
            final Element customFontsElement = (Element) getDoc().getElementsByTagName("customfonts").item(0);

            checkExists(name, customFontsElement);

            final Element elementToAdd = getDoc().createElement("file");
            elementToAdd.setAttribute("name", name);
            elementToAdd.setAttribute("path", path);

            customFontsElement.appendChild(elementToAdd);

            writeDoc();
        } catch (final Exception e) {
            LogWriter.writeLog("Exception " + e + " adding custom font to properties file");
            e.printStackTrace();
        }
    }

    public Map<String, String> getCustomFonts() {
        final Map<String, String> customFonts = new HashMap<>();

        try {
            final NodeList nl = getDoc().getElementsByTagName("customfonts");

            if (nl != null && nl.getLength() > 0) {
                final NodeList allCustomFonts = ((Element) nl.item(0)).getElementsByTagName("*");

                for (int i = 0; i < allCustomFonts.getLength(); i++) {
                    final Node item = allCustomFonts.item(i);
                    final NamedNodeMap attrs = item.getAttributes();

                    final String name = attrs.getNamedItem("name").getNodeValue();
                    final String path = attrs.getNamedItem("path").getNodeValue();

                    customFonts.put(name, path);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            LogWriter.writeLog("Exception " + e + " getting custom fonts");
            return null;
        }

        return customFonts;
    }

    @Override
    public boolean checkAllElementsPresent() throws Exception {

        //assume true and set to false if wrong
        boolean hasAllElements = true;

        NodeList allElements = getDoc().getElementsByTagName("*");
        List<String> elementsInTree = new ArrayList<>(allElements.getLength());

        for (int i = 0; i < allElements.getLength(); i++) {
            elementsInTree.add(allElements.item(i).getNodeName());
        }

        Element propertiesElement = null;

        if (elementsInTree.contains("properties")) {
            propertiesElement = (Element) getDoc().getElementsByTagName("properties").item(0);
        } else {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();

            setDoc(db.newDocument());

            propertiesElement = getDoc().createElement("properties");
            getDoc().appendChild(propertiesElement);

            allElements = getDoc().getElementsByTagName("*");
            elementsInTree = new ArrayList<>(allElements.getLength());

            for (int i = 0; i < allElements.getLength(); i++) {
                elementsInTree.add(allElements.item(i).getNodeName());
            }

            hasAllElements = false;
        }

        if (!elementsInTree.contains("recentdesfiles")) {
            final Element recentDes = getDoc().createElement("recentdesfiles");
            propertiesElement.appendChild(recentDes);

            hasAllElements = false;
        }

        if (!elementsInTree.contains("recentpdffiles")) {
            final Element recentPDF = getDoc().createElement("recentpdffiles");
            propertiesElement.appendChild(recentPDF);

            hasAllElements = false;
        }

        if (!elementsInTree.contains("customfonts")) {
            final Element customFonts = getDoc().createElement("customfonts");
            propertiesElement.appendChild(customFonts);

            hasAllElements = false;
        }

        return hasAllElements;
    }
}
