package org.pdf.forms.gui.commands;

import java.awt.*;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class AlignHorizontallyCommand implements Command {

    private final IMainFrame mainFrame;

    public AlignHorizontallyCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        alignHorizontally(selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        mainFrame.setPropertiesCompound(selectedWidgets);
        mainFrame.setPropertiesToolBar(selectedWidgets);
        mainFrame.updateHierarchy();

        designerPanel.repaint();
    }

    private void alignHorizontally(final Set<IWidget> selectedWidgets) {
        final int averageCenterPoint = calculateAverageXCenterPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(averageCenterPoint - widget.getBounds().width / 2);
        }
    }

    private int calculateAverageXCenterPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()){
            return 0;
        }

        int averageCenterPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();
            averageCenterPoint += bounds.x + bounds.width / 2;
        }

        return averageCenterPoint / widgets.size();
    }
}
