package org.pdf.forms.gui.properties.paragraph;

import static javax.swing.SwingConstants.VERTICAL;
import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class ParagraphPropertiesPanel extends JPanel {

    private final Logger logger = LoggerFactory.getLogger(ParagraphPropertiesPanel.class);

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

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

    ParagraphPropertiesPanel() {
        initComponents();
        horizontalAlignJustify.setVisible(false);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JLabel currentlyEditingLabel = new JLabel();
        currentlyEditingLabel.setText("Currently Editing:");

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
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Top.gif")));
        button.setToolTipText("Vertical Align Top");
        button.setName("top");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignVerticalCenterButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Middle.gif")));
        button.setToolTipText("Vertical Align Center");
        button.setName("center");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignVerticalBottomButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Bottom.gif")));
        button.setToolTipText("Vertical Align Bottom");
        button.setName("bottom");
        button.addActionListener(this::updateVerticalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalLeftButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Left.gif")));
        button.setToolTipText("Horizontal Align Left");
        button.setName("left");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalCenterButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Center.gif")));
        button.setToolTipText("Horizontal Align Center");
        button.setName("center");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalRightButton() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Right.gif")));
        button.setToolTipText("Horizontal Align Right");
        button.setName("right");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JToggleButton createAlignHorizontalJustify() {
        final JToggleButton button = new JToggleButton(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Paragraph Align Justify.gif")));
        button.setToolTipText("Horizontal Align Justify");
        button.setName("justify");
        button.addActionListener(this::updateHorizontalAlignment);
        return button;
    }

    private JComboBox<String> createEditingBox() {
        final JComboBox<String> comboBox = new JComboBox<>(new String[]{"Caption and Value", "Caption properties", "Value properties"});
        comboBox.addActionListener(this::updateCurrentlyEditingBox);
        return comboBox;
    }

    private void updateCurrentlyEditingBox(final ActionEvent evt) {
        setProperties(widgetsAndProperties, currentlyEditingBox.getSelectedIndex());
    }

    private void updateVerticalAlignment(final ActionEvent evt) {
        updateAlignment("Vertical Alignment", evt);
    }

    private void updateHorizontalAlignment(final ActionEvent evt) {
        updateAlignment("Horizontal Alignment", evt);
    }

    private void updateAlignment(
            final String propertyName,
            final ActionEvent evt) {
        final Set<Map.Entry<IWidget, Element>> entries = widgetsAndProperties.entrySet();
        for (final Map.Entry<IWidget, Element> entry : entries) {
            final IWidget widget = entry.getKey();
            final Element paragraphElement = entry.getValue();

            final List<Element> paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

            final Element captionElement = paragraphList.get(0);
            final Element valueElement;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = paragraphList.get(1);
            } else {
                valueElement = null;
            }

            final Element captionAlignment = XMLUtils.getPropertyElement(captionElement, propertyName).get();
            Optional<Element> valueAlignment = Optional.empty();
            if (widget.allowEditCaptionAndValue()) {
                valueAlignment = XMLUtils.getPropertyElement(valueElement, propertyName);
            }

            final String alignment = ((JComponent) evt.getSource()).getName();

            final Object selectedItem = currentlyEditingBox.getSelectedItem();
            if ("Caption and Value".equals(selectedItem)) {
                captionAlignment.getAttributeNode("value").setValue(alignment);
                valueAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
            } else if ("Caption properties".equals(selectedItem)) {
                captionAlignment.getAttributeNode("value").setValue(alignment);
            } else if ("Value properties".equals(selectedItem)) {
                valueAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
            } else {
                logger.warn("Unexpected selected item {}", selectedItem);
            }

            widget.setParagraphProperties(widgetsAndProperties.get(widget), currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgetsAndProperties.keySet());
        designerPanel.repaint();
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {

        this.widgetsAndProperties = widgetsAndProperties;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.allowEditCaptionAndValue()) {
                allowEditCaptionAndValue = true;
                break;
            }
        }

        final int editing;
        if (allowEditCaptionAndValue) {
            editing = currentlyEditing;
        } else {
            editing = 1;
        }

        currentlyEditingBox.setSelectedIndex(editing);
        currentlyEditingBox.setEnabled(allowEditCaptionAndValue);

        String horizontalAlignmentToUse = null;
        String verticalAlignmentToUse = null;
        for (final Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element paragraphPropertiesElement = entry.getValue();

            /* get caption properties */
            final Element captionElement = (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

            final String captionHorizontalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Horizontal Alignment").orElse("left");
            final String captionVerticalAlignment = XMLUtils.getAttributeFromChildElement(captionElement, "Vertical Alignment").orElse("top");

            final String valueHorizontalAlignment;
            final String valueVerticalAlignment;

            if (widget.allowEditCaptionAndValue()) {
                final Element valueElement = (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_value").item(0);

                valueHorizontalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Horizontal Alignment").orElse("left");
                valueVerticalAlignment = XMLUtils.getAttributeFromChildElement(valueElement, "Vertical Alignment").orElse("top");
            } else {
                valueHorizontalAlignment = captionHorizontalAlignment;
                valueVerticalAlignment = captionVerticalAlignment;
            }

            final String[] alignments = getAlignments(currentlyEditing, captionHorizontalAlignment, valueHorizontalAlignment, captionVerticalAlignment, valueVerticalAlignment);

            final String horizontalAlignment = alignments[0];
            final String verticalAlignment = alignments[1];
            if (horizontalAlignmentToUse == null) {
                horizontalAlignmentToUse = horizontalAlignment;
                verticalAlignmentToUse = verticalAlignment;
            } else {
                if (!horizontalAlignmentToUse.equals(horizontalAlignment)) {
                    horizontalAlignmentToUse = "mixed";
                }
                if (!verticalAlignmentToUse.equals(verticalAlignment)) {
                    verticalAlignmentToUse = "mixed";
                }
            }
        }
        selectHorizontalAlignment(horizontalAlignmentToUse);
        selectVerticalAlignment(verticalAlignmentToUse);
    }

    private void selectHorizontalAlignment(final String horizontalAlignmentToUse) {
        if ("mixed".equals(horizontalAlignmentToUse)) {
            removeButtonGroupSelection(horizontalAlignmentButtonGroup);
        } else if ("left".equals(horizontalAlignmentToUse)) {
            horizontalAlignLeft.setSelected(true);
        } else if ("right".equals(horizontalAlignmentToUse)) {
            horizontalAlignRight.setSelected(true);
        } else if ("center".equals(horizontalAlignmentToUse)) {
            horizontalAlignCenter.setSelected(true);
        } else if ("justify".equals(horizontalAlignmentToUse)) {
            horizontalAlignJustify.setSelected(true);
        } else {
            logger.warn("Unexpected horizontal alignment {}", horizontalAlignmentToUse);
        }
    }

    private void selectVerticalAlignment(final String verticalAlignmentToUse) {
        if ("mixed".equals(verticalAlignmentToUse)) {
            removeButtonGroupSelection(verticalAlignmentButtonGroup);
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

    private void removeButtonGroupSelection(final ButtonGroup buttonGroup) {
        buttonGroup.setSelected(new JToggleButton("").getModel(), true);
    }

    private String[] getAlignments(
            final int currentlyEditing,
            final String captionHorizontalAlignment,
            final String valueHorizontalAlignment,
            final String captionVerticalAlignment,
            final String valueVerticalAlignment) {

        String verticalAlignmentToUse = null;
        String horizontalAlignmentToUse = null;

        switch (currentlyEditing) {
            case IWidget.COMPONENT_BOTH:
                if (captionHorizontalAlignment.equals(valueHorizontalAlignment)) {
                    // both value and caption are the same
                    horizontalAlignmentToUse = captionHorizontalAlignment;
                } else {
                    /* caption and value are different so use hack to push all buttons out */
                    horizontalAlignmentToUse = "mixed";
                }

                if (captionVerticalAlignment.equals(valueVerticalAlignment)) {
                    // both value and caption are the same
                    verticalAlignmentToUse = captionVerticalAlignment;
                } else {
                    /* caption and value are different so use hack to push all buttons out */
                    verticalAlignmentToUse = "mixed";
                }

                break;
            case IWidget.COMPONENT_CAPTION:
                /* just set the caption properties */
                horizontalAlignmentToUse = captionHorizontalAlignment;
                verticalAlignmentToUse = captionVerticalAlignment;

                break;

            case IWidget.COMPONENT_VALUE:
                /* just set the value properties */
                horizontalAlignmentToUse = valueHorizontalAlignment;
                verticalAlignmentToUse = valueVerticalAlignment;

                break;
            default:
                break;
        }

        return new String[]{
                horizontalAlignmentToUse, verticalAlignmentToUse};
    }

}
