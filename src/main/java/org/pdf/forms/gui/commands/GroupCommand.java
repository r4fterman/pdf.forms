package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;

class GroupCommand implements Command {

    private final IMainFrame mainFrame;

    GroupCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final GroupWidget groupWidget = buildGroupWidget();

        final IDesigner designerPanel = getDesignerPanel();
        designerPanel.addWidget(groupWidget);
        designerPanel.removeSelectedWidgets();
        designerPanel.setSelectedWidgets(Set.of(groupWidget));
        designerPanel.repaint();
    }

    private GroupWidget buildGroupWidget() {
        final IDesigner designerPanel = getDesignerPanel();
        final List<IWidget> widgetsInGroup = getWidgetInGroup(designerPanel.getSelectedWidgets(), designerPanel.getWidgets());

        final GroupWidget groupWidget = new GroupWidget();
        groupWidget.setWidgetsInGroup(widgetsInGroup);
        return groupWidget;
    }

    private List<IWidget> getWidgetInGroup(
            final Set<IWidget> selectedWidgets,
            final List<IWidget> widgets) {
        return widgets.stream()
                .filter(selectedWidgets::contains)
                .collect(toUnmodifiableList());
    }

    private IDesigner getDesignerPanel() {
        return mainFrame.getDesigner();
    }
}
