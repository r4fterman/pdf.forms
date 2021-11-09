package org.pdf.forms.gui.properties.border;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class BorderPropertiesTab extends JPanel implements Dockable {

    private final BorderPropertiesPanel borderPanel = new BorderPropertiesPanel();

    public BorderPropertiesTab(final IDesigner designer) {
        borderPanel.setDesignerPanel(designer);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || (widgets.iterator().next() instanceof Page) || (widgets.iterator().next().getType() == IWidget.IMAGE)) {
            removeAll();
        } else {
            borderPanel.setProperties(widgets);
            add(borderPanel);
        }

        updateUI();
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("border", "Border", "Set border properties");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
