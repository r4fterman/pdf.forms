package org.pdf.forms.gui.components;

import javax.swing.*;

public class CheckBoxMenuItemWithID extends JCheckBoxMenuItem {

    private final int id;

    public CheckBoxMenuItemWithID(
            final String text,
            final boolean enabled,
            final int id) {
        super(text, enabled);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
