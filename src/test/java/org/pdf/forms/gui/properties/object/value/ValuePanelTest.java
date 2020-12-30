package org.pdf.forms.gui.properties.object.value;

import javax.swing.*;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.gui.designer.DesignerMock;

class ValuePanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new ValuePanel(new DesignerMock());
    }
}
