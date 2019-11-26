package org.pdf.forms.gui.library;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;

class LibraryPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new LibraryPanel(getDesignerPanel());
    }
}
