package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Borders {

    private List<Property> property;

    public Borders() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getBorderStyle() {
        return getPropertyValue("Border Style");
    }

    public void setBorderStyle(final String style) {
        setPropertyValue("Border Style", style);
    }

    public Optional<String> getBorderWidth() {
        return getPropertyValue("Border Width");
    }

    public void setBorderWidth(final String width) {
        setPropertyValue("Border Width", width);
    }

    public Optional<String> getBorderColor() {
        return getPropertyValue("Border Color");
    }

    public void setBorderColor(final String color) {
        setPropertyValue("Border Color", color);
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
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Borders) {
            final Borders borders = (Borders) o;
            return Objects.equals(property, borders.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Borders.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
