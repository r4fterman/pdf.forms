package org.pdf.forms.gui.properties.paragraph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParagraphPropertiesTab extends JPanel implements Dockable {

    private final ParagraphPropertiesPanel paragraphPanel = new ParagraphPropertiesPanel();

    public ParagraphPropertiesTab(final IDesigner designer) {
        paragraphPanel.setDesignerPanel(designer);

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
                final Element layoutProperties = (Element) properties.getElementsByTagName("paragraph").item(0);
                widgetsAndProperties.put(widget, layoutProperties);
            }

            paragraphPanel.setProperties(widgetsAndProperties, 0);
            add(paragraphPanel);
        }

        updateUI();
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("paragraph", "Paragraph", "Set paragraph properties");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
