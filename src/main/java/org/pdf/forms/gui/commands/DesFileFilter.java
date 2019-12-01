package org.pdf.forms.gui.commands;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DesFileFilter extends FileFilter {

    @Override
    public boolean accept(final File f) {
        return f.isFile() && f.getName().endsWith(".des");
    }

    @Override
    public String getDescription() {
        return "PDF Forms Designer File (*.des)";
    }
}
