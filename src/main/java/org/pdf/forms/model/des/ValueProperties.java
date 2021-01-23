package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "value")
public class ValueProperties {

    private List<Property> property;

    public ValueProperties() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getDefault() {
        return getProperty("Default");
    }

    private Optional<String> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .map(Property::getValue)
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ValueProperties) {
            final ValueProperties that = (ValueProperties) o;
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
        return new StringJoiner(", ", ValueProperties.class.getSimpleName() + "[", "]")
                .add("propertyList=" + property)
                .toString();
    }
}
