package org.pdf.forms.gui.properties.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
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

public class BorderPropertiesTab extends JPanel implements Dockable {

    private final BorderPropertiesPanel borderPanel = new BorderPropertiesPanel();
    private final DockKey key = new DockKey("Border");

    public BorderPropertiesTab(final IDesigner designer) {
        borderPanel.setDesignerPanel(designer);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || (widgets.iterator().next() instanceof Page)) {
            removeAll();
        } else {
            final Map<IWidget, Element> widgetsAndProperties = new HashMap<>();
            for (final IWidget widget : widgets) {
                if (widget.getType() == IWidget.IMAGE) {
                    removeAll();
                    return;
                }

                final Document properties = widget.getProperties();
                final Element layoutProperties = (Element) properties.getElementsByTagName("border").item(0);
                widgetsAndProperties.put(widget, layoutProperties);
            }

            borderPanel.setProperties(widgetsAndProperties);
            add(borderPanel);
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
