package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "font")
public class FontProperties {

    @XmlElement(name = "font_caption")
    private FontCaption fontCaption;
    @XmlElement(name = "font_value")
    private FontValue fontValue;

    public FontProperties() {
        this.fontCaption = new FontCaption();
    }

    public FontCaption getFontCaption() {
        return fontCaption;
    }

    public void setFontCaption(final FontCaption fontCaption) {
        this.fontCaption = fontCaption;
    }

    public FontValue getFontValue() {
        if (fontValue == null) {
            setFontValue(new FontValue());
        }
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
