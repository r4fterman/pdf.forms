package org.pdf.forms.gui.hierarchy;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;

class HierarchyPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new HierarchyPanel(getDesignerPanel());
    }
}
