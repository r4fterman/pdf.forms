package org.pdf.forms.widgets.components;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;

public class SplitComponent extends JPanel {

    public static final int CAPTION_LEFT = 0;
    public static final int CAPTION_RIGHT = 1;
    public static final int CAPTION_TOP = 2;
    public static final int CAPTION_BOTTOM = 3;
    public static final int CAPTION_NONE = 4;
    public static final int DIVIDER_SIZE = 5;
    private static final int SPACER = 10;

    private final PdfCaption caption;
    private final JComponent value;

    private final JPanel valuePanel;
    private final JPanel captionPanel;

    private JComponent component;
    private int captionPosition;

    public SplitComponent(
            final String captionText,
            final JComponent value,
            final int captionPosition,
            final FontHandler fontHandler) {
        this.value = value;

        this.caption = new PdfCaption(captionText, fontHandler);
        this.captionPanel = new JPanel();
        captionPanel.setLayout(new BorderLayout());
        captionPanel.setMinimumSize(new Dimension(0, 0));
        captionPanel.setBackground(IDesigner.PAGE_COLOR);
        captionPanel.add(caption, BorderLayout.CENTER);

        this.valuePanel = new JPanel();
        valuePanel.setLayout(new BorderLayout());
        valuePanel.setMinimumSize(new Dimension(0, 0));
        valuePanel.setBackground(IDesigner.PAGE_COLOR);
        valuePanel.add(value, BorderLayout.CENTER);

        this.component = new JSplitPane();

        setLayout(new BorderLayout());
        setCaptionPosition(captionPosition);
    }

    @Override
    public void setBorder(final Border border) {
        if (value != null) {
            value.setBorder(border);
        }
    }

    @Override
    public void setBackground(final Color color) {
        if (value != null) {
            value.setBackground(color);
        }
    }

    public void setCaptionPosition(final int position) {
        this.captionPosition = position;

        remove(component);

        if (position == CAPTION_NONE) {
            this.component = new JPanel();
            component.setLayout(new BorderLayout());
            component.add(valuePanel, BorderLayout.CENTER);
        } else {
            this.component = createSplitPane(position);
        }

        add(component, BorderLayout.CENTER);
    }

    private JSplitPane createSplitPane(final int position) {
        final JSplitPane splitPane = new JSplitPane();
        if (position == SplitComponent.CAPTION_LEFT) {
            configureSplitPaneCaptionLeft(splitPane);
        } else if (position == SplitComponent.CAPTION_TOP) {
            configureSplitPaneCaptionTop(splitPane);
        } else if (position == SplitComponent.CAPTION_RIGHT) {
            configureSplitPaneCaptionRight(splitPane);
        } else if (position == SplitComponent.CAPTION_BOTTOM) {
            configureSplitPaneCaptionBottom(splitPane);
        }

        setSplitPaneDividerColor(splitPane);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(DIVIDER_SIZE);

        return splitPane;
    }

    private void configureSplitPaneCaptionTop(final JSplitPane splitPane) {
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        splitPane.setTopComponent(captionPanel);
        splitPane.setBottomComponent(valuePanel);

        splitPane.setResizeWeight(0);

        splitPane.setDividerLocation((int) caption.getPreferredSize().getHeight());
    }

    private void configureSplitPaneCaptionBottom(final JSplitPane splitPane) {
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        splitPane.setTopComponent(valuePanel);
        splitPane.setBottomComponent(captionPanel);

        splitPane.setResizeWeight(1);
    }

    private void configureSplitPaneCaptionLeft(final JSplitPane splitPane) {
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setLeftComponent(captionPanel);
        splitPane.setRightComponent(valuePanel);

        splitPane.setResizeWeight(0);

        splitPane.setDividerLocation((int) caption.getPreferredSize().getWidth() + SPACER);
    }

    private void configureSplitPaneCaptionRight(final JSplitPane splitPane) {
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setRightComponent(captionPanel);
        splitPane.setLeftComponent(valuePanel);

        splitPane.setResizeWeight(1);

        splitPane.setDividerLocation((int) value.getPreferredSize().getWidth());
    }

    private void setSplitPaneDividerColor(final JSplitPane splitPane) {
        final SplitPaneUI splitUI = splitPane.getUI();
        if (splitUI instanceof BasicSplitPaneUI) {
            // obviously this will not work if the ui does not extend Basic...
            final int dividerSize = splitPane.getDividerSize();
            final BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitUI).getDivider();
            final Border dividerBorder = divider.getBorder();

            int insetHorizontal = 0;
            int insetVertical = 0;
            if (dividerBorder != null) {
                final Insets borderInsets = dividerBorder.getBorderInsets(divider);
                insetHorizontal = borderInsets.left + borderInsets.right;
                insetVertical = borderInsets.top + borderInsets.bottom;
            }

            // this border uses a fillRect
            final Border colorBorder = BorderFactory.createMatteBorder(
                    dividerSize - insetVertical,
                    dividerSize - insetHorizontal,
                    0,
                    0,
                    Color.yellow);

            final Border newBorder;
            if (dividerBorder == null) {
                newBorder = colorBorder;
            } else {
                newBorder = BorderFactory.createCompoundBorder(dividerBorder, colorBorder);
            }
            divider.setBorder(newBorder);
        }
    }

    public JComponent getValue() {
        return value;
    }

    public PdfCaption getCaption() {
        return caption;
    }

    public void setDividerLocation(final int x) {
        ((JSplitPane) component).setDividerLocation(x);
    }

    public int getDividerLocation() {
        return ((JSplitPane) component).getDividerLocation();
    }

    public int getOrientation() {
        return ((JSplitPane) component).getOrientation();
    }

    public int getCaptionPosition() {
        return captionPosition;
    }
}
