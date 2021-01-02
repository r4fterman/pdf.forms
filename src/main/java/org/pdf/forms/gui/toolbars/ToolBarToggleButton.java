package org.pdf.forms.gui.toolbars;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.*;

public class ToolBarToggleButton extends JToggleButton {

    ToolBarToggleButton() {
        super();
    }

    void init(
            final String path,
            final String toolTip) {
        setToolTipText(toolTip);

        setBorderPainted(false);

        final URL url = getClass().getResource(path);
        if (url != null) {
            final ImageIcon fontIcon = new ImageIcon(url);
            setIcon(fontIcon);
            createPressedLook(this, fontIcon);
        }
    }

    /**
     * Create a pressed look of the <b>icon</b> and added it to the pressed Icon of <b>button</b>.
     */
    private void createPressedLook(
            final AbstractButton button,
            final ImageIcon icon) {
        final BufferedImage image = new BufferedImage(
                icon.getIconWidth() + 2,
                icon.getIconHeight() + 2,
                BufferedImage.TYPE_INT_ARGB);

        final Graphics2D g = (Graphics2D) image.getGraphics();
        g.drawImage(icon.getImage(), 1, 1, null);
        g.dispose();
        button.setPressedIcon(new ImageIcon(image));
    }

}
