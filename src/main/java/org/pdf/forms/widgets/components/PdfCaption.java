package org.pdf.forms.widgets.components;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.widgets.IWidget;

public class PdfCaption extends JLabel implements IPdfComponent {

    private static final float FONT_SIZE = 11f;

    public PdfCaption(
            final String text,
            final FontHandler fontHandler) {
        super(text);
        setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));
    }

    private boolean standardUnderline;
    private boolean isStrikethrough;
    private boolean doubleUnderline;
    private boolean wordUnderline;

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        final String text = getText();
        final FontMetrics f = getFontMetrics(getFont());
        final Rectangle2D textBounds = f.getStringBounds(text, graphics);

        int x = getInsets().left;
        int y = getHeight() / 2 + (int) (textBounds.getHeight() / 2);
        final int width = (int) textBounds.getWidth() + x;

        if (standardUnderline) {
            graphics.drawLine(x, y, width, y);

            if (doubleUnderline) {
                graphics.drawLine(x, y + 2, width, y + 2);
            }
        } else if (wordUnderline) {
            underlineWord(graphics, text, f, x, y);
        }

        if (isStrikethrough) {
            graphics.drawLine(x, getHeight() / 2, width, getHeight() / 2);
        }
    }

    private void underlineWord(
            final Graphics graphics,
            final String text,
            final FontMetrics f,
            final int x,
            final int y) {

        int startX = x;
        for (int i = 0; i < text.length(); i++) {
            final char currentChar = text.charAt(i);
            final int charWidth = f.charWidth(currentChar);

            if (currentChar != ' ') {
                graphics.drawLine(startX, y, startX + charWidth, y);

                if (doubleUnderline) {
                    graphics.drawLine(startX, y + 2, startX + charWidth, y + 2);
                }
            }
            startX += charWidth;
        }
    }

    @Override
    public void setUnderlineType(final int type) {
        switch (type) {
            case IWidget.UNDERLINE_SINGLE:
                standardUnderline = true;
                doubleUnderline = false;
                wordUnderline = false;

                break;
            case IWidget.UNDERLINE_DOUBLE:
                standardUnderline = true;
                doubleUnderline = true;
                wordUnderline = false;

                break;
            case IWidget.UNDERLINE_WORD_SINGLE:
                standardUnderline = false;
                doubleUnderline = false;
                wordUnderline = true;

                break;
            case IWidget.UNDERLINE_WORD_DOUBLE:
                standardUnderline = false;
                doubleUnderline = true;
                wordUnderline = true;

                break;
            default:
                break;
        }
    }

    @Override
    public void setStrikethrough(final boolean isStrikethrough) {
        this.isStrikethrough = isStrikethrough;
    }
}
