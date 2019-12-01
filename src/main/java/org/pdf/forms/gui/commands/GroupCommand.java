package org.pdf.forms.gui.commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;

class GroupCommand implements Command {

    private IMainFrame mainFrame;

    GroupCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        group();
    }

    private void group() {
        final IDesigner designerPanel = mainFrame.getDesigner();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        final List<IWidget> widgetsInGroup = designerPanel.getWidgets().stream()
                .filter(selectedWidgets::contains)
                .collect(Collectors.toList());

        final GroupWidget groupWidget = new GroupWidget();
        groupWidget.setWidgetsInGroup(widgetsInGroup);
        designerPanel.addWidget(groupWidget);
        designerPanel.removeSelectedWidgets();
        designerPanel.setSelectedWidgets(Set.of(groupWidget));
        designerPanel.repaint();
    }
}
