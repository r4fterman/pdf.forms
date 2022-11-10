package org.pdf.forms.model.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.pdf.forms.model.des.Property;
import org.pdf.forms.model.des.Widget;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "custom_component")
public class CustomComponent {

    private List<Property> property;
    private List<Widget> widget;

    public CustomComponent() {
        this.property = new ArrayList<>();
        this.widget = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public List<Widget> getWidget() {
        return widget;
    }

    public void setWidget(final List<Widget> widget) {
        this.widget = widget;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CustomComponent) {
            final CustomComponent that = (CustomComponent) o;
            return Objects.equals(property, that.property) && Objects.equals(widget, that.widget);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, widget);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomComponent.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("widget=" + widget)
                .toString();
    }
}
