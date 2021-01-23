package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class FieldProperties {

    private List<Property> property;

    public FieldProperties() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public boolean allowCustomTextEntry() {
        return getProperty("Allow Custom Text Entry")
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    private Optional<String> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .map(Property::getValue)
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof FieldProperties) {
            final FieldProperties that = (FieldProperties) o;
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
        return new StringJoiner(", ", FieldProperties.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
