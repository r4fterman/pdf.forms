package org.pdf.forms.gui.windows;

import java.io.File;

import javax.swing.*;

import org.pdf.forms.gui.UIDialogTest;

class FileFinderDialogTest extends UIDialogTest {

    @Override
    protected JDialog createDialog(final JFrame frame) {
        return new FileFinderDialog(frame, new File("test.txt"));
    }
}
