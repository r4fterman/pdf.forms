package org.pdf.forms.gui.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pdf.forms.utils.configuration.WindowBuilder;
import org.pdf.forms.utils.configuration.WindowConfigurationBuilder;

@XmlRootElement(name = "window_configuration")
@XmlType(propOrder = "window")
public class WindowConfiguration {

    private static final String SCRIPT_EDITOR = "SCRIPT_EDITOR";
    private static final String HIERARCHY = "HIERARCHY";
    private static final String LIBRARY = "LIBRARY";
    private static final String PROPERTIES = "PROPERTIES";

    public static final WindowConfiguration DEFAULT = new WindowConfigurationBuilder()
            .addWindow(new WindowBuilder().withCommand(SCRIPT_EDITOR).withName("Script Editor").withVisible(true)
                    .build())
            .addWindow(new WindowBuilder().withCommand(HIERARCHY).withName("Hierarchy").withVisible(true).build())
            .addWindow(new WindowBuilder().withCommand(LIBRARY).withName("Library").withVisible(true).build())
            .addWindow(new WindowBuilder().withCommand(PROPERTIES).withName("Properties").withVisible(true).build())
            .build();

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

    public boolean isScriptEditorVisible() {
        return isWindowCommandVisible(SCRIPT_EDITOR);
    }

    public boolean isHierarchyVisible() {
        return isWindowCommandVisible(HIERARCHY);
    }

    public boolean isLibraryVisible() {
        return isWindowCommandVisible(LIBRARY);
    }

    public boolean isPropertiesVisible() {
        return isWindowCommandVisible(PROPERTIES);
    }

    public boolean isWindowCommandVisible(final String command) {
        return window.stream()
                .filter(Window::isVisible)
                .anyMatch(w -> w.getCommand().equals(command));
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
