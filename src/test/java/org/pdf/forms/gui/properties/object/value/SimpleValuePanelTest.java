package org.pdf.forms.gui.properties.object.value;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.widgets.IWidget;

class SimpleValuePanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new SimpleValuePanel(IWidget.TEXT_FIELD);
    }
}
