package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Widget {

    private List<Property> property;
    private Properties properties;
    private JavaScriptContent javascript;

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

    public JavaScriptContent getJavaScript() {
        return javascript;
    }

    public void setJavascript(final JavaScriptContent javascript) {
        this.javascript = javascript;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Widget) {
            final Widget widget = (Widget) o;
            return Objects.equals(property, widget.property)
                    && Objects.equals(properties, widget.properties)
                    && Objects.equals(javascript, widget.javascript);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, properties, javascript);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Widget.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("properties=" + properties)
                .add("javascript=" + javascript)
                .toString();
    }
}
