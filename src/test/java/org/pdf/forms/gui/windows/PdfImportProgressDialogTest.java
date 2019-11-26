package org.pdf.forms.gui.windows;

import javax.swing.JFrame;

import org.pdf.forms.gui.UIDialogTest;

class PdfImportProgressDialogTest extends UIDialogTest {

    @Override
    protected void createDialog(final JFrame frame) {
        new PdfImportProgressDialog(frame);
    }
}
