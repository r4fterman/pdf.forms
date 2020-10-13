package org.pdf.forms.gui.commands;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.gui.DesignerPanel;

class ZoomCommand implements Command {

    private final IMainFrame mainFrame;

    ZoomCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        zoom(mainFrame.getCurrentSelectedScaling() / 100d);
    }

    private void zoom(final double scaling) {
        if (mainFrame.getDesignerCompoundContent() == DesignerPanel.PREVIEW) {
            mainFrame.setCurrentSelectedScaling(round(scaling * 100));

            final DesignerPanel designerPanel = mainFrame.getDesignerPanel();
            designerPanel.previewZoom(scaling);
        }
        //mainFrame.setScaling(mainFrame.getScaling() * scaling); @scale
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 2);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }
}
