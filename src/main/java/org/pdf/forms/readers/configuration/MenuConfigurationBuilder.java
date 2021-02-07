package org.pdf.forms.readers.configuration;

import java.util.ArrayList;
import java.util.List;

import org.pdf.forms.model.configuration.Menu;
import org.pdf.forms.model.configuration.MenuConfiguration;

public class MenuConfigurationBuilder {

    private final List<Menu> menus = new ArrayList<>();

    public MenuConfigurationBuilder addMenu(final Menu menu) {
        menus.add(menu);
        return this;
    }

    public MenuConfiguration build() {
        final MenuConfiguration menuConfiguration = new MenuConfiguration();
        menuConfiguration.setMenu(List.copyOf(menus));
        return menuConfiguration;
    }
}
