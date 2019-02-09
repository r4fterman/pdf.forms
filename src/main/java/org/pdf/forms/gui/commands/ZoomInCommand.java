package org.pdf.forms.gui.commands;

import org.pdf.forms.gui.IMainFrame;

class ZoomInCommand extends ZoomCommand {

    private final IMainFrame mainFrame;

    ZoomInCommand(final IMainFrame mainFrame) {
        super(mainFrame);
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        zoom(mainFrame.getCurrentScaling() * (3d / 2d));
    }
}
