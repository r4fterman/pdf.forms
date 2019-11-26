package org.pdf.forms.gui.properties.object.field;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.widgets.IWidget;

class ListFieldPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new ListFieldPanel(IWidget.LIST_BOX);
    }
}
