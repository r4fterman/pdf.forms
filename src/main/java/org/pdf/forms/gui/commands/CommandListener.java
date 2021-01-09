package org.pdf.forms.gui.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jpedal.examples.simpleviewer.gui.generic.GUIButton;
import org.jpedal.examples.simpleviewer.gui.swing.SwingCombo;
import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
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
        logger.debug("Action ID: {}", id);
        commands.executeCommand(id);
    }

    private int getId(final Object source) {
        logger.debug("Action event: {}", source.getClass().getCanonicalName());
        if (source instanceof GUIButton) {
            return ((GUIButton) source).getID();
        }
        if (source instanceof SwingMenuItem) {
            return ((SwingMenuItem) source).getID();
        }
        if (source instanceof SwingCombo) {
            return ((SwingCombo) source).getID();
        }
        logger.warn("Unknown event source: {}", source.getClass().getCanonicalName());
        return Commands.NONE;
    }
}
