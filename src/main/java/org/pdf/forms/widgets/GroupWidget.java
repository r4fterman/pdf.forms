package org.pdf.forms.widgets;

import static java.util.stream.Collectors.toList;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;

import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.Widget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.utils.WidgetSelection;

/**
 * Special widget object that represents a group of widgets. Although this class
 * implements IWidget (as all widgets must do) it does not extend Widget
 */
public class GroupWidget implements IWidget {

    private static int nextWidgetNumber = 1;

    private static final int TYPE = IWidget.GROUP;

    private final Icon icon;
    private final Widget widget;

    private List<IWidget> widgetsInGroup;
    private String widgetName;

    public GroupWidget() {
        icon = new ImageIcon(getClass().getResource("/org/pdf/forms/res/Group.gif"));

        this.widgetName = "Group" + nextWidgetNumber;
        nextWidgetNumber++;

        this.widget = new Widget();
        widget.setType("GROUP");
        widget.setName(widgetName);
    }

    @Override
    public List<IWidget> getWidgetsInGroup() {
        return widgetsInGroup;
    }

    @Override
    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
        this.widgetsInGroup = widgetsInGroup;
        final List<Widget> widgets = widgetsInGroup.stream()
                .map(IWidget::getWidgetModel)
                .collect(toList());
        widget.setWidgets(widgets);
    }

    @Override
    public Rectangle getBounds() {
        return WidgetSelection.getMultipleWidgetBounds(new HashSet<>(widgetsInGroup));
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String getWidgetName() {
        return widgetName;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    public void setName(final String widgetName) {
        this.widgetName = widgetName;
    }

    @Override
    public void setAllProperties() {
        // not supported by groups
    }

    @Override
    public int getArrayNumber() {
        return 0;
    }

    @Override
    public JavaScriptContent getJavaScript() {
        //todo: must be added to model
        return new JavaScriptContent();
    }

    @Override
    public Widget getWidgetModel() {
        //todo: must be added to model
        return new Widget();
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        // not supported by groups
    }

    @Override
    public void setLayoutProperties() {
        // not supported by groups
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        // not supported by groups
    }

    @Override
    public void setCaptionProperties() {
        // not supported by groups
    }

    @Override
    public JComponent getWidget() {
        return null;
    }

    @Override
    public void setPosition(
            final int x,
            final int y) {
        // not supported by groups
    }

    @Override
    public void setX(final int x) {
        // not supported by groups
    }

    @Override
    public void setY(final int y) {
        // not supported by groups
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
        // not supported by groups
    }

    @Override
    public int getResizeTypeForSplitComponent(
            final int mouseX,
            final int mouseY) {
        return 0;
    }

    @Override
    public boolean allowEditCaptionAndValue() {
        return false;
    }

    @Override
    public boolean allowEditOfCaptionOnClick() {
        return false;
    }

    @Override
    public Dimension getBoxSize() {
        return null;
    }

    @Override
    public JComponent getValueComponent() {
        return null;
    }

    @Override
    public PdfCaption getCaptionComponent() {
        return null;
    }

    @Override
    public void setLastX(final int lastX) {
    }

    @Override
    public void setLastY(final int lastY) {
    }

    @Override
    public int getLastX() {
        return 0;
    }

    @Override
    public int getLastY() {
        return 0;
    }

    @Override
    public Point getAbsoluteLocationsOfCaption() {
        return null;
    }

    @Override
    public Point getAbsoluteLocationsOfValue() {
        return null;
    }

    @Override
    public boolean isComponentSplit() {
        return false;
    }

    @Override
    public double getResizeHeightRatio() {
        return 0;
    }

    @Override
    public double getResizeWidthRatio() {
        return 0;
    }

    @Override
    public void setResizeHeightRatio(final double resizeHeightRatio) {
    }

    @Override
    public void setResizeWidthRatio(final double resizeWidthRation) {
    }

    @Override
    public double getResizeFromTopRatio() {
        return 0;
    }

    @Override
    public double getResizeFromLeftRatio() {
        return 0;
    }

    @Override
    public void setResizeFromTopRatio(final double resizeHeightRatio) {
    }

    @Override
    public void setResizeFromLeftRatio(final double resizeWidthRation) {
    }

    @Override
    public void setObjectProperties() {
    }

    @Override
    public void setBorderAndBackgroundProperties() {
    }

}
