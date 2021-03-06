package org.pdf.forms.gui.commands;

import java.awt.*;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class AlignLeftCommand implements Command {

    private final IMainFrame mainFrame;

    public AlignLeftCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        alignLeft(selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        mainFrame.setPropertiesCompound(selectedWidgets);
        mainFrame.setPropertiesToolBar(selectedWidgets);
        mainFrame.updateHierarchy();

        designerPanel.repaint();
    }

    private void alignLeft(final Set<IWidget> selectedWidgets) {
        final int leftPoint = calculateLeftPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(leftPoint);
        }
    }

    private int calculateLeftPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int leftPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.x;
            if (point < leftPoint) {
                leftPoint = point;
            }
        }
        return leftPoint;
    }
}
