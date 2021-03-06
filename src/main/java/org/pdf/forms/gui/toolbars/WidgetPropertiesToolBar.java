package org.pdf.forms.gui.toolbars;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.model.des.FontProperties;
import org.pdf.forms.model.des.ParagraphProperties;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class WidgetPropertiesToolBar extends VLToolBar {

    private static final String[] FONT_SIZES = {
            "6",
            "8",
            "10",
            "12",
            "14",
            "16",
            "18",
            "20",
            "24",
            "28",
            "36",
            "48",
            "72"
    };

    private final Logger logger = LoggerFactory.getLogger(WidgetPropertiesToolBar.class);

    private final IDesigner designerPanel;

    private final ButtonGroup alignmentGroup;
    private final JComboBox<String> fontBox;
    private final JComboBox<String> fontSize;
    private final ToolBarToggleButton fontBold;
    private final ToolBarToggleButton fontItalic;
    private final ToolBarToggleButton alignLeft;
    private final ToolBarToggleButton alignCenter;
    private final ToolBarToggleButton alignRight;

    public WidgetPropertiesToolBar(
            final FontHandler fontHandler,
            final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
        this.fontBox = createFontComboBox(fontHandler);
        this.fontSize = createFontSizeComboBox();
        this.fontBold = createFontBoldButton();
        this.fontItalic = createFontItalicButton();
        this.alignLeft = createAlignLeftButton();
        this.alignCenter = createAlignCenterButton();
        this.alignRight = createAlignRightButton();

        add(fontBox);
        add(fontSize);
        addSeparator();
        add(fontBold);
        add(fontItalic);
        add(createFontUnderlineButton());
        addSeparator();
        add(alignLeft);
        add(alignCenter);
        add(alignRight);

        alignmentGroup = new ButtonGroup();
        alignmentGroup.add(alignLeft);
        alignmentGroup.add(alignCenter);
        alignmentGroup.add(alignRight);
    }

    private ToolBarToggleButton createAlignRightButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Right.gif", "Align Right");
        button.addActionListener(actionEvent -> updateAlignment("right"));
        return button;
    }

    private ToolBarToggleButton createAlignCenterButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Center.gif", "Align Center");
        button.addActionListener(actionEvent -> updateAlignment("center"));
        return button;
    }

    private ToolBarToggleButton createAlignLeftButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Paragraph Align Left.gif", "Align Left");
        button.addActionListener(actionEvent -> updateAlignment("left"));
        return button;
    }

    private ToolBarToggleButton createFontUnderlineButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Underline.gif", "Underline");
        button.setEnabled(false);
        return button;
    }

    private ToolBarToggleButton createFontItalicButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Italic.gif", "Italic");
        button.addActionListener(actionEvent -> updateFont());
        return button;
    }

    private ToolBarToggleButton createFontBoldButton() {
        final ToolBarToggleButton button = new ToolBarToggleButton();
        button.init("/org/pdf/forms/res/Bold.gif", "Bold");
        button.addActionListener(actionEvent -> updateFont());
        return button;
    }

    private JComboBox<String> createFontSizeComboBox() {
        final JComboBox<String> comboBox = new JComboBox<>(FONT_SIZES);
        comboBox.setEditable(true);
        comboBox.setPreferredSize(new Dimension(60, 24));
        comboBox.addActionListener(actionEvent -> updateFont());
        return comboBox;
    }

    private JComboBox<String> createFontComboBox(final FontHandler fontHandler) {
        final JComboBox<String> comboBox = new JComboBox<>(fontHandler.getFontFamilies());
        comboBox.setPreferredSize(new Dimension(160, 24));
        comboBox.addActionListener(actionEvent -> updateFont());
        return comboBox;
    }

    private void updateAlignment(final String alignment) {
        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();
        for (final IWidget widget: widgets) {
            final ParagraphProperties paragraphProperties = widget.getWidgetModel().getProperties().getParagraph();

            paragraphProperties.getParagraphCaption().setHorizontalAlignment(alignment);
            if (widget.allowEditCaptionAndValue()) {
                paragraphProperties.getParagraphValue().setHorizontalAlignment(alignment);
            }
            widget.setParagraphProperties(IWidget.COMPONENT_BOTH);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private void updateFont() {

        final String fontName = (String) fontBox.getSelectedItem();
        final String fontSize = (String) this.fontSize.getSelectedItem();
        final String fontStyle = String.valueOf(getSelectedFontStyle());

        final Set<IWidget> widgets = designerPanel.getSelectedWidgets();
        for (final IWidget widget: widgets) {
            final FontProperties fontProperties = widget.getWidgetModel().getProperties().getFont();

            fontProperties.getFontCaption().setFontName(fontName);
            fontProperties.getFontCaption().setFontSize(fontSize);
            fontProperties.getFontCaption().setFontStyle(fontStyle);

            if (widget.allowEditCaptionAndValue()) {
                fontProperties.getFontValue().setFontName(fontName);
                fontProperties.getFontValue().setFontSize(fontSize);
                fontProperties.getFontValue().setFontStyle(fontStyle);
            }
            widget.setFontProperties(IWidget.COMPONENT_BOTH);
        }

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.repaint();
    }

    private int getSelectedFontStyle() {
        if (fontBold.isSelected()) {
            if (fontItalic.isSelected()) {
                return IWidget.STYLE_BOLDITALIC;
            }
            return IWidget.STYLE_BOLD;
        }

        if (fontItalic.isSelected()) {
            return IWidget.STYLE_ITALIC;
        }
        return 0;
    }

    private void setFontProperties(final Set<IWidget> widgets) {
        selectFontNameInComboBox(widgets);
        selectFontSizeInComboBox(widgets);
        selectFontStyle(widgets);
    }

    private void selectFontNameInComboBox(final Set<IWidget> widgets) {
        final String fontNameToUse = getFontName(widgets);
        if (fontNameToUse.equals("mixed")) {
            setComboValue(this.fontBox, null);
        } else {
            setComboValue(this.fontBox, fontNameToUse);
        }
    }

    private void selectFontSizeInComboBox(final Set<IWidget> widgets) {
        final String fontSizeToUse = getFontSize(widgets);
        if (fontSizeToUse.equals("mixed")) {
            setComboValue(this.fontSize, null);
        } else {
            setComboValue(this.fontSize, fontSizeToUse);
        }
    }

    private void selectFontStyle(final Set<IWidget> widgets) {
        final String fontStyleToUse = getFontStyle(widgets);
        if (fontStyleToUse.equals("mixed") || fontStyleToUse.equals("0")) {
            fontBold.setSelected(false);
            fontItalic.setSelected(false);
            return;
        }

        final int style = Integer.parseInt(fontStyleToUse);
        if (style == IWidget.STYLE_BOLD) {
            fontBold.setSelected(true);
        } else if (style == IWidget.STYLE_ITALIC) {
            fontItalic.setSelected(true);
        } else if (style == IWidget.STYLE_BOLDITALIC) {
            fontBold.setSelected(true);
            fontItalic.setSelected(true);
        }
    }

    private String getFontName(final Set<IWidget> widgets) {
        final List<String> values = widgets.stream()
                .map(widget -> {
                    final FontProperties fontProperties = widget.getWidgetModel().getProperties().getFont();
                    if (widget.allowEditCaptionAndValue()) {
                        return fontProperties.getFontValue().getFontName().orElse("");
                    }
                    return fontProperties.getFontCaption().getFontName().orElse("");
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(values);
    }

    private String getFontSize(final Set<IWidget> widgets) {
        final List<String> values = widgets.stream()
                .map(widget -> {
                    final FontProperties fontProperties = widget.getWidgetModel().getProperties().getFont();
                    if (widget.allowEditCaptionAndValue()) {
                        return fontProperties.getFontValue().getFontSize().orElse("");
                    }
                    return fontProperties.getFontCaption().getFontSize().orElse("");
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(values);
    }

    private String getFontStyle(final Set<IWidget> widgets) {
        final List<String> values = widgets.stream()
                .map(widget -> {
                    final FontProperties fontProperties = widget.getWidgetModel().getProperties().getFont();
                    if (widget.allowEditCaptionAndValue()) {
                        return fontProperties.getFontValue().getFontStyle().orElse("");
                    }
                    return fontProperties.getFontCaption().getFontStyle().orElse("");
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(values);
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues = Collections.frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

    private void setParagraphProperties(final Set<IWidget> widgets) {
        final String horizontalAlignmentToUse = getHorizontalAlignmentToUse(widgets);
        if ("mixed".equals(horizontalAlignmentToUse)) {
            alignmentGroup.clearSelection();
        } else if ("left".equals(horizontalAlignmentToUse)) {
            alignLeft.setSelected(true);
        } else if ("center".equals(horizontalAlignmentToUse)) {
            alignCenter.setSelected(true);
        } else if ("right".equals(horizontalAlignmentToUse)) {
            alignRight.setSelected(true);
        } else {
            logger.warn("Unexpected horizontal alignment {}", horizontalAlignmentToUse);
        }
    }

    private String getHorizontalAlignmentToUse(final Set<IWidget> widgets) {
        final List<String> values = widgets.stream()
                .map(widget -> {
                    final ParagraphProperties paragraphProperties = widget.getWidgetModel().getProperties()
                            .getParagraph();

                    if (widget.allowEditCaptionAndValue()) {
                        return paragraphProperties.getParagraphValue()
                                .getHorizontalAlignment().orElse("left");
                    }
                    return paragraphProperties.getParagraphCaption()
                            .getHorizontalAlignment().orElse("left");
                })
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(values);
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            setState(false);
            return;
        }

        for (final IWidget widget: widgets) {
            if (widget.getType() == IWidget.IMAGE) {
                setState(false);
                return;
            }
        }

        setState(true);

        setFontProperties(widgets);
        setParagraphProperties(widgets);
    }

    private void setState(final boolean enabled) {
        if (!enabled) {
            setItemQuietly(fontBox);
            setItemQuietly(fontSize);

            setSelectedQuietly(fontBold);
            setSelectedQuietly(fontItalic);

            alignmentGroup.clearSelection();
        }

        fontBox.setEnabled(enabled);
        fontSize.setEnabled(enabled);

        fontBold.setEnabled(enabled);
        fontItalic.setEnabled(enabled);

        alignLeft.setEnabled(enabled);
        alignCenter.setEnabled(enabled);
        alignRight.setEnabled(enabled);
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
            final Object value) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);
        comboBox.setSelectedItem(value);
        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    private void setItemQuietly(final JComboBox<String> comboBox) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);
        comboBox.setSelectedItem(null);
        comboBox.addActionListener(listener);
    }

    private void setSelectedQuietly(final JToggleButton toggleButton) {
        final ActionListener listener = toggleButton.getActionListeners()[0];
        toggleButton.removeActionListener(listener);
        toggleButton.setSelected(false);
        toggleButton.addActionListener(listener);
    }
}
