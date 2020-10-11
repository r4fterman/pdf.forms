package org.pdf.forms.widgets.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JCheckBox;

//http://forum.java.sun.com/thread.jspa?forumID=57&threadID=641479
public class CheckBoxIcon implements Icon {

    private Image onImage;
    private Image offImage;

    public void setOnOffImage(
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
            int y1 = (height / 2) - (onImage.getHeight(null) / 2);
            g2.drawImage(onImage, 0, y1, null);
        } else {
            int y1 = (height / 2) - (offImage.getHeight(null) / 2);
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
