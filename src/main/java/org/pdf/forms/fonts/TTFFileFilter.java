package org.pdf.forms.fonts;

import java.io.File;

import javax.swing.filechooser.FileFilter;

class TTFFileFilter extends FileFilter {

    @Override
    public boolean accept(final File file) {
        return file.isFile() && file.getName().endsWith(".ttf");
    }

    @Override
    public String getDescription() {
        return "TTF Files (*.ttf)";
    }
}
