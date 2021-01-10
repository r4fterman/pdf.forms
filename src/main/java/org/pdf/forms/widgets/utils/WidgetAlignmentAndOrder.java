package org.pdf.forms.widgets.utils;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public final class WidgetAlignmentAndOrder {

    public static final String ALIGN_LEFT = "Align Left";
    public static final String ALIGN_RIGHT = "Align Right";
    public static final String ALIGN_TOP = "Align Top";
    public static final String ALIGN_BOTTOM = "Align Bottom";
    public static final String ALIGN_VERTICALLY = "Align Verticaly";
    public static final String ALIGN_HORIZONTALLY = "Align Horizontaly";
    public static final String BRING_TO_FRONT = "Bring to Front";
    public static final String SEND_TO_BACK = "Send to Back";
    public static final String BRING_FORWARDS = "Bring Forwards";
    public static final String SEND_BACKWARDS = "Send Backwards";

    private static final String RES_ROOT = "/org/pdf/forms/res/";

    private static final Map<String, String> ALIGN_BUTTONS_TO_ICONS = Map.of(
            ALIGN_LEFT, RES_ROOT + ALIGN_LEFT + ".gif",
            ALIGN_RIGHT, RES_ROOT + ALIGN_RIGHT + ".gif",
            ALIGN_TOP, RES_ROOT + ALIGN_TOP + ".gif",
            ALIGN_BOTTOM, RES_ROOT + ALIGN_BOTTOM + ".gif",
            ALIGN_VERTICALLY, RES_ROOT + ALIGN_VERTICALLY + ".gif",
            ALIGN_HORIZONTALLY, RES_ROOT + ALIGN_HORIZONTALLY + ".gif"
    );

    private static final List<String> ORDER_BUTTONS = List.of(
            RES_ROOT + BRING_TO_FRONT + ".gif",
            RES_ROOT + SEND_TO_BACK + ".gif",
            RES_ROOT + BRING_FORWARDS + ".gif",
            RES_ROOT + SEND_BACKWARDS + ".gif"
    );

    private static final Map<String, Consumer<Set<IWidget>>> ALIGNMENT_MAP = Map.of(
            WidgetAlignmentAndOrder.ALIGN_LEFT, WidgetAlignmentAndOrder::alignLeft,
            WidgetAlignmentAndOrder.ALIGN_RIGHT, WidgetAlignmentAndOrder::alignRight,
            WidgetAlignmentAndOrder.ALIGN_TOP, WidgetAlignmentAndOrder::alignTop,
            WidgetAlignmentAndOrder.ALIGN_BOTTOM, WidgetAlignmentAndOrder::alignBottom,
            WidgetAlignmentAndOrder.ALIGN_VERTICALLY, WidgetAlignmentAndOrder::alignVertically,
            WidgetAlignmentAndOrder.ALIGN_HORIZONTALLY, WidgetAlignmentAndOrder::alignHorizontally
    );

    public static String[] getAlignButtons() {
        return ALIGN_BUTTONS_TO_ICONS.values().toArray(new String[0]);
    }

    public static String[] getOrderButtons() {
        return ORDER_BUTTONS.toArray(new String[0]);
    }

    public static String getIconPath(final String alignment) {
        return ALIGN_BUTTONS_TO_ICONS.getOrDefault(alignment, RES_ROOT);
    }

    public static void alignAndOrder(
            final IDesigner designerPanel,
            final String type) {
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        ALIGNMENT_MAP
                .getOrDefault(type,
                        selected -> changeOrderOfSelectedWidgets(type, designerPanel.getWidgets(), selected))
                .accept(selectedWidgets);

        PropertyChanger.updateSizeAndPosition(selectedWidgets);

        designerPanel.getMainFrame().setPropertiesCompound(selectedWidgets);
        designerPanel.getMainFrame().setPropertiesToolBar(selectedWidgets);
        designerPanel.getMainFrame().updateHierarchy();

        designerPanel.repaint();
    }

    private static void changeOrderOfSelectedWidgets(
            final String type,
            final List<IWidget> allWidgets,
            final Set<IWidget> selectedWidgets) {
        // Note: This method takes a reference to the list of widgets stored in the
        // designer panel. It then alters the list here.

        final int size = allWidgets.size() - 1;
        if (WidgetAlignmentAndOrder.BRING_TO_FRONT.equals(type)) {
            bringToFront(allWidgets, size, selectedWidgets);
        } else if (WidgetAlignmentAndOrder.SEND_TO_BACK.equals(type)) {
            sendToBack(allWidgets, size, selectedWidgets);
        } else if (WidgetAlignmentAndOrder.BRING_FORWARDS.equals(type)) {
            bringForwards(allWidgets, size, selectedWidgets);
        } else if (WidgetAlignmentAndOrder.SEND_BACKWARDS.equals(type)) {
            sendBackwards(allWidgets, size, selectedWidgets);
        }
    }

    private static void sendBackwards(
            final List<IWidget> allWidgets,
            final int size,
            final Set<IWidget> selectedWidgets) {
        final Set<IWidget> newSet = new HashSet<>(Set.copyOf(selectedWidgets));

        for (int i = size; i >= 0; i--) {
            final IWidget widget = allWidgets.get(i);
            if (newSet.remove(widget)) {
                if (i > 0) {
                    allWidgets.add(i - 1, allWidgets.remove(i));
                }
                i = size + 1;
            }
        }
    }

    private static void bringForwards(
            final List<IWidget> allWidgets,
            final int size,
            final Set<IWidget> selectedWidgets) {
        final Set<IWidget> newSet = new HashSet<>(Set.copyOf(selectedWidgets));

        for (int i = 0; i < size; i++) {
            final IWidget widget = allWidgets.get(i);
            if (newSet.remove(widget)) {
                allWidgets.add(i + 1, allWidgets.remove(i));
                i = -1;
            }
        }
    }

    private static void sendToBack(
            final List<IWidget> allWidgets,
            final int size,
            final Set<IWidget> selectedWidgets) {
        final Set<IWidget> newSet = new HashSet<>(Set.copyOf(selectedWidgets));

        for (int i = size; i >= 0; i--) {
            final IWidget widget = allWidgets.get(i);
            if (newSet.remove(widget)) {
                if (i > 0) {
                    allWidgets.add(0, allWidgets.remove(i));
                }
                i = size + 1;
            }
        }
    }

    private static void bringToFront(
            final List<IWidget> allWidgets,
            final int size,
            final Set<IWidget> selectedWidgets) {
        final Set<IWidget> newSet = new HashSet<>(Set.copyOf(selectedWidgets));

        for (int i = 0; i < size; i++) {
            final IWidget widget = allWidgets.get(i);
            if (newSet.remove(widget)) {
                allWidgets.add(size, allWidgets.remove(i));
                i = -1;
            }
        }
    }

    private static void alignHorizontally(final Set<IWidget> selectedWidgets) {
        final int averageCenterPoint = calculateAverageXCenterPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(averageCenterPoint - widget.getBounds().width / 2);
        }
    }

    private static void alignVertically(final Set<IWidget> selectedWidgets) {
        final int averageCenterPoint = calculateAverageYCenterPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setY(averageCenterPoint - widget.getBounds().height / 2);
        }
    }

    private static void alignBottom(final Set<IWidget> selectedWidgets) {
        final int bottomPoint = calculateBottomPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setY(bottomPoint - widget.getBounds().height);
        }
    }

    private static void alignTop(final Set<IWidget> selectedWidgets) {
        final int topPoint = calculateTopPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setY(topPoint);
        }
    }

    private static void alignRight(final Set<IWidget> selectedWidgets) {
        final int rightPoint = calculateRightPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(rightPoint - widget.getBounds().width);
        }
    }

    private static void alignLeft(final Set<IWidget> selectedWidgets) {
        final int leftPoint = calculateLeftPoint(selectedWidgets);
        for (final IWidget widget: selectedWidgets) {
            widget.setX(leftPoint);
        }
    }

    private static int calculateBottomPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int bottomPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.y + bounds.height;
            if (point > bottomPoint) {
                bottomPoint = point;
            }
        }
        return bottomPoint;
    }

    private static int calculateTopPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int topPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.y;
            if (point < topPoint) {
                topPoint = point;
            }
        }
        return topPoint;
    }

    private static int calculateRightPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int rightPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.x + bounds.width;
            if (point > rightPoint) {
                rightPoint = point;
            }
        }
        return rightPoint;
    }

    private static int calculateLeftPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int leftPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();

            final int point = bounds.x;
            if (point < leftPoint) {
                leftPoint = point;
            }
        }
        return leftPoint;
    }

    private static int calculateAverageXCenterPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int averageCenterPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();
            averageCenterPoint += bounds.x + bounds.width / 2;
        }

        return averageCenterPoint / widgets.size();
    }

    private static int calculateAverageYCenterPoint(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            return 0;
        }

        int averageCenterPoint = 0;
        for (final IWidget widget: widgets) {
            final Rectangle bounds = widget.getBounds();
            averageCenterPoint += bounds.y + bounds.height / 2;
        }

        return averageCenterPoint / widgets.size();
    }

    private WidgetAlignmentAndOrder() {
        // do nothing
    }
}
