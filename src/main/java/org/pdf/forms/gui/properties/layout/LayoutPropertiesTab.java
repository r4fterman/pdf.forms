package org.pdf.forms.gui.properties.layout;

import static java.util.stream.Collectors.toUnmodifiableMap;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            final Map<IWidget, Element> widgetsAndProperties = widgets.stream()
                    .collect(toUnmodifiableMap(
                            widget -> widget,
                            widget -> {
                                final Document properties = widget.getProperties();
                                return (Element) properties.getElementsByTagName("layout").item(0);
                            }
                    ));

            layoutPanel.setProperties(widgetsAndProperties);
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
