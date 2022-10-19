package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "binding")
public class BindingProperties {

    private static final String NAME = "Name";
    private static final String ARRAY_NUMBER = "Array Number";

    private List<Property> property;

    public BindingProperties() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getName() {
        return getPropertyValue(NAME);
    }

    public void setName(final String name) {
        setPropertyValue(NAME, name);
    }

    public Optional<String> getArrayNumber() {
        return getPropertyValue(ARRAY_NUMBER);
    }

    public void setArrayNumber(final String arrayNumber) {
        setPropertyValue(ARRAY_NUMBER, arrayNumber);
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
        if (o instanceof BindingProperties) {
            final BindingProperties that = (BindingProperties) o;
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
        return new StringJoiner(", ", BindingProperties.class.getSimpleName() + "[", "]")
                .add("propertyList=" + property)
                .toString();
    }
}
