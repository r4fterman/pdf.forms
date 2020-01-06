package org.pdf.forms.gui.toolbars;

import javax.swing.JButton;

public class SwingButton extends JButton {

    private int id;

    public SwingButton() {
    }

    public void init(
            final String url,
            final int id,
            final String toolTip) {
        setText(url);
        setToolTipText(toolTip);
        this.id= id;
    }

    public int getID() {
        return id;
    }
}
