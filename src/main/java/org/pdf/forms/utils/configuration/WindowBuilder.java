package org.pdf.forms.utils.configuration;

import org.pdf.forms.gui.configuration.Window;

public class WindowBuilder {

    private String command = "COMMAND";
    private String name = "Command";
    private boolean visible = false;

    public WindowBuilder withCommand(final String command) {
        this.command = command;
        return this;
    }

    public WindowBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public WindowBuilder withVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    public Window build() {
        final Window window = new Window();
        window.setCommand(command);
        window.setName(name);
        window.setVisible(visible);
        return window;
    }
}
