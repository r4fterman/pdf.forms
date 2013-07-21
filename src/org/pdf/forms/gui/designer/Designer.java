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
 * Designer.java
 * ---------------
 */
package org.pdf.forms.gui.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Designer extends PdfDecoder implements IDesigner {

    public static final String version = "0.8b05";

    private List widgets;// = new ArrayList();

    //private List widgetsInAddedOrder;// = new ArrayList();

    private Set selectedWidgets = new HashSet();

    private WidgetSelection widgetSelection;

    private int resizeType = DesignerMouseMotionListener.DEFAULT_CURSOR;

    private boolean isResizing;

    private int pageWidth, pageHeight;

    private int inset;

    private int widgetToAdd = IWidget.NONE;

    private IMainFrame mainFrame;

    private DesignerSelectionBox selectionBox;

    private Rule horizontalRuler, verticalRuler;

    private Point dragBoxMouseLocation = null;

    private boolean currentlyDraging;

    private boolean isResizingSplitComponent;

    private Point mouseLocation = new Point();

    private String currentlyOpenPDF;

    private CaptionChanger captionChanger = new CaptionChanger();

    private int drawingState;

    private Page currentPage;

//	private double scale = 1; @scale

    public Designer(int inset, Rule horizontalRuler, Rule verticalRuler,
                    IMainFrame mainFrame) {

        super();

//        try {
//            URL testFile = new URL("http://www.jpedal.org/forms/forms.lic");
//            URLConnection yc = testFile.openConnection();
//            yc.getInputStream();
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "This application is no longer valid");
//            System.exit(1);
//        }

        //setDisplayForms(false);

        setBackground(BACKGROUND_COLOR);

        this.selectionBox = new DesignerSelectionBox(this);
        this.widgetSelection = new WidgetSelection(this);

        this.horizontalRuler = horizontalRuler;
        this.verticalRuler = verticalRuler;
        this.inset = inset;
        this.mainFrame = mainFrame;

//        new DropTarget(this, new DropableComponent(this));

        addMouseListener(new DesignerMouseListener(this));
        addMouseMotionListener(new DesignerMouseMotionListener(this));
        addKeyListener(new DesignerKeyListener(this));

        setLayout(null);
    }

    private void drawPage(Graphics2D g2) {
        if (drawingState != IDesigner.CLOSED) {

            //Rectangle r = new Rectangle((int) Math.round((inset) / scale), (int) Math.round ((inset) / scale), pageWidth, pageHeight);
            Rectangle r = new Rectangle(inset, inset, currentPage.getWidth(), currentPage.getHeight());

            if (drawingState != IDesigner.PDFPAGE) {
//				g2.setPaint(IDesigner.PAGE_COLOR);
//				g2.fillRect((int) Math.round((inset) / scale), (int) Math.round ((inset) / scale), pageWidth, pageHeight);
                g2.setPaint(IDesigner.PAGE_COLOR);
                g2.fill(r);
            }

            g2.setColor(Color.black);
            g2.draw(r);

//			g2.setPaint(Color.black);
//			g2.drawRect((int) Math.round((inset - 1) / scale), (int) Math.round ((inset - 1) / scale), pageWidth + 1, pageHeight + 1);

        }
    }

    private void drawWidget(IWidget widget, Graphics2D g2) {

        if (widget.getType() == IWidget.GROUP) {
            widgetSelection.drawMulitipleSelectionBox(g2, new HashSet(widget.getWidgetsInGroup()), false);

            for (Iterator it = widget.getWidgetsInGroup().iterator(); it.hasNext(); ) {
                IWidget w = (IWidget) it.next();

                drawWidget(w, g2);
            }

        } else {

            Document document = widget.getProperties();
            Element objectProperties = (Element) document.getElementsByTagName("object").item(0);

            Element fieldProperties =
                    (Element) objectProperties.getElementsByTagName("field").item(0);

            String visibility = XMLUtils.getAttributeFromChildElement(fieldProperties, "Presence");

            if (visibility.equals("Visible")) {
                g2.translate(widget.getX(), widget.getY());

                JComponent c = widget.getWidget();
                c.paint(g2);

                widgetSelection.drawSingleSectionBox(g2, widget, selectedWidgets.size() == 1 && selectedWidgets.contains(widget));

                g2.translate(-widget.getX(), -widget.getY());
            }
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        AffineTransform transform = g2.getTransform();
//		transform.scale(scale, scale); @scale
        g2.setTransform(transform);

        /**
         * draw the basic blank page
         */
        drawPage(g2);

        /**
         * draw each widget in turn onto the page
         */
        for (Iterator iter = widgets.iterator(); iter.hasNext(); ) {
            drawWidget((IWidget) iter.next(), g2);
        }

        IWidget selectedWidget;
        if (selectedWidgets.isEmpty())
            selectedWidget = null;
        else
            selectedWidget = (IWidget) selectedWidgets.iterator().next();

        /**
         * if we're dragging out a component from the library, dragBoxMouseLocation will
         * not be null, so we need to draw the special box, and display the tooltip.
         */
        if (dragBoxMouseLocation != null) {
            Rectangle boxSize = selectedWidget.getBounds();
            drawDragOutBoxAndDisplayTooltip(g2, boxSize);
        }

        if (selectedWidget != null) {
            Rectangle boxSize = WidgetSelection.getMultipleWidgetBounds(selectedWidgets);

            /**
             * if we are dragging the widget, or resizing it, we need to display the
             * required tooltip which displays the components size/coordinates details
             */
            if (currentlyDraging) {
                displayTooltip(g2, boxSize, false);
            } else if (isResizing) {
                displayTooltip(g2, boxSize, true);
            }
        }

        if (selectedWidgets.size() > 1 || (selectedWidget != null && selectedWidget.getType() == IWidget.GROUP)) {
            widgetSelection.drawMulitipleSelectionBox(g2, selectedWidgets, true);
        }

        //g2.setTransform(new AffineTransform()); @scale broken
        selectionBox.paintBox(g2);
    }

    private void drawDragOutBoxAndDisplayTooltip(Graphics2D g2, Rectangle boxSize) {
        int dragBoxX = dragBoxMouseLocation.x - (boxSize.width / 2);
        int dragBoxY = dragBoxMouseLocation.y - (boxSize.height / 2);

        float[] dashPattern = {1, 1};
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1,
                dashPattern, 0));
        g2.setColor(Color.black);

        g2.drawRect(dragBoxX, dragBoxY, boxSize.width, boxSize.height);

        g2.setStroke(new BasicStroke());

        boxSize.setLocation(dragBoxX, dragBoxY);

        displayTooltip(g2, boxSize, false);
    }

    private void displayTooltip(Graphics2D g2, Rectangle boxSize, boolean isResizing) {
        JToolTip tooltip = new JToolTip();

        String text;

        int units = (int) (Rule.INCH / 2.54);

        if (isResizing) {
            double width = (double) boxSize.width / (double) units;
            double height = (double) boxSize.height / (double) units;

            width = round(width, 3);
            height = round(height, 3);

            text = width + "cm x " + height + "cm";
        } else {
            double xLocation = (double) (boxSize.x - inset) / (double) units;
            double yLocation = (double) (boxSize.y - inset) / (double) units;

            xLocation = round(xLocation, 3);
            yLocation = round(yLocation, 3);

            text = xLocation + "cm , " + yLocation + "cm";
        }

        tooltip.setTipText(text);
        tooltip.setSize(tooltip.getPreferredSize());

        int translateX = mouseLocation.x - (tooltip.getSize().width / 2);
        int translateY = mouseLocation.y + 20;

        g2.translate(translateX, translateY);

        tooltip.paint(g2);

        g2.translate(-translateX, -translateY);
    }

    private double round(double number, int decPlaces) {

        double exponential = Math.pow(10, decPlaces);

        number *= exponential;
        number = Math.round(number);
        number /= exponential;

        return number;
    }

    public void close() {
        removeWidgetsFromHierarchy(widgets);

        widgets.clear();
        //widgetsInAddedOrder.clear();
        selectedWidgets.clear();

        drawingState = IDesigner.CLOSED;

        currentlyOpenPDF = null;

        pageHeight = pageWidth = 0;

        closePdfFile();

        updateUI();
    }

    // GETTERS AND SETTERS /////////

    public void setScale(float scale) {

    }

    public Dimension getPreferredSize() {
        if (drawingState == IDesigner.PDFPAGE)
            return getMaximumSize();
        else
            return new Dimension(inset * 2 + pageWidth, inset * 2 + pageHeight);
    }

    public void displayPage(final Page page) {

        this.currentPage = page;

        widgets = page.getWidgets();

//		widgetsInAddedOrder = page.getWidgetsInAddedOrder();

        selectedWidgets.clear();

        String pdfFile = page.getPdfFileLocation();
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

                if (pdfFile.startsWith("http:") || pdfFile.startsWith("file:"))
                    openPdfFileFromURL(pdfFile);
                else
                    openPdfFile(pdfFile);

                currentlyOpenPDF = pdfFile;

                setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
                setInset(inset, inset);

                int pdfPageNumber = page.getPdfPageNumber();
                setPageParameters(1, pdfPageNumber);
                decodePage(pdfPageNumber);

                pageWidth = getPdfPageData().getCropBoxWidth(pdfPageNumber);
                pageHeight = getPdfPageData().getCropBoxHeight(pdfPageNumber);

//                if (!page.widgetsAddedToDesigner()) {
//                    List widgestList = WidgetParser.parseWidgets(getCurrentFormRenderer(), pageHeight);
//
//                    for (Iterator it = widgestList.iterator(); it.hasNext();) {
//                        IWidget widget = (IWidget) it.next();
//						if (widget != null)
//							addWidget(widget);
//                    }
//
//                    page.setWidgetsAddedToDesigner(true);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateUI();
    }

//    private void setDisplayForms(boolean displayForms) {
//		this.displayForms = displayForms;
//	}

    public IWidget getWidgetAt(int x, int y) {

        for (ListIterator iter = widgets.listIterator(widgets.size()); iter.hasPrevious(); ) {
            IWidget widget = (IWidget) iter.previous();

            if (widget.getBounds().contains(x, y))//if (widget.getBounds().contains(x / scale, y / scale)) @scale
                return widget;
        }

        return null;
    }

    public void addWidget(IWidget widget) {
        mainFrame.addWidgetToHierarchy(widget);

        mainFrame.addWidgetToPage(widget);

        Set set = new HashSet();
        set.add(widget);
        mainFrame.setPropertiesCompound(set);
        mainFrame.setPropertiesToolBar(set);
    }

    public void addWidget(int index, IWidget w) {
        widgets.add(index, w);
//		widgetsInAddedOrder.add(w);
    }

    public void removeSelectedWidgets() {
        this.widgets.removeAll(selectedWidgets);
//		this.widgetsInAddedOrder.removeAll(selectedWidgets);

        removeWidgetsFromHierarchy(selectedWidgets);
    }

    private void removeWidgetsFromHierarchy(Collection widgets) {
        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            mainFrame.removeWidgetFromHierarchy(widget);
        }
    }

    public void removeWidget(IWidget widgetToRemove, List widgets) {

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget w = (IWidget) it.next();

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

    public Set getSelectedWidgets() {
        return selectedWidgets;
    }

    public void setWidgetToAdd(int widgetToAdd) {
        this.widgetToAdd = widgetToAdd;
    }

    public int getWidgetToAdd() {
        return widgetToAdd;
    }

    public void setSelectedWidgets(Set selectedWidgets) {
        this.selectedWidgets = selectedWidgets;

        if (selectedWidgets.isEmpty())
            widgetSelection.hideGroupingButtons();

    }

    public void setResizeType(int resizeType) {
        this.resizeType = resizeType;
    }

    public int getResizeType() {
        return resizeType;
    }

    public void setIsResizing(boolean isResizing) {
        this.isResizing = isResizing;
    }

    public boolean isResizing() {
        return isResizing;
    }

    public DesignerSelectionBox getSelectionBox() {
        return selectionBox;
    }

    public void resetPaletteButtons() {
        mainFrame.resetPaletteButtons();
    }

    public void setProperties(Set widget) {
        mainFrame.setPropertiesCompound(widget);
        mainFrame.setPropertiesToolBar(widget);
    }

    public void updateRulers(Point point) {
        mouseLocation.setLocation(point);

        horizontalRuler.updateMarker(point.x);
        verticalRuler.updateMarker(point.y);
    }

    public void setDragBoxLocation(Point dragBoxLocation) {
        this.dragBoxMouseLocation = dragBoxLocation;
        repaint();
    }

    public void setCurrentlyDraging(boolean currentlyDraging) {
        this.currentlyDraging = currentlyDraging;
    }

    public List getWidgets() {
        return widgets;
    }

//	public IWidget getOldestSelectedWidget() {
//	IWidget oldestSelectedWidget = null;

//for (Iterator iter = widgetsInAddedOrder.iterator(); iter.hasNext();) {
//	IWidget widget = (IWidget) iter.next();
//	if (selectedWidgets.contains(widget)) {
//	oldestSelectedWidget = widget;
//	break;
//	}
//	}

//	return oldestSelectedWidget;
//	}

    public void setIsResizingSplitComponent(boolean isResizingSplitComponentSplitComponent) {
        this.isResizingSplitComponent = isResizingSplitComponentSplitComponent;
    }

    public boolean isResizingSplitComponent() {
        return isResizingSplitComponent;
    }

    public WidgetSelection getWidgetSelection() {
        return widgetSelection;
    }

    public CaptionChanger getCaptionChanger() {
        return captionChanger;
    }

    public IMainFrame getMainFrame() {
        return mainFrame;
    }

//	@scale
//public double getScale() {
//	return scale;
//	}

//	public void setScale(double scale) {
//	this.scale = scale;
//	repaint();
//	}
}