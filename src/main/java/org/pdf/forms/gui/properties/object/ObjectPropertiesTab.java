package org.pdf.forms.gui.properties.object;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class ObjectPropertiesTab extends JPanel implements Dockable {

    private final ObjectPropertiesPanel objectPanel = new ObjectPropertiesPanel();

    public ObjectPropertiesTab(final IDesigner designer) {
        objectPanel.setDesignerPanel(designer);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        removeAll();

        if (!widgets.isEmpty()) {
            final int type = getWidgetType(List.copyOf(widgets));
            if (type == -1) {
                add(new JLabel("Mixed components"));
            } else {
                objectPanel.setProperties(widgets, type);
                add(objectPanel);
            }
        }

        updateUI();
    }

    private int getWidgetType(final List<IWidget> widgets) {
        final int firstType = widgets.get(0).getType();
        if (widgets.stream().allMatch(widget -> widget.getType() == firstType)) {
            return firstType;
        }
        return -1;
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("object", "Object", "Set object properties");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
