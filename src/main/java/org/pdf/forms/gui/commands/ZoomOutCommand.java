package org.pdf.forms.gui.commands;

import org.pdf.forms.gui.IMainFrame;

class ZoomOutCommand extends ZoomCommand {

    private final IMainFrame mainFrame;

    ZoomOutCommand(final IMainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        zoom(mainFrame.getCurrentScaling() * (2d / 3d));
    }
}
