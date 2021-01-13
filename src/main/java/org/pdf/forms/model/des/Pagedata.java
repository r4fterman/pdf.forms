package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Pagedata {

    private List<Property> property;

    public Pagedata() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Pagedata) {
            final Pagedata pagedata = (Pagedata) o;
            return Objects.equals(property, pagedata.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Pagedata.class.getSimpleName() + "[", "]")
                .add("properties=" + property)
                .toString();
    }
}
