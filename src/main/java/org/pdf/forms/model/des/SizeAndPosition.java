package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "sizeandposition")
public class SizeAndPosition {

    private static final String X = "X";
    private static final String Y = "Y";
    private static final String WIDTH = "Width";
    private static final String HEIGHT = "Height";

    private List<Property> property;

    public SizeAndPosition() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getX() {
        return getPropertyValue(X);
    }

    public void setX(final int x) {
        setPropertyValue(X, String.valueOf(x));
    }

    public Optional<String> getY() {
        return getPropertyValue(Y);
    }

    public void setY(final int y) {
        setPropertyValue(Y, String.valueOf(y));
    }

    public Optional<String> getWidth() {
        return getPropertyValue(WIDTH);
    }

    public void setWidth(final int width) {
        setPropertyValue(WIDTH, String.valueOf(width));
    }

    public Optional<String> getHeight() {
        return getPropertyValue(HEIGHT);
    }

    public void setHeight(final int height) {
        setPropertyValue(HEIGHT, String.valueOf(height));
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
        if (o instanceof SizeAndPosition) {
            final SizeAndPosition that = (SizeAndPosition) o;
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
        return new StringJoiner(", ", SizeAndPosition.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
