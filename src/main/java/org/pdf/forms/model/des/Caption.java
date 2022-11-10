package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Caption {

    private static final String POSITION = "Position";
    private static final String RESERVE = "Reserve";

    private List<Property> property;

    public Caption() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getPosition() {
        return getPropertyValue(POSITION);
    }

    public boolean isPositionNone() {
        return getPosition()
                .filter(pos -> pos.equals("None"))
                .isPresent();
    }

    public void setPosition(final String position) {
        setPropertyValue(POSITION, position);
    }

    public Optional<String> getReserve() {
        return getPropertyValue(RESERVE);
    }

    public void setReserve(final String reserve) {
        setPropertyValue(RESERVE, reserve);
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
        if (o instanceof Caption) {
            final Caption caption = (Caption) o;
            return Objects.equals(property, caption.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Caption.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
