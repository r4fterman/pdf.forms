package org.pdf.forms.model.properties;

import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Font {

    private String name;
    private String path;

    public Font() {
        // need default constructor for JAXB
    }

    public Font(
            final String name,
            final String path) {
        this.name = name;
        this.path = path;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Font) {
            final Font font = (Font) o;
            return Objects.equals(name, font.name)
                    && Objects.equals(path, font.path);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Font.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("path='" + path + "'")
                .toString();
    }
}
