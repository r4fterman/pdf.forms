package org.pdf.forms.gui.commands;

import java.awt.*;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class AlignRightCommand implements Command {

    private final IMainFrame mainFrame;

    public AlignRightCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        alignRight(selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        mainFrame.setPropertiesCompound(selectedWidgets);
        mainFrame.setPropertiesToolBar(selectedWidgets);
        mainFrame.updateHierarchy();

        designerPanel.repaint();
    }

    private void alignRight(final Set<IWidget> selectedWidgets) {
        final int rightPoint = calculateRightPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(rightPoint - widget.getBounds().width);
        }
    }

    private int calculateRightPoint(final Set<IWidget> widgets) {
        int rightPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.x + bounds.width;
            if (point > rightPoint) {
                rightPoint = point;
            }
        }
        return rightPoint;
    }
}
