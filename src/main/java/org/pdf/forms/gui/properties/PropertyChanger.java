package org.pdf.forms.gui.properties;

import java.awt.*;
import java.util.Set;

import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.FontProperties;
import org.pdf.forms.model.des.FontValue;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.IWidget;

public final class PropertyChanger {

    public static void updateFont(
            final Set<IWidget> widgets,
            final String fontName,
            final String fontSize,
            final int fontStyle) {
        for (final IWidget widget: widgets) {
            final FontProperties fontProperties = widget.getWidgetModel().getProperties().getFont();

            final FontCaption fontCaption = fontProperties.getFontCaption();
            fontCaption.setFontName(fontName);
            fontCaption.setFontSize(fontSize);
            fontCaption.setFontStyle(String.valueOf(fontStyle));

            if (widget.allowEditCaptionAndValue()) {
                final FontValue fontValue = fontProperties.getFontValue();
                fontValue.setFontName(fontName);
                fontValue.setFontSize(fontSize);
                fontValue.setFontStyle(String.valueOf(fontStyle));

            }
        }
    }

    public static void updateSizeAndPosition(final Set<IWidget> widgets) {
        for (final IWidget widget: widgets) {
            final SizeAndPosition sizeAndPosition = widget.getWidgetModel().getProperties().getLayout()
                    .getSizeAndPosition();

            sizeAndPosition.setX(widget.getX());
            sizeAndPosition.setY(widget.getY());
            sizeAndPosition.setWidth(widget.getWidth());
            sizeAndPosition.setHeight(widget.getHeight());
        }
    }

    public static void updateSizeAndPosition(
            final Set<IWidget> widgets,
            final Point point,
            final Dimension dimension) {
        widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition())
                .forEach(sizeAndPosition -> {
                    updatePosition(point, sizeAndPosition);
                    updateSize(dimension, sizeAndPosition);
                });
    }

    private static void updatePosition(
            final Point point,
            final SizeAndPosition sizeAndPosition) {
        if (point == null) {
            return;
        }

        if (point.x > -1) {
            sizeAndPosition.setX(point.x);
        }
        if (point.y > -1) {
            sizeAndPosition.setY(point.y);
        }
    }

    private static void updateSize(
            final Dimension dimension,
            final SizeAndPosition sizeAndPosition) {
        if (dimension == null) {
            return;
        }

        if (dimension.width > -1) {
            sizeAndPosition.setWidth(dimension.width);
        }
        if (dimension.height > -1) {
            sizeAndPosition.setHeight(dimension.height);
        }
    }

    private PropertyChanger() {
        // do nothing
    }
}
