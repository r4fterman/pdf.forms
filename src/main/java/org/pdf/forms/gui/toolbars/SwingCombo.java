package org.pdf.forms.gui.toolbars;

import javax.swing.JComboBox;

public class SwingCombo extends JComboBox<String> {

    private int id;

    public SwingCombo(final String[] items) {
        super(items);
    }

    public void setID(final int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
