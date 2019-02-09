package org.pdf.forms.gui.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GroupElementsCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(GroupElementsCommand.class);
    private final IMainFrame mainFrame;

    GroupElementsCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        group();
    }

    private void group() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        final List<IWidget> widgetsInGroup = new ArrayList<>();
        final GroupWidget gw = new GroupWidget();

        final List<IWidget> allWidgets = designerPanel.getWidgets();

        for (final IWidget widget : allWidgets) {
            if (selectedWidgets.contains(widget)) {
                widgetsInGroup.add(widget);
            }
        }

        gw.setWidgetsInGroup(widgetsInGroup);
        designerPanel.addWidget(gw);

        designerPanel.removeSelectedWidgets();

        final Set<IWidget> set = new HashSet<>();
        set.add(gw);
        designerPanel.setSelectedWidgets(set);

        designerPanel.repaint();
    }

}
