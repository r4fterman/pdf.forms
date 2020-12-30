package org.pdf.forms.gui.properties.object.field;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.gui.designer.DesignerMock;

class RadioButtonFieldPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new RadioButtonFieldPanel(new DesignerMock());
    }
}
