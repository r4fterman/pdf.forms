package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

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
        return getProperty("Border Style");
    }

    public Optional<String> getBorderWidth() {
        return getProperty("Border Width");
    }

    public Optional<String> getBorderColor() {
        return getProperty("Border Color");
    }

    private Optional<String> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .map(Property::getValue)
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
