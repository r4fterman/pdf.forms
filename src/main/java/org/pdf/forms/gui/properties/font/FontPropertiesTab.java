package org.pdf.forms.gui.properties.font;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.*;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            final Map<IWidget, Element> widgetsAndProperties = convertToWidgetsAndProperties(widgets);
            fontPanel.setProperties(widgetsAndProperties, 0);
            add(fontPanel);
        }

        updateUI();
    }

    private Map<IWidget, Element> convertToWidgetsAndProperties(final Set<IWidget> widgets) {
        return widgets.stream()
                .filter(widget -> {
                    if (widget.getType() == IWidget.IMAGE) {
                        removeAll();
                        return false;
                    }
                    return true;
                })
                .collect(toUnmodifiableMap(
                        widget -> widget,
                        widget -> {
                            final Document properties = widget.getProperties();
                            return (Element) properties.getElementsByTagName("font").item(
                                    0);
                        })
                );
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
