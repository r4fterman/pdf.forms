package org.pdf.forms.gui.properties.paragraph;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class ParagraphPropertiesTab extends JPanel implements Dockable {

    private final ParagraphPropertiesPanel paragraphPanel;

    public ParagraphPropertiesTab(final IDesigner designer) {
        this.paragraphPanel = new ParagraphPropertiesPanel(designer);

        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(0, 0));
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty() || (widgets.iterator().next() instanceof Page)) {
            removeAll();
        } else {
            //todo: for an image all component on this container were removed???
//            if (widget.getType() == IWidget.IMAGE) {
//                removeAll();

            paragraphPanel.setProperties(widgets, 0);
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
