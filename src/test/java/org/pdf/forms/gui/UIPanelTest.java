package org.pdf.forms.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pdf.forms.gui.designer.Designer;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.windows.SplashWindow;

@Disabled
public abstract class UIPanelTest {

    private Designer designerPanel;

    @BeforeEach
    void setUp() {
        final Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        final Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);
        final SplashWindow splashWindow = new SplashWindow("DEV-TEST");
        final IMainFrame mainFrame = new VLFrame(splashWindow, "DEV-TEST");
        this.designerPanel = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, mainFrame, "DEV-TEST");
    }

    @Test
    void buildAndShowPanel() throws Exception {
        final JFrame frame = new JFrame();

        frame.getContentPane().add(createPanel());

        frame.pack();
        frame.setVisible(true);

        Thread.sleep(10000);
    }

    public Designer getDesignerPanel() {
        return designerPanel;
    }

    protected abstract JPanel createPanel();
}
