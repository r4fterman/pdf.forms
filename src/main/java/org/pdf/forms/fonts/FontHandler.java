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
 * FontHandler.java
 * ---------------
 */
package org.pdf.forms.fonts;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pdf.forms.utils.DesignerPropertiesFile;

import com.google.common.collect.ImmutableList;

public class FontHandler {

    private static FontHandler instance;

    private static Map<Font, String> fontFileMap = new TreeMap<>((o1, o2) -> {
        String font1 = o1.getFontName();
        String font2 = o2.getFontName();

        return font1.compareToIgnoreCase(font2);
    });

    private String javaFontDir = System.getProperty("java.home") + "/lib/fonts";

    private String[] fontDirectoriesWindows = { "c:/windows/fonts", "c:/winnt/fonts", "d:/windows/fonts",
            "d:/winnt/fonts" };

    private String[] fontDirectoriesUnix = { "/usr/X/lib/X11/fonts/TrueType",
            "/usr/openwin/lib/X11/fonts/TrueType",
            "/usr/share/fonts/default/TrueType", "/usr/X11R6/lib/X11/fonts/ttf", "/Library/Fonts",
            "/System/Library/Fonts" };

    private List fontDirectories = ImmutableList.builder().addAll(Arrays.asList(fontDirectoriesWindows))
            .addAll(Arrays.asList(fontDirectoriesUnix)).add(javaFontDir).build();

    private FontHandler() {
        for (Object directory : fontDirectories) {
            String dir = (String) directory;
            registerDirectory(dir);
        }

        //TODO need to check if file has moved, and if so offer user chance to browse
        Map customFonts = DesignerPropertiesFile.getInstance().getCustomFonts();
        for (final Object o : customFonts.keySet()) {
            String name = (String) o;
            String path = (String) customFonts.get(name);

            registerFont(new File(path));
        }
    }

    private void registerDirectory(final String dir) {
        try {
            File folder = new File(dir);
            if (!folder.exists() || !folder.isDirectory()) {
                return;
            }

            File[] fontFiles = folder.listFiles();
            if (fontFiles == null) {
                return;
            }

            for (File fontFile : fontFiles) {
                String name = fontFile.getPath().toLowerCase();

                if (!name.endsWith("ttf")) {
                    continue;
                }

                registerFont(fontFile);
            }
        } catch (Exception e) {
            //empty on purpose
        }
    }

    /**
     * todo adapt this method to handle a duff file, behave nicely, and tell
     * any method that relies on it what happened
     */
    String registerFont(final File file) {
        try {
            String fontLocation = file.getPath();
            FileInputStream fontStream = new FileInputStream(fontLocation);
            Font f = Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);

            fontFileMap.put(f, fontLocation);

            return f.getFontName();
        } catch (Exception e) {
            System.out.println("error reading font in FontHandler = " + file);
        }

        return null;
    }

    public static FontHandler getInstance() {
        // it's ok, we can call this constructor
        if (instance == null) {
            instance = new FontHandler();
        }

        return instance;
    }

    public Font getDefaultFont() {
        return (Font) ((TreeMap) fontFileMap).firstKey();
    }

    public Map<Font, String> getFontFileMap() {
        return fontFileMap;
    }

    public String getFontDirectory(final Font font) {
        final String fontPath = getAbsoluteFontPath(font);
        final String fileName = new File(fontPath).getName();
        return fontPath.substring(0, fontPath.length() - fileName.length());
    }

    public String getAbsoluteFontPath(final Font font) {
        final String fontName = font.getName();

        return fontFileMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(fontName))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(() -> fontFileMap.get(getDefaultFont()));
    }

    public Font getFontFromName(String fontName) {
        return fontFileMap.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals(fontName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(this::getDefaultFont);
    }
}
