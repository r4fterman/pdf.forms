package org.pdf.forms.utils.configuration;

import java.util.ArrayList;
import java.util.List;

import org.pdf.forms.gui.configuration.Window;
import org.pdf.forms.gui.configuration.WindowConfiguration;

public class WindowConfigurationBuilder {

    private final List<Window> windows = new ArrayList<>();

    public WindowConfigurationBuilder addWindow(final Window window) {
        windows.add(window);
        return this;
    }

    public WindowConfiguration build() {
        final WindowConfiguration windowConfiguration = new WindowConfiguration();
        windowConfiguration.setWindow(List.copyOf(windows));
        return windowConfiguration;
    }
}
