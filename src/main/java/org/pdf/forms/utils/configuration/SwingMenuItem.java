package org.pdf.forms.utils.configuration;

import javax.swing.JMenuItem;

public class SwingMenuItem extends JMenuItem {

    private int id;

    public SwingMenuItem(final String text) {
        super(text);
    }

    public void setID(final int command) {
        this.id = command;
    }

    public int getID() {
        return id;
    }
}
