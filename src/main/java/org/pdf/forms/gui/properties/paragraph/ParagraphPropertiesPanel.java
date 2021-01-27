package org.pdf.forms.gui.properties.paragraph;

import static java.util.stream.Collectors.toUnmodifiableList;
import static javax.swing.SwingConstants.VERTICAL;
import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.model.des.Properties;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParagraphPropertiesPanel extends JPanel {

    private static final String[] EDITING_MODES = {
            "Caption and Value",
            "Caption properties",
            "Value properties"
    };

    private final Logger logger = LoggerFactory.getLogger(ParagraphPropertiesPanel.class);
    private final IDesigner designerPanel;

    private Set<IWidget> widgets;

    private ButtonGroup horizontalAlignmentButtonGroup;
    private ButtonGroup verticalAlignmentButtonGroup;
    private JComboBox<String> currentlyEditingBox;
    private JToggleButton horizontalAlignJustify;

    private JToggleButton horizontalAlignLeft;
    private JToggleButton horizontalAlignCenter;
    private JToggleButton horizontalAlignRight;
    private JToggleButton verticalAlignTop;
    private JToggleButton verticalAlignCenter;
    private JToggleButton verticalAlignBottom;

    public ParagraphPropertiesPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
        initComponents();
        horizontalAlignJustify.setVisible(false);
    }

    private void initComponents() {
        final JLabel currentlyEditingLabel = new JLabel("Currently Editing:");

        currentlyEditingBox = createEditingBox();
        horizontalAlignLeft = createAlignHorizontalLeftButton();
        horizontalAlignCenter = createAlignHorizontalCenterButton();
        horizontalAlignRight = createAlignHorizontalRightButton();
        horizontalAlignJustify = createAlignHorizontalJustify();

        horizontalAlignmentButtonGroup = new ButtonGroup();
        horizontalAlignmentButtonGroup.add(horizontalAlignLeft);
        horizontalAlignmentButtonGroup.add(horizontalAlignCenter);
        horizontalAlignmentButtonGroup.add(horizontalAlignRight);
        horizontalAlignmentButtonGroup.add(horizontalAlignJustify);

        final JSeparator separator = new JSeparator(VERTICAL);

        verticalAlignTop = createAlignVerticalTopButton();
        verticalAlignCenter = createAlignVerticalCenterButton();
        verticalAlignBottom = createAlignVerticalBottomButton();

        verticalAlignmentButtonGroup = new ButtonGroup();
        verticalAlignmentButtonGroup.add(verticalAlignTop);
        verticalAlignmentButtonGroup.add(verticalAlignCenter);
        verticalAlignmentButtonGroup.add(verticalAlignBottom);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(currentlyEditingLabel)
                                                .addPreferredGap(RELATED)
                                                .add(currentlyEditingBox, PREFERRED_SIZE, 162, PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(horizontalAlignLeft, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(horizontalAlignCenter, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(horizontalAlignRight, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(horizontalAlignJustify, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(separator, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(verticalAlignTop, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(verticalAlignCenter, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .add(verticalAlignBottom, PREFERRED_SIZE, 25, PREFERRED_SIZE)))
                                .addContainerGap(140, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(BASELINE)
                                        .add(currentlyEditingLabel)
                                        .add(currentlyEditingBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addPreferredGap(RELATED)
                                .add(layout.createParallelGroup(LEADING)
                                        .add(layout.createParallelGroup(BASELINE)
                                                .add(horizontalAlignLeft, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .add(horizontalAlignCenter, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .add(horizontalAlignRight, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .add(horizontalAlignJustify, PREFERRED_SIZE, 25, PREFERRED_SIZE))
                                        .add(layout.createParallelGroup(BASELINE)
                                                .add(verticalAlignTop, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .add(verticalAlignCenter, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .add(verticalAlignBottom, PREFERRED_SIZE, 25, PREFERRED_SIZE))
                                        .add(separator, PREFERRED_SIZE, 24, PREFERRED_SIZE))
                                .addContainerGap(236, Short.MAX_VALUE))
        );
    }

    private JToggleButton createAlignVerticalTopButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Top.gif")));
        button.setToolTipText("Vertical Align Top");
        button.setName("top");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignVerticalCenterButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Middle.gif")));
        button.setToolTipText("Vertical Align Center");
        button.setName("center");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignVerticalBottomButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Bottom.gif")));
        button.setToolTipText("Vertical Align Bottom");
        button.setName("bottom");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalLeftButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Left.gif")));
        button.setToolTipText("Horizontal Align Left");
        button.setName("left");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalCenterButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Center.gif")));
        button.setToolTipText("Horizontal Align Center");
        button.setName("center");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalRightButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Right.gif")));
        button.setToolTipText("Horizontal Align Right");
        button.setName("right");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalJustify() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Paragraph Align Justify.gif")));
        button.setToolTipText("Horizontal Align Justify");
        button.setName("justify");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JComboBox<String> createEditingBox() {
        final JComboBox<String> comboBox = new JComboBox<>(EDITING_MODES);
        comboBox.addActionListener(this::updateCurrentlyEditingBox);
        return comboBox;
    }

    private void updateCurrentlyEditingBox(final ActionEvent evt) {
        setProperties(widgets, currentlyEditingBox.getSelectedIndex());
    }

    private void updateVerticalAlignment(final ActionEvent evt) {
        final String alignment = ((JComponent) evt.getSource()).getName();

        widgets.forEach(widget -> {
            final Object selectedItem = currentlyEditingBox.getSelectedItem();
            if ("Caption and Value".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphCaption().setVerticalAlignment(
                        alignment);
                widget.getWidgetModel().getProperties().getParagraph().getParagraphValue().setVerticalAlignment(
                        alignment);
            } else if ("Caption properties".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphCaption().setVerticalAlignment(
                        alignment);
            } else if ("Value properties".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphValue().setVerticalAlignment(
                        alignment);
            } else {
                logger.warn("Unexpected selected item {}", selectedItem);
            }

            widget.setParagraphProperties(currentlyEditingBox.getSelectedIndex());
        });

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);
        designerPanel.repaint();
    }

    private void updateHorizontalAlignment(final ActionEvent evt) {
        final String alignment = ((JComponent) evt.getSource()).getName();

        widgets.forEach(widget -> {
            final Object selectedItem = currentlyEditingBox.getSelectedItem();
            if ("Caption and Value".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphCaption().setHorizontalAlignment(
                        alignment);
                widget.getWidgetModel().getProperties().getParagraph().getParagraphValue().setHorizontalAlignment(
                        alignment);
            } else if ("Caption properties".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphCaption().setHorizontalAlignment(
                        alignment);
            } else if ("Value properties".equals(selectedItem)) {
                widget.getWidgetModel().getProperties().getParagraph().getParagraphValue().setHorizontalAlignment(
                        alignment);
            } else {
                logger.warn("Unexpected selected item {}", selectedItem);
            }

            widget.setParagraphProperties(currentlyEditingBox.getSelectedIndex());
        });

        designerPanel.getMainFrame().setPropertiesToolBar(widgets);
        designerPanel.repaint();
    }

    public void setProperties(
            final Set<IWidget> widgets,
            final int currentlyEditing) {
        this.widgets = widgets;

        final boolean allowEditCaptionAndValue = widgets.stream().anyMatch(IWidget::allowEditCaptionAndValue);

        final int editing;
        if (allowEditCaptionAndValue) {
            editing = currentlyEditing;
        } else {
            editing = 1;
        }

        currentlyEditingBox.setSelectedIndex(editing);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        final String horizontalAlignmentToUse = getHorizontalAlignment(currentlyEditing, widgets);
        selectHorizontalAlignment(horizontalAlignmentToUse);

        final String verticalAlignmentToUse = getVerticalAlignment(currentlyEditing, widgets);
        selectVerticalAlignment(verticalAlignmentToUse);
    }

    private String getHorizontalAlignment(
            final int currentlyEditing,
            final Set<IWidget> widgets) {
        final List<String> horizontalAlignmentValues = widgets.stream()
                .map(widget -> {
                    final Properties properties = widget.getWidgetModel().getProperties();

                    final String horizontalAlignmentCaption = properties.getParagraph().getParagraphValue()
                            .getHorizontalAlignment().orElse("left");
                    final String horizontalAlignmentValue = properties.getParagraph().getParagraphCaption()
                            .getHorizontalAlignment().orElse("left");

                    return getAlignmentValue(horizontalAlignmentCaption,
                            horizontalAlignmentValue,
                            widget,
                            currentlyEditing
                    );
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(horizontalAlignmentValues);
    }

    private String getVerticalAlignment(
            final int currentlyEditing,
            final Set<IWidget> widgets) {
        final List<String> verticalAlignmentValues = widgets.stream()
                .map(widget -> {
                    final Properties properties = widget.getWidgetModel().getProperties();

                    final String verticalAlignmentCaption = properties.getParagraph().getParagraphCaption()
                            .getVerticalAlignment().orElse("top");
                    final String verticalAlignmentValue = properties.getParagraph().getParagraphValue()
                            .getVerticalAlignment().orElse("top");

                    return getAlignmentValue(verticalAlignmentCaption, verticalAlignmentValue, widget, currentlyEditing
                    );
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(verticalAlignmentValues);
    }

    private String getAlignmentValue(
            final String verticalAlignmentCaption,
            final String verticalAlignmentValue,
            final IWidget widget,
            final int currentlyEditing) {
        final String alignmentValue = getAlignmentForWidget(verticalAlignmentCaption, verticalAlignmentValue, widget);
        if (currentlyEditing == IWidget.COMPONENT_BOTH) {
            if (verticalAlignmentCaption.equals(alignmentValue)) {
                return verticalAlignmentCaption;
            }
            // caption and value are different so use hack to push all buttons out
            return "mixed";
        }
        if (currentlyEditing == IWidget.COMPONENT_CAPTION) {
            return verticalAlignmentCaption;
        }
        if (currentlyEditing == IWidget.COMPONENT_VALUE) {
            return verticalAlignmentValue;
        }
        return "";
    }

    private String getAlignmentForWidget(
            final String verticalAlignmentCaption,
            final String verticalAlignmentValue,
            final IWidget widget) {
        if (widget.allowEditCaptionAndValue()) {
            return verticalAlignmentValue;
        }
        return verticalAlignmentCaption;
    }

    private void selectHorizontalAlignment(final String horizontalAlignmentToUse) {
        switch (horizontalAlignmentToUse) {
            case "mixed":
                horizontalAlignmentButtonGroup.clearSelection();
                break;
            case "left":
                horizontalAlignLeft.setSelected(true);
                break;
            case "right":
                horizontalAlignRight.setSelected(true);
                break;
            case "center":
                horizontalAlignCenter.setSelected(true);
                break;
            case "justify":
                horizontalAlignJustify.setSelected(true);
                break;
            default:
                logger.warn("Unexpected horizontal alignment {}", horizontalAlignmentToUse);
                break;
        }
    }

    private void selectVerticalAlignment(final String verticalAlignmentToUse) {
        if ("mixed".equals(verticalAlignmentToUse)) {
            verticalAlignmentButtonGroup.clearSelection();
        } else if ("top".equals(verticalAlignmentToUse)) {
            verticalAlignTop.setSelected(true);
        } else if ("bottom".equals(verticalAlignmentToUse)) {
            verticalAlignBottom.setSelected(true);
        } else if ("center".equals(verticalAlignmentToUse)) {
            verticalAlignCenter.setSelected(true);
        } else {
            logger.warn("Unexpected vertical alignment {}", verticalAlignmentToUse);
        }
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues = Collections.frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }
}
