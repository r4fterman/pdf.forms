package org.pdf.forms.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.jupiter.api.Test;

public abstract class UIDialogTest {

    @Test
    void buildAndShowDialog() throws Exception {
        final JFrame frame = new JFrame();

        final JDialog dialog = createDialog(frame);

        frame.pack();
        frame.setVisible(true);

        Thread.sleep(1000);

        dialog.dispose();
        frame.dispose();
    }

    protected abstract JDialog createDialog(JFrame frame);
}
