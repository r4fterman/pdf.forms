package org.pdf.forms.gui.properties.font;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class FontPropertiesTab extends JPanel implements Dockable {

    private final FontPropertiesPanel fontPanel;

    public FontPropertiesTab(
            final IDesigner designer,
            final FontHandler fontHandler) {
        this.fontPanel = new FontPropertiesPanel(fontHandler);
        fontPanel.setDesignerPanel(designer);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || widgets.iterator().next() instanceof Page) {
            removeAll();
        } else {
            //todo: for an image all component on this container were removed???
            //if (widget.getType() == IWidget.IMAGE) {
            //    removeAll();
            fontPanel.setProperties(widgets, 0);
            add(fontPanel);
        }

        updateUI();
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("font", "Font", "Set font properties");
    }

    @Override
    public Component getComponent() {
        return this;
    }

    public void updateAvailableFonts() {
        fontPanel.updateAvailableFonts();
    }
}
