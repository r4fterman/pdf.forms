package org.pdf.forms.gui.components;

import javax.swing.*;

public class MenuItemWithID extends JMenuItem {

    private final int id;

    public MenuItemWithID(
            final String text,
            final int id) {
        super(text);
        this.id = id;
    }

    public MenuItemWithID(
            final String text,
            final Icon icon,
            final int id) {
        super(text, icon);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
