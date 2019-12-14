package org.pdf.forms.gui.library;

import javax.swing.JPanel;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.UIPanelTest;
import org.pdf.forms.widgets.utils.WidgetFactory;

class LibraryPanelTest extends UIPanelTest {

    @Override
    protected JPanel createPanel() {
        final FontHandler fontHandler = new FontHandler();
        final WidgetFactory widgetFactory = new WidgetFactory(fontHandler);
        return new LibraryPanel(getDesignerPanel(), widgetFactory);
    }
}
