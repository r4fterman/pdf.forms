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
import java.util.Map;
import java.util.Optional;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class ParagraphPropertiesPanel extends JPanel {

    private static final String[] EDITING_MODES = {"Caption and Value", "Caption properties", "Value properties"};

    private final Logger logger = LoggerFactory.getLogger(ParagraphPropertiesPanel.class);
    private final IDesigner designerPanel;

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
        final String alignment = ((JComponent) evt.getSource()).getName();

        for (final Map.Entry<IWidget, Element> entry: widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element paragraphElement = entry.getValue();

            final Object selectedItem = currentlyEditingBox.getSelectedItem();
            if ("Caption and Value".equals(selectedItem)) {
                setCaptionAndValueAlignment(paragraphElement, propertyName, alignment, widget);
            } else if ("Caption properties".equals(selectedItem)) {
                setCaptionPropertiesAlignment(paragraphElement, propertyName, alignment);
            } else if ("Value properties".equals(selectedItem)) {
                setValuePropertiesAlignment(paragraphElement, propertyName, alignment, widget);
            } else {
                logger.warn("Unexpected selected item {}", selectedItem);
            }

            widget.setParagraphProperties(paragraphElement, currentlyEditingBox.getSelectedIndex());
        }

        designerPanel.getMainFrame().setPropertiesToolBar(widgetsAndProperties.keySet());
        designerPanel.repaint();
    }

    private void setValuePropertiesAlignment(
            final Element paragraphElement,
            final String propertyName,
            final String alignment,
            final IWidget widget) {
        final List<Element> paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

        final Optional<Element> valueAlignment = getValueAlignment(widget, paragraphList.get(1), propertyName);
        valueAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
    }

    private void setCaptionPropertiesAlignment(
            final Element paragraphElement,
            final String propertyName,
            final String alignment) {
        final List<Element> paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

        final Element captionElement = paragraphList.get(0);
        final Optional<Element> captionAlignment = XMLUtils.getPropertyElement(captionElement, propertyName);
        captionAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
    }

    private void setCaptionAndValueAlignment(
            final Element paragraphElement,
            final String propertyName,
            final String alignment,
            final IWidget widget) {
        final List<Element> paragraphList = XMLUtils.getElementsFromNodeList(paragraphElement.getChildNodes());

        final Optional<Element> valueAlignment = getValueAlignment(widget, paragraphList.get(1), propertyName);

        final Element captionElement = paragraphList.get(0);
        final Optional<Element> captionAlignment = XMLUtils.getPropertyElement(captionElement, propertyName);
        captionAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
        valueAlignment.ifPresent(element -> element.getAttributeNode("value").setValue(alignment));
    }

    public void setProperties(
            final Map<IWidget, Element> widgetsAndProperties,
            final int currentlyEditing) {
        this.widgetsAndProperties = widgetsAndProperties;

        boolean allowEditCaptionAndValue = false;
        for (final IWidget widget: widgetsAndProperties.keySet()) {
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

        final String horizontalAlignmentToUse = getHorizontalAlignment(currentlyEditing, widgetsAndProperties);
        selectHorizontalAlignment(horizontalAlignmentToUse);

        final String verticalAlignmentToUse = getVerticalAlignment(currentlyEditing, widgetsAndProperties);
        selectVerticalAlignment(verticalAlignmentToUse);
    }

    private String getHorizontalAlignment(
            final int currentlyEditing,
            final Map<IWidget, Element> widgetsAndProperties) {
        final List<String> horizontalAlignmentValues = widgetsAndProperties.entrySet().stream()
                .map(entry -> getAlignment(
                        currentlyEditing,
                        entry.getKey(),
                        entry.getValue(),
                        "Horizontal Alignment",
                        "left"))
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(horizontalAlignmentValues, horizontalAlignmentValues.get(0)) == horizontalAlignmentValues
                .size();
        if (listContainsOnlyEqualValues) {
            return horizontalAlignmentValues.get(0);
        }
        return "mixed";
    }

    private String getVerticalAlignment(
            final int currentlyEditing,
            final Map<IWidget, Element> widgetsAndProperties) {
        final List<String> verticalAlignmentValues = widgetsAndProperties.entrySet().stream()
                .map(entry -> getAlignment(
                        currentlyEditing,
                        entry.getKey(),
                        entry.getValue(),
                        "Vertical Alignment",
                        "top"))
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(verticalAlignmentValues, verticalAlignmentValues.get(0)) == verticalAlignmentValues.size();
        if (listContainsOnlyEqualValues) {
            return verticalAlignmentValues.get(0);
        }
        return "mixed";
    }

    private String getAlignment(
            final int currentlyEditing,
            final IWidget widget,
            final Element paragraphPropertiesElement,
            final String attributeName,
            final String defaultValue) {
        final String alignmentCaption = getPropertyValue(
                paragraphPropertiesElement,
                "paragraph_caption",
                attributeName,
                defaultValue);

        final String alignmentValue;
        if (widget.allowEditCaptionAndValue()) {
            alignmentValue = getPropertyValue(
                    paragraphPropertiesElement,
                    "paragraph_value",
                    attributeName,
                    defaultValue);
        } else {
            alignmentValue = alignmentCaption;
        }

        return getAlignment(
                currentlyEditing,
                alignmentCaption,
                alignmentValue);
    }

    private Optional<Element> getValueAlignment(
            final IWidget widget,
            final Element paragraphElement,
            final String propertyName) {
        if (!widget.allowEditCaptionAndValue()) {
            return Optional.empty();
        }

        final Optional<Element> valueElement;
        if (widget.allowEditCaptionAndValue()) {
            valueElement = Optional.ofNullable(paragraphElement);
        } else {
            valueElement = Optional.empty();
        }

        return valueElement
                .map(element -> XMLUtils.getPropertyElement(element, propertyName))
                .flatMap(element -> element);
    }

    private void selectHorizontalAlignment(final String horizontalAlignmentToUse) {
        if ("mixed".equals(horizontalAlignmentToUse)) {
            horizontalAlignmentButtonGroup.clearSelection();
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

    private String getAlignment(
            final int currentlyEditing,
            final String captionAlignment,
            final String valueAlignment) {
        if (currentlyEditing == IWidget.COMPONENT_BOTH) {
            if (captionAlignment.equals(valueAlignment)) {
                return captionAlignment;
            } else {
                // caption and value are different so use hack to push all buttons out
                return "mixed";
            }
        } else if (currentlyEditing == IWidget.COMPONENT_CAPTION) {
            return captionAlignment;
        } else if (currentlyEditing == IWidget.COMPONENT_VALUE) {
            return valueAlignment;
        }

        return "";
    }

    private String getPropertyValue(
            final Element propertiesElement,
            final String propertiesName,
            final String attributeName,
            final String defaultValue) {
        final Element propertyElement = (Element) propertiesElement.getElementsByTagName(propertiesName).item(0);
        return XMLUtils.getAttributeValueFromChildElement(propertyElement, attributeName).orElse(defaultValue);
    }

}
