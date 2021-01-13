package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "font")
public class FontProperties {

    private FontCaption fontCaption;
    private FontValue fontValue;

    public FontCaption getFontCaption() {
        return fontCaption;
    }

    public void setFontCaption(final FontCaption fontCaption) {
        this.fontCaption = fontCaption;
    }

    public FontValue getFontValue() {
        return fontValue;
    }

    public void setFontValue(final FontValue fontValue) {
        this.fontValue = fontValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof FontProperties) {
            final FontProperties that = (FontProperties) o;
            return Objects.equals(fontCaption, that.fontCaption)
                    && Objects.equals(fontValue, that.fontValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontCaption, fontValue);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FontProperties.class.getSimpleName() + "[", "]")
                .add("fontCaption=" + fontCaption)
                .add("fontValue=" + fontValue)
                .toString();
    }
}
