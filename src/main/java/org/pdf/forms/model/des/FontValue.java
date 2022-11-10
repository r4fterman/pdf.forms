package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "font_value")
public class FontValue {

    private static final String FONT_NAME = "Font Name";
    private static final String FONT_SIZE = "Font Size";
    private static final String FONT_STYLE = "Font Style";
    private static final String UNDERLINE = "Underline";
    private static final String STRIKETHROUGH = "Strikethrough";
    private static final String COLOR = "Color";

    private List<Property> property;

    public FontValue() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getFontName() {
        return getPropertyValue(FONT_NAME);
    }

    public void setFontName(final String fontName) {
        setPropertyValue(FONT_NAME, fontName);
    }

    public Optional<String> getFontSize() {
        return getPropertyValue(FONT_SIZE);
    }

    public void setFontSize(final String fontSize) {
        setPropertyValue(FONT_SIZE, fontSize);
    }

    public Optional<String> getFontStyle() {
        return getPropertyValue(FONT_STYLE);
    }

    public void setFontStyle(final String fontStyle) {
        setPropertyValue(FONT_STYLE, fontStyle);
    }

    public Optional<String> getUnderline() {
        return getPropertyValue(UNDERLINE);
    }

    public void setUnderline(final String underline) {
        setPropertyValue(UNDERLINE, underline);
    }

    public Optional<String> getStrikeThrough() {
        return getPropertyValue(STRIKETHROUGH);
    }

    public void setStrikeThrough(final String strikeThrough) {
        setPropertyValue(STRIKETHROUGH, strikeThrough);
    }

    public Optional<String> getColor() {
        return getPropertyValue(COLOR);
    }

    public void setColor(final String color) {
        setPropertyValue(COLOR, color);
    }

    private Optional<String> getPropertyValue(final String propertyName) {
        return getProperty(propertyName)
                .map(Property::getValue);
    }

    private void setPropertyValue(
            final String propertyName,
            final String propertyValue) {
        getProperty(propertyName)
                .ifPresentOrElse(
                        p -> p.setValue(propertyValue),
                        () -> property.add(new Property(propertyName, propertyValue))
                );
    }

    private Optional<Property> getProperty(final String propertyName) {
        return property.stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof FontValue) {
            final FontValue fontValue = (FontValue) o;
            return Objects.equals(property, fontValue.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FontValue.class.getSimpleName() + "[", "]")
                .add("propertyList=" + property)
                .toString();
    }
}
