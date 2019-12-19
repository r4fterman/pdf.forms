package org.pdf.forms.widgets.utils;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.widgets.IWidget;

public class WidgetAlignmentAndOrder {

    private static final String ALIGN_LEFT = "Align Left";
    private static final String ALIGN_RIGHT = "Align Right";
    private static final String ALIGN_TOP = "Align Top";
    private static final String ALIGN_BOTTOM = "Align Bottom";
    private static final String ALIGN_VERTICALLY = "Align Verticaly";
    private static final String ALIGN_HORIZONTALLY = "Align Horizontaly";
    public static final String BRING_TO_FRONT = "Bring to Front";
    public static final String SEND_TO_BACK = "Send to Back";
    public static final String BRING_FORWARDS = "Bring Forwards";
    public static final String SEND_BACKWARDS = "Send Backwards";

    private static final String RES_ROOT = "/org/pdf/forms/res/";

    private static final String[] ALIGN_BUTTONS = new String[] {
            RES_ROOT + ALIGN_LEFT + ".gif",
            RES_ROOT + ALIGN_RIGHT + ".gif",
            RES_ROOT + ALIGN_TOP + ".gif",
            RES_ROOT + ALIGN_BOTTOM + ".gif",
            "Seperator",
            RES_ROOT + ALIGN_VERTICALLY + ".gif",
            RES_ROOT + ALIGN_HORIZONTALLY + ".gif",
            "Seperator", };

    private static final String[] ORDER_BUTTONS = new String[] {
            RES_ROOT + BRING_TO_FRONT + ".gif",
            RES_ROOT + SEND_TO_BACK + ".gif",
            RES_ROOT + BRING_FORWARDS + ".gif",
            RES_ROOT + SEND_BACKWARDS + ".gif" };

    private static final Map<String, Consumer<Set<IWidget>>> ALIGNMENT_MAP = Map.of(
            WidgetAlignmentAndOrder.ALIGN_LEFT, WidgetAlignmentAndOrder::alignLeft,
            WidgetAlignmentAndOrder.ALIGN_RIGHT, WidgetAlignmentAndOrder::alignRight,
            WidgetAlignmentAndOrder.ALIGN_TOP, WidgetAlignmentAndOrder::alignTop,
            WidgetAlignmentAndOrder.ALIGN_BOTTOM, WidgetAlignmentAndOrder::alignBottom,
            WidgetAlignmentAndOrder.ALIGN_VERTICALLY, WidgetAlignmentAndOrder::alignVertically,
            WidgetAlignmentAndOrder.ALIGN_HORIZONTALLY, WidgetAlignmentAndOrder::alignHorizontally
    );


    public static String[] getAlignButtons() {
        return ALIGN_BUTTONS;
    }

    public static String[] getOrderButtons() {
        return ORDER_BUTTONS;
    }

    public static void alignAndOrder(
            final IDesigner designerPanel,
            final String type) {
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        ALIGNMENT_MAP
                .getOrDefault(type, selected -> changeOrderOfSelectedWidgets(type, designerPanel.getWidgets(), selected))
                .accept(selectedWidgets);

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
            final String type,
            final List<IWidget> widgets,
            final Set<IWidget> selectedWidgets) {
        final int size = widgets.size() - 1;
        final Set<IWidget> newSet = new HashSet<>(Set.copyOf(selectedWidgets));

        switch (type) {
            case WidgetAlignmentAndOrder.BRING_TO_FRONT:
                for (int i = 0; i < size; i++) {
                    final IWidget widget = widgets.get(i);
                    if (newSet.remove(widget)) {
                        widgets.add(size, widgets.remove(i));
                        i = -1;
                    }
                }
                break;
            case WidgetAlignmentAndOrder.SEND_TO_BACK:
                for (int i = size; i >= 0; i--) {
                    final IWidget widget = widgets.get(i);
                    if (newSet.remove(widget)) {
                        if (i > 0) {
                            widgets.add(0, widgets.remove(i));
                        }
                        i = size + 1;
                    }
                }
                break;
            case WidgetAlignmentAndOrder.BRING_FORWARDS:
                for (int i = 0; i < size; i++) {
                    final IWidget widget = widgets.get(i);
                    if (newSet.remove(widget)) {
                        widgets.add(i + 1, widgets.remove(i));
                        i = -1;
                    }
                }
                break;
            case WidgetAlignmentAndOrder.SEND_BACKWARDS:
                for (int i = size; i >= 0; i--) {
                    final IWidget widget = widgets.get(i);
                    if (newSet.remove(widget)) {
                        if (i > 0) {
                            widgets.add(i - 1, widgets.remove(i));
                        }

                        i = size + 1;
                    }
                }
                break;
            default:
                break;
        }
    }

    private static void alignHorizontally(final Set<IWidget> selectedWidgets) {
        int averageCenterPoint = 0;
        for (final Object widget1 : selectedWidgets) {
            final IWidget widget = (IWidget) widget1;
            final Rectangle bounds = widget.getBounds();

            averageCenterPoint += bounds.x + bounds.width / 2;
        }

        averageCenterPoint = averageCenterPoint / selectedWidgets.size();

        for (final IWidget widget : selectedWidgets) {
            widget.setX(averageCenterPoint - widget.getBounds().width / 2);
        }
    }

    private static void alignVertically(final Set<IWidget> selectedWidgets) {
        int averageCenterPoint = 0;
        for (final Object widget1 : selectedWidgets) {
            final IWidget widget = (IWidget) widget1;
            final Rectangle bounds = widget.getBounds();

            averageCenterPoint += bounds.y + bounds.height / 2;
        }

        averageCenterPoint = averageCenterPoint / selectedWidgets.size();

        for (final IWidget widget : selectedWidgets) {
            widget.setY(averageCenterPoint - widget.getBounds().height / 2);
        }
    }

    private static void alignBottom(final Set<IWidget> selectedWidgets) {
        int bottomPoint = 0;
        boolean firstPass = true;
        for (final IWidget widget : selectedWidgets) {
            final Rectangle bounds = widget.getBounds();

            if (firstPass) {
                bottomPoint = bounds.y + bounds.height;
                firstPass = false;
            } else {
                final int tempBottomPoint = bounds.y + bounds.height;
                if (tempBottomPoint > bottomPoint) {
                    bottomPoint = tempBottomPoint;
                }
            }
        }

        for (final IWidget widget : selectedWidgets) {
            widget.setY(bottomPoint - widget.getBounds().height);
        }
    }

    private static void alignTop(final Set<IWidget> selectedWidgets) {
        int topPoint = 0;
        boolean firstPass = true;
        for (final IWidget widget : selectedWidgets) {
            final Rectangle bounds = widget.getBounds();

            if (firstPass) {
                topPoint = bounds.y;
                firstPass = false;
            } else {
                final int tempTopPoint = bounds.y;
                if (tempTopPoint < topPoint) {
                    topPoint = tempTopPoint;
                }
            }
        }

        for (final IWidget widget : selectedWidgets) {
            widget.setY(topPoint);
        }
    }

    private static void alignRight(final Set<IWidget> selectedWidgets) {
        int rightPoint = 0;
        boolean firstPass = true;
        for (final IWidget widget : selectedWidgets) {
            final Rectangle bounds = widget.getBounds();

            if (firstPass) {
                rightPoint = bounds.x + bounds.width;
                firstPass = false;
            } else {
                final int tempRightPoint = bounds.x + bounds.width;
                if (tempRightPoint > rightPoint) {
                    rightPoint = tempRightPoint;
                }
            }
        }

        for (final IWidget widget : selectedWidgets) {
            widget.setX(rightPoint - widget.getBounds().width);
        }
    }

    private static void alignLeft(final Set<IWidget> selectedWidgets) {
        int leftPoint = 0;
        boolean firstPass = true;
        for (final IWidget widget : selectedWidgets) {
            final Rectangle bounds = widget.getBounds();

            if (firstPass) {
                leftPoint = bounds.x;
                firstPass = false;
            } else {
                final int tempLeftPoint = bounds.x;
                if (tempLeftPoint < leftPoint) {
                    leftPoint = tempLeftPoint;
                }
            }
        }

        for (final IWidget widget : selectedWidgets) {
            widget.setX(leftPoint);
        }
    }
}
