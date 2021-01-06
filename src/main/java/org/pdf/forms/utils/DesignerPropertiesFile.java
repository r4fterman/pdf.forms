package org.pdf.forms.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final Logger logger = LoggerFactory.getLogger(DesignerPropertiesFile.class);

    public DesignerPropertiesFile(final File directory) {
        super(new File(directory, ".properties.xml"));
    }

    public String[] getRecentDesignerDocuments() {
        return getRecentDocuments(RECENT_DESIGNER_FILES);
    }

    public String[] getRecentPDFDocuments() {
        return getRecentDocuments(RECENT_PDF_FILES);
    }

    private String[] getRecentDocuments(final String type) {
        try {
            final List<String> fileNames = getFileNames(type);

            //prune unwanted entries
            while (fileNames.size() > getNumberOfRecentDocuments()) {
                fileNames.remove(0);
            }

            Collections.reverse(fileNames);

            return fileNames.toArray(new String[getNumberOfRecentDocuments()]);
        } catch (Exception e) {
            logger.error("Error getting recent documents", e);
            return new String[0];
        }
    }

    private List<String> getFileNames(final String type) {
        final NodeList nodeList = getDocument().getElementsByTagName(type);
        if (nodeList == null || nodeList.getLength() == 0) {
            return Collections.emptyList();

        }

        final List<String> fileNames = new ArrayList<>();
        final NodeList allRecentDocs = ((Element) nodeList.item(0)).getElementsByTagName("*");
        for (int i = 0; i < allRecentDocs.getLength(); i++) {
            final Node item = allRecentDocs.item(i);
            final NamedNodeMap attrs = item.getAttributes();
            final String name = attrs.getNamedItem("name").getNodeValue();
            fileNames.add(name);
        }
        return fileNames;
    }

    public void addRecentDesignerDocument(final String file) {
        addRecentDocument(getDocument(), file, RECENT_DESIGNER_FILES);
    }

    public void addRecentPDFDocument(final String file) {
        addRecentDocument(getDocument(), file, RECENT_PDF_FILES);
    }

    private void addRecentDocument(
            final Document document,
            final String file,
            final String type) {
        try {
            final Element recentElement = (Element) document.getElementsByTagName(type).item(0);

            checkExists(file, recentElement);

            final Element elementToAdd = document.createElement("file");
            elementToAdd.setAttribute("name", file);

            recentElement.appendChild(elementToAdd);

            removeOldFiles(recentElement);

            writeDoc();
        } catch (Exception e) {
            logger.error("Error adding recent document to properties file", e);
        }
    }

    public void addCustomFont(
            final String name,
            final String path) {
        try {
            final Element customFontsElement = (Element) getDocument().getElementsByTagName(CUSTOM_FONTS).item(0);

            checkExists(name, customFontsElement);

            final Element elementToAdd = getDocument().createElement("file");
            elementToAdd.setAttribute("name", name);
            elementToAdd.setAttribute("path", path);

            customFontsElement.appendChild(elementToAdd);

            writeDoc();
        } catch (Exception e) {
            logger.error("Error adding custom font to properties file", e);
        }
    }

    public Map<String, String> getCustomFonts() {
        final NodeList nodeList = getDocument().getElementsByTagName(CUSTOM_FONTS);
        if (nodeList.getLength() == 0) {
            return Collections.emptyMap();
        }

        final Map<String, String> customFonts = new HashMap<>();

        final NodeList allCustomFonts = ((Element) nodeList.item(0)).getElementsByTagName("*");
        for (int i = 0; i < allCustomFonts.getLength(); i++) {
            final Node item = allCustomFonts.item(i);
            Optional.ofNullable(item.getAttributes())
                    .ifPresent(attrs -> {
                        final String name = attrs.getNamedItem("name").getNodeValue();
                        final String path = attrs.getNamedItem("path").getNodeValue();

                        customFonts.put(name, path);
                    });
        }

        return customFonts;
    }

    @Override
    public boolean checkForModelUpdate(final Document document) {
        boolean modelUpdated = false;

        if (isElementMissing(document, "properties")) {
            createNewModelInstance(document);
            modelUpdated = true;
        }
        if (isElementMissing(document, RECENT_DESIGNER_FILES)) {
            updateElementInModel(document, RECENT_DESIGNER_FILES);
            modelUpdated = true;
        }
        if (isElementMissing(document, RECENT_PDF_FILES)) {
            updateElementInModel(document, RECENT_PDF_FILES);
            modelUpdated = true;
        }
        if (isElementMissing(document, CUSTOM_FONTS)) {
            updateElementInModel(document, CUSTOM_FONTS);
            modelUpdated = true;
        }

        return modelUpdated;
    }

}
