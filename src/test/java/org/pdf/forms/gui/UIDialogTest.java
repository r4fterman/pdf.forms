package org.pdf.forms.gui;

import javax.swing.*;

import org.junit.jupiter.api.Test;

public abstract class UIDialogTest {

    @Test
    void buildAndShowDialog() throws Exception {
        final JFrame frame = new JFrame();
        final JDialog dialog = createDialog(frame);

        try {
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            // for testing dialog MUST be non-modal
            dialog.setModal(false);
            dialog.setVisible(true);

            Thread.sleep(10000);
        } finally {
            dialog.setVisible(false);
            dialog.dispose();
            frame.dispose();
        }
    }

    protected abstract JDialog createDialog(JFrame frame);
}
