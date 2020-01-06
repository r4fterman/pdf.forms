package org.pdf.forms.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.pdf.forms.gui.toolbars.SwingButton;
import org.pdf.forms.gui.toolbars.SwingCombo;
import org.pdf.forms.utils.configuration.SwingMenuItem;

public class CommandListener implements ActionListener {

    private final Commands commands;

    public CommandListener(final Commands commands) {
        this.commands = commands;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final int id = getId(event.getSource());
        commands.executeCommand(id);
    }

    private int getId(final Object source) {
        if (source instanceof SwingButton) {
            return ((SwingButton) source).getID();
        }
        if (source instanceof SwingMenuItem) {
            return ((SwingMenuItem) source).getID();
        }
        if (source instanceof SwingCombo) {
            return ((SwingCombo) source).getID();
        }
        return Commands.NONE;
    }
}
