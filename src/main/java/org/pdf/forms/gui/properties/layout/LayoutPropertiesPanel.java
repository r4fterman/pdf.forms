package org.pdf.forms.gui.properties.layout;

import javax.swing.*;
import java.util.Map;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class LayoutPropertiesPanel extends JPanel {

    private final MarginPanel marginPanel;
    private final CaptionPanel captionPanel;
    private final SizeAndPositionPanel sizeAndPositionPanel;

    LayoutPropertiesPanel(final IDesigner designerPanel) {
        this.marginPanel = new MarginPanel();
        this.captionPanel = new CaptionPanel(designerPanel);
        this.sizeAndPositionPanel = new SizeAndPositionPanel(designerPanel);

        initComponents();
    }

    private void initComponents() {
        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(GroupLayout.LEADING, marginPanel, 0, 266, Short.MAX_VALUE)
                                        .add(GroupLayout.LEADING, layout.createParallelGroup(GroupLayout.TRAILING, false)
                                                .add(GroupLayout.LEADING, captionPanel, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                                .add(GroupLayout.LEADING, sizeAndPositionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .add(142, 142, 142))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(sizeAndPositionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(marginPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(captionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(106, Short.MAX_VALUE))
        );
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        sizeAndPositionPanel.setProperties(widgetsAndProperties);
        captionPanel.setProperties(widgetsAndProperties);
    }

}
