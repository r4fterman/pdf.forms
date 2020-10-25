package org.pdf.forms.gui.properties.font;

import java.util.Objects;

public class FontProperties {

    private final String valueFontName;
    private final String valueFontSize;
    private final String valueFontStyle;
    private final String valueUnderline;
    private final String valueStrikethrough;
    private final String valueColor;

    public FontProperties(
            final String valueFontName,
            final String valueFontSize,
            final String valueFontStyle,
            final String valueUnderline,
            final String valueStrikethrough,
            final String valueColor) {
        this.valueFontName = valueFontName;
        this.valueFontSize = valueFontSize;
        this.valueFontStyle = valueFontStyle;
        this.valueUnderline = valueUnderline;
        this.valueStrikethrough = valueStrikethrough;
        this.valueColor = valueColor;
    }

    public String getValueFontName() {
        return valueFontName;
    }

    public String getValueFontSize() {
        return valueFontSize;
    }

    public String getValueFontStyle() {
        return valueFontStyle;
    }

    public String getValueUnderline() {
        return valueUnderline;
    }

    public String getValueStrikethrough() {
        return valueStrikethrough;
    }

    public String getValueColor() {
        return valueColor;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof FontProperties) {
            final FontProperties that = (FontProperties) o;
            return Objects.equals(valueFontName, that.valueFontName)
                    && Objects.equals(valueFontSize, that.valueFontSize)
                    && Objects.equals(valueFontStyle, that.valueFontStyle)
                    && Objects.equals(valueUnderline, that.valueUnderline)
                    && Objects.equals(valueStrikethrough, that.valueStrikethrough)
                    && Objects.equals(valueColor, that.valueColor);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                valueFontName,
                valueFontSize,
                valueFontStyle,
                valueUnderline,
                valueStrikethrough,
                valueColor);
    }
}
