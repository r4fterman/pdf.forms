/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * CheckBoxIcon.java
 * ---------------
 */
package org.pdf.forms.widgets.components;

import javax.swing.*;
import java.awt.*;

public class CheckBoxIcon implements Icon { //http://forum.java.sun.com/thread.jspa?forumID=57&threadID=641479

    private Image onImage, offImage;

    public void setOnOffImage(Image onImage, Image offImage) {
        this.onImage = onImage;
        this.offImage = offImage;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;

        JCheckBox checkBox = (JCheckBox) c;

        int height = c.getHeight();
        if (checkBox.isSelected()) {
            y = (height / 2) - (onImage.getHeight(null) / 2);
            g2.drawImage(onImage, 0, y, null);
        } else {
            y = (height / 2) - (offImage.getHeight(null) / 2);
            g2.drawImage(offImage, 0, y, null);
        }
    }

    public int getIconWidth() {
        return onImage.getWidth(null);
    }

    public int getIconHeight() {
        return onImage.getHeight(null);
    }
}