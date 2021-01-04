package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class CaptionPanel extends JPanel {

    private static final String[] CAPTIONS = {
            "Left",
            "Right",
            "Top",
            "Bottom",
            "None"
    };

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

    private void updateCaptionPosition(final ActionEvent event) {
        final String captionPosition = (String) captionLocationBox.getSelectedItem();
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

        if (isComponentSplit) {
            final String captionPositionToUse = getCaptionPositionToUse(widgetsAndProperties);
            if (captionPositionToUse.equals("mixed")) {
                captionLocationBox.setSelectedItem(null);
            } else {
                captionLocationBox.setSelectedItem(captionPositionToUse);
            }
        } else {
            captionLocationBox.setSelectedItem(null);
        }
    }

    private String getCaptionPositionToUse(final Map<IWidget, Element> widgetsAndProperties) {
        final List<String> captionValues = widgetsAndProperties.entrySet().stream()
                .map(entry -> {
                    final IWidget widget = entry.getKey();
                    final Element widgetProperties = entry.getValue();
                    if (widget.isComponentSplit()) {
                        final Element caption = (Element) widgetProperties.getElementsByTagName("caption").item(0);
                        return XMLUtils.getAttributeValueFromChildElement(caption, "Position").orElse("");
                    }
                    return "";
                })
                .collect(Collectors.toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(captionValues, captionValues.get(0)) == captionValues.size();
        if (listContainsOnlyEqualValues) {
            return captionValues.get(0);
        }
        return "mixed";

    }

    private boolean isComponentSplit(final Map<IWidget, Element> widgetsAndProperties) {
        return widgetsAndProperties.entrySet().stream()
                .anyMatch(entry -> entry.getKey().isComponentSplit());
    }
}
