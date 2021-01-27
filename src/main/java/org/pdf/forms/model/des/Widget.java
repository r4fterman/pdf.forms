package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Widget {

    private static final String NAME = "name";
    private static final String TYPE = "type";

    private List<Property> property;
    private Properties properties;
    @XmlElement(name = "javascript")
    private JavaScriptContent javaScript;

    public Widget() {
        this.property = new ArrayList<>();
        this.properties = new Properties();
        this.javaScript = new JavaScriptContent();
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

    public JavaScriptContent getJavaScript() {
        return javaScript;
    }

    public Optional<String> getType() {
        return getPropertyValue(TYPE);
    }

    public void setType(final String type) {
        setPropertyValue(TYPE, type);
    }

    public Optional<String> getName() {
        return getPropertyValue(NAME);
    }

    public void setName(final String name) {
        setPropertyValue(NAME, name);
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

    public void setJavaScript(final JavaScriptContent javaScript) {
        this.javaScript = javaScript;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Widget) {
            final Widget widget = (Widget) o;
            return Objects.equals(property, widget.property)
                    && Objects.equals(properties, widget.properties)
                    && Objects.equals(javaScript, widget.javaScript);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, properties, javaScript);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Widget.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("properties=" + properties)
                .add("javascript=" + javaScript)
                .toString();
    }
}
