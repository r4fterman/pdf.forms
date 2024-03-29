package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "backgroundfill")
public class BackgroundFill {

    private static final String STYLE = "Style";
    private static final String FILL_COLOR = "Fill Color";

    private List<Property> property;

    public BackgroundFill() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getStyle() {
        return getPropertyValue(STYLE);
    }

    public void setStyle(final String style) {
        setPropertyValue(STYLE, style);
    }

    public Optional<String> getFillColor() {
        return getPropertyValue(FILL_COLOR);
    }

    public void setFillColor(final String color) {
        setPropertyValue(FILL_COLOR, color);
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
        if (o instanceof BackgroundFill) {
            final BackgroundFill that = (BackgroundFill) o;
            return Objects.equals(property, that.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BackgroundFill.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
