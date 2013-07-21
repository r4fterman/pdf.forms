/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * DesignerSelectionBox.java
 * ---------------
 */
package org.pdf.forms.gui.designer.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class DesignerSelectionBox {

    private Rectangle currentRect = null;

    private Rectangle rectToDraw = null;

    private Rectangle previousRectDrawn = new Rectangle();

    private IDesigner designerPanel;

    public DesignerSelectionBox(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    public void updateSize(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        currentRect.setSize(x - currentRect.x, y - currentRect.y);
        updateDrawableRect(designerPanel.getWidth(), designerPanel.getHeight());
        Rectangle totalRepaint = rectToDraw.union(previousRectDrawn);
        designerPanel.repaint(totalRepaint.x, totalRepaint.y, totalRepaint.width, totalRepaint.height);
    }

    public void updateDrawableRect(int compWidth, int compHeight) {
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

    public void setCurrentRect(Rectangle currentRect) {
        this.currentRect = currentRect;
    }

    public Rectangle getCurrentRect() {
        return currentRect;
    }

    public void paintBox(Graphics2D g2) {
        if (currentRect != null) {
            //g2.setPaint(Color.green);

            float[] dashPattern = {1, 1};
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1,
                    dashPattern, 0));
            g2.setColor(Color.black);

            g2.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width - 1, rectToDraw.height - 1);
        }
    }

    public void setSelectedWedgets() {

        if (currentRect == null)
            return;

        int width = currentRect.width;
        int height = currentRect.height;

        if (width < 0) {
            currentRect.setLocation(currentRect.x + width, currentRect.y);
            currentRect.setSize(-currentRect.width, currentRect.height);
        }
        if (height < 0) {
            currentRect.setLocation(currentRect.x, currentRect.y + height);
            currentRect.setSize(currentRect.width, -currentRect.height);
        }

        List widgets = designerPanel.getWidgets();

        Set widgetSet = new HashSet();
        for (Iterator iter = widgets.iterator(); iter.hasNext(); ) {
            IWidget widget = (IWidget) iter.next();

            Rectangle bounds = widget.getBounds();

//            double scale = designerPanel.getScale(); @scale
//            bounds = new Rectangle((int) (bounds.x * scale), (int) (bounds.y * scale), (int) (bounds.width * scale), (int) (bounds.height * scale)); //@scale

            bounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);

            if (currentRect.contains(bounds))
                widgetSet.add(widget);
        }

        designerPanel.setSelectedWidgets(widgetSet);

        designerPanel.getMainFrame().setPropertiesCompound(widgetSet);
        designerPanel.getMainFrame().setPropertiesToolBar(widgetSet);
    }
}
