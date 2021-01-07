package org.pdf.forms.utils.configuration;

import java.util.ArrayList;
import java.util.List;

import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;

public class MenuBuilder {

    private final List<Item> items = new ArrayList<>();
    private String name = "MENU";
    private boolean visible = false;

    public MenuBuilder addItem(final Item item) {
        items.add(item);
        return this;
    }

    public MenuBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder withVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    public Menu build() {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setVisible(visible);
        menu.setItem(List.copyOf(items));
        return menu;
    }
}
