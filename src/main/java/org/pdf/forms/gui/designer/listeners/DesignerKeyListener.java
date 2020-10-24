package org.pdf.forms.gui.designer.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DesignerKeyListener implements KeyListener {

    private final IDesigner designerPanel;

    public DesignerKeyListener(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
            designerPanel.getMainFrame().handleArrayNumberOnWidgetDeletion(selectedWidgets);

            designerPanel.removeSelectedWidgets();
            designerPanel.setSelectedWidgets(new HashSet<>());
            designerPanel.repaint();
        }
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        // do nothing
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // do nothing
    }
}
