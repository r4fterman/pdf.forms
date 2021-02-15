package org.pdf.forms.fonts;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public interface FontDirectories {

    default List<File> getDirectories() {
        return Stream.concat(
                Stream.of(getJavaFontDirectory()),
                getFontDirectories().stream()
        )
                .collect(toUnmodifiableList());
    }

    default File getJavaFontDirectory() {
        return new File(System.getProperty("java.home"), "lib/fonts");
    }

    List<File> getFontDirectories();

}
