package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
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

        final JLabel positionLabel = new JLabel("Position:");

        captionLocationBox = new JComboBox<>(CAPTIONS);
        captionLocationBox.addActionListener(this::updateCaptionPosition);

        final JLabel reserveLabel = new JLabel("Reserve:");
        reserveLabel.setEnabled(false);

        final JTextField reserveBox = new JTextField();
        reserveBox.setEnabled(false);

        final GroupLayout groupLayout = new GroupLayout(this);
        setLayout(groupLayout);

        final GroupLayout.ParallelGroup horizontalGroup = groupLayout
                .createParallelGroup(LEADING)
                .add(groupLayout.createSequentialGroup()
                             .add(positionLabel)
                             .addPreferredGap(RELATED)
                             .add(captionLocationBox, PREFERRED_SIZE, 68, PREFERRED_SIZE)
                             .add(22, 22, 22)
                             .add(reserveLabel).addPreferredGap(RELATED)
                             .add(reserveBox, PREFERRED_SIZE, 66, PREFERRED_SIZE));
        groupLayout.setHorizontalGroup(horizontalGroup);

        final GroupLayout.ParallelGroup verticalGroup = groupLayout
                .createParallelGroup(LEADING)
                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                             .add(positionLabel)
                             .add(reserveLabel)
                             .add(captionLocationBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                             .add(reserveBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE));
        groupLayout.setVerticalGroup(verticalGroup);
    }

    @SuppressWarnings("unchecked")
    private void updateCaptionPosition(final ActionEvent event) {
        final String captionPosition = (String) ((JComboBox<String>) event.getSource()).getSelectedItem();
        if (captionPosition == null) {
            return;
        }

        widgetsAndProperties.entrySet().stream()
                .filter(entry -> entry.getKey().isComponentSplit())
                .forEach(entry -> {
                    final Element widgetProperties = entry.getValue();
                    final Optional<Element> position = XMLUtils.getPropertyElement(widgetProperties, "Position");
                    if (position.isPresent()) {
                        final Element captionPositionElement = position.get();
                        captionPositionElement.getAttributeNode("value").setValue(captionPosition);

                        entry.getKey().setLayoutProperties(widgetProperties);
                    }
                });

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final boolean isComponentSplit = isComponentSplit(widgetsAndProperties);
        captionLocationBox.setEnabled(isComponentSplit);

        String captionPositionToUse = null;
        for (final Map.Entry<IWidget, Element> entry: widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element props = entry.getValue();
            final boolean componentSplit = widget.isComponentSplit();

            final String captionPosition = getCaptionPosition(props, componentSplit);
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

    private String getCaptionPosition(
            final Element props,
            final boolean componentSplit) {
        if (componentSplit) {
            final Element caption = (Element) props.getElementsByTagName("caption").item(0);
            final Optional<String> position = XMLUtils.getAttributeFromChildElement(caption, "Position");
            if (position.isPresent()) {
                return position.get();
            }
        }
        return null;
    }

    private boolean isComponentSplit(final Map<IWidget, Element> widgetsAndProperties) {
        return widgetsAndProperties.entrySet().stream()
                .anyMatch(entry -> entry.getKey().isComponentSplit());
    }
}
