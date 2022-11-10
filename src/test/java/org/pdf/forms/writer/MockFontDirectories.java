package org.pdf.forms.writer;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.pdf.forms.fonts.FontDirectories;

public class MockFontDirectories implements FontDirectories {
    @Override
    public List<File> getFontDirectories() {
        return Collections.emptyList();
    }
}
