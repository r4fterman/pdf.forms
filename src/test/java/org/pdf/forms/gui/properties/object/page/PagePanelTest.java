package org.pdf.forms.gui.properties.object.page;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;

class PagePanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new PagePanel(getDesignerPanel());
    }
}
