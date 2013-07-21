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
 * SplitComponent.java
 * ---------------
 */
package org.pdf.forms.widgets.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

import org.pdf.forms.gui.designer.IDesigner;

public class SplitComponent extends JPanel {
    public static final int CAPTION_LEFT = 0;
    public static final int CAPTION_RIGHT = 1;
    public static final int CAPTION_TOP = 2;
    public static final int CAPTION_BOTTOM = 3;
    public static final int CAPTION_NONE = 4;

    public static final int DIVIDER_SIZE = 5;

    private PdfCaption caption;
    protected JComponent value;

    private JComponent component;
    private JPanel valuePanel;
    private JPanel captionPanel;
    private int captionPosition;

    public SplitComponent(String captionText, JComponent value, int captionPosition) {

        setLayout(new BorderLayout());

        caption = new PdfCaption(captionText);

        this.value = value;

        captionPanel = new JPanel();
        captionPanel.setLayout(new BorderLayout());

        captionPanel.setMinimumSize(new Dimension(0, 0));
        captionPanel.setBackground(IDesigner.PAGE_COLOR);
        captionPanel.add(caption, BorderLayout.CENTER);

        valuePanel = new JPanel();
        valuePanel.setLayout(new BorderLayout());

        valuePanel.setMinimumSize(new Dimension(0, 0));
        valuePanel.setBackground(IDesigner.PAGE_COLOR);
        valuePanel.add(value, BorderLayout.CENTER);

        component = new JSplitPane();

        setCaptionPosition(captionPosition);

//		setSplitPaneDividerColor(sp, Color.yellow);
//sp.setBorder(BorderFactory.createEmptyBorder());
//sp.setDividerSize(DIVIDER_SIZE);

//		add(sp, BorderLayout.CENTER);
    }

    public void setBorder(Border border) {
        if (value != null)
            value.setBorder(border);
    }

    public void setBackground(Color color) {
        if (value != null)
            value.setBackground(color);
    }

    public void setCaptionPosition(int position) {
        this.captionPosition = position;

        remove(component);

        if (position == CAPTION_NONE) {
            component = new JPanel();
            component.setLayout(new BorderLayout());
            component.add(valuePanel, BorderLayout.CENTER);
        } else {
            component = new JSplitPane();
            JSplitPane sp = (JSplitPane) component;

            switch (position) {
                case SplitComponent.CAPTION_LEFT:

                    sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

                    sp.setLeftComponent(captionPanel);
                    sp.setRightComponent(valuePanel);

                    sp.setResizeWeight(0);

                    sp.setDividerLocation((int) caption.getPreferredSize().getWidth() + 10);

                    break;

                case SplitComponent.CAPTION_TOP:

                    sp.setOrientation(JSplitPane.VERTICAL_SPLIT);

                    sp.setTopComponent(captionPanel);
                    sp.setBottomComponent(valuePanel);

                    sp.setResizeWeight(0);

                    sp.setDividerLocation((int) caption.getPreferredSize().getHeight());

                    break;

                case SplitComponent.CAPTION_RIGHT:

                    sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

                    sp.setRightComponent(captionPanel);
                    sp.setLeftComponent(valuePanel);

                    sp.setResizeWeight(1);

                    sp.setDividerLocation((int) value.getPreferredSize().getWidth());

                    break;

                case SplitComponent.CAPTION_BOTTOM:

                    sp.setOrientation(JSplitPane.VERTICAL_SPLIT);

                    sp.setTopComponent(valuePanel);
                    sp.setBottomComponent(captionPanel);

                    sp.setResizeWeight(1);

                    break;

                default:
                    break;

            }

            setSplitPaneDividerColor(sp, Color.yellow);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.setDividerSize(DIVIDER_SIZE);

        }

        add(component, BorderLayout.CENTER);
    }

    private void setSplitPaneDividerColor(JSplitPane splitPane, Color newDividerColor) {
        SplitPaneUI splitUI = splitPane.getUI();
        if (splitUI instanceof BasicSplitPaneUI) { // obviously this will not work if the ui doen't extend Basic...
            int divSize = splitPane.getDividerSize();
            BasicSplitPaneDivider div = ((BasicSplitPaneUI) splitUI).getDivider();
            Border divBorder = div.getBorder();
            Border newBorder = null;
            Border colorBorder = null;

            int insetsh = 0;
            int insetsv = 0;

            if (divBorder != null) {
                Insets i = divBorder.getBorderInsets(div);
                insetsh = i.left + i.right;
                insetsv = i.top + i.bottom;
            }

            // this border uses a fillRect
            colorBorder = BorderFactory.createMatteBorder(divSize - insetsv, divSize - insetsh, 0, 0, newDividerColor);

            if (divBorder == null) {
                newBorder = colorBorder;
            } else {
                newBorder = BorderFactory.createCompoundBorder(divBorder, colorBorder);
            }
            div.setBorder(newBorder);
        }
    }

    /**
     * Sets the x and y alignment of the caption, the two integers taken are SwingConstants.??? For example
     * SwingConstants.CENTER
     *
     * @param alignmentX
     * @param alignmentY
     */
    public void setCaptionAlignment(int alignmentX, int alignmentY) {
        caption.setHorizontalAlignment(alignmentX);
        caption.setVerticalAlignment(alignmentY);
    }

    public JComponent getValue() {
        return value;
    }

    public PdfCaption getCaption() {
        return caption;
    }

    public void setDividerLocation(int x) {
        ((JSplitPane) component).setDividerLocation(x);
    }

    public int getDividerLocation() {
        return ((JSplitPane) component).getDividerLocation();
    }

    public int getOrientation() {
        return ((JSplitPane) component).getOrientation();
    }

    public int getCaptionPosition() {
        return captionPosition;
    }
}
