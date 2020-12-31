package org.pdf.forms.gui.properties;

import java.awt.*;
import java.util.List;
import java.util.Set;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PropertyChanger {

    private static void setProperty(
            final String value,
            final Element captionElement,
            final Element valueElement) {
        if (value != null) {
            if (captionElement != null) {
                captionElement.getAttributeNode("value").setValue(value);
            }
            if (valueElement != null) {
                valueElement.getAttributeNode("value").setValue(value);
            }
        }
    }

    public static void updateFont(
            final Set<IWidget> widgets,
            final String fontName,
            final String fontSize,
            final int fontStyle) {
        for (final IWidget widget: widgets) {
            final Document properties = widget.getProperties();

            final Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            final List<Element> fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name").get();
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size").get();
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style").get();

            Element valueFontName = null;
            Element valueFontSize = null;
            Element valueFontStyle = null;

            if (widget.allowEditCaptionAndValue()) {
                valueFontName = XMLUtils.getPropertyElement(valueElement, "Font Name").get();
                valueFontSize = XMLUtils.getPropertyElement(valueElement, "Font Size").get();
                valueFontStyle = XMLUtils.getPropertyElement(valueElement, "Font Style").get();
            }

            setProperty(fontName, captionFontName, valueFontName);
            setProperty(fontSize, captionFontSize, valueFontSize);

            setProperty(fontStyle + "", captionFontStyle, valueFontStyle);

            widget.setFontProperties(fontElement, IWidget.COMPONENT_BOTH);
        }
    }

    public static void updateSizeAndPosition(final Set<IWidget> widgetSelection) {
        for (final IWidget widget: widgetSelection) {
            final Element element = (Element) widget.getProperties().getElementsByTagName("sizeandposition").item(0);

            XMLUtils.getPropertyElement(element, "X")
                    .ifPresent(xCord -> xCord.getAttributeNode("value").setValue(String.valueOf(widget.getX())));

            XMLUtils.getPropertyElement(element, "Y")
                    .ifPresent(yCord -> yCord.getAttributeNode("value").setValue(String.valueOf(widget.getY())));

            XMLUtils.getPropertyElement(element, "Width")
                    .ifPresent(width -> width.getAttributeNode("value").setValue(String.valueOf(widget.getWidth())));

            XMLUtils.getPropertyElement(element, "Height")
                    .ifPresent(height -> height.getAttributeNode("value").setValue(String.valueOf(widget.getHeight())));
        }
    }

    public static void updateSizeAndPosition(
            final Set<IWidget> widgets,
            final Point point,
            final Dimension dimension) {
        widgets.stream()
                .map(widget -> (Element) widget.getProperties().getElementsByTagName("sizeandposition").item(0))
                .forEach(element -> {
                    updatePosition(point, element);
                    updateSize(dimension, element);
                });
    }

    private static void updatePosition(
            final Point point,
            final Element element) {
        if (point == null) {
            return;
        }

        if (point.x > -1) {
            final String valueX = String.valueOf(point.getX());
            XMLUtils.getPropertyElement(element, "X")
                    .ifPresent(xCord -> xCord.getAttributeNode("value").setValue(valueX));
        }
        if (point.y > -1) {
            final String valueY = String.valueOf(point.getY());
            XMLUtils.getPropertyElement(element, "Y")
                    .ifPresent(yCord -> yCord.getAttributeNode("value").setValue(valueY));
        }
    }

    private static void updateSize(
            final Dimension dimension,
            final Element element) {
        if (dimension == null) {
            return;
        }

        if (dimension.width > -1) {
            final String width = String.valueOf(dimension.getWidth());
            XMLUtils.getPropertyElement(element, "Width")
                    .ifPresent(propertyElement ->
                            propertyElement.getAttributeNode("value").setValue(width));
        }
        if (dimension.height > -1) {
            final String height = String.valueOf(dimension.getHeight());
            XMLUtils.getPropertyElement(element, "Height")
                    .ifPresent(propertyElement ->
                            propertyElement.getAttributeNode("value").setValue(height));
        }
    }
}
