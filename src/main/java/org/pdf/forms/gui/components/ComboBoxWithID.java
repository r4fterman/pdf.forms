package org.pdf.forms.gui.components;

import javax.swing.*;

public class ComboBoxWithID<T> extends JComboBox<T> {

    private final int id;

    public ComboBoxWithID(
            final T[] model,
            final boolean editable,
            final int id) {
        super(model);
        this.id = id;

        setEditable(editable);
    }

    public int getId() {
        return id;
    }
}
