package org.pdf.forms.gui.properties;

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
            captionElement.getAttributeNode("value").setValue(value);
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
        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();

            final Element fontElement = (Element) properties.getElementsByTagName("font").item(0);

            final List fontList = XMLUtils.getElementsFromNodeList(fontElement.getChildNodes());
            final Element captionElement = (Element) fontList.get(0);
            Element valueElement = null;
            if (widget.allowEditCaptionAndValue()) {
                valueElement = (Element) fontList.get(1);
            }

            final Element captionFontName = XMLUtils.getPropertyElement(captionElement, "Font Name").get();
            final Element captionFontSize = XMLUtils.getPropertyElement(captionElement, "Font Size").get();
            final Element captionFontStyle = XMLUtils.getPropertyElement(captionElement, "Font Style").get();

            Element valueFontName = null, valueFontSize = null, valueFontStyle = null;

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
        for (final IWidget widget : widgetSelection) {
            final Document properties = widget.getProperties();

            final Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            final Element xCord = XMLUtils.getPropertyElement(element, "X").get();
            xCord.getAttributeNode("value").setValue(widget.getX() + "");

            final Element yCord = XMLUtils.getPropertyElement(element, "Y").get();
            yCord.getAttributeNode("value").setValue(widget.getY() + "");

            final Element width = XMLUtils.getPropertyElement(element, "Width").get();
            width.getAttributeNode("value").setValue(widget.getWidth() + "");

            final Element height = XMLUtils.getPropertyElement(element, "Height").get();
            height.getAttributeNode("value").setValue(widget.getHeight() + "");
        }
    }

    public static void updateSizeAndPosition(
            final Set<IWidget> widgetSelection,
            final Integer[] props) {
        //TODO can we change the props parameter type to int[]?

        for (final IWidget widget : widgetSelection) {
            final Document properties = widget.getProperties();

            final Element element = (Element) properties.getElementsByTagName("sizeandposition").item(0);

            if (props[0] != null) {
                final Element xCord = XMLUtils.getPropertyElement(element, "X").get();
                xCord.getAttributeNode("value").setValue(props[0] + "");
            }

            if (props[1] != null) {
                final Element yCord = XMLUtils.getPropertyElement(element, "Y").get();
                yCord.getAttributeNode("value").setValue(props[1] + "");
            }

            if (props[2] != null) {
                final Element width = XMLUtils.getPropertyElement(element, "Width").get();
                width.getAttributeNode("value").setValue(props[2] + "");
            }

            if (props[3] != null) {
                final Element height = XMLUtils.getPropertyElement(element, "Height").get();
                height.getAttributeNode("value").setValue(props[3] + "");
            }
        }
    }
}
