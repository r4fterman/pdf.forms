package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Draw {

    private List<Property> property;

    public Draw() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getLocation() {
        return getPropertyValue("Location");
    }

    public void setLocation(final String location) {
        setPropertyValue("Location", location);
    }

    public Optional<String> getSizing() {
        return getPropertyValue("Sizing");
    }

    public void setSizing(final String sizing) {
        setPropertyValue("Sizing", sizing);
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
        if (o instanceof Draw) {
            final Draw draw = (Draw) o;
            return Objects.equals(property, draw.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Draw.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
