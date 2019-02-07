package org.pdf.forms.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.gui.commands.RecentDocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DesignerPropertiesFile extends PropertiesFile {

    private static DesignerPropertiesFile instance;

    private final Logger logger = LoggerFactory.getLogger(DesignerPropertiesFile.class);

    private DesignerPropertiesFile(final File designerPropertiesFile) {
        super(designerPropertiesFile);
    }

    public static DesignerPropertiesFile getInstance(final File configDir) {
        if (instance == null) {
            final File configFile = new File(configDir, ".properties.xml");

            instance = new DesignerPropertiesFile(configFile);
        }

        return instance;
    }

    public String[] getRecentDocuments(final String type) {
        final String[] recentDocuments;

        try {
            final NodeList nl = getDoc().getElementsByTagName(type);
            final List<String> fileNames = new ArrayList<>();

            if (nl != null && nl.getLength() > 0) {
                final NodeList allRecentDocs = ((Element) nl.item(0)).getElementsByTagName("*");

                for (int i = 0; i < allRecentDocs.getLength(); i++) {
                    final Node item = allRecentDocs.item(i);
                    final NamedNodeMap attrs = item.getAttributes();
                    final String name = attrs.getNamedItem("name").getNodeValue();
                    fileNames.add(name);
                }
            }

            //prune unwanted entries
            while (fileNames.size() > getNoOfRecentDocs()) {
                fileNames.remove(0);
            }

            Collections.reverse(fileNames);

            recentDocuments = fileNames.toArray(new String[getNoOfRecentDocs()]);
            return recentDocuments;
        } catch (final Exception e) {
            logger.error("Error getting recent documents", e);
            return null;
        }
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
            logger.error("Error adding recent document to properties file", e);
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
            logger.error("Error adding custom font to properties file", e);
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
            return customFonts;
        } catch (final Exception e) {
            logger.error("Exception " + e + " getting custom fonts");
            return Collections.emptyMap();
        }

    }

    @Override
    public boolean checkAllElementsPresent() throws ParserConfigurationException {

        //assume true and set to false if wrong
        boolean hasAllElements = true;

        NodeList allElements = getDoc().getElementsByTagName("*");
        List<String> elementsInTree = new ArrayList<>(allElements.getLength());

        for (int i = 0; i < allElements.getLength(); i++) {
            elementsInTree.add(allElements.item(i).getNodeName());
        }

        final Element propertiesElement;
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

        if (!elementsInTree.contains(RecentDocumentType.RECENT_DES_FILES.getValue())) {
            final Element recentDes = getDoc().createElement("recentdesfiles");
            propertiesElement.appendChild(recentDes);

            hasAllElements = false;
        }

        if (!elementsInTree.contains(RecentDocumentType.RECENT_PDF_FILES.getValue())) {
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

    @Override
    void destroy() {
        instance = null;
    }
}
