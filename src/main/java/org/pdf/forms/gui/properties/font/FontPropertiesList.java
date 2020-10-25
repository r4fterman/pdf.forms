package org.pdf.forms.gui.properties.font;

import static java.util.stream.Collectors.toSet;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class FontPropertiesList {

    private final String fontName;
    private final String fontSize;
    private final String fontStyle;
    private final String underline;
    private final String strikethrough;
    private final String color;

    public FontPropertiesList(final List<FontProperties> fontProperties) {
        this.fontName = calculateListValue(fontProperties, FontProperties::getValueFontName);
        this.fontSize = calculateListValue(fontProperties, FontProperties::getValueFontSize);
        this.fontStyle = calculateListValue(fontProperties, FontProperties::getValueFontStyle);
        this.underline = calculateListValue(fontProperties, FontProperties::getValueUnderline);
        this.strikethrough = calculateListValue(fontProperties, FontProperties::getValueStrikethrough);
        this.color = calculateListValue(fontProperties, FontProperties::getValueColor);
    }

    private String calculateListValue(
            final List<FontProperties> fontProperties,
            final Function<FontProperties, String> mappingFunction) {
        final Set<String> set = fontProperties.stream()
                .map(mappingFunction)
                .collect(toSet());

        if (set.size() == 1) {
            return set.iterator().next();
        }
        return "mixed";
    }

    public String getFontName() {
        if ("mixed".equals(fontName)) {
            return null;
        }
        return fontName;
    }

    public String getFontSize() {
        if ("mixed".equals(fontSize)) {
            return null;
        }
        return fontSize;
    }

    public Integer getFontStyle() {
        if ("mixed".equals(fontStyle)) {
            return null;
        }
        return Integer.valueOf(fontStyle);
    }

    public Integer getUnderline() {
        if ("mixed".equals(underline)) {
            return null;
        }
        return Integer.valueOf(underline);
    }

    public Integer getStrikethrough() {
        if ("mixed".equals(strikethrough)) {
            return null;
        }
        return Integer.valueOf(strikethrough);
    }

    public Color getColor() {
        if ("mixed".equals(color)) {
            return null;
        }
        return new Color(Integer.parseInt(color));
    }
}
