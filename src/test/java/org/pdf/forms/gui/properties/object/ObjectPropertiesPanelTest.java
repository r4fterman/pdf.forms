package org.pdf.forms.gui.properties.object;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.gui.designer.DesignerMock;

class ObjectPropertiesPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new ObjectPropertiesPanel(new DesignerMock());
    }
}
