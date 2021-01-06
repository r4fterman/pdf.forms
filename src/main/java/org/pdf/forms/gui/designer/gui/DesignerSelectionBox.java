package org.pdf.forms.gui.designer.gui;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DesignerSelectionBox {

    private final Rectangle previousRectDrawn = new Rectangle();
    private final IDesigner designerPanel;

    private Rectangle currentRect = null;
    private Rectangle rectToDraw = null;

    public DesignerSelectionBox(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    public void updateSize(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();

        currentRect.setSize(x - currentRect.x, y - currentRect.y);
        updateDrawableRect(designerPanel.getWidth(), designerPanel.getHeight());
        final Rectangle totalRepaint = rectToDraw.union(previousRectDrawn);
        designerPanel.repaint(totalRepaint.x, totalRepaint.y, totalRepaint.width, totalRepaint.height);
    }

    public void updateDrawableRect(
            final int compWidth,
            final int compHeight) {
        final Rectangle rectangleToDraw = calculateRectangleToDraw(currentRect, compWidth, compHeight);

        //Update rectToDraw after saving old value.
        if (rectToDraw == null) {
            this.rectToDraw = rectangleToDraw;
        } else {
            previousRectDrawn.setBounds(
                    rectToDraw.x,
                    rectToDraw.y,
                    rectToDraw.width,
                    rectToDraw.height);

            rectToDraw.setBounds(
                    rectangleToDraw.x,
                    rectangleToDraw.y,
                    rectangleToDraw.width,
                    rectangleToDraw.height);
        }
    }

    private Rectangle calculateRectangleToDraw(
            final Rectangle rectangle,
            final int componentWidth,
            final int componentHeight) {
        int x = rectangle.x;
        int y = rectangle.y;
        int width = rectangle.width;
        int height = rectangle.height;

        // Make the width and height positive, if necessary.
        if (width < 0) {
            width = -width;

            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }

        if (height < 0) {
            height = -height;

            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }

        //The rectangle shouldn't extend past the drawing area.
        width = calculateRectangleBounds(componentWidth, x, width);
        height = calculateRectangleBounds(componentHeight, y, height);

        return new Rectangle(x, y, width, height);
    }

    private int calculateRectangleBounds(
            final int maxValue,
            final int delta,
            final int valueToUpdate) {
        if ((delta + valueToUpdate) > maxValue) {
            return maxValue - delta;
        }
        return valueToUpdate;
    }

    public void setCurrentRect(final Rectangle currentRect) {
        this.currentRect = currentRect;
    }

    public Rectangle getCurrentRect() {
        return currentRect;
    }

    public void paintBox(final Graphics2D g2) {
        if (currentRect != null) {
            final float[] dashPattern = {
                    1,
                    1
            };

            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1,
                    dashPattern, 0));
            g2.setColor(Color.black);

            g2.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width - 1, rectToDraw.height - 1);
        }
    }

    public void setSelectedWidgets() {
        if (currentRect == null) {
            return;
        }

        final int width = currentRect.width;
        if (width < 0) {
            currentRect.setLocation(currentRect.x + width, currentRect.y);
            currentRect.setSize(-currentRect.width, currentRect.height);
        }

        final int height = currentRect.height;
        if (height < 0) {
            currentRect.setLocation(currentRect.x, currentRect.y + height);
            currentRect.setSize(currentRect.width, -currentRect.height);
        }

        final Set<IWidget> widgetSet = designerPanel.getWidgets().stream()
                .filter(widget -> currentRect.contains(widget.getBounds()))
                .collect(toUnmodifiableSet());

        designerPanel.setSelectedWidgets(widgetSet);
        designerPanel.getMainFrame().setPropertiesCompound(widgetSet);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetSet);
    }
}
