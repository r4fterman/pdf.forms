package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Margins {

    private static final String BOTTOM = "Bottom";
    private static final String TOP = "Top";
    private static final String RIGHT = "Right";
    private static final String LEFT = "Left";

    private List<Property> property;

    public Margins() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getLeft() {
        return getPropertyValue(LEFT);
    }

    public void setLeft(final String left) {
        setPropertyValue(LEFT, left);
    }

    public Optional<String> getRight() {
        return getPropertyValue(RIGHT);
    }

    public void setRight(final String right) {
        setPropertyValue(RIGHT, right);
    }

    public Optional<String> getTop() {
        return getPropertyValue(TOP);
    }

    public void setTop(final String top) {
        setPropertyValue(TOP, top);
    }

    public Optional<String> getBottom() {
        return getPropertyValue(BOTTOM);
    }

    public void setBottom(final String bottom) {
        setPropertyValue(BOTTOM, bottom);
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
        if (o instanceof Margins) {
            final Margins margins = (Margins) o;
            return Objects.equals(property, margins.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Margins.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
