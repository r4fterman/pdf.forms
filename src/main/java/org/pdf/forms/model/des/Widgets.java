package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Widgets {

    private List<Widget> widget;

    public Widgets() {
        this.widget = new ArrayList<>();
    }

    public List<Widget> getWidget() {
        return widget;
    }

    public void setWidget(final List<Widget> widget) {
        this.widget = widget;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Widgets) {
            final Widgets widgets = (Widgets) o;
            return Objects.equals(widget, widgets.widget);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(widget);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Widgets.class.getSimpleName() + "[", "]")
                .add("widget=" + widget)
                .toString();
    }
}
