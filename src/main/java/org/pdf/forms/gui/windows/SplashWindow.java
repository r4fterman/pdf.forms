package org.pdf.forms.gui.windows;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.model.des.Version;

public class SplashWindow extends JWindow {

    private final SplashPanel splashPanel;

    public SplashWindow(final Version version) {
        this.splashPanel = new SplashPanel(version);

        getContentPane().add(splashPanel, BorderLayout.CENTER);

        setSize(717, 275);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void setStatusMaximum(final int max) {
        splashPanel.setStatusMaximum(max);
    }

    public void setProgress(
            final int progress,
            final String text) {
        splashPanel.setProgress(progress, text);
    }
}
