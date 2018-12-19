/*
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
* WidgetAlignmentAndOrder.java
* ---------------
*/
package org.pdf.forms.widgets.utils;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class WidgetAlignmentAndOrder {
    public static final String ALIGN_LEFT = "Align Left";
    public static final String ALIGN_RIGHT = "Align Right";
    public static final String ALIGN_TOP = "Align Top";
    public static final String ALIGN_BOTTOM = "Align Bottom";
    public static final String ALIGN_VERTICALY = "Align Verticaly";
    public static final String ALIGN_HORIZONTALY = "Align Horizontaly";
    public static final String BRING_TO_FRONT = "Bring to Front";
    public static final String SEND_TO_BACK = "Send to Back";
    public static final String BRING_FORWARDS = "Bring Forwards";
    public static final String SEND_BACKWARDS = "Send Backwards";

    private static Set selectedWidgets;

    private static String resRoot = "/org/pdf/forms/res/";

    private static String[] alignButtons = new String[] {
            resRoot + ALIGN_LEFT + ".gif",
            resRoot + ALIGN_RIGHT + ".gif",
            resRoot + ALIGN_TOP + ".gif",
            resRoot + ALIGN_BOTTOM + ".gif",
            "Seperator",
            resRoot + ALIGN_VERTICALY + ".gif",
            resRoot + ALIGN_HORIZONTALY + ".gif",
            "Seperator", };

    private static String[] orderButtons = new String[] {
            resRoot + BRING_TO_FRONT + ".gif",
            resRoot + SEND_TO_BACK + ".gif",
            resRoot + BRING_FORWARDS + ".gif",
            resRoot + SEND_BACKWARDS + ".gif" };

    public static String[] getAlignButtons() {
        return alignButtons;
    }

    public static String[] getOrderButtons() {
        return orderButtons;
    }

    public static void alignAndOrder(
            final IDesigner designerPanel,
            final String type) {

        selectedWidgets = designerPanel.getSelectedWidgets();

        switch (type) {
        case WidgetAlignmentAndOrder.ALIGN_LEFT:
            alignLeft();
            break;
        case WidgetAlignmentAndOrder.ALIGN_RIGHT:
            alignRight();
            break;
        case WidgetAlignmentAndOrder.ALIGN_TOP:
            alignTop();
            break;
        case WidgetAlignmentAndOrder.ALIGN_BOTTOM:
            alignBottom();
            break;
        case WidgetAlignmentAndOrder.ALIGN_VERTICALY:
            alignVerticaly();
            break;
        case WidgetAlignmentAndOrder.ALIGN_HORIZONTALY:
            alignHorizontaly();
            break;
        default:
            changeOrderOfSelectedWidgets(type, designerPanel.getWidgets());
            break;
        }

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        designerPanel.getMainFrame().setPropertiesCompound(selectedWidgets);
        designerPanel.getMainFrame().setPropertiesToolBar(selectedWidgets);
        designerPanel.getMainFrame().updateHierarchy();

        designerPanel.repaint();
    }

    /**
     * Note: This method takes a reference to the list of widgets stored in the
     * designer panel.  It then alters the list here.
     */
    private static void changeOrderOfSelectedWidgets(
            String type,
            List widgets) {

        int size = widgets.size() - 1;
        Set newSet = (Set) ((HashSet) selectedWidgets).clone();

        if (type.equals(WidgetAlignmentAndOrder.BRING_TO_FRONT)) {
            for (int i = 0; i < size; i++) {
                IWidget widget = (IWidget) widgets.get(i);

                if (newSet.remove(widget)) {
                    if (i < size) {
                        widgets.add(size, widgets.remove(i));
                    }

                    i = -1;
                }
            }
        } else if (type.equals(WidgetAlignmentAndOrder.SEND_TO_BACK)) {
            for (int i = size; i >= 0; i--) {
                IWidget widget = (IWidget) widgets.get(i);

                if (newSet.remove(widget)) {
                    if (i > 0) {
                        widgets.add(0, widgets.remove(i));
                    }

                    i = size + 1;
                }
            }
        } else if (type.equals(WidgetAlignmentAndOrder.BRING_FORWARDS)) {
            for (int i = 0; i < size; i++) {
                IWidget widget = (IWidget) widgets.get(i);

                if (newSet.remove(widget)) {
                    if (i < size) {
                        widgets.add(i + 1, widgets.remove(i));
                    }

                    i = -1;
                }
            }
        } else if (type.equals(WidgetAlignmentAndOrder.SEND_BACKWARDS)) {
            for (int i = size; i >= 0; i--) {
                IWidget widget = (IWidget) widgets.get(i);

                if (newSet.remove(widget)) {
                    if (i > 0) {
                        widgets.add(i - 1, widgets.remove(i));
                    }

                    i = size + 1;
                }
            }
        }
    }

    private static void alignHorizontaly() {
        int averageCenterPoint = 0;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            averageCenterPoint += bounds.x + bounds.width / 2;
        }

        averageCenterPoint = averageCenterPoint / selectedWidgets.size();

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setX(averageCenterPoint - widget.getBounds().width / 2);
        }
    }

    private static void alignVerticaly() {
        int averageCenterPoint = 0;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            averageCenterPoint += bounds.y + bounds.height / 2;
        }

        averageCenterPoint = averageCenterPoint / selectedWidgets.size();

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setY(averageCenterPoint - widget.getBounds().height / 2);
        }
    }

    private static void alignBottom() {
        int bottomPoint = 0;
        boolean firstPass = true;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            if (firstPass) {
                bottomPoint = bounds.y + bounds.height;
                firstPass = false;
            } else {
                int tempBottomPoint = bounds.y + bounds.height;
                if (tempBottomPoint > bottomPoint) {
                    bottomPoint = tempBottomPoint;
                }
            }
        }

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setY(bottomPoint - widget.getBounds().height);
        }
    }

    private static void alignTop() {
        int topPoint = 0;
        boolean firstPass = true;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            if (firstPass) {
                topPoint = bounds.y;
                firstPass = false;
            } else {
                int tempTopPoint = bounds.y;
                if (tempTopPoint < topPoint) {
                    topPoint = tempTopPoint;
                }
            }
        }

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setY(topPoint);
        }
    }

    private static void alignRight() {
        int rightPoint = 0;
        boolean firstPass = true;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            if (firstPass) {
                rightPoint = bounds.x + bounds.width;
                firstPass = false;
            } else {
                int tempRightPoint = bounds.x + bounds.width;
                if (tempRightPoint > rightPoint) {
                    rightPoint = tempRightPoint;
                }
            }
        }

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setX(rightPoint - widget.getBounds().width);
        }
    }

    private static void alignLeft() {
        int leftPoint = 0;
        boolean firstPass = true;
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            Rectangle bounds = widget.getBounds();

            if (firstPass) {
                leftPoint = bounds.x;
                firstPass = false;
            } else {
                int tempLeftPoint = bounds.x;
                if (tempLeftPoint < leftPoint) {
                    leftPoint = tempLeftPoint;
                }
            }
        }

        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widget.setX(leftPoint);
        }
    }
}
