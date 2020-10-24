package org.pdf.forms.gui.designer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jpedal.PdfDecoder;
import org.pdf.forms.Configuration;
import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.designer.listeners.DesignerKeyListener;
import org.pdf.forms.gui.designer.listeners.DesignerMouseListener;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
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
    private final Set<IWidget> selectedWidgets = new HashSet<>();
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

    public Designer(
            final int inset,
            final Rule horizontalRuler,
            final Rule verticalRuler,
            final IMainFrame mainFrame,
            final String version,
            final FontHandler fontHandler,
            final WidgetFactory widgetFactory,
            final Configuration configuration) {
        super();

        setBackground(BACKGROUND_COLOR);

        selectionBox = new DesignerSelectionBox(this);
        widgetSelection = new WidgetSelection(this, version, fontHandler, widgetFactory, configuration);

        this.horizontalRuler = horizontalRuler;
        this.verticalRuler = verticalRuler;
        this.inset = inset;
        this.mainFrame = mainFrame;

        addMouseListener(new DesignerMouseListener(this, widgetFactory));
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

            for (final IWidget drawWidget: widget.getWidgetsInGroup()) {
                drawWidget(drawWidget, g2);
            }
        } else {
            final Document document = widget.getProperties();
            final Element objectProperties = (Element) document.getElementsByTagName("object").item(0);
            final Element fieldProperties = (Element) objectProperties.getElementsByTagName("field").item(0);

            final String visibility = XMLUtils.getAttributeFromChildElement(fieldProperties, "Presence").orElse("");
            if (visibility.equals("Visible")) {
                g2.translate(widget.getX(), widget.getY());

                widget.getWidget().paint(g2);

                widgetSelection.drawSingleSectionBox(g2,
                                                     widget,
                                                     selectedWidgets.size() == 1 && selectedWidgets.contains(widget));

                g2.translate(-widget.getX(), -widget.getY());
            }
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D graphics2D = (Graphics2D) g;

        final AffineTransform transform = graphics2D.getTransform();
        //  transform.scale(scale, scale); @scale
        graphics2D.setTransform(transform);

        /*
         * draw the basic blank page
         */
        drawPage(graphics2D);

        /*
         * draw each widget in turn onto the page
         */
        for (final IWidget widget: widgets) {
            drawWidget(widget, graphics2D);
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
        if (dragBoxMouseLocation != null && selectedWidget != null) {
            final Rectangle boxSize = selectedWidget.getBounds();
            drawDragOutBoxAndDisplayTooltip(graphics2D, boxSize);
        }

        if (selectedWidget != null) {
            final Rectangle boxSize = WidgetSelection.getMultipleWidgetBounds(selectedWidgets);

            /*
             * if we are dragging the widget, or resizing it, we need to display the
             * required tooltip which displays the components size/coordinates details
             */
            if (currentlyDragging) {
                displayTooltip(graphics2D, boxSize, false);
            } else if (isResizing) {
                displayTooltip(graphics2D, boxSize, true);
            }
        }

        if (selectedWidgets.size() > 1 || (selectedWidget != null && selectedWidget.getType() == IWidget.GROUP)) {
            widgetSelection.drawMulitipleSelectionBox(graphics2D, selectedWidgets, true);
        }

        selectionBox.paintBox(graphics2D);
    }

    private void drawDragOutBoxAndDisplayTooltip(
            final Graphics2D g2,
            final Rectangle boxSize) {
        final int dragBoxX = dragBoxMouseLocation.x - (boxSize.width / 2);
        final int dragBoxY = dragBoxMouseLocation.y - (boxSize.height / 2);

        final float[] dashPattern = {1, 1};
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dashPattern, 0));
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
        final int units = (int) (Rule.INCH / 2.54);
        final String text = getTooltipText(boxSize, isResizing, units);

        final JToolTip tooltip = new JToolTip();
        tooltip.setTipText(text);
        tooltip.setSize(tooltip.getPreferredSize());

        final int translateX = mouseLocation.x - (tooltip.getSize().width / 2);
        final int translateY = mouseLocation.y + 20;

        g2.translate(translateX, translateY);

        tooltip.paint(g2);

        g2.translate(-translateX, -translateY);
    }

    private String getTooltipText(
            final Rectangle boxSize,
            final boolean isResizing,
            final double units) {
        if (isResizing) {
            final double width = (double) boxSize.width / units;
            final double height = (double) boxSize.height / units;
            return round(width) + "cm x " + round(height) + "cm";
        }

        final double xLocation = (double) (boxSize.x - inset) / units;
        final double yLocation = (double) (boxSize.y - inset) / units;
        return round(xLocation) + "cm , " + round(yLocation) + "cm";
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

                setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
                setInset(inset, inset);

                final int pdfPageNumber = page.getPdfPageNumber();
                setPageParameters(1, pdfPageNumber);
                decodePage(pdfPageNumber);

                pageWidth = getPdfPageData().getCropBoxWidth(pdfPageNumber);
                pageHeight = getPdfPageData().getCropBoxHeight(pdfPageNumber);
            } catch (final Exception e) {
                logger.error("Error displaying page", e);
            }
        }

        updateUI();
    }

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
    }

    @Override
    public void removeSelectedWidgets() {
        widgets.removeAll(selectedWidgets);

        removeWidgetsFromHierarchy(selectedWidgets);
    }

    private void removeWidgetsFromHierarchy(final Collection<IWidget> widgets) {
        for (final IWidget widget: widgets) {
            mainFrame.removeWidgetFromHierarchy(widget);
        }
    }

    @Override
    public void removeWidget(
            final IWidget widgetToRemove,
            final List<IWidget> widgets) {
        for (final Iterator<IWidget> iterator = widgets.iterator(); iterator.hasNext();) {
            final IWidget widget = iterator.next();
            if (removeWidgetFromIteration(widgetToRemove, iterator, widget)) {
                break;
            }
        }

        mainFrame.updateHierarchy();
        repaint();
    }

    private boolean removeWidgetFromIteration(
            final IWidget widgetToRemove,
            final Iterator<IWidget> iterator,
            final IWidget widget) {
        if (widget.getType() == IWidget.GROUP) {
            return removeFromGroup(widgetToRemove, iterator, widget);
        }
        return removeWidget(widgetToRemove, iterator, widget);
    }

    private boolean removeFromGroup(
            final IWidget widgetToRemove,
            final Iterator<IWidget> iterator,
            final IWidget widget) {
        removeWidget(widgetToRemove, widget.getWidgetsInGroup());
        return removeWidget(widgetToRemove, iterator, widget);
    }

    private boolean removeWidget(
            final IWidget widgetToRemove,
            final Iterator<IWidget> iterator,
            final IWidget widget) {
        if (widget == widgetToRemove) {
            iterator.remove();
            return true;
        }
        return false;
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
        this.selectedWidgets.clear();
        this.selectedWidgets.addAll(selectedWidgets);

        if (this.selectedWidgets.isEmpty()) {
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
    public void setCurrentlyDragging(final boolean currentlyDragging) {
        this.currentlyDragging = currentlyDragging;
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
