package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "paragraph_caption")
public class ParagraphCaption {

    private static final String VERTICAL_ALIGNMENT = "Vertical Alignment";
    private static final String HORIZONTAL_ALIGNMENT = "Horizontal Alignment";

    private List<Property> property;

    public ParagraphCaption() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getHorizontalAlignment() {
        return getPropertyValue(HORIZONTAL_ALIGNMENT);
    }

    public void setHorizontalAlignment(final String alignment) {
        setPropertyValue(HORIZONTAL_ALIGNMENT, alignment);
    }

    public Optional<String> getVerticalAlignment() {
        return getPropertyValue(VERTICAL_ALIGNMENT);
    }

    public void setVerticalAlignment(final String alignment) {
        setPropertyValue(VERTICAL_ALIGNMENT, alignment);
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
        if (o instanceof ParagraphCaption) {
            final ParagraphCaption that = (ParagraphCaption) o;
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
        return new StringJoiner(", ", ParagraphCaption.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
