package org.pdf.forms.gui.commands;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.AboutPanel;

class ShowAboutPanelCommand implements Command {

    private IMainFrame mainFrame;

    ShowAboutPanelCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
    }

}
