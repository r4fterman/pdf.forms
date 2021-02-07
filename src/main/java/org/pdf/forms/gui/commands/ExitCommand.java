package org.pdf.forms.gui.commands;

import java.awt.*;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;

public class ExitCommand implements Command {

    private final IMainFrame mainFrame;

    public ExitCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final int value = JOptionPane.showConfirmDialog(
                (Component) mainFrame,
                "Do you really want to quit?",
                "Exit Program",
                JOptionPane.YES_NO_OPTION);

        if (value == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
