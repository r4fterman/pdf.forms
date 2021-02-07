package org.pdf.forms.gui.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class SendBackwardCommand implements Command {

    private final IMainFrame mainFrame;

    public SendBackwardCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        final IDesigner designerPanel = mainFrame.getDesigner();
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        changeOrderOfSelectedWidgets(designerPanel.getWidgets(), selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        mainFrame.setPropertiesCompound(selectedWidgets);
        mainFrame.setPropertiesToolBar(selectedWidgets);
        mainFrame.updateHierarchy();

        designerPanel.repaint();
    }

    private void changeOrderOfSelectedWidgets(
            final List<IWidget> allWidgets,
            final Set<IWidget> selectedWidgets) {
        // Note: This method takes a reference to the list of widgets stored in the
        // designer panel. It then alters the list here.

        final int size = allWidgets.size() - 1;
        sendBackwards(allWidgets, size, selectedWidgets);
    }

    private void sendBackwards(
            final List<IWidget> allWidgets,
            final int size,
            final Set<IWidget> selectedWidgets) {
        final Set<IWidget> copiedSet = Set.copyOf(selectedWidgets);
        final Set<IWidget> newSet = new HashSet<>(copiedSet);
        for (int i = size; i >= 0; i--) {
            final IWidget widget = allWidgets.get(i);
            if (newSet.remove(widget)) {
                if (i > 0) {
                    allWidgets.add(i - 1, allWidgets.remove(i));
                }
                i = size + 1;
            }
        }
    }
}
