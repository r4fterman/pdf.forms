package org.pdf.forms.fonts;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.pdf.forms.model.properties.Font;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FontHandler {

    private final Logger logger = LoggerFactory.getLogger(FontHandler.class);
    private final List<Font> fonts = new ArrayList<>();
    private final DesignerPropertiesFile designerPropertiesFile;

    public FontHandler(
            final DesignerPropertiesFile designerPropertiesFile,
            final FontDirectories fontDirectories) {
        this.designerPropertiesFile = designerPropertiesFile;
        fontDirectories.getDirectories().forEach(this::registerDirectory);

        updateFonts();
    }

    public void updateFonts() {
        //todo: need to check if file has moved, and if so offer user chance to browse
        designerPropertiesFile.getCustomFonts().forEach(font -> registerFont(new File(font.getPath())));
    }

    private void registerDirectory(final File fontDirectory) {
        try {
            if (!fontDirectory.exists() || !fontDirectory.isDirectory()) {
                return;
            }

            final File[] fontFiles = fontDirectory.listFiles((directory, fileName) -> fileName.toLowerCase()
                    .endsWith(".ttf"));
            if (fontFiles == null) {
                return;
            }

            Arrays.stream(fontFiles).forEach(this::registerFont);
        } catch (final Exception e) {
            logger.info("Error registering directory. {}", e.getMessage());
        }
    }

    Optional<String> registerFont(final File fontFile) {
        try {
            final java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);
            final String fontName = font.getFontName();

            this.fonts.add(new Font(fontName, fontFile.getPath()));

            return Optional.of(fontName);
        } catch (FontFormatException | IOException e) {
            logger.warn("Unable reading font in FontHandler. {}", e.getMessage());
        }

        return Optional.empty();
    }

    public java.awt.Font getDefaultFont() {
        if (!getFonts().isEmpty()) {
            final Font font = getFonts().get(0);
            final Optional<java.awt.Font> javaFont = font.convertToJavaFont();
            if (javaFont.isPresent()) {
                return javaFont.get();
            }
        }
        return new java.awt.Font("Arial", java.awt.Font.PLAIN, 11);
    }

    public List<Font> getFonts() {
        fonts.sort(Comparator.comparing(Font::getName));
        return List.copyOf(fonts);
    }

    public Optional<String> getFontDirectory(final java.awt.Font font) {
        return getAbsoluteFontPath(font)
                .map(fontPath -> {
                    final String fileName = new File(fontPath).getName();
                    return fontPath.substring(0, fontPath.length() - fileName.length());
                });
    }

    public Optional<String> getAbsoluteFontPath(final java.awt.Font font) {
        final String fontName = font.getName();

        return fonts.stream()
                .filter(f -> f.getName().equals(fontName))
                .findFirst()
                .map(Font::getPath);
    }

    public java.awt.Font getFontFromName(final String fontName) {
        return fonts.stream()
                .filter(font -> font.getName().equals(fontName))
                .findFirst()
                .flatMap(Font::convertToJavaFont)
                .orElseGet(this::getDefaultFont);
    }

    public String[] getFontFamilies() {
        return fonts.stream()
                .map(Font::getName)
                .toArray(String[]::new);
    }

}
