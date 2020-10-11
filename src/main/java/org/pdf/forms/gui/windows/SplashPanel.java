package org.pdf.forms.gui.windows;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

class SplashPanel extends JPanel {

    SplashPanel(final String version) {
        initComponents(version);
    }

    private void initComponents(final String version) {
        final JLabel jLabel2 = new JLabel();
        final JLabel jLabel1 = new JLabel();
        progressBar = new JProgressBar();

        setLayout(null);

        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setText("Version: " + version);
        add(jLabel2);
        jLabel2.setBounds(600, 230, 100, 14);

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/headerout.png"))); // NOI18N
        jLabel1.setPreferredSize(new Dimension(612, 332));
        add(jLabel1);
        jLabel1.setBounds(0, 0, 730, 255);
        add(progressBar);
        progressBar.setBounds(0, 255, 717, 19);
    }

    void setStatusMaximum(final int max) {
        progressBar.setMaximum(max);
    }

    void setProgress(
            final int progress,
            final String text) {
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        progressBar.setString(text);
    }

    private JProgressBar progressBar;

}
