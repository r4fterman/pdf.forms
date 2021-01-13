package org.pdf.forms.utils;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.pdf.forms.model.properties.CustomProperties;
import org.pdf.forms.model.properties.Font;
import org.pdf.forms.readers.properties.PropertiesFileReader;

public class DesignerPropertiesFile {

    public static final int NO_OF_RECENT_DOCS = 6;

    private final CustomProperties customProperties;

    public DesignerPropertiesFile(final File directory) {
        this.customProperties = new PropertiesFileReader(getFile(directory)).getCustomProperties();
    }

    public List<String> getRecentDesignerDocuments() {
        final List<String> list = customProperties.getRecentDesFiles().getFile().stream()
                .map(org.pdf.forms.model.properties.File::getName)
                .collect(toUnmodifiableList());
        return fillList(list);
    }

    public List<String> getRecentPDFDocuments() {
        final List<String> list = customProperties.getRecentPdfFiles().getFile().stream()
                .map(org.pdf.forms.model.properties.File::getName)
                .collect(toUnmodifiableList());
        return fillList(list);
    }

    public void addRecentDesignerDocument(final String file) {
        customProperties.getRecentDesFiles().getFile().add(0, new org.pdf.forms.model.properties.File(file));
    }

    public void addRecentPDFDocument(final String file) {
        customProperties.getRecentPdfFiles().getFile().add(0, new org.pdf.forms.model.properties.File(file));
    }

    public void addCustomFont(
            final String name,
            final String path) {
        customProperties.getCustomFonts().getFont().add(0, new org.pdf.forms.model.properties.Font(name, path));
    }

    public Map<String, String> getCustomFonts() {
        return customProperties.getCustomFonts().getFont().stream()
                .collect(toUnmodifiableMap(
                        Font::getName,
                        Font::getPath
                ));
    }

    private List<String> fillList(final List<String> list) {
        if (list.size() == DesignerPropertiesFile.NO_OF_RECENT_DOCS) {
            return list;
        }
        if (list.size() > DesignerPropertiesFile.NO_OF_RECENT_DOCS) {
            return list.stream().limit(DesignerPropertiesFile.NO_OF_RECENT_DOCS).collect(toUnmodifiableList());
        }

        final int listLength = DesignerPropertiesFile.NO_OF_RECENT_DOCS - list.size();
        final List<String> missingElements = new ArrayList<>(listLength);
        for (int i = 0; i < listLength; i++) {
            missingElements.add("");
        }

        return Stream.concat(list.stream(), missingElements.stream())
                .collect(toUnmodifiableList());
    }

    private File getFile(final File directory) {
        return new File(directory, ".properties.xml");

    }
}
