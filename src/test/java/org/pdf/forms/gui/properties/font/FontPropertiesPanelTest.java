package org.pdf.forms.gui.properties.font;

import javax.swing.JPanel;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.UIPanelTest;

class FontPropertiesPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        final FontHandler fontHandler = new FontHandler();
        return new FontPropertiesPanel(fontHandler);
    }
}
