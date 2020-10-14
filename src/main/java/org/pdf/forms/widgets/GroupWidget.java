package org.pdf.forms.widgets;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Special widget object that represents a group of widgets. Although this class
 * implements IWidget (as all widgets must do) it does not extend Widget
 */
public class GroupWidget implements IWidget {

    private static int nextWidgetNumber = 1;

    private static final int TYPE = IWidget.GROUP;

    private final Logger logger = LoggerFactory.getLogger(GroupWidget.class);

    private final Icon icon;
    private final Element widgetsElement;

    private List<IWidget> widgetsInGroup;
    private String widgetName;
    private Document properties;

    private Element setupProperties() {
        try {
            properties = XMLUtils.createNewDocument();
        } catch (final ParserConfigurationException e) {
            logger.error("Error setting up properties", e);
        }

        return XMLUtils.createAndAppendElement(properties, "widget", properties);
    }

    public GroupWidget() {
        icon = new ImageIcon(getClass().getResource("/org/pdf/forms/res/Group.gif"));

        this.widgetName = "Group" + nextWidgetNumber;
        nextWidgetNumber++;

        final Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(properties, "type", "GROUP", rootElement);
        XMLUtils.addBasicProperty(properties, "name", widgetName, rootElement);

        widgetsElement = XMLUtils.createAndAppendElement(properties, "widgets", rootElement);
    }

    @Override
    public List<IWidget> getWidgetsInGroup() {
        return widgetsInGroup;
    }

    @Override
    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
        this.widgetsInGroup = widgetsInGroup;

        final List<Element> widgets = XMLUtils.getElementsFromNodeList(widgetsElement.getChildNodes());
        for (final Element widget : widgets) {
            final Node parent = widget.getParentNode();
            parent.removeChild(widget);
        }

        for (final IWidget widget : widgetsInGroup) {
            final Document widgetProperties = widget.getProperties();
            final Element widgetRoot = widgetProperties.getDocumentElement();
            widgetsElement.appendChild(properties.importNode(widgetRoot, true));
        }
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
    public Document getProperties() {
        return properties;
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
    public void setParagraphProperties(
            final Element paragraphProperties,
            final int currentlyEditing) {
        // not supported by groups
    }

    @Override
    public void setLayoutProperties(final Element paragraphProperties) {
        // not supported by groups
    }

    @Override
    public void setFontProperties(
            final Element parentElement,
            final int currentlyEditing) {
        // not supported by groups
    }

    @Override
    public void setCaptionProperties(final Element captionProperties) {
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
    public void setObjectProperties(final Element parentElement) {
    }

    @Override
    public void setBorderAndBackgroundProperties(final Element borderProperties) {
    }

}
