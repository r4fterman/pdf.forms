package org.pdf.forms.model.configuration;

import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Item {

    private String command;
    private String name;
    private boolean visible;

    public Item() {
        this.command = "";
        this.name = "";
        this.visible = false;
    }

    @XmlAttribute
    public String getCommand() {
        return command;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlAttribute
    public boolean isVisible() {
        return visible;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Item) {
            final Item item = (Item) o;
            return visible == item.visible
                    && Objects.equals(command, item.command)
                    && Objects.equals(name, item.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, name, visible);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                .add("command='" + command + "'")
                .add("name='" + name + "'")
                .add("visible=" + visible)
                .toString();
    }
}
