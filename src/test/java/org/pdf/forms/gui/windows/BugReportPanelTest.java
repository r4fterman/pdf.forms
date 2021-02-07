package org.pdf.forms.gui.windows;

import java.util.LinkedHashMap;

import javax.swing.*;

import org.pdf.forms.gui.UIPanelTest;

class BugReportPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        final JFrame parent = new JFrame();
        final JDialog parentDialog = new JDialog(parent);
        return new BugReportPanel(new LinkedHashMap<>(), parentDialog);
    }
}
