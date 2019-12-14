package org.pdf.forms.widgets.components;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.widgets.IWidget;

public class PdfButton extends JButton implements IPdfComponent {

    private static final float FONT_SIZE = 11f;

    public PdfButton(
            final String string,
            final FontHandler fontHandler) {
        super(string);
        setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));

        /* needed so the background color of the button can be set */
        setContentAreaFilled(false);
        setOpaque(true);
    }

    private boolean standardUnderline;
    private boolean isStrikethrough;
    private boolean doubleUnderline;
    private boolean wordUnderline;

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        final String text = getText();

        final FontMetrics fontMetrics = getFontMetrics(getFont());

        final Rectangle2D textBounds = fontMetrics.getStringBounds(text, graphics);

        int x = getInsets().left;
        int y = getHeight() / 2 + (int) (textBounds.getHeight() / 2);
        final int width = (int) textBounds.getWidth() + x;

        if (standardUnderline) {
            graphics.drawLine(x, y, width, y);

            if (doubleUnderline) {
                graphics.drawLine(x, y + 2, width, y + 2);
            }
        } else if (wordUnderline) {
            final int startX = x;

            for (int i = 0; i < text.length(); i++) {
                final char currentChar = text.charAt(i);

                final int charWidth = fontMetrics.charWidth(currentChar);

                if (currentChar != ' ') {
                    graphics.drawLine(x, y, x + charWidth, y);

                    if (doubleUnderline) {
                        y += 2;
                        graphics.drawLine(x, y, x + charWidth, y);
                        y -= 2;
                    }
                }

                x += charWidth;
            }

            x = startX;
        }

        if (isStrikethrough) {
            y = getHeight() / 2;

            graphics.drawLine(x, y, width, y);
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
