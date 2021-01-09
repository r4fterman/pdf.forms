package org.pdf.forms.gui.commands;

import java.awt.*;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class AlignTopCommand implements Command {

    private final IMainFrame mainFrame;

    public AlignTopCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        alignTop(selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        mainFrame.setPropertiesCompound(selectedWidgets);
        mainFrame.setPropertiesToolBar(selectedWidgets);
        mainFrame.updateHierarchy();

        designerPanel.repaint();
    }

    private void alignTop(final Set<IWidget> selectedWidgets) {
        final int topPoint = calculateTopPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setY(topPoint);
        }
    }

    private int calculateTopPoint(final Set<IWidget> widgets) {
        int topPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.y;
            if (point < topPoint) {
                topPoint = point;
            }
        }
        return topPoint;
    }
}
