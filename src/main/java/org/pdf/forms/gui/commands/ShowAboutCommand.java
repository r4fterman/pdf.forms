package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.AboutPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ShowAboutCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ShowAboutCommand.class);
    private final IMainFrame mainFrame;

    ShowAboutCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        about();
    }

    private void about() {
        JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
    }

}
