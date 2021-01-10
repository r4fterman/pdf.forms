package org.pdf.forms.gui.components;

import javax.swing.*;

public class ButtonWithID extends JButton {

    private final int id;

    public ButtonWithID(
            final Icon icon,
            final String tooltipText,
            final int id) {
        super(icon);
        this.id = id;

        setToolTipText(tooltipText);
    }

    public ButtonWithID(
            final String text,
            final Icon icon,
            final String tooltipText,
            final int id) {
        super(text, icon);
        this.id = id;

        setToolTipText(tooltipText);
    }

    public int getId() {
        return id;
    }
}
