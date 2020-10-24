package org.pdf.forms.gui.designer.listeners;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.captionchanger.CaptionChanger;
import org.pdf.forms.gui.designer.gui.DesignerSelectionBox;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetSelection;

public class DesignerMouseListener implements MouseListener {

    private final IDesigner designerPanel;

    private final DesignerSelectionBox selectionBox;

    private final CaptionChanger captionChanger;

    private final WidgetSelection widgetSelection;
    private final WidgetFactory widgetFactory;

    public DesignerMouseListener(
            final IDesigner designerPanel,
            final WidgetFactory widgetFactory) {
        this.designerPanel = designerPanel;
        this.widgetFactory = widgetFactory;

        this.widgetSelection = designerPanel.getWidgetSelection();
        this.captionChanger = designerPanel.getCaptionChanger();
        this.selectionBox = designerPanel.getSelectionBox();
    }

    /**
     * Method called when the mouse is clicked.  The primary goal of this method is to pick up the location of the
     * click, and if the mouse has been clicked inside a caption box, then display the caption changer
     *
     * @param e a MouseEvent object passed in by Java
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
            final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

            /* get a flatterned set of widgets, this means if widgets are in a group, then get them out */
            final Set<IWidget> flatternedWidgets = widgetSelection.getFlatternedWidgets(selectedWidgets);

            for (final IWidget widget: flatternedWidgets) {
                if (!widget.allowEditOfCaptionOnClick()) {
                    continue;
                }

                final Rectangle captionBounds = widget.getCaptionComponent().getBounds();
                captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());

                /* see if the click is inside the caption bounds */
                if (captionBounds.contains(e.getX(), e.getY())) {
                    captionChanger.displayCaptionChanger(widget, designerPanel);
                }
            }

            designerPanel.validate();
            designerPanel.repaint();
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        designerPanel.grabFocus();

        captionChanger.closeCaptionChanger();

        final int x = event.getX();
        final int y = event.getY();

        /*
         * if the widget is being resized the mouse is not over the
         * the widget so we need to keep the present selectedWidget,
         * otherwise, we can get the current selectedWidget
         */
        if (designerPanel.getResizeType() == DesignerMouseMotionListener.DEFAULT_CURSOR) {
            // widget not being resized
            final IWidget selectedWidget = designerPanel.getWidgetAt(x, y);

            if (selectedWidget == null) {
                // user has clicked off all widgets, so empty the selected widgets Set
                designerPanel.setSelectedWidgets(new HashSet<>());
            } else {

                /*
                 * if the new selected widget is already selected the we leave it selected
                 * and dont change anything
                 */
                if (!designerPanel.getSelectedWidgets().contains(selectedWidget)) {

                    /*
                     * the user has clicked on a widget that isnt already selected
                     * so we need to create a new set with just the new selected widget
                     */
                    final Set<IWidget> selectedWidgets = new HashSet<>();
                    selectedWidgets.add(selectedWidget);

                    designerPanel.setSelectedWidgets(selectedWidgets);

                    designerPanel.getMainFrame().setPropertiesCompound(selectedWidgets);
                    designerPanel.getMainFrame().setPropertiesToolBar(selectedWidgets);
                }
            }
        }

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        /*
         * if no widget is selected then set up the selection box, otherwise
         * a widget is either about to be moved or resized, so set up for that
         */
        if (selectedWidgets.isEmpty()) {
            // set up the selection box
            selectionBox.setCurrentRect(new Rectangle(x, y, 0, 0));
            selectionBox.updateDrawableRect(designerPanel.getWidth(), designerPanel.getHeight());
        } else {
            // set up the widget to be moved or resized
            final Rectangle selectionBoxBounds = widgetSelection.getSelectionBoxBounds();
            for (IWidget widget: widgetSelection.getFlatternedWidgets(selectedWidgets)) {
                widget.setResizeHeightRatio(widget.getBoxSize().getHeight() / (selectionBoxBounds.getHeight()));
                widget.setResizeWidthRatio(widget.getBoxSize().getWidth() / (selectionBoxBounds.getWidth()));

                widget.setResizeFromTopRatio(
                        (widget.getY() - (selectionBoxBounds.getY() + WidgetSelection.BOX_MARGIN)) / selectionBoxBounds
                                .getHeight());

                widget.setResizeFromLeftRatio(
                        (widget.getX() - (selectionBoxBounds.getX() + WidgetSelection.BOX_MARGIN)) / selectionBoxBounds
                                .getWidth());

                widget.setLastX(widget.getX() - x);
                widget.setLastY(widget.getY() - y);
            }

            widgetSelection.setLastX(x);
            widgetSelection.setLastY(y);
        }

        designerPanel.setProperties(selectedWidgets);
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        if (designerPanel.getWidgetToAdd() == IWidget.NONE) {
            selectionBox.setSelectedWedgets();
            selectionBox.setCurrentRect(null);

            designerPanel.setResizeType(DesignerMouseMotionListener.DEFAULT_CURSOR);
            designerPanel.setIsResizing(false);
            designerPanel.setIsResizingSplitComponent(false);
            designerPanel.setCurrentlyDragging(false);
        } else {
            final int widgetToAdd = designerPanel.getWidgetToAdd();

            final Rectangle bounds = selectionBox.getCurrentRect();

            final IWidget widget;
            if (widgetToAdd == IWidget.RADIO_BUTTON) {
                final IMainFrame mainFrame = designerPanel.getMainFrame();
                widget = widgetFactory.createRadioButtonWidget(mainFrame.getFormsDocument()
                                                                       .getPage(mainFrame.getCurrentPage()), bounds);
            } else if (widgetToAdd == IWidget.CHECK_BOX) {
                final IMainFrame mainFrame = designerPanel.getMainFrame();
                widget = widgetFactory.createCheckBoxWidget(mainFrame.getFormsDocument()
                                                                    .getPage(mainFrame.getCurrentPage()), bounds);
            } else {
                widget = widgetFactory.createWidget(widgetToAdd, bounds);
            }
            widget.setX(bounds.x);
            widget.setY(bounds.y);

            final Set<IWidget> set = new HashSet<>();
            set.add(widget);

            designerPanel.setSelectedWidgets(set);
            designerPanel.addWidget(widget);

            designerPanel.setWidgetToAdd(IWidget.NONE);
            designerPanel.resetPaletteButtons();

            selectionBox.setCurrentRect(null);
        }

        designerPanel.repaint();
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        final int widgetToAdd = designerPanel.getWidgetToAdd();
        if (widgetToAdd != IWidget.NONE) {
            designerPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        // do nothing
    }
}
