package org.pdf.forms.gui.commands;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ZoomCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(ZoomCommand.class);
    private final IMainFrame mainFrame;

    ZoomCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        zoom(mainFrame.getCurrentSelectedScaling() / 100d);
    }

    void zoom(final double scaling) {
        if (mainFrame.getDesignerCompoundContent() == DesignerCompound.PREVIEW) {

            mainFrame.setCurrentSelectedScaling(round(scaling * 100));

            final DesignerCompound desgnerCompound = mainFrame.getDesignerCompound();
            desgnerCompound.previewZoom(scaling);
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
