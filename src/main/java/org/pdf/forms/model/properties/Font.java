package org.pdf.forms.model.properties;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlType
public class Font {

    private final Logger logger = LoggerFactory.getLogger(Font.class);

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

    public Optional<java.awt.Font> convertToJavaFont() {
        try {
            final java.awt.Font javaFont = java.awt.Font.createFont(
                    java.awt.Font.TRUETYPE_FONT,
                    new File(getPath())
            );
            return Optional.of(javaFont);
        } catch (FontFormatException | IOException e) {
            logger.error("Unable to create font. {}", getPath(), e);
        }
        return Optional.empty();
    }

}
