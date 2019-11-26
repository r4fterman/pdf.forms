/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * Designer.java
 * ---------------
 */
package org.pdf.forms.gui.designer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JToolTip;

import org.jpedal.PdfDecoder;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.designer.listeners.DesignerKeyListener;
import org.pdf.forms.gui.designer.listeners.DesignerMouseListener;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Designer extends PdfDecoder implements IDesigner {

    private final Logger logger = LoggerFactory.getLogger(Designer.class);

    private final WidgetSelection widgetSelection;
    private final IMainFrame mainFrame;
    private final DesignerSelectionBox selectionBox;
    private final Rule horizontalRuler;
    private final Rule verticalRuler;
    private final Point mouseLocation = new Point();
    private final CaptionChanger captionChanger = new CaptionChanger();

    private List<IWidget> widgets;
    private Set<IWidget> selectedWidgets = new HashSet<>();
    private int resizeType = DesignerMouseMotionListener.DEFAULT_CURSOR;
    private int widgetToAdd = IWidget.NONE;
    private Point dragBoxMouseLocation = null;

    private boolean isResizing;
    private int pageWidth;
    private int pageHeight;
    private final int inset;
    private boolean currentlyDragging;
    private boolean isResizingSplitComponent;
    private int drawingState;
    private Page currentPage;
    private String currentlyOpenPDF;

    public Designer(
            final int inset,
            final Rule horizontalRuler,
            final Rule verticalRuler,
            final IMainFrame mainFrame,
            final String version) {
        super();

        setBackground(BACKGROUND_COLOR);

        selectionBox = new DesignerSelectionBox(this);
        widgetSelection = new WidgetSelection(this, version);

        this.horizontalRuler = horizontalRuler;
        this.verticalRuler = verticalRuler;
        this.inset = inset;
        this.mainFrame = mainFrame;

        addMouseListener(new DesignerMouseListener(this));
        addMouseMotionListener(new DesignerMouseMotionListener(this));
        addKeyListener(new DesignerKeyListener(this));

        setLayout(null);
    }

    private void drawPage(final Graphics2D g2) {
        if (drawingState != IDesigner.CLOSED) {

            final Rectangle r = new Rectangle(inset, inset, currentPage.getWidth(), currentPage.getHeight());

            if (drawingState != IDesigner.PDFPAGE) {
                g2.setPaint(IDesigner.PAGE_COLOR);
                g2.fill(r);
            }

            g2.setColor(Color.black);
            g2.draw(r);
        }
    }

    private void drawWidget(
            final IWidget widget,
            final Graphics2D g2) {

        if (widget.getType() == IWidget.GROUP) {
            widgetSelection.drawMulitipleSelectionBox(g2, new HashSet<>(widget.getWidgetsInGroup()), false);

            for (final Object o : widget.getWidgetsInGroup()) {
                final IWidget w = (IWidget) o;

                drawWidget(w, g2);
            }

        } else {

            final Document document = widget.getProperties();
            final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);

            final Element fieldProperties =
                    (Element) objectProperties.getElementsByTagName("field").item(0);

            final String visibility = XMLUtils.getAttributeFromChildElement(fieldProperties, "Presence").orElse("");

            if (visibility.equals("Visible")) {
                g2.translate(widget.getX(), widget.getY());

                final JComponent c = widget.getWidget();
                c.paint(g2);

                widgetSelection.drawSingleSectionBox(g2, widget, selectedWidgets.size() == 1 && selectedWidgets.contains(widget));

                g2.translate(-widget.getX(), -widget.getY());
            }
        }
    }

    @Override
    public void paintComponent(final Graphics g) {

        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        final AffineTransform transform = g2.getTransform();
        //  transform.scale(scale, scale); @scale
        g2.setTransform(transform);

        /*
         * draw the basic blank page
         */
        drawPage(g2);

        /*
         * draw each widget in turn onto the page
         */
        for (final IWidget widget : widgets) {
            drawWidget(widget, g2);
        }

        final IWidget selectedWidget;
        if (selectedWidgets.isEmpty()) {
            selectedWidget = null;
        } else {
            selectedWidget = selectedWidgets.iterator().next();
        }

        /*
         * if we're dragging out a component from the library, dragBoxMouseLocation will
         * not be null, so we need to draw the special box, and display the tooltip.
         */
        if (dragBoxMouseLocation != null) {
            final Rectangle boxSize = selectedWidget.getBounds();
            drawDragOutBoxAndDisplayTooltip(g2, boxSize);
        }

        if (selectedWidget != null) {
            final Rectangle boxSize = WidgetSelection.getMultipleWidgetBounds(selectedWidgets);

            /*
             * if we are dragging the widget, or resizing it, we need to display the
             * required tooltip which displays the components size/coordinates details
             */
            if (currentlyDragging) {
                displayTooltip(g2, boxSize, false);
            } else if (isResizing) {
                displayTooltip(g2, boxSize, true);
            }
        }

        if (selectedWidgets.size() > 1 || (selectedWidget != null && selectedWidget.getType() == IWidget.GROUP)) {
            widgetSelection.drawMulitipleSelectionBox(g2, selectedWidgets, true);
        }

        selectionBox.paintBox(g2);
    }

    private void drawDragOutBoxAndDisplayTooltip(
            final Graphics2D g2,
            final Rectangle boxSize) {
        final int dragBoxX = dragBoxMouseLocation.x - (boxSize.width / 2);
        final int dragBoxY = dragBoxMouseLocation.y - (boxSize.height / 2);

        final float[] dashPattern = {
                1,
                1 };
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1,
                dashPattern, 0));
        g2.setColor(Color.black);

        g2.drawRect(dragBoxX, dragBoxY, boxSize.width, boxSize.height);

        g2.setStroke(new BasicStroke());

        boxSize.setLocation(dragBoxX, dragBoxY);

        displayTooltip(g2, boxSize, false);
    }

    private void displayTooltip(
            final Graphics2D g2,
            final Rectangle boxSize,
            final boolean isResizing) {
        final JToolTip tooltip = new JToolTip();

        final String text;

        final int units = (int) (Rule.INCH / 2.54);

        if (isResizing) {
            double width = (double) boxSize.width / (double) units;
            double height = (double) boxSize.height / (double) units;

            width = round(width);
            height = round(height);

            text = width + "cm x " + height + "cm";
        } else {
            double xLocation = (double) (boxSize.x - inset) / (double) units;
            double yLocation = (double) (boxSize.y - inset) / (double) units;

            xLocation = round(xLocation);
            yLocation = round(yLocation);

            text = xLocation + "cm , " + yLocation + "cm";
        }

        tooltip.setTipText(text);
        tooltip.setSize(tooltip.getPreferredSize());

        final int translateX = mouseLocation.x - (tooltip.getSize().width / 2);
        final int translateY = mouseLocation.y + 20;

        g2.translate(translateX, translateY);

        tooltip.paint(g2);

        g2.translate(-translateX, -translateY);
    }

    private double round(final double number) {

        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }

    @Override
    public void close() {
        removeWidgetsFromHierarchy(widgets);

        widgets.clear();
        selectedWidgets.clear();

        drawingState = IDesigner.CLOSED;

        currentlyOpenPDF = null;

        pageHeight = 0;
        pageWidth = 0;

        closePdfFile();

        updateUI();
    }

    @Override
    public Dimension getPreferredSize() {
        if (drawingState == IDesigner.PDFPAGE) {
            return getMaximumSize();
        } else {
            return new Dimension(inset * 2 + pageWidth, inset * 2 + pageHeight);
        }
    }

    @Override
    public void displayPage(final Page page) {
        currentPage = page;

        widgets = page.getWidgets();

        //  widgetsInAddedOrder = page.getWidgetsInAddedOrder();

        selectedWidgets.clear();

        final String pdfFile = page.getPdfFileLocation();
        if (pdfFile == null) {
            drawingState = IDesigner.SIMPLEPAGE;

            pageWidth = page.getWidth();
            pageHeight = page.getHeight();

            closePdfFile();
        } else {
            drawingState = IDesigner.PDFPAGE;

            try {
                closePdfFile();

                setDisplayForms(false);

                if (pdfFile.startsWith("http:") || pdfFile.startsWith("file:")) {
                    openPdfFileFromURL(pdfFile);
                } else {
                    openPdfFile(pdfFile);
                }

                currentlyOpenPDF = pdfFile;

                setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
                setInset(inset, inset);

                final int pdfPageNumber = page.getPdfPageNumber();
                setPageParameters(1, pdfPageNumber);
                decodePage(pdfPageNumber);

                pageWidth = getPdfPageData().getCropBoxWidth(pdfPageNumber);
                pageHeight = getPdfPageData().getCropBoxHeight(pdfPageNumber);

                //                if (!page.widgetsAddedToDesigner()) {
                //                    List widgestList = WidgetParser.parseWidgets(getCurrentFormRenderer(), pageHeight);
                //
                //                    for (Iterator it = widgestList.iterator(); it.hasNext();) {
                //                        IWidget widget = (IWidget) it.next();
                //      if (widget != null)
                //       addWidget(widget);
                //                    }
                //
                //                    page.setWidgetsAddedToDesigner(true);
                //                }
            } catch (final Exception e) {
                logger.error("Error displaying page", e);
            }
        }

        updateUI();
    }

    //    private void setDisplayForms(boolean displayForms) {
    //  this.displayForms = displayForms;
    // }

    @Override
    public IWidget getWidgetAt(
            final int x,
            final int y) {

        for (int i = widgets.size() - 1; i > 0; i--) {
            final IWidget widget = widgets.get(i);

            if (widget.getBounds().contains(x, y)) {
                return widget;
            }
        }

        return null;
    }

    @Override
    public void addWidget(final IWidget widget) {
        mainFrame.addWidgetToHierarchy(widget);
        mainFrame.addWidgetToPage(widget);

        final Set<IWidget> set = new HashSet<>();
        set.add(widget);
        mainFrame.setPropertiesCompound(set);
        mainFrame.setPropertiesToolBar(set);
    }

    @Override
    public void addWidget(
            final int index,
            final IWidget w) {
        widgets.add(index, w);
        //  widgetsInAddedOrder.add(w);
    }

    @Override
    public void removeSelectedWidgets() {
        widgets.removeAll(selectedWidgets);
        //  this.widgetsInAddedOrder.removeAll(selectedWidgets);

        removeWidgetsFromHierarchy(selectedWidgets);
    }

    private void removeWidgetsFromHierarchy(final Collection<IWidget> widgets) {
        for (final IWidget widget : widgets) {
            mainFrame.removeWidgetFromHierarchy(widget);
        }
    }

    @Override
    public void removeWidget(
            final IWidget widgetToRemove,
            final List<IWidget> widgets) {

        for (final Iterator<IWidget> it = widgets.iterator(); it.hasNext(); ) {
            final IWidget w = it.next();

            if (w.getType() == IWidget.GROUP) {
                removeWidget(widgetToRemove, w.getWidgetsInGroup());

                if (w == widgetToRemove) {
                    it.remove();
                    break;
                }

            } else {
                if (w == widgetToRemove) {
                    it.remove();
                    break;
                }
            }
        }

        mainFrame.updateHierarchy();
        //widgetsInAddedOrder.remove(widgetToRemove);

        repaint();
    }

    @Override
    public Set<IWidget> getSelectedWidgets() {
        return selectedWidgets;
    }

    @Override
    public void setWidgetToAdd(final int widgetToAdd) {
        this.widgetToAdd = widgetToAdd;
    }

    @Override
    public int getWidgetToAdd() {
        return widgetToAdd;
    }

    @Override
    public void setSelectedWidgets(final Set<IWidget> selectedWidgets) {
        this.selectedWidgets = selectedWidgets;

        if (selectedWidgets.isEmpty()) {
            widgetSelection.hideGroupingButtons();
        }
    }

    @Override
    public void setResizeType(final int resizeType) {
        this.resizeType = resizeType;
    }

    @Override
    public int getResizeType() {
        return resizeType;
    }

    @Override
    public void setIsResizing(final boolean isResizing) {
        this.isResizing = isResizing;
    }

    @Override
    public boolean isResizing() {
        return isResizing;
    }

    @Override
    public DesignerSelectionBox getSelectionBox() {
        return selectionBox;
    }

    @Override
    public void resetPaletteButtons() {
        mainFrame.resetPaletteButtons();
    }

    @Override
    public void setProperties(final Set<IWidget> widget) {
        mainFrame.setPropertiesCompound(widget);
        mainFrame.setPropertiesToolBar(widget);
    }

    @Override
    public void updateRulers(final Point point) {
        mouseLocation.setLocation(point);

        horizontalRuler.updateMarker(point.x);
        verticalRuler.updateMarker(point.y);
    }

    @Override
    public void setDragBoxLocation(final Point dragBoxLocation) {
        dragBoxMouseLocation = dragBoxLocation;
        repaint();
    }

    @Override
    public void setCurrentlyDraging(final boolean currentlyDraging) {
        currentlyDragging = currentlyDraging;
    }

    @Override
    public List<IWidget> getWidgets() {
        return widgets;
    }

    @Override
    public void setIsResizingSplitComponent(final boolean isResizingSplitComponentSplitComponent) {
        isResizingSplitComponent = isResizingSplitComponentSplitComponent;
    }

    @Override
    public boolean isResizingSplitComponent() {
        return isResizingSplitComponent;
    }

    @Override
    public WidgetSelection getWidgetSelection() {
        return widgetSelection;
    }

    @Override
    public CaptionChanger getCaptionChanger() {
        return captionChanger;
    }

    @Override
    public IMainFrame getMainFrame() {
        return mainFrame;
    }

}
