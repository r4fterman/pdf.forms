package org.pdf.forms.model.properties;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class File {

    private String name;

    public File() {
        // need default constructor for JAXB
    }

    public File(final String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof File) {
            final File file = (File) o;
            return Objects.equals(name, file.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", File.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
