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
* ToolBarToggleButton.java
* ---------------
*/
package org.pdf.forms.gui.toolbars;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class ToolBarToggleButton extends JToggleButton {

    ToolBarToggleButton() {
        super();
    }

    void init(
            final String path,
            final String toolTip) {
        /* bookmarks icon */
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
        final BufferedImage image = new BufferedImage(icon.getIconWidth() + 2, icon.getIconHeight() + 2, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) image.getGraphics();
        g.drawImage(icon.getImage(), 1, 1, null);
        g.dispose();
        final ImageIcon iconPressed = new ImageIcon(image);
        button.setPressedIcon(iconPressed);
    }

    public void setIcon(final ImageIcon icon) {
        super.setIcon(icon);
    }
}
