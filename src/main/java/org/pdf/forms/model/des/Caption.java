package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Caption {

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
        return getProperty("Position");
    }

    public boolean isPositionNone() {
        return getPosition()
                .filter(pos -> pos.equals("None"))
                .isPresent();
    }

    public Optional<String> getReserve() {
        return getProperty("Reserve");
    }

    private Optional<String> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .map(Property::getValue)
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
