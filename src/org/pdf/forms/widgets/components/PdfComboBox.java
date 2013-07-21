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
 * PdfComboBox.java
 * ---------------
 */
package org.pdf.forms.widgets.components;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;

public class PdfComboBox extends JComboBox implements IPdfComponent {

    public PdfComboBox() {
        setEditable(true);
        setFont(FontHandler.getInstance().getDefaultFont().deriveFont(11f));
    }

//    public PdfComboBox(Object[] values) {
//        super(values);
//        setFont(IWidget.FONT);
//    }
//
//    private boolean standardUnderline;
//    private boolean isStrikethrough;
//    private boolean doubleUnderline;
//    private boolean wordUnderline;
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        String text = (String) getSelectedItem();
//
//        FontMetrics f = getFontMetrics(getFont());
//
//        Rectangle2D textBounds = f.getStringBounds(text, g);
//
//        int x = getInsets().left;
//        int y = getHeight() / 2 + (int) (textBounds.getHeight() / 2);
//        int w = (int) textBounds.getWidth() + x;
//
//        if (standardUnderline) {
//
//            g.drawLine(x, y, w, y);
//
//            if (doubleUnderline)
//                g.drawLine(x, y + 2, w, y + 2);
//
//
//        } else if (wordUnderline) {
//            int startX = x;
//
//            for (int i = 0; i < text.length(); i++) {
//                char currentChar = text.charAt(i);
//
//                int charWidth = f.charWidth(currentChar);
//
//                if (currentChar != ' ') {
//                    g.drawLine(x, y, x + charWidth, y);
//
//                    if (doubleUnderline) {
//                        y += 2;
//                        g.drawLine(x, y, x + charWidth, y);
//                        y -= 2;
//                    }
//                }
//
//                x += charWidth;
//            }
//
//            x = startX;
//        }
//
//        if (isStrikethrough) {
//            y = getHeight() / 2;
//
//            g.drawLine(x, y, w, y);
//        }
//    }

    public void setUnderlineType(int type) {
//        switch (type) {
//            case FontProperties.UNDERLINE_SINGLE:
//                standardUnderline = true;
//                doubleUnderline = false;
//                wordUnderline = false;
//
//                break;
//            case FontProperties.UNDERLINE_DOUBLE:
//                standardUnderline = true;
//                doubleUnderline = true;
//                wordUnderline = false;
//
//                break;
//            case FontProperties.UNDERLINE_WORD_SINGLE:
//                standardUnderline = false;
//                doubleUnderline = false;
//                wordUnderline = true;
//
//                break;
//            case FontProperties.UNDERLINE_WORD_DOUBLE:
//                standardUnderline = false;
//                doubleUnderline = true;
//                wordUnderline = true;
//
//                break;
//        }
    }

    public void setStikethrough(boolean isStrikethrough) {
//        this.isStrikethrough = isStrikethrough;
    }

    public void setHorizontalAlignment(int alignment) {

    }

    public void setVerticalAlignment(int alignment) {
    }

    public String getText() {
        return null;
    }

    public void setText(String text) {
    }
}
