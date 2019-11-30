package org.pdf.forms.gui.windows;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.pdf.forms.gui.UIDialogTest;

class PdfImportProgressDialogTest extends UIDialogTest {

    @Override
    protected JDialog createDialog(final JFrame frame) {
        return new PdfImportProgressDialog(frame);
    }
}
