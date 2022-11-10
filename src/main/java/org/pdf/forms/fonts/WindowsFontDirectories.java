package org.pdf.forms.fonts;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WindowsFontDirectories implements FontDirectories {

    private static final String[] WINDOWS_FONT_DIRECTORIES = {
            "c:/windows/fonts",
            "c:/winnt/fonts",
            "d:/windows/fonts",
            "d:/winnt/fonts"
    };

    @Override
    public List<File> getFontDirectories() {
        return Arrays.stream(WINDOWS_FONT_DIRECTORIES)
                .map(File::new)
                .collect(toUnmodifiableList());
    }
}
