package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class CaptionPanel extends JPanel {

    private static final String[] CAPTIONS = {"Left", "Right", "Top", "Bottom", "None"};

    private final IDesigner designerPanel;

    private JComboBox<String> captionLocationBox;
    private Map<IWidget, Element> widgetsAndProperties;

    public CaptionPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initializePanel();
    }

    private void initializePanel() {
        setBorder(BorderFactory.createTitledBorder("Caption"));

        final JLabel positionLabel = new JLabel();
        positionLabel.setText("Position:");

        captionLocationBox = new JComboBox<>();
        captionLocationBox.setModel(new DefaultComboBoxModel<>(CAPTIONS));
        captionLocationBox.addActionListener(this::updateCaptionPosition);

        final JLabel reserveLabel = new JLabel();
        reserveLabel.setText("Reserve:");
        reserveLabel.setEnabled(false);

        final JTextField reserveBox = new JTextField();
        reserveBox.setEnabled(false);

        final org.jdesktop.layout.GroupLayout groupLayout = new org.jdesktop.layout.GroupLayout(this);
        setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(positionLabel)
                                .addPreferredGap(RELATED)
                                .add(captionLocationBox, PREFERRED_SIZE, 68, PREFERRED_SIZE)
                                .add(22, 22, 22)
                                .add(reserveLabel)
                                .addPreferredGap(RELATED)
                                .add(reserveBox, PREFERRED_SIZE, 66, PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(positionLabel)
                                .add(reserveLabel)
                                .add(captionLocationBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .add(reserveBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
        );
    }

    private void updateCaptionPosition(final ActionEvent evt) {
        final String captionPosition = (String) ((JComboBox<String>) evt.getSource()).getSelectedItem();
        if (captionPosition != null) {
            widgetsAndProperties.entrySet().stream()
                    .filter(entry -> entry.getKey().isComponentSplit())
                    .forEach(entry -> {
                        final Element widgetProperties = entry.getValue();
                        final Element captionPositionElement = XMLUtils.getPropertyElement(widgetProperties, "Position").get();
                        captionPositionElement.getAttributeNode("value").setValue(captionPosition);
                        entry.getKey().setLayoutProperties(widgetProperties);
                    });
            designerPanel.repaint();
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        boolean isComponentSplit = false;
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.isComponentSplit()) {
                isComponentSplit = true;
                break;
            }
        }
        /* set the caption alignment box */
        captionLocationBox.setEnabled(isComponentSplit);

        String captionPositionToUse = null;
        for (final Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element props = entry.getValue();
            final boolean componentSplit = widget.isComponentSplit();

            /* add caption properties */
            String captionPosition = null;
            if (componentSplit) {
                final Element caption = (Element) props.getElementsByTagName("caption").item(0);
                captionPosition = XMLUtils.getAttributeFromChildElement(caption, "Position").get();
            }

            if (captionPositionToUse == null) {
                captionPositionToUse = captionPosition;
            } else {
                if (componentSplit && !captionPositionToUse.equals(captionPosition)) {
                    captionPositionToUse = "mixed";
                }
            }
        }

        if (isComponentSplit) {
            final Object selectedItem;
            if ("mixed".equals(captionPositionToUse)) {
                selectedItem = null;
            } else {
                selectedItem = captionPositionToUse;
            }
            captionLocationBox.setSelectedItem(selectedItem);
        } else {
            captionLocationBox.setSelectedItem(null);
        }
    }
}
