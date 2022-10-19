package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "value")
public class ValueProperties {

    private static final String TYPE = "Type";
    private static final String DEFAULT = "Default";
    private static final String EMPTY_MESSAGE = "Empty Message";

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
        return getPropertyValue(DEFAULT);
    }

    public void setDefault(final String defaultValue) {
        setPropertyValue(DEFAULT, defaultValue);
    }

    public Optional<String> getType() {
        return getPropertyValue(TYPE);
    }

    public void setType(final String type) {
        setPropertyValue(TYPE, type);
    }

    public Optional<String> getEmptyMessage() {
        return getPropertyValue(EMPTY_MESSAGE);
    }

    public void setEmptyMessage(final String emptyMessage) {
        setPropertyValue(EMPTY_MESSAGE, emptyMessage);
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

    private Optional<String> getPropertyValue(final String propertyName) {
        return getProperty(propertyName)
                .map(Property::getValue);
    }

    private Optional<Property> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
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
