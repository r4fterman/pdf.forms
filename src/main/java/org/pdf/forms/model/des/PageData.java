package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class PageData {

    private List<Property> property;

    public PageData() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Optional<Integer> getWidth() {
        return getPropertyValue("width")
                .map(Integer::valueOf);
    }

    public Optional<Integer> getHeight() {
        return getPropertyValue("height")
                .map(Integer::valueOf);
    }

    public Optional<String> getPdfFileLocation() {
        return getPropertyValue("pdffilelocation");
    }

    public Optional<Integer> getPdfPageNumber() {
        return getPropertyValue("pdfpagenumber")
                .map(Integer::valueOf);
    }

    private Optional<String> getPropertyValue(final String propertyName) {
        return getProperty(propertyName)
                .map(Property::getValue);
    }

    private Optional<Property> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof PageData) {
            final PageData pagedata = (PageData) o;
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
        return new StringJoiner(", ", PageData.class.getSimpleName() + "[", "]")
                .add("properties=" + property)
                .toString();
    }
}
