package org.pdf.forms.gui.windows;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;

import org.pdf.forms.gui.commands.DesFileFilter;
import org.pdf.forms.gui.components.AbstractDialog;

public class FileFinderDialog extends AbstractDialog {

    private String fileLocation;

    private JLabel fileLabel;
    private JTextField locationBox;

    public FileFinderDialog(
            final JFrame parent,
            final File fileToFind) {
        super(parent);

        buildDialog();
        applyData(fileToFind);
        setInitialSelection();

        setDialogSize(500, 150);
    }

    private void applyData(final File fileToFind) {
        fileLabel.setText(fileToFind.getName());
        locationBox.setText(fileToFind.getAbsolutePath());
    }

    @Override
    protected String getDialogTitle() {
        return "Unable to find file";
    }

    private void setInitialSelection() {
        locationBox.requestFocus();
    }

    @Override
    protected Component createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createInfoPanel(), BorderLayout.NORTH);
        panel.add(createBrowsePanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JComponent createInfoPanel() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));

        final JLabel label = new JLabel("Unable to locate the required PDF document:");

        fileLabel = new JLabel();

        panel.add(label, BorderLayout.WEST);
        panel.add(fileLabel, BorderLayout.CENTER);

        return panel;
    }

    private JComponent createBrowsePanel() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));

        locationBox = new JTextField();
        locationBox.setSize(locationBox.getPreferredSize());
        locationBox.setMaximumSize(locationBox.getPreferredSize());

        final JButton browseButton = new JButton();
        browseButton.setText("Browse");
        browseButton.addActionListener(this::browseClicked);

        panel.add(locationBox, BorderLayout.CENTER);
        panel.add(browseButton, BorderLayout.EAST);

        return panel;
    }

    private void browseClicked(final ActionEvent evt) {
        final String path = locationBox.getText();

        final JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new DesFileFilter());

        final int state = chooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            final File fileToOpen = chooser.getSelectedFile();
            if (fileToOpen != null) {
                locationBox.setText(fileToOpen.getAbsolutePath());
            }
        }
    }

    public String getFileLocation() {
        return fileLocation;
    }

}
