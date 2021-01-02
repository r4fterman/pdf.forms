package org.pdf.forms.widgets.components;

import java.awt.*;

import javax.swing.*;

public class CheckBoxIcon implements Icon {

    private final Image onImage;
    private final Image offImage;

    public CheckBoxIcon(
            final Image onImage,
            final Image offImage) {
        this.onImage = onImage;
        this.offImage = offImage;
    }

    @Override
    public void paintIcon(
            final Component c,
            final Graphics g,
            final int x,
            final int y) {
        final Graphics2D g2 = (Graphics2D) g;

        final JCheckBox checkBox = (JCheckBox) c;

        final int height = c.getHeight();
        if (checkBox.isSelected()) {
            final int y1 = (height / 2) - (onImage.getHeight(null) / 2);
            g2.drawImage(onImage, 0, y1, null);
        } else {
            final int y1 = (height / 2) - (offImage.getHeight(null) / 2);
            g2.drawImage(offImage, 0, y1, null);
        }
    }

    @Override
    public int getIconWidth() {
        return onImage.getWidth(null);
    }

    @Override
    public int getIconHeight() {
        return onImage.getHeight(null);
    }
}
