package org.pdf.forms.utils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DesignerPropertiesFile extends PropertiesFile {

    private static final String RECENT_DESIGNER_FILES = "recentdesfiles";
    private static final String RECENT_PDF_FILES = "recentpdffiles";
    private static final String CUSTOM_FONTS = "customfonts";

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

    public String[] getRecentDesignerDocuments() {
        return getRecentDocuments(RECENT_DESIGNER_FILES);
    }

    public String[] getRecentPDFDocuments() {
        return getRecentDocuments(RECENT_PDF_FILES);
    }

    private String[] getRecentDocuments(final String type) {
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
            return new String[0];
        }
    }

    public void addRecentDesignerDocument(final String file) {
        addRecentDocument(file, RECENT_DESIGNER_FILES);
    }

    public void addRecentPDFDocument(final String file) {
        addRecentDocument(file, RECENT_PDF_FILES);
    }

    private void addRecentDocument(
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
            final Element customFontsElement = (Element) getDoc().getElementsByTagName(CUSTOM_FONTS).item(0);

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
            final NodeList nl = getDoc().getElementsByTagName(CUSTOM_FONTS);

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
            logger.error("Error getting custom fonts", e);
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
            final Document doc = XMLUtils.createNewDocument();
            setDoc(doc);

            propertiesElement = getDoc().createElement("properties");
            getDoc().appendChild(propertiesElement);

            allElements = getDoc().getElementsByTagName("*");
            elementsInTree = new ArrayList<>(allElements.getLength());

            for (int i = 0; i < allElements.getLength(); i++) {
                elementsInTree.add(allElements.item(i).getNodeName());
            }

            hasAllElements = false;
        }

        if (!elementsInTree.contains(RECENT_DESIGNER_FILES)) {
            final Element recentDes = getDoc().createElement(RECENT_DESIGNER_FILES);
            propertiesElement.appendChild(recentDes);

            hasAllElements = false;
        }

        if (!elementsInTree.contains(RECENT_PDF_FILES)) {
            final Element recentPDF = getDoc().createElement(RECENT_PDF_FILES);
            propertiesElement.appendChild(recentPDF);

            hasAllElements = false;
        }

        if (!elementsInTree.contains(CUSTOM_FONTS)) {
            final Element customFonts = getDoc().createElement(CUSTOM_FONTS);
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
