package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "caption_properties")
public class CaptionProperties {

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
        return getPropertyValue("Text");
    }

    public void setTextValue(final String value) {
        getProperty("Text")
                .ifPresentOrElse(
                        p -> p.setValue(value),
                        () -> property.add(new Property("Text", value)));
    }

    private Optional<String> getPropertyValue(final String propertyName) {
        return getProperty(propertyName)
                .map(Property::getValue);
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
