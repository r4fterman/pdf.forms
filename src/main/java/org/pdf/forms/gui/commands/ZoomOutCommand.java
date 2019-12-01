package org.pdf.forms.gui.commands;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.gui.DesignerCompound;

class ZoomOutCommand implements Command {

    private final IMainFrame mainFrame;

    ZoomOutCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        zoom(mainFrame.getCurrentScaling() * (2d / 3d));
    }

    private void zoom(final double scaling) {
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
