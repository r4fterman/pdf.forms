package org.pdf.forms.gui.designer.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DesignerSelectionBox {

    private Rectangle currentRect = null;

    private Rectangle rectToDraw = null;

    private final Rectangle previousRectDrawn = new Rectangle();

    private final IDesigner designerPanel;

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
        int x = currentRect.x;
        int y = currentRect.y;
        int width = currentRect.width;
        int height = currentRect.height;

        //Make the width and height positive, if necessary.
        if (width < 0) {
            width = 0 - width;
            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }
        if (height < 0) {
            height = 0 - height;
            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }

        //The rectangle shouldn't extend past the drawing area.
        if ((x + width) > compWidth) {
            width = compWidth - x;
        }
        if ((y + height) > compHeight) {
            height = compHeight - y;
        }

        //Update rectToDraw after saving old value.
        if (rectToDraw != null) {
            previousRectDrawn.setBounds(rectToDraw.x, rectToDraw.y,
                    rectToDraw.width, rectToDraw.height);
            rectToDraw.setBounds(x, y, width, height);
        } else {
            rectToDraw = new Rectangle(x, y, width, height);
        }
    }

    public void setCurrentRect(final Rectangle currentRect) {
        this.currentRect = currentRect;
    }

    public Rectangle getCurrentRect() {
        return currentRect;
    }

    public void paintBox(final Graphics2D g2) {
        if (currentRect != null) {
            //g2.setPaint(Color.green);

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

    public void setSelectedWedgets() {

        if (currentRect == null) {
            return;
        }

        final int width = currentRect.width;
        final int height = currentRect.height;

        if (width < 0) {
            currentRect.setLocation(currentRect.x + width, currentRect.y);
            currentRect.setSize(-currentRect.width, currentRect.height);
        }
        if (height < 0) {
            currentRect.setLocation(currentRect.x, currentRect.y + height);
            currentRect.setSize(currentRect.width, -currentRect.height);
        }

        final List<IWidget> widgets = designerPanel.getWidgets();

        final Set<IWidget> widgetSet = new HashSet<>();
        for (final IWidget widget : widgets) {
            Rectangle bounds = widget.getBounds();

            //            double scale = designerPanel.getScale(); @scale
            //            bounds = new Rectangle((int) (bounds.x * scale), (int) (bounds.y * scale), (int) (bounds.width * scale), (int) (bounds.height * scale)); //@scale

            bounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);

            if (currentRect.contains(bounds)) {
                widgetSet.add(widget);
            }
        }

        designerPanel.setSelectedWidgets(widgetSet);

        designerPanel.getMainFrame().setPropertiesCompound(widgetSet);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetSet);
    }
}
