package org.pdf.forms.gui.properties.font;

import javax.swing.JPanel;

import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.UIPanelTest;

class FontPropertiesPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        final Configuration configuration = new Configuration();
        final FontHandler fontHandler = new FontHandler(configuration);
        return new FontPropertiesPanel(fontHandler);
    }
}
