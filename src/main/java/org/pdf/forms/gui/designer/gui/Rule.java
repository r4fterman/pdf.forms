package org.pdf.forms.gui.designer.gui;

import java.awt.*;
import java.util.Optional;

import javax.swing.*;

public class Rule extends JComponent {

    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int SIZE = 20;

    public static final double DPI = 72;

    private final boolean isMetric;

    private final int orientation;
    private int increment;
    private final int start;
    private final int end;
    private int incrementsPerUnit;
    private int markerLocation;

    public Rule(
            final int inset,
            final int orientation,
            final boolean isMetric) {

        this.orientation = orientation;
        this.isMetric = isMetric;

        start = inset;

        if (this.orientation == HORIZONTAL) {
            end = Toolkit.getDefaultToolkit().getScreenSize().width;
        } else {
            end = Toolkit.getDefaultToolkit().getScreenSize().height;
        }

        setIncrementAndUnits();

        setFont(new Font("SansSerif", Font.PLAIN, 10));
    }

    private void setIncrementAndUnits() {
        final int units;
        if (isMetric) {
            this.incrementsPerUnit = 10;

            // dots per centimeter
            units = (int) (INCH / 2.54);
            this.increment = Math.round((float) units / (float) incrementsPerUnit);
        } else {
            this.incrementsPerUnit = 2;
            units = INCH;
            this.increment = units / incrementsPerUnit;
        }
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
            paintComponentHorizontal(g2);
        } else {
            g2.drawLine(0, markerLocation, getWidth(), markerLocation);
            paintComponentVertical(g2);
        }
    }

    private void paintComponentHorizontal(final Graphics2D g2) {
        int incrementCount = 0;
        int numberCount = 0;

        for (int i = start; i < end; i += increment) {
            g2.drawLine(i, SIZE - 1, i, SIZE - getTickLength(incrementCount) - 1);

            final Optional<String> text = getComponentText(incrementCount, numberCount);
            if (text.isPresent()) {
                g2.drawString(text.get(), i + 3, 10);
                numberCount++;
            }

            incrementCount++;
        }
    }

    private void paintComponentVertical(final Graphics2D g2) {
        int incrementCount = 0;
        int numberCount = 0;
        for (int i = start; i < end; i += increment) {
            g2.drawLine(SIZE - 1, i, SIZE - getTickLength(incrementCount) - 1, i);

            final Optional<String> text = getComponentText(incrementCount, numberCount);
            if (text.isPresent()) {
                final FontMetrics metrics = g2.getFontMetrics();
                final int y = (int) metrics.getStringBounds(text.get(), g2).getWidth() + i + 2;

                g2.rotate(-Math.PI / 2.0, 9, y);
                g2.drawString(text.get(), 9, y);
                g2.rotate(Math.PI / 2.0, 9, y);

                numberCount++;
            }

            incrementCount++;
        }
    }

    private Optional<String> getComponentText(
            final int incrementCount,
            final int numberCount) {
        if (incrementCount % incrementsPerUnit == 0) {
            final String text = String.valueOf(numberCount);
            return Optional.of(text);
        }
        return Optional.empty();
    }

    private int getTickLength(final int incrementCount) {
        if (incrementCount % incrementsPerUnit == 0) {
            return 13;
        }
        if (incrementCount % (incrementsPerUnit / 2) == 0) {
            return 8;
        }
        return 5;
    }

    public void updateMarker(final int location) {
        markerLocation = location;

        repaint();
    }

}
