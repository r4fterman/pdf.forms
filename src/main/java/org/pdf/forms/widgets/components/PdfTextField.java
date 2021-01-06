package org.pdf.forms.widgets.components;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Element;
import javax.swing.text.FieldView;
import javax.swing.text.View;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.widgets.IWidget;

public class PdfTextField extends JTextField implements IPdfComponent {

    private static final float FONT_SIZE = 11f;

    private boolean standardUnderline;
    private boolean isStrikethrough;
    private boolean doubleUnderline;
    private boolean wordUnderline;

    public PdfTextField(
            final String captionText,
            final FontHandler fontHandler) {
        super(captionText);

        setOpaque(true);
        setText(captionText);
        setFont(fontHandler.getDefaultFont().deriveFont(FONT_SIZE));
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        final String text = getText();
        final FontMetrics f = getFontMetrics(getFont());
        final Rectangle2D textBounds = f.getStringBounds(text, graphics);

        final int x = getInsets().left;
        final int y = getHeight() / 2 + (int) (textBounds.getHeight() / 2);
        final int w = (int) textBounds.getWidth() + x;

        if (standardUnderline) {
            graphics.drawLine(x, y, w, y);

            if (doubleUnderline) {
                graphics.drawLine(x, y + 2, w, y + 2);
            }
        } else if (wordUnderline) {
            underlineWord(graphics, text, f, x, y);
        }

        if (isStrikethrough) {
            graphics.drawLine(x, getHeight() / 2, w, getHeight() / 2);
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

    @Override
    public void setVerticalAlignment(final int alignment) {
        if (alignment == SwingConstants.CENTER) {
            setUI(new BasicTextFieldUI());
            return;
        }

        setUI(new BasicTextFieldUI() {
            @Override
            public View create(final Element element) {
                return new FieldView(element) {
                    @Override
                    protected Shape adjustAllocation(final Shape shape) {
                        if (shape == null) {
                            return null;
                        }

                        final Rectangle bounds = (Rectangle) super.adjustAllocation(shape);

                        // adjust the vertical alignment
                        final int height = shape.getBounds().height;
                        final int vSpan = (int) getPreferredSpan(Y_AXIS);
                        if (height != vSpan) {
                            final int slop = bounds.height - vSpan;
                            if (alignment == SwingConstants.TOP) {
                                bounds.y = shape.getBounds().y;
                            } else if (alignment == SwingConstants.BOTTOM) {
                                bounds.y = height - vSpan;
                            }
                            bounds.height -= slop;
                        }
                        return bounds;
                    }
                };
            }
        });
    }
}
