package org.pdf.forms.gui.properties.object.draw;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

class ImageFileFilter extends FileFilter {

    private final List<String> suffixes = List.of("gif", "jpeg", "png");

    @Override
    public boolean accept(final File file) {
        return file.isFile() && suffixes.stream().anyMatch(suffix -> file.getName().endsWith(suffix));
    }

    @Override
    public String getDescription() {
        return suffixes.stream().collect(joining(", ", "Images(", ")"));
    }
}
