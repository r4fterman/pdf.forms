package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "caption_properties")
public class CaptionProperties {

    private static final String TEXT = "Text";
    private static final String DIVISOR_LOCATION = "Divisor Location";

    private List<Property> property;

    public CaptionProperties() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<String> getTextValue() {
        return getPropertyValue(TEXT);
    }

    public void setTextValue(final String value) {
        setPropertyValue(TEXT, value);
    }

    public Optional<String> getDividerLocation() {
        return getPropertyValue(DIVISOR_LOCATION);
    }

    public void setDividerLocation(final String location) {
        setPropertyValue(DIVISOR_LOCATION, location);
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
        if (o instanceof CaptionProperties) {
            final CaptionProperties that = (CaptionProperties) o;
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
        return new StringJoiner(", ", CaptionProperties.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
