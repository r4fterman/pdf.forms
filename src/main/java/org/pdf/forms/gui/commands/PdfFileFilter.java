package org.pdf.forms.gui.commands;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PdfFileFilter extends FileFilter {

    @Override
    public boolean accept(final File file) {
        return file.isFile() && file.getName().endsWith(".pdf");
    }

    @Override
    public String getDescription() {
        return "PDF File (*.pdf)";
    }

}
