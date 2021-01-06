package org.pdf.forms.fonts;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.utils.DesignerPropertiesFile;

public class FontSelector extends javax.swing.JPanel {

    private final FontHandler fontHandler;
    private final IMainFrame mainFrame;
    private final JDialog parentDialog;
    private final DesignerPropertiesFile designerPropertiesFile;

    private final JList<String> fontsList;

    public FontSelector(
            final FontHandler fontHandler,
            final IMainFrame mainFrame,
            final JDialog parentDialog,
            final DesignerPropertiesFile designerPropertiesFile) {
        this.fontHandler = fontHandler;
        this.parentDialog = parentDialog;
        this.mainFrame = mainFrame;
        this.designerPropertiesFile = designerPropertiesFile;
        this.fontsList = new JList<>();

        initComponents();
        populateFontsAvailable();
    }

    private void initComponents() {
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(fontsList);

        final JLabel availableFontsLabel = new JLabel();
        availableFontsLabel.setText("Available Fonts:");

        final JButton addFontButton = new JButton();
        addFontButton.setText("Add Font");
        addFontButton.addActionListener(this::addFontClicked);

        final JButton okButton = new JButton();
        okButton.setText("OK");
        okButton.addActionListener(this::okClicked);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(availableFontsLabel)
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(scrollPane, GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                                        .add(GroupLayout.TRAILING,
                                                                okButton,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                        .add(GroupLayout.TRAILING,
                                                                addFontButton,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(availableFontsLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(addFontButton)
                                                .addPreferredGap(LayoutStyle.RELATED, 327, Short.MAX_VALUE)
                                                .add(okButton))
                                        .add(scrollPane, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }

    private void okClicked(final ActionEvent event) {
        parentDialog.setVisible(false);
    }

    private void addFontClicked(final ActionEvent event) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new TTFFileFilter());

        final int state = chooser.showOpenDialog((Component) mainFrame);
        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            final String name = fontHandler.registerFont(fileToOpen);
            designerPropertiesFile.addCustomFont(name, fileToOpen.getAbsolutePath());

            populateFontsAvailable();
        }
    }

    private void populateFontsAvailable() {
        final DefaultListModel<String> model = new DefaultListModel<>();

        fontHandler.getFontFileMap()
                .forEach((key, value) -> model.addElement(key.getFontName() + "->" + value));

        fontsList.setModel(model);
    }

}
