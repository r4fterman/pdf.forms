package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.model.des.Caption;
import org.pdf.forms.widgets.IWidget;

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
    private Set<IWidget> widgets;

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

        widgets.stream()
                .filter(IWidget::isComponentSplit)
                .forEach(widget -> {
                    final Caption caption = widget.getWidgetModel().getProperties().getLayout().getCaption();
                    caption.setPosition(captionPosition);
                });

        designerPanel.repaint();
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final boolean isComponentSplit = isComponentSplit(widgets);
        captionLocationBox.setEnabled(isComponentSplit);

        if (isComponentSplit) {
            final String captionPositionToUse = getCaptionPositionToUse(widgets);
            if (captionPositionToUse.equals("mixed")) {
                captionLocationBox.setSelectedItem(null);
            } else {
                captionLocationBox.setSelectedItem(captionPositionToUse);
            }
        } else {
            captionLocationBox.setSelectedItem(null);
        }
    }

    private String getCaptionPositionToUse(final Set<IWidget> widgets) {
        final List<String> captionValues = widgets.stream()
                .map(widget -> {
                    if (widget.isComponentSplit()) {
                        return widget.getWidgetModel().getProperties().getLayout().getCaption().getPosition()
                                .orElse("");
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

    private boolean isComponentSplit(final Set<IWidget> widgets) {
        return widgets.stream()
                .anyMatch(IWidget::isComponentSplit);
    }
}
