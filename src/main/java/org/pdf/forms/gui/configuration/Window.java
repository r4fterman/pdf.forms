package org.pdf.forms.gui.configuration;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Window {

    private String command;
    private String name;
    private boolean visible;

    public Window() {
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
        if (o instanceof Window) {
            final Window window = (Window) o;
            return visible == window.visible && Objects.equals(command, window.command) && Objects.equals(
                    name,
                    window.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, name, visible);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Window.class.getSimpleName() + "[", "]")
                .add("command='" + command + "'")
                .add("name='" + name + "'")
                .add("visible=" + visible)
                .toString();
    }
}
