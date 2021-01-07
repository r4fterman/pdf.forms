package org.pdf.forms.gui.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "window_configuration")
@XmlType(propOrder = "window")
public class WindowConfiguration {

    private List<Window> window;

    public WindowConfiguration() {
        this.window = new ArrayList<>();
    }

    public List<Window> getWindow() {
        return window;
    }

    public void setWindow(final List<Window> window) {
        this.window = window;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof WindowConfiguration) {
            final WindowConfiguration that = (WindowConfiguration) o;
            return Objects.equals(window, that.window);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(window);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WindowConfiguration.class.getSimpleName() + "[", "]")
                .add("windows=" + window)
                .toString();
    }
}
