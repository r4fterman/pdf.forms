package org.pdf.forms.model.des;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

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
    private static final String ANCHOR = "Anchor";
    private static final String ROTATION = "Rotation";
    private static final String X_EXPAND_TO_FIT = "XExpand to fit";
    private static final String Y_EXPAND_TO_FIT = "YExpand to fit";
    @Deprecated
    private static final String DEPRECATED_EXPAND_TO_FIT = "Expand to fit";

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

    public Optional<String> getAnchor() {
        return getPropertyValue(ANCHOR);
    }

    public void setAnchor(final String anchor) {
        setPropertyValue(ANCHOR, anchor);
    }

    public Optional<String> getRotation() {
        return getPropertyValue(ROTATION);
    }

    public void setRotation(final String rotation) {
        setPropertyValue(ROTATION, rotation);
    }

    public Optional<String> getXExpandToFit() {
        final Optional<String> propertyValue = getPropertyValue(X_EXPAND_TO_FIT);
        if (propertyValue.isPresent()) {
            return propertyValue;
        }

        final List<String> values = getPropertyValues(DEPRECATED_EXPAND_TO_FIT);
        if (values.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(values.get(0));
    }

    private List<String> getPropertyValues(final String propertyName) {
        return property.stream()
                .filter(p -> p.getName().equals(propertyName))
                .map(Property::getValue)
                .collect(toUnmodifiableList());
    }

    public void setXExpandToFit(final String xExpandToFit) {
        removeObsoleteProperty(DEPRECATED_EXPAND_TO_FIT);
        setPropertyValue(X_EXPAND_TO_FIT, xExpandToFit);
    }

    public Optional<String> getYExpandToFit() {
        return getPropertyValue(Y_EXPAND_TO_FIT);
    }

    public void setYExpandToFit(final String yExpandToFit) {
        removeObsoleteProperty(DEPRECATED_EXPAND_TO_FIT);
        setPropertyValue(Y_EXPAND_TO_FIT, yExpandToFit);
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

    private void removeObsoleteProperty(final String propertyName) {
        this.property = property.stream()
                .filter(p -> p.getName().equals(propertyName))
                .collect(toList());
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
