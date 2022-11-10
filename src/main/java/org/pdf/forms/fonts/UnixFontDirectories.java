package org.pdf.forms.fonts;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UnixFontDirectories implements FontDirectories {

    private static final String[] UNIX_FONT_DIRECTORIES = {
            "/usr/X/lib/X11/fonts/TrueType",
            "/usr/openwin/lib/X11/fonts/TrueType",
            "/usr/share/fonts",
            "/usr/share/fonts/default/TrueType",
            "/usr/share/fonts/truetype/liberation",
            "/usr/X11R6/lib/X11/fonts/ttf",
            "/Library/Fonts",
            "/System/Library/Fonts"
    };

    @Override
    public List<File> getFontDirectories() {
        return Arrays.stream(UNIX_FONT_DIRECTORIES)
                .map(File::new)
                .collect(toUnmodifiableList());
    }

}
