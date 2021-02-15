package org.pdf.forms.fonts;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.*;

import org.pdf.forms.gui.components.AbstractDialog;
import org.pdf.forms.model.properties.Font;

public class FontManagementDialog extends AbstractDialog {

    private JList<FontEntry> fontList;
    private JButton removeButton;

    public FontManagementDialog(
            final JFrame parentFrame,
            final List<Font> systemFonts,
            final List<Font> customFonts) {
        super(parentFrame);

        buildDialog();
        applyData(systemFonts, customFonts);
        setInitialSelection();

        setDialogSize(500, 300);
    }

    @Override
    protected String getDialogTitle() {
        return "Font Management";
    }

    @Override
    protected Component createMainPanel() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.add(createListPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.EAST);

        fontList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                final int idx = fontList.getSelectedIndex();
                if (idx >= 0) {
                    final FontEntry fontEntry = fontList.getModel().getElementAt(idx);
                    removeButton.setEnabled(fontEntry.isCustomFont());
                }
            }
        });

        return panel;
    }

    private JComponent createListPanel() {
        this.fontList = new JList<>(new DefaultListModel<>());
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(fontList);
        scrollPane.setViewportView(fontList);
        return scrollPane;
    }

    private JComponent createButtonPanel() {
        final JButton addButton = new JButton("Add...");
        addButton.addActionListener(this::selectFont);

        this.removeButton = new JButton("Remove...");
        removeButton.addActionListener(this::removeFont);

        addButton.setMaximumSize(removeButton.getMaximumSize());

        final Box verticalBox = Box.createVerticalBox();
        verticalBox.add(addButton);
        verticalBox.add(removeButton);
        return verticalBox;
    }

    private void selectFont(final ActionEvent actionEvent) {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new TTFFileFilter());

        final int state = chooser.showOpenDialog(this);
        final File fontFile = chooser.getSelectedFile();
        if (fontFile != null && state == JFileChooser.APPROVE_OPTION) {
            addCustomFont(fontFile);
        }
    }

    private void addCustomFont(final File fontFile) {
        final DefaultListModel<FontEntry> model = (DefaultListModel<FontEntry>) fontList.getModel();
        model.addElement(new FontEntry(new Font("", fontFile.getPath()), FontEntry.FontType.CUSTOM));
    }

    private void removeFont(final ActionEvent actionEvent) {
        final DefaultListModel<FontEntry> model = (DefaultListModel<FontEntry>) fontList.getModel();
        model.removeElement(fontList.getSelectedValue());
    }

    private void applyData(
            final List<Font> systemFonts,
            final List<Font> customFonts) {
        final List<FontEntry> fontEntries = Stream.concat(
                convertToSystemFontEntries(systemFonts).stream(),
                convertToCustomFontEntries(customFonts).stream()
        )
                .collect(toUnmodifiableList());

        final DefaultListModel<FontEntry> model = (DefaultListModel<FontEntry>) fontList.getModel();
        fontEntries.forEach(model::addElement);
    }

    private void setInitialSelection() {
        fontList.setSelectedIndex(0);
    }

    private List<FontEntry> convertToSystemFontEntries(final List<Font> systemFonts) {
        return systemFonts.stream()
                .map(font -> new FontEntry(font, FontEntry.FontType.SYSTEM))
                .collect(toUnmodifiableList());
    }

    private List<FontEntry> convertToCustomFontEntries(final List<Font> customFonts) {
        return customFonts.stream()
                .map(font -> new FontEntry(font, FontEntry.FontType.CUSTOM))
                .collect(toUnmodifiableList());
    }

    public List<Font> getCustomFonts() {
        final List<Font> fonts = new ArrayList<>();

        final ListModel<FontEntry> model = fontList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            final FontEntry fontEntry = model.getElementAt(i);
            if (fontEntry.isCustomFont()) {
                fonts.add(fontEntry.getFont());
            }
        }
        return List.copyOf(fonts);
    }

    private static class FontEntry {

        private enum FontType {
            SYSTEM, CUSTOM
        }

        private final Font font;

        private final FontType fontType;

        FontEntry(
                final Font font,
                final FontType fontType) {
            this.font = font;
            this.fontType = fontType;
        }

        public Font getFont() {
            return font;
        }

        public boolean isCustomFont() {
            return fontType == FontType.CUSTOM;
        }

        @Override
        public String toString() {
            return font.getName() + " -> " + font.getPath();
        }
    }
}
