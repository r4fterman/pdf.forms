package org.pdf.forms.gui.properties.layout;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class LayoutPropertiesTab extends JPanel implements Dockable {

    private final LayoutPropertiesPanel layoutPanel = new LayoutPropertiesPanel();
    private final DockKey key = new DockKey("Layout");

    public LayoutPropertiesTab(final IDesigner designer) {
        layoutPanel.setDesignerPanel(designer);
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
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
