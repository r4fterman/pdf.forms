package org.pdf.forms.utils.configuration;

import java.util.ArrayList;
import java.util.List;

import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;

public class MenuBuilder {

    private final List<Item> items = new ArrayList<>();

    public MenuBuilder addItem(final Item item) {
        items.add(item);
        return this;
    }

    public Menu build() {
        final Menu menu = new Menu();
        menu.setItem(List.copyOf(items));
        return menu;
    }
}
