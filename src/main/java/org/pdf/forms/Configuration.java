package org.pdf.forms;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.pdf.forms.fonts.FontDirectories;
import org.pdf.forms.fonts.UnixFontDirectories;
import org.pdf.forms.fonts.WindowsFontDirectories;

public class Configuration {

    public File getConfigDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public FontDirectories getFontDirectories() {
        if (isWindows()) {
            return new WindowsFontDirectories();
        }
        return new UnixFontDirectories();
    }

    private boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}
