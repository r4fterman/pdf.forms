package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class CheckBoxGroups implements ButtonGroups {

    private List<Property> property;

    public CheckBoxGroups() {
        this.property = new ArrayList<>();
    }

    @Override
    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CheckBoxGroups) {
            final CheckBoxGroups that = (CheckBoxGroups) o;
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
        return new StringJoiner(", ", CheckBoxGroups.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
