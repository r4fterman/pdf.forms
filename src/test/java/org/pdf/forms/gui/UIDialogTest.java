package org.pdf.forms.gui;

import javax.swing.JFrame;

import org.junit.jupiter.api.Test;

public abstract class UIDialogTest {

    @Test
    void buildAndShowDialog() throws Exception {
        final JFrame frame = new JFrame();

        createDialog(frame);

        frame.pack();
        frame.setVisible(true);

        Thread.sleep(10000);
    }

    protected abstract void createDialog(final JFrame frame);
}
