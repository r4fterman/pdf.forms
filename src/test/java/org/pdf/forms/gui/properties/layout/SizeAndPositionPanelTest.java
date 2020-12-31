package org.pdf.forms.gui.properties.layout;

import javax.swing.*;

import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.gui.designer.DesignerMock;

class SizeAndPositionPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        return new SizeAndPositionPanel(new DesignerMock());
    }
}