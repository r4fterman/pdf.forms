package org.pdf.forms.gui.properties.paragraph;

import javax.swing.JPanel;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.gui.designer.DesignerMock;

class ParagraphPropertiesPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new ParagraphPropertiesPanel(new DesignerMock());
    }
}
