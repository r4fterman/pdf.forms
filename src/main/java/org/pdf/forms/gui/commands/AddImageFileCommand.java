package org.pdf.forms.gui.commands;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;

public class AddImageFileCommand implements Command {

    private static final java.util.List<String> IMAGE_FILE_EXTENSIONS = List.of(
            ".tif",
            ".tiff",
            ".png",
            ".jpg",
            ".jpeg",
            ".gif"
    );

    public static boolean isImage(final File file) {
        final String filePath = file.getAbsolutePath();
        return IMAGE_FILE_EXTENSIONS.stream()
                .anyMatch(filePath::endsWith);
    }

    private final IMainFrame mainFrame;

    public AddImageFileCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        selectImageFile()
                .ifPresent(this::openFile);
    }

    private Optional<File> selectImageFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new ImageFileFilter());

        final int state = chooser.showOpenDialog((Component) mainFrame);
        if (state == JFileChooser.APPROVE_OPTION) {
            return Optional.ofNullable(chooser.getSelectedFile());
        }
        return Optional.empty();
    }

    public void openFile(final File imageFile) {
        //todo: open image and add to
    }
}
