package org.pdf.forms.gui.windows;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.pdf.forms.gui.components.AbstractDialog;

public class PdfImportProgressDialog extends AbstractDialog {

    private int noOfPages;
    private boolean isCancelled = false;

    private JLabel infoText;
    private JProgressBar progressBar;
    private JButton stopButton;

    public PdfImportProgressDialog(final JFrame frame) {
        super(frame);

        buildDialog();

        setDialogSize(500, 250);
    }

    @Override
    protected String getDialogTitle() {
        return "Import PDF files";
    }

    @Override
    protected Component createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout(5,5));

        panel.add(createAnimationPanel(), BorderLayout.CENTER);
        panel.add(createProgressPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAnimationPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        final JLabel animationLabel = new JLabel(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/animation.gif"))); // NOI18N

        panel.add(animationLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        this.progressBar = new JProgressBar();

        this.stopButton = new JButton("Stop");
        stopButton.addActionListener(this::stopButtonClicked);

        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(stopButton, BorderLayout.EAST);

        return panel;
    }

    private void stopButtonClicked(final ActionEvent evt) {
        stopButton.setEnabled(false);
        isCancelled = true;
    }

    public void setStatusMaximum(final int max) {
        noOfPages = max;
        progressBar.setMaximum(max);
    }

    public void setProgress(final int progress) {
        progressBar.setValue(progress);
        infoText.setText("Importing PDF page " + (progress + 1) + " of " + noOfPages + ":");
    }

    public boolean isCancelled() {
        return isCancelled;
    }

}
