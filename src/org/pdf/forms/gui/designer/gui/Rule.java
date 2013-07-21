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
 * Rule.java
 * ---------------
 */
package org.pdf.forms.gui.designer.gui;

import javax.swing.*;
import java.awt.*;

public class Rule extends JComponent {

    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 20;

    public static final double DPI = 72;

    public boolean isMetric;

    private int orientation, increment, units, start, end, tickLength, incrementsPerUnit;
    private int markerLocation;

    public Rule(int inset, int o, boolean m) {

        orientation = o;
        isMetric = m;

        start = inset;

        if (orientation == HORIZONTAL)
            end = Toolkit.getDefaultToolkit().getScreenSize().width;
        else
            end = Toolkit.getDefaultToolkit().getScreenSize().height;

        setIncrementAndUnits();

        setFont(new Font("SansSerif", Font.PLAIN, 10));
    }

    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
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

    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

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
            if (incrementCount % incrementsPerUnit == 0) {
                tickLength = 13;
                text = numberCount + "";
                numberCount++;
            } else {
                if (incrementCount % (incrementsPerUnit / 2) == 0)
                    tickLength = 8;
                else
                    tickLength = 5;

                text = null;
            }

            incrementCount++;

            if (orientation == HORIZONTAL) {
                g2.drawLine(i, SIZE - 1, i, SIZE - tickLength - 1);

                if (text != null)
                    g2.drawString(text, i + 3, 10);

            } else {
                g2.drawLine(SIZE - 1, i, SIZE - tickLength - 1, i);

                if (text != null) {
                    FontMetrics metrics = g2.getFontMetrics();
                    int width = (int) metrics.getStringBounds(text, g2).getWidth() + i + 2;

                    g2.rotate(-Math.PI / 2.0, 9, width);

                    g2.drawString(text, 9, width);

                    g2.rotate(Math.PI / 2.0, 9, width);
                }
            }
        }
    }

    public void updateMarker(int location) {
        markerLocation = location;

        repaint();
    }

//	private void init(Graphics2D g2) {
//		bounds = getBounds();
//						
//		
//		
//		//start = 15;
//		
//		initilized = true;
//	}
}
