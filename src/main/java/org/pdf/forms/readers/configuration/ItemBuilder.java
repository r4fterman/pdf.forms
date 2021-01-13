package org.pdf.forms.readers.configuration;

import org.pdf.forms.model.configuration.Item;

public class ItemBuilder {

    private String command = "COMMAND";
    private String name = "Name";
    private boolean visible = false;

    public ItemBuilder withCommand(final String command) {
        this.command = command;
        return this;
    }

    public ItemBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder withVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    public Item build() {
        final Item item = new Item();
        item.setCommand(command);
        item.setName(name);
        item.setVisible(visible);
        return item;
    }
}
