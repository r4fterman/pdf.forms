package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {

    private static final List<String> IMAGE_FILE_EXTENSIONS = List.of(
            ".tif",
            ".tiff",
            ".png",
            ".jpg",
            ".jpeg",
            ".gif"
    );

    @Override
    public boolean accept(final File file) {
        final String filePath = file.getAbsolutePath();
        return IMAGE_FILE_EXTENSIONS.stream()
                .anyMatch(filePath::endsWith);
    }

    @Override
    public String getDescription() {
        return IMAGE_FILE_EXTENSIONS.stream()
                .collect(joining(", ", "Image Files (", ")"));
    }
}
