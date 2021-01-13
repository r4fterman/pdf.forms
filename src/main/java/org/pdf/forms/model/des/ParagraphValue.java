package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "paragraph_value")
public class ParagraphValue {

    private List<Property> property;

    public ParagraphValue() {
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
        if (o instanceof ParagraphValue) {
            final ParagraphValue that = (ParagraphValue) o;
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
        return new StringJoiner(", ", ParagraphValue.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
