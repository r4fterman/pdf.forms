package org.pdf.forms.gui.designer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class Rule extends JComponent {

    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int SIZE = 20;

    public static final double DPI = 72;

    private boolean isMetric;

    private final int orientation;
    private int increment;
    private final int start;
    private final int end;
    private int incrementsPerUnit;
    private int markerLocation;

    public Rule(
            final int inset,
            final int o,
            final boolean m) {

        orientation = o;
        isMetric = m;

        start = inset;

        if (orientation == HORIZONTAL) {
            end = Toolkit.getDefaultToolkit().getScreenSize().width;
        } else {
            end = Toolkit.getDefaultToolkit().getScreenSize().height;
        }

        setIncrementAndUnits();

        setFont(new Font("SansSerif", Font.PLAIN, 10));
    }

    public void setIsMetric(final boolean isMetric) {
        this.isMetric = isMetric;
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
        int units;
        if (isMetric) {

            incrementsPerUnit = 10;

            units = (int) (INCH / 2.54); // dots per centimeter
            increment = Math.round((float) units / (float) incrementsPerUnit);
        } else {
            units = INCH;
            increment = units / 2;
        }
    }

    public boolean isMetric() {
        return this.isMetric;
    }

    public int getIncrement() {
        return increment;
    }

    public void setPreferredHeight(final int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(final int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.black);

        if (orientation == HORIZONTAL) {
            g2.drawLine(markerLocation, 0, markerLocation, getHeight());
        } else {
            g2.drawLine(0, markerLocation, getWidth(), markerLocation);
        }

        int incrementCount = 0;
        int numberCount = 0;
        String text;

        for (int i = start; i < end; i += increment) {
            int tickLength;
            if (incrementCount % incrementsPerUnit == 0) {
                tickLength = 13;
                text = numberCount + "";
                numberCount++;
            } else {
                if (incrementCount % (incrementsPerUnit / 2) == 0) {
                    tickLength = 8;
                } else {
                    tickLength = 5;
                }

                text = null;
            }

            incrementCount++;

            if (orientation == HORIZONTAL) {
                g2.drawLine(i, SIZE - 1, i, SIZE - tickLength - 1);

                if (text != null) {
                    g2.drawString(text, i + 3, 10);
                }

            } else {
                g2.drawLine(SIZE - 1, i, SIZE - tickLength - 1, i);

                if (text != null) {
                    final FontMetrics metrics = g2.getFontMetrics();
                    final int width = (int) metrics.getStringBounds(text, g2).getWidth() + i + 2;

                    g2.rotate(-Math.PI / 2.0, 9, width);

                    g2.drawString(text, 9, width);

                    g2.rotate(Math.PI / 2.0, 9, width);
                }
            }
        }
    }

    public void updateMarker(final int location) {
        markerLocation = location;

        repaint();
    }

}
