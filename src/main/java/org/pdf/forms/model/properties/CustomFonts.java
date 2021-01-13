package org.pdf.forms.model.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "customfonts")
public class CustomFonts {

    private List<Font> font;

    public CustomFonts() {
        this.font = new ArrayList<>();
    }

    public List<Font> getFont() {
        return font;
    }

    public void setFont(final List<Font> font) {
        this.font = font;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CustomFonts) {
            final CustomFonts that = (CustomFonts) o;
            return Objects.equals(font, that.font);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(font);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomFonts.class.getSimpleName() + "[", "]")
                .add("font=" + font)
                .toString();
    }
}
