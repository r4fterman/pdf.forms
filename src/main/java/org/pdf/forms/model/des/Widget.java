package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Widget {

    private List<Property> property;
    private Properties properties;

    public Widget() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(final Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Widget) {
            final Widget widget = (Widget) o;
            return Objects.equals(property, widget.property)
                    && Objects.equals(properties, widget.properties);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, properties);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Widget.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("properties=" + properties)
                .toString();
    }
}
