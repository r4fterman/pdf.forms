package org.pdf.forms.gui.properties.layout;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class LayoutPropertiesTab extends JPanel implements Dockable {

    private final LayoutPropertiesPanel layoutPanel;

    public LayoutPropertiesTab(final IDesigner designer) {
        layoutPanel = new LayoutPropertiesPanel(designer);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || (widgets.iterator().next() instanceof Page)) {
            removeAll();
        } else {
            layoutPanel.setProperties(widgets);
            add(layoutPanel);
        }

        updateUI();
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("layout", "Layout", "Set layout properties");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
