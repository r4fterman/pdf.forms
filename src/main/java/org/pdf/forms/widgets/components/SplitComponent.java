package org.pdf.forms.widgets.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
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
    private JComponent value;

    private JComponent component;
    private final JPanel valuePanel;
    private final JPanel captionPanel;
    private int captionPosition;

    public SplitComponent(
            final String captionText,
            final JComponent value,
            final int captionPosition,
            final FontHandler fontHandler) {
        setLayout(new BorderLayout());

        caption = new PdfCaption(captionText, fontHandler);

        this.value = value;

        captionPanel = new JPanel();
        captionPanel.setLayout(new BorderLayout());

        captionPanel.setMinimumSize(new Dimension(0, 0));
        captionPanel.setBackground(IDesigner.PAGE_COLOR);
        captionPanel.add(caption, BorderLayout.CENTER);

        valuePanel = new JPanel();
        valuePanel.setLayout(new BorderLayout());

        valuePanel.setMinimumSize(new Dimension(0, 0));
        valuePanel.setBackground(IDesigner.PAGE_COLOR);
        valuePanel.add(value, BorderLayout.CENTER);

        component = new JSplitPane();

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
            component = new JPanel();
            component.setLayout(new BorderLayout());
            component.add(valuePanel, BorderLayout.CENTER);
        } else {
            component = new JSplitPane();
            final JSplitPane sp = (JSplitPane) component;

            switch (position) {
                case SplitComponent.CAPTION_LEFT:

                    sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

                    sp.setLeftComponent(captionPanel);
                    sp.setRightComponent(valuePanel);

                    sp.setResizeWeight(0);

                    sp.setDividerLocation((int) caption.getPreferredSize().getWidth() + SPACER);

                    break;

                case SplitComponent.CAPTION_TOP:

                    sp.setOrientation(JSplitPane.VERTICAL_SPLIT);

                    sp.setTopComponent(captionPanel);
                    sp.setBottomComponent(valuePanel);

                    sp.setResizeWeight(0);

                    sp.setDividerLocation((int) caption.getPreferredSize().getHeight());

                    break;

                case SplitComponent.CAPTION_RIGHT:

                    sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

                    sp.setRightComponent(captionPanel);
                    sp.setLeftComponent(valuePanel);

                    sp.setResizeWeight(1);

                    sp.setDividerLocation((int) value.getPreferredSize().getWidth());

                    break;

                case SplitComponent.CAPTION_BOTTOM:

                    sp.setOrientation(JSplitPane.VERTICAL_SPLIT);

                    sp.setTopComponent(valuePanel);
                    sp.setBottomComponent(captionPanel);

                    sp.setResizeWeight(1);

                    break;

                default:
                    break;

            }

            setSplitPaneDividerColor(sp);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.setDividerSize(DIVIDER_SIZE);

        }

        add(component, BorderLayout.CENTER);
    }

    private void setSplitPaneDividerColor(final JSplitPane splitPane) {
        final SplitPaneUI splitUI = splitPane.getUI();
        if (splitUI instanceof BasicSplitPaneUI) { // obviously this will not work if the ui doen't extend Basic...
            final int divSize = splitPane.getDividerSize();
            final BasicSplitPaneDivider div = ((BasicSplitPaneUI) splitUI).getDivider();
            final Border divBorder = div.getBorder();
            Border newBorder = null;
            Border colorBorder = null;

            int insetsh = 0;
            int insetsv = 0;

            if (divBorder != null) {
                final Insets i = divBorder.getBorderInsets(div);
                insetsh = i.left + i.right;
                insetsv = i.top + i.bottom;
            }

            // this border uses a fillRect
            colorBorder = BorderFactory.createMatteBorder(divSize - insetsv, divSize - insetsh, 0, 0, Color.yellow);

            if (divBorder == null) {
                newBorder = colorBorder;
            } else {
                newBorder = BorderFactory.createCompoundBorder(divBorder, colorBorder);
            }
            div.setBorder(newBorder);
        }
    }

    /**
     * Sets the x and y alignment of the caption, the two integers taken are SwingConstants.???
     * For example SwingConstants.CENTER
     *
     * @param alignmentX
     * @param alignmentY
     */
    public void setCaptionAlignment(
            final int alignmentX,
            final int alignmentY) {
        caption.setHorizontalAlignment(alignmentX);
        caption.setVerticalAlignment(alignmentY);
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
