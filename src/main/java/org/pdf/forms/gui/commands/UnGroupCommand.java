package org.pdf.forms.gui.commands;

import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

class UnGroupCommand implements Command {

    private final IMainFrame mainFrame;

    UnGroupCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        final IWidget selectedWidget = selectedWidgets.iterator().next();

        designerPanel.removeSelectedWidgets();
        final List<IWidget> widgetsInGroup = selectedWidget.getWidgetsInGroup();
        for (final IWidget widget : widgetsInGroup) {
            designerPanel.addWidget(widget);
        }

        final Set<IWidget> widgets = Set.copyOf(widgetsInGroup);

        designerPanel.setSelectedWidgets(widgets);
        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.getMainFrame().setPropertiesToolBar(widgets);

        designerPanel.repaint();
    }

}
