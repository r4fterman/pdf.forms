package org.pdf.forms.gui.library;

import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.utils.WidgetFactory;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class LibraryPanel extends JPanel implements Dockable {

    private static final String[] TOOL_ELEMENTS = {
            "Text Field",
            "Text",
            "Button",
            "Radio Button",
            "Check Box",
            "Drop-down List",
            "List Box",
            "Image"};

    private final JList<String> list;
    private final ListSelectionListener listener;

    public LibraryPanel(
            final IDesigner designer,
            final WidgetFactory widgetFactory) {
        setLayout(new BorderLayout());

        this.list = new JList<>(TOOL_ELEMENTS);

        list.setCellRenderer(new LibraryPanelCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        this.listener = e -> {
            designer.setWidgetToAdd(list.getSelectedIndex());
            designer.getCaptionChanger().closeCaptionChanger();
        };

        list.addListSelectionListener(listener);

        final DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(
                list,
                DnDConstants.ACTION_COPY_OR_MOVE,
                new DragableComponent(dragSource, designer, widgetFactory));

        add(new JScrollPane(list));
    }

    public void setState(final boolean state) {
        list.setEnabled(state);
    }

    private static class LibraryPanelCellRenderer extends JLabel implements ListCellRenderer<String> {

        @Override
        public Component getListCellRendererComponent(
                final JList<? extends String> list,
                final String value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {
            setText(value);
            setIcon(getImageIcon(value));
            setBackground(getBackgroundColor(isSelected, list.getBackground()));
            setForeground(getForegroundColor(isSelected, list.getForeground()));
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }

        private Color getForegroundColor(
                final boolean isSelected,
                final Color defaultColor) {
            if (isSelected) {
                return Color.BLACK;
            }
            return defaultColor;
        }

        private Color getBackgroundColor(
                final boolean isSelected,
                final Color defaultColor) {
            if (isSelected) {
                return new Color(236, 233, 216);
            }
            return defaultColor;

        }

        private Icon getImageIcon(final String value) {
            final URL resource = getClass().getResource("/org/pdf/forms/res/" + value + ".gif");
            return new ImageIcon(resource);
        }
    }

    public void resetButtons() {
        list.removeListSelectionListener(listener);
        list.clearSelection();
        list.addListSelectionListener(listener);
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("library", "Library", "Shows library tools");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
