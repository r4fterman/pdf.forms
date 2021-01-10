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

    @Override
    public void mouseClicked(final MouseEvent event) {
        if (event.getClickCount() != 2) {
            return;
        }

        event.consume();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        /* get a flattened set of widgets, this means if widgets are in a group, then get them out */
        widgetSelection.getFlattenedWidgets(selectedWidgets).stream()
                .filter(IWidget::allowEditOfCaptionOnClick)
                .forEach(widget -> {
                    final Rectangle captionBounds = widget.getCaptionComponent().getBounds();
                    captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());

                    /* see if the click is inside the caption bounds */
                    if (captionBounds.contains(event.getX(), event.getY())) {
                        captionChanger.displayCaptionChanger(widget, designerPanel);
                    }
                });

        designerPanel.validate();
        designerPanel.repaint();
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        event.consume();

        designerPanel.grabFocus();

        captionChanger.closeCaptionChanger();

        final int x = event.getX();
        final int y = event.getY();

        syncSelectedWidgets(x, y);
        final Set<IWidget> selectedWidgets = setUpWidgetSelection(x, y);

        designerPanel.setProperties(selectedWidgets);
    }

    private Set<IWidget> setUpWidgetSelection(
            final int x,
            final int y) {
        // if no widget is selected then set up the selection box,
        // otherwise a widget is either about to be moved or resized, so set up for that
        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();
        if (selectedWidgets.isEmpty()) {
            // set up the selection box
            selectionBox.setCurrentRect(new Rectangle(x, y, 0, 0));
            selectionBox.updateDrawableRect(designerPanel.getWidth(), designerPanel.getHeight());

            widgetSelection.setLastX(x);
            widgetSelection.setLastY(y);

            return selectedWidgets;
        }

        // set up the widget to be moved or resized
        final Rectangle selectionBoxBounds = widgetSelection.getSelectionBoxBounds();
        for (final IWidget widget: widgetSelection.getFlattenedWidgets(selectedWidgets)) {
            widget.setResizeHeightRatio(widget.getBoxSize().getHeight() / selectionBoxBounds.getHeight());
            widget.setResizeWidthRatio(widget.getBoxSize().getWidth() / selectionBoxBounds.getWidth());

            final double resizeHeightRatio =
                    (widget.getY() - (selectionBoxBounds.getY() + WidgetSelection.BOX_MARGIN))
                            / selectionBoxBounds.getHeight();
            widget.setResizeFromTopRatio(resizeHeightRatio);

            final double resizeWidthRation =
                    (widget.getX() - (selectionBoxBounds.getX() + WidgetSelection.BOX_MARGIN))
                            / selectionBoxBounds.getWidth();
            widget.setResizeFromLeftRatio(resizeWidthRation);

            widget.setLastX(widget.getX() - x);
            widget.setLastY(widget.getY() - y);

            widgetSelection.setLastX(x);
            widgetSelection.setLastY(y);
        }
        return selectedWidgets;
    }

    private void syncSelectedWidgets(
            final int x,
            final int y) {
        // if the widget is being resized the mouse is not over the
        // the widget so we need to keep the present selectedWidget,
        // otherwise, we can get the current selectedWidget
        if (designerPanel.getResizeType() != DesignerMouseMotionListener.DEFAULT_CURSOR) {
            return;
        }

        // widget not being resized
        final IWidget selectedWidget = designerPanel.getWidgetAt(x, y);
        if (selectedWidget == null) {
            // user has clicked off all widgets, so empty the selected widgets Set
            designerPanel.setSelectedWidgets(new HashSet<>());
            return;
        }

        // if the new selected widget is already selected the we leave it selected
        // and dont change anything
        if (!designerPanel.getSelectedWidgets().contains(selectedWidget)) {
            // the user has clicked on a widget that isnt already selected
            // so we need to create a new set with just the new selected widget
            final Set<IWidget> selectedWidgets = Set.of(selectedWidget);
            designerPanel.setSelectedWidgets(selectedWidgets);

            designerPanel.getMainFrame().setPropertiesCompound(selectedWidgets);
            designerPanel.getMainFrame().setPropertiesToolBar(selectedWidgets);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        if (designerPanel.getWidgetToAdd() == IWidget.NONE) {
            selectionBox.setSelectedWidgets();
            selectionBox.setCurrentRect(null);

            designerPanel.setResizeType(DesignerMouseMotionListener.DEFAULT_CURSOR);
            designerPanel.setIsResizing(false);
            designerPanel.setIsResizingSplitComponent(false);
            designerPanel.setCurrentlyDragging(false);
        } else {
            final int widgetToAdd = designerPanel.getWidgetToAdd();
            final Rectangle bounds = selectionBox.getCurrentRect();

            final IWidget widget = createWidgetOfType(widgetToAdd, bounds);
            widget.setX(bounds.x);
            widget.setY(bounds.y);

            final Set<IWidget> set = Set.of(widget);
            designerPanel.setSelectedWidgets(set);
            designerPanel.addWidget(widget);

            designerPanel.setWidgetToAdd(IWidget.NONE);
            designerPanel.resetPaletteButtons();

            selectionBox.setCurrentRect(null);
        }

        designerPanel.repaint();
    }

    private IWidget createWidgetOfType(
            final int widgetType,
            final Rectangle bounds) {
        if (widgetType == IWidget.RADIO_BUTTON) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            return widgetFactory.createRadioButtonWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()),
                    bounds);
        }

        if (widgetType == IWidget.CHECK_BOX) {
            final IMainFrame mainFrame = designerPanel.getMainFrame();
            return widgetFactory.createCheckBoxWidget(
                    mainFrame.getFormsDocument().getPage(mainFrame.getCurrentPage()),
                    bounds);
        }

        return widgetFactory.createWidget(widgetType, bounds);
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        event.consume();

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
