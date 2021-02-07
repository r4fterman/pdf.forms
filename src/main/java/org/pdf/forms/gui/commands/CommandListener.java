package org.pdf.forms.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.pdf.forms.gui.components.ButtonWithID;
import org.pdf.forms.gui.components.CheckBoxMenuItemWithID;
import org.pdf.forms.gui.components.ComboBoxWithID;
import org.pdf.forms.gui.components.MenuItemWithID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandListener implements ActionListener {

    private final Logger logger = LoggerFactory.getLogger(CommandListener.class);

    private final Commands commands;

    public CommandListener(final Commands commands) {
        this.commands = commands;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final int id = getId(event.getSource());
        logger.info("Action ID: {}", id);
        commands.executeCommand(id);
    }

    private int getId(final Object source) {
        logger.info("Action event: {}", source.getClass().getCanonicalName());
        if (source instanceof ButtonWithID) {
            return ((ButtonWithID) source).getId();
        }
        if (source instanceof MenuItemWithID) {
            return ((MenuItemWithID) source).getId();
        }
        if (source instanceof ComboBoxWithID) {
            return ((ComboBoxWithID<?>) source).getId();
        }
        if (source instanceof CheckBoxMenuItemWithID) {
            return ((CheckBoxMenuItemWithID) source).getId();
        }
        logger.warn("Unknown event source: {}", source.getClass().getCanonicalName());
        return Commands.NONE;
    }
}
