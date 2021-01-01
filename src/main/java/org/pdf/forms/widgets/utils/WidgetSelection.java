package org.pdf.forms.widgets.utils;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WidgetSelection {

    public static final int BOX_MARGIN = 3;
    private static final int RESIZE_NODE_SIZE = 4;
    private static final int WIDTH = 22;
    private static final int HEIGHT = 22;

    private final Logger logger = LoggerFactory.getLogger(WidgetSelection.class);

    private final JButton groupButton = new JButton();
    private final JButton unGroupButton = new JButton();

    private Rectangle selectionBoxBounds = new Rectangle();
    private int lastY;
    private int lastX;

    public WidgetSelection(
            final IDesigner designerPanel,
            final String version,
            final FontHandler fontHandler,
            final WidgetFactory widgetFactory,
            final Configuration configuration) {
        groupButton.setVisible(false);
        groupButton.setSize(WIDTH, HEIGHT);
        groupButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Grouped.gif")));
        groupButton.setToolTipText("Group Selection");

        groupButton.addActionListener(e -> {
            final Commands commands = new Commands(designerPanel.getMainFrame(),
                    version,
                    fontHandler,
                    widgetFactory,
                    configuration);
            commands.executeCommand(Commands.GROUP);
        });

        unGroupButton.setVisible(false);
        unGroupButton.setSize(WIDTH, HEIGHT);
        unGroupButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Ungrouped.gif")));
        unGroupButton.setToolTipText("Ungroup Selection");

        unGroupButton.addActionListener(e -> {
            final Commands commands = new Commands(designerPanel.getMainFrame(),
                    version,
                    fontHandler,
                    widgetFactory,
                    configuration);
            commands.executeCommand(Commands.UNGROUP);
        });

        designerPanel.add(groupButton);
        designerPanel.add(unGroupButton);
    }

    private void resizeWidgets(final IWidget selectedWidget) {
        final double resizeWidthRatio = selectedWidget.getResizeWidthRatio();
        final double resizeHeightRatio = selectedWidget.getResizeHeightRatio();

        final double resizeFromLeftRatio = selectedWidget.getResizeFromLeftRatio();
        final double resizeFromTopRatio = selectedWidget.getResizeFromTopRatio();

        selectedWidget.setSize(
                (int) Math.round(selectionBoxBounds.width * resizeWidthRatio) - (BOX_MARGIN * 2),
                (int) Math.round(selectionBoxBounds.height * resizeHeightRatio) - (BOX_MARGIN * 2));

        selectedWidget.setY(
                (int) Math.round(
                        selectionBoxBounds.y + BOX_MARGIN + (selectionBoxBounds.height * resizeFromTopRatio)));

        selectedWidget.setX(
                (int) Math.round(
                        selectionBoxBounds.x + BOX_MARGIN + (selectionBoxBounds.width * resizeFromLeftRatio)));
    }

    private void drawBox(
            final Graphics2D g2,
            final Dimension boxSize) {
        g2.setColor(new Color(0, 153, 153));
        g2.drawRect(-BOX_MARGIN, -BOX_MARGIN, boxSize.width, boxSize.height);
    }

    private void drawNodes(
            final Graphics2D g2,
            final Rectangle rectangle) {
        g2.fillRect(rectangle.x - RESIZE_NODE_SIZE + 1,
                rectangle.y - RESIZE_NODE_SIZE + 1,
                RESIZE_NODE_SIZE,
                RESIZE_NODE_SIZE);
        g2.fillRect(rectangle.x + rectangle.width,
                rectangle.y - RESIZE_NODE_SIZE + 1,
                RESIZE_NODE_SIZE,
                RESIZE_NODE_SIZE);
        g2.fillRect(rectangle.x - RESIZE_NODE_SIZE + 1,
                rectangle.y + rectangle.height,
                RESIZE_NODE_SIZE,
                RESIZE_NODE_SIZE);
        g2.fillRect(rectangle.x + rectangle.width, rectangle.y + rectangle.height, RESIZE_NODE_SIZE, RESIZE_NODE_SIZE);
    }

    /**
     * Draws a selection box for a single widget.
     *
     * @param g2        the g2 to draw the selection box onto
     * @param widget    the widget to draw the selection box round
     * @param drawNodes if true then nodes will be drawn around the selection box, and this widget will be internally
     *                  marked as the selected widget
     */
    public void drawSingleSectionBox(
            final Graphics2D g2,
            final IWidget widget,
            final boolean drawNodes) {

        groupButton.setVisible(false);
        unGroupButton.setVisible(false);

        final Rectangle localSelectionBoxBounds = widget.getBounds();

        localSelectionBoxBounds.setLocation(widget.getX() - BOX_MARGIN, widget.getY() - BOX_MARGIN);
        localSelectionBoxBounds.setSize(widget.getBoxSize());

        drawBox(g2, localSelectionBoxBounds.getSize());

        if (drawNodes) {
            drawNodes(g2,
                    new Rectangle(-BOX_MARGIN,
                            -BOX_MARGIN,
                            localSelectionBoxBounds.width,
                            localSelectionBoxBounds.height));

            selectionBoxBounds = localSelectionBoxBounds;
        }
    }

    /**
     * Draws a selection box around a set of selected widgets.
     *
     * @param g2              the g2 to draw the selection box onto
     * @param selectedWidgets the set of widgets to draw the box round
     * @param drawNodes       whether or not nodes should be drawn
     */
    public void drawMulitipleSelectionBox(
            final Graphics2D g2,
            final Set<IWidget> selectedWidgets,
            final boolean drawNodes) {

        groupButton.setVisible(false);
        unGroupButton.setVisible(false);

        final Set<IWidget> flattenedWidgets = getFlatternedWidgets(selectedWidgets);
        final Rectangle multipleWidgetBounds = getMultipleWidgetBounds(flattenedWidgets);

        selectionBoxBounds = new Rectangle(multipleWidgetBounds.x - BOX_MARGIN, multipleWidgetBounds.y - BOX_MARGIN,
                multipleWidgetBounds.width + (BOX_MARGIN * 2), multipleWidgetBounds.height + (BOX_MARGIN * 2));

        g2.translate(multipleWidgetBounds.x, multipleWidgetBounds.y);

        drawBox(g2, selectionBoxBounds.getSize());

        if (drawNodes) {
            drawNodes(g2, new Rectangle(-BOX_MARGIN, -BOX_MARGIN, selectionBoxBounds.width, selectionBoxBounds.height));
        }

        g2.translate(-multipleWidgetBounds.x, -multipleWidgetBounds.y);

        if (drawNodes) {
            final JButton button;
            if (selectedWidgets.size() == 1 && selectedWidgets.iterator().next().getType() == IWidget.GROUP) {
                button = unGroupButton;
            } else {
                button = groupButton;
            }

            button.setLocation((int) (selectionBoxBounds.x + (selectionBoxBounds.width * 0.7)),
                    selectionBoxBounds.y + selectionBoxBounds.height + 20);

            button.setVisible(true);
        }
    }

    public int getResizeType(
            final int mouseX,
            final int mouseY,
            final Set<IWidget> selectedWidgets) {

        if (selectionBoxBounds == null || selectedWidgets.isEmpty()) {
            return DesignerMouseMotionListener.DEFAULT_CURSOR;
        }

        final int widgetX = selectionBoxBounds.x;
        final int widgetY = selectionBoxBounds.y;

        int resizeType;

        final IWidget selectedWidget = selectedWidgets.iterator().next();

        /*
         * if just one widget is selected, and it is split, check to see if the cursor is over
         * the split
         */
        if (selectedWidgets.size() == 1 && selectedWidget.isComponentSplit()) {
            resizeType = selectedWidget.getResizeTypeForSplitComponent(mouseX, mouseY);

            if (resizeType != -1) {
                return resizeType;
            }

        }

        if (mouseX >= widgetX - WidgetSelection.BOX_MARGIN - 5 && mouseX <= widgetX
                && mouseY >= widgetY - WidgetSelection.BOX_MARGIN - 5 && mouseY <= widgetY) {

            resizeType = DesignerMouseMotionListener.NW_RESIZE_CURSOR;

        } else if (mouseX >= getSelectionBoxBounds().width + widgetX
                && mouseX <= getSelectionBoxBounds().width + widgetX + 5 + WidgetSelection.BOX_MARGIN
                && mouseY >= getSelectionBoxBounds().height + widgetY
                && mouseY <= getSelectionBoxBounds().height + widgetY + 5 + WidgetSelection.BOX_MARGIN) {

            resizeType = DesignerMouseMotionListener.SE_RESIZE_CURSOR;

        } else if (mouseX >= getSelectionBoxBounds().width + widgetX
                && mouseX <= getSelectionBoxBounds().width + widgetX + 5 + WidgetSelection.BOX_MARGIN
                && mouseY >= widgetY - 5 - WidgetSelection.BOX_MARGIN
                && mouseY <= widgetY) {

            resizeType = DesignerMouseMotionListener.NE_RESIZE_CURSOR;

        } else if (mouseX >= widgetX - 5 - WidgetSelection.BOX_MARGIN
                && mouseX <= widgetX
                && mouseY >= getSelectionBoxBounds().height + widgetY
                && mouseY <= getSelectionBoxBounds().height + widgetY + 5 + WidgetSelection.BOX_MARGIN) {

            resizeType = DesignerMouseMotionListener.SW_RESIZE_CURSOR;

        } else {
            resizeType = DesignerMouseMotionListener.DEFAULT_CURSOR;
        }

        return resizeType;
    }

    public void moveWidgets(
            final Set<IWidget> selectedWidgets,
            final int mouseX,
            final int mouseY) {
        for (final IWidget selectedWidget: selectedWidgets) {

            final int widgetLastX = selectedWidget.getLastX();
            final int widgetLastY = selectedWidget.getLastY();

            //double scale = designerPanel.getScale(); @scale
            //            selectedWidget.setX((int) ((widgetLastX + mouseX) / scale));
            //            selectedWidget.setY((int) ((widgetLastY + mouseY) / scale));

            selectedWidget.setX(widgetLastX + mouseX);
            selectedWidget.setY(widgetLastY + mouseY);
        }
    }

    public void resizeWidgets(
            final Set<IWidget> selectedWidgets,
            final IDesigner designerPanel,
            final int mouseX,
            final int mouseY,
            final int resizeType) {

        // double scale = designerPanel.getScale(); @scale

        if (resizeType == DesignerMouseMotionListener.SE_RESIZE_CURSOR) {
            //                int x = (int) (selectionBoxBounds.x * scale); @scale
            //                int y = (int) (selectionBoxBounds.y * scale); @scale

            final int x = selectionBoxBounds.x;
            final int y = selectionBoxBounds.y;

            selectionBoxBounds.setSize(mouseX - x, mouseY - y);

            for (final IWidget selectedWidget: selectedWidgets) {
                resizeWidgets(selectedWidget);
            }

            designerPanel.setIsResizing(true);
        } else if (resizeType == DesignerMouseMotionListener.NE_RESIZE_CURSOR) {
            final int x = selectionBoxBounds.x;
            final int height = selectionBoxBounds.height;

            int dy = selectionBoxBounds.y;
            selectionBoxBounds.setLocation(selectionBoxBounds.x, lastY + mouseY);
            dy -= selectionBoxBounds.getY();

            selectionBoxBounds.setSize(mouseX - x, height + dy);

            for (final IWidget selectedWidget: selectedWidgets) {
                resizeWidgets(selectedWidget);
            }

            designerPanel.setIsResizing(true);
        } else if (resizeType == DesignerMouseMotionListener.SW_RESIZE_CURSOR) {
            final int y = selectionBoxBounds.y;
            final int width = selectionBoxBounds.width;

            int dx = selectionBoxBounds.x;
            selectionBoxBounds.setLocation(lastX + mouseX, selectionBoxBounds.y);
            dx -= selectionBoxBounds.getX();

            selectionBoxBounds.setSize(width + dx, mouseY - y);

            for (final IWidget widget: selectedWidgets) {
                resizeWidgets(widget);
            }

            designerPanel.setIsResizing(true);
        } else if (resizeType == DesignerMouseMotionListener.NW_RESIZE_CURSOR) {
            final int width = selectionBoxBounds.width;
            final int height = selectionBoxBounds.height;

            int dy = selectionBoxBounds.y;
            selectionBoxBounds.setLocation(selectionBoxBounds.x, lastY + mouseY);
            dy -= selectionBoxBounds.getY();

            int dx = selectionBoxBounds.x;
            selectionBoxBounds.setLocation(lastX + mouseX, selectionBoxBounds.y);
            dx -= selectionBoxBounds.getX();

            selectionBoxBounds.setSize(width + dx, height + dy);

            for (final IWidget selectedWidget: selectedWidgets) {
                resizeWidgets(selectedWidget);
            }

            designerPanel.setIsResizing(true);
        } else if (resizeType == DesignerMouseMotionListener.RESIZE_SPLIT_HORIZONTAL_CURSOR) {
            final int x = selectionBoxBounds.x;

            final IWidget selectedWidget = selectedWidgets.iterator().next();

            final Document properties = selectedWidget.getProperties();
            final Element captionProperties = XMLUtils.getElementsFromNodeList(properties
                    .getElementsByTagName("caption_properties")).get(0);

            final Element divisorLocationElement = XMLUtils.getPropertyElement(captionProperties, "Divisor Location")
                    .get();

            divisorLocationElement.getAttributeNode("value").setValue(String.valueOf(mouseX - x));

            selectedWidget.setCaptionProperties(captionProperties);
            //                selectedWidget.setDivisorLocation(mouseX - x);

            designerPanel.setIsResizingSplitComponent(true);
        } else if (resizeType == DesignerMouseMotionListener.RESIZE_SPLIT_VERTICAL_CURSOR) {
            final int y = selectionBoxBounds.y;

            final IWidget selectedWidget = selectedWidgets.iterator().next();

            final Document properties = selectedWidget.getProperties();
            final Element captionProperties =
                    XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("caption_properties")).get(0);

            final Element divisorLocationElement = XMLUtils.getPropertyElement(captionProperties, "Divisor Location")
                    .get();

            divisorLocationElement.getAttributeNode("value").setValue(String.valueOf(mouseY - y));

            selectedWidget.setCaptionProperties(captionProperties);
            //                selectedWidget.setDivisorLocation(mouseY - y);

            designerPanel.setIsResizingSplitComponent(true);
        } else {
            logger.warn("{} Manual exit because of impossible situation in {}", resizeType, getClass());
        }
    }

    public void setLastX(final int lastX) {
        this.lastX = selectionBoxBounds.x - lastX;
    }

    public void setLastY(final int lastY) {
        //        this.lastY = (int) (selectionBoxBounds.y * designerPanel.getScale() - lastY); @scale
        this.lastY = selectionBoxBounds.y - lastY;
    }

    public Rectangle getSelectionBoxBounds() {
        return selectionBoxBounds;
    }

    public void hideGroupingButtons() {
        groupButton.setVisible(false);
        unGroupButton.setVisible(false);
    }

    public Set<IWidget> getFlatternedWidgets(final Set<IWidget> widgets) {
        final Set<IWidget> set = new HashSet<>();
        for (final IWidget widget: widgets) {
            if (widget.getType() == IWidget.GROUP) {
                set.addAll(getFlatternedWidgets(new HashSet<>(widget.getWidgetsInGroup())));
            } else {
                set.add(widget);
            }
        }

        return set;
    }

    public static Rectangle getMultipleWidgetBounds(final Set<IWidget> selectedWidgets) {
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        for (final IWidget widget: selectedWidgets) {
            final Rectangle widgetBounds = widget.getBounds();

            final int leftPoint = Math.min(widgetBounds.x, rectangle.x);
            final int topPoint = Math.min(widgetBounds.y, rectangle.y);
            final int rightPoint = Math.max(widgetBounds.x + widgetBounds.width, rectangle.x + rectangle.width);
            final int bottomPoint = Math.max(widgetBounds.y + widgetBounds.height, rectangle.y + rectangle.height);

            rectangle = new Rectangle(leftPoint, topPoint, rightPoint - leftPoint, bottomPoint - topPoint);
        }

        return rectangle;
    }
}
