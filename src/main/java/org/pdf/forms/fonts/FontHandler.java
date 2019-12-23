package org.pdf.forms.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pdf.forms.Configuration;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public final class FontHandler {

    private static final Map<Font, String> FONT_FILE_MAP = new TreeMap<>((font1, font2) -> {
        final String fontName1 = font1.getFontName();
        final String fontName2 = font2.getFontName();

        return fontName1.compareToIgnoreCase(fontName2);
    });

    private final Logger logger = LoggerFactory.getLogger(FontHandler.class);

    public FontHandler(final Configuration configuration) {
        final String javaFontDir = System.getProperty("java.home") + "/lib/fonts";
        final String[] fontDirectoriesWindows = {
                "c:/windows/fonts",
                "c:/winnt/fonts",
                "d:/windows/fonts",
                "d:/winnt/fonts"
        };
        final String[] fontDirectoriesUnix = {
                "/usr/X/lib/X11/fonts/TrueType",
                "/usr/openwin/lib/X11/fonts/TrueType",
                "/usr/share/fonts",
                "/usr/share/fonts/default/TrueType",
                "/usr/X11R6/lib/X11/fonts/ttf",
                "/Library/Fonts",
                "/System/Library/Fonts"
        };
        final List<String> fontDirectories = ImmutableList.<String>builder()
                .addAll(Arrays.asList(fontDirectoriesWindows))
                .addAll(Arrays.asList(fontDirectoriesUnix))
                .add(javaFontDir)
                .build();

        fontDirectories.forEach(this::registerDirectory);

        //TODO need to check if file has moved, and if so offer user chance to browse
        DesignerPropertiesFile.getInstance(configuration.getConfigDirectory()).getCustomFonts().forEach((key, value) -> registerFont(new File(value)));
    }

    private void registerDirectory(final String fontDirectory) {
        System.out.println("FontHandler.registerDirectory: " + fontDirectory);
        logger.info("FontHandler.registerDirectory: {}", fontDirectory);
        try {
            final File folder = new File(fontDirectory);
            if (!folder.exists() || !folder.isDirectory()) {
                return;
            }

            final File[] fontFiles = folder.listFiles((directory, fileName) -> fileName.toLowerCase().endsWith(".ttf"));
            if (fontFiles == null) {
                return;
            }

            Arrays.stream(fontFiles).forEach(this::registerFont);
        } catch (final Exception e) {
            logger.info("Error registering directory", e);
        }
    }

    String registerFont(final File file) {
        System.out.println("FontHandler.registerFont: " + file);
        logger.info("FontHandler.registerFont: {}", file);
        // TODO adapt this method to handle a duff file, behave nicely, and tell
        try {
            final String fontLocation = file.getPath();
            final FileInputStream fontStream = new FileInputStream(fontLocation);
            final Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            FONT_FILE_MAP.put(font, fontLocation);

            return font.getFontName();
        } catch (final FontFormatException | IOException e) {
            logger.warn("Unable reading font in FontHandler {}", file.getAbsolutePath(), e);
        }

        return null;
    }

    public Font getDefaultFont() {
        return (Font) ((TreeMap) FONT_FILE_MAP).firstKey();
    }

    public Map<Font, String> getFontFileMap() {
        return FONT_FILE_MAP;
    }

    public String getFontDirectory(final Font font) {
        final String fontPath = getAbsoluteFontPath(font);
        final String fileName = new File(fontPath).getName();
        return fontPath.substring(0, fontPath.length() - fileName.length());
    }

    public String getAbsoluteFontPath(final Font font) {
        final String fontName = font.getName();

        return FONT_FILE_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(fontName))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(() -> FONT_FILE_MAP.get(getDefaultFont()));
    }

    public Font getFontFromName(final String fontName) {
        return FONT_FILE_MAP.keySet().stream()
                .filter(font -> font.getName().equals(fontName))
                .findFirst()
                .orElseGet(this::getDefaultFont);
    }

    public String[] getFontFamilies() {
        return FONT_FILE_MAP.keySet().stream()
                .map(Font::getFontName)
                .toArray(String[]::new);
    }
}
