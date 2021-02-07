package org.pdf.forms.gui.windows;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.model.des.Version;

class SplashPanel extends JPanel {

    private JProgressBar progressBar;

    SplashPanel(final Version version) {
        initComponents(version);
    }

    private void initComponents(final Version version) {
        setLayout(null);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBounds(0, 255, 717, 19);

        final JLabel versionLabel = new JLabel();
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        versionLabel.setText("Version: " + version.getVersion());
        versionLabel.setBounds(600, 230, 100, 14);

        final JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/headerout.png")));
        imageLabel.setPreferredSize(new Dimension(612, 332));
        imageLabel.setBounds(0, 0, 730, 255);

        add(versionLabel);
        add(imageLabel);
        add(progressBar);
    }

    void setStatusMaximum(final int max) {
        progressBar.setMaximum(max);
    }

    void setProgress(
            final int progress,
            final String text) {
        progressBar.setValue(progress);
        progressBar.setString(text);
    }

}
