package org.pdf.forms.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.Designer;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.windows.SplashWindow;
import org.pdf.forms.widgets.utils.WidgetFactory;

public abstract class UIPanelTest {

    private Designer designerPanel;

    @BeforeEach
    void setUp() {
        final String version = "DEV-TEST";

        final Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        final Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);

        final SplashWindow splashWindow = new SplashWindow(version);

        final Configuration configuration = new Configuration();
        final FontHandler fontHandler = new FontHandler(configuration);
        final WidgetFactory widgetFactory = new WidgetFactory(fontHandler);
        final IMainFrame mainFrame = new VLFrame(splashWindow, version, fontHandler, widgetFactory, configuration);
        this.designerPanel = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, mainFrame, version, fontHandler, widgetFactory, configuration);
    }

    @Test
    void buildAndShowPanel() throws Exception {
        final JFrame frame = new JFrame();

        frame.getContentPane().add(createPanel());

        frame.pack();
        frame.setVisible(true);

        Thread.sleep(1000);

        frame.dispose();
    }

    protected Designer getDesignerPanel() {
        return designerPanel;
    }

    protected abstract JPanel createPanel();
}
