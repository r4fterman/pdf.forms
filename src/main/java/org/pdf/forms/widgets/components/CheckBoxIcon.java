/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
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
