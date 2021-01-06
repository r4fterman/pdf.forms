package org.pdf.forms.gui.properties.font;

import javax.swing.*;

import org.pdf.forms.gui.UIPanelTest;

class FontPropertiesPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new FontPropertiesPanel(getFontHandler());
    }
}
