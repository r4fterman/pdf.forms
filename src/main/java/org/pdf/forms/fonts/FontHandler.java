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
 * FontHandler.java
 * ---------------
 */
package org.pdf.forms.fonts;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pdf.forms.utils.DesignerPropertiesFile;

import com.google.common.collect.ImmutableList;

public class FontHandler {

    private static FontHandler instance;

    private static Map fontFileMap = new TreeMap(new Comparator() {
        public int compare(Object o1, Object o2) {
            String font1 = ((Font) o1).getFontName();
            String font2 = ((Font) o2).getFontName();

            return font1.compareToIgnoreCase(font2);
        }
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
        for (int i = 0; i < fontDirectories.size(); i++) {
            String dir = (String) fontDirectories.get(i);
            registerDirectory(dir);
        }

        //TODO need to check if file has moved, and if so offer user chance to browse
        Map customFonts = DesignerPropertiesFile.getInstance().getCustomFonts();
        for (Iterator it = customFonts.keySet().iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            String path = (String) customFonts.get(name);

            registerFont(new File(path));
        }
    }

    private void registerDirectory(String dir) {
        try {
            File file = new File(dir);
            if (!file.exists() || !file.isDirectory())
                return;

            String files[] = file.list();

            if (files == null)
                return;

            for (int k = 0; k < files.length; ++k) {
                file = new File(dir, files[k]);
                String name = file.getPath().toLowerCase();

                if (!name.endsWith("ttf"))
                    continue;

                registerFont(file);
            }
        } catch (Exception e) {
            //empty on purpose
        }
    }

    /**
     * todo adapt this method to handle a duff file, behave nicely, and tell
     * any method that relies on it what happened
     */
    public String registerFont(File file) {
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
        if (instance == null)
            // it's ok, we can call this constructor
            instance = new FontHandler();

        return instance;
    }

    public Font getDefaultFont() {
        return (Font) ((TreeMap) fontFileMap).firstKey();
    }

    public Map getFontFileMap() {
        return fontFileMap;
    }

    public String getFontDirectory(Font font) {
        String fontName = font.getName();

        for (Iterator it = fontFileMap.keySet().iterator(); it.hasNext(); ) {
            Font collectionFont = (Font) it.next();
            if (collectionFont.getName().equals(fontName)) {
                font = collectionFont;
                break;
            }
        }

        String fontPath = (String) fontFileMap.get(font);

        if (fontPath == null) {
            fontPath = (String) fontFileMap.get(getDefaultFont());
        }

        String fileName = new File(fontPath).getName();

        return fontPath.substring(0, fontPath.length() - fileName.length());
    }

    public String getAbsoluteFontPath(Font font) {
        String fontName = font.getName();

        for (Iterator it = fontFileMap.keySet().iterator(); it.hasNext(); ) {
            Font collectionFont = (Font) it.next();
            if (collectionFont.getName().equals(fontName)) {
                font = collectionFont;
                break;
            }
        }

        String fontPath = (String) fontFileMap.get(font);
        if (fontPath == null) {
            fontPath = (String) fontFileMap.get(getDefaultFont());
        }

        return fontPath;
    }

    public Font getFontFromName(String fontName) {
        Font returnedFont = null;

        for (Iterator it = fontFileMap.keySet().iterator(); it.hasNext(); ) {
            Font collectionFont = (Font) it.next();
            if (collectionFont.getName().equals(fontName)) {
                returnedFont = collectionFont;
                break;
            }
        }

        return returnedFont;
    }
}
