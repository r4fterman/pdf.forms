package org.pdf.forms.gui.hierarchy.tree;

import javax.swing.*;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;

public class WidgetTreeNodeRenderer implements TreeNodeRenderer {

    private final IWidget widget;
    private final IMainFrame mainFrame;

    public WidgetTreeNodeRenderer(
            final IWidget widget,
            final IMainFrame mainFrame) {
        this.widget = widget;
        this.mainFrame = mainFrame;
    }

    @Override
    public String getText() {
        final String widgetName = widget.getWidgetName();
        if (mainFrame.getWidgetArrays().isWidgetArrayInList(widgetName)) {
            final int arrayNumber = widget.getArrayNumber();
            return widgetName + "[" + arrayNumber + "]";
        }
        return widgetName;
    }

    @Override
    public Icon getIcon() {
        return widget.getIcon();
    }
}
