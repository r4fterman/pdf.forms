package org.pdf.forms.gui.library;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.net.URL;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.utils.WidgetFactory;

public class LibraryPanel extends JPanel implements Dockable {

    private final JList<String> list;
    private final ListSelectionListener listener;

    public LibraryPanel(
            final IDesigner designer,
            final WidgetFactory widgetFactory) {
        setLayout(new BorderLayout());

        list = new JList<>(new String[]{
                "Text Field",
                "Text",
                "Button",
                "Radio Button",
                "Check Box",
                "Drop-down List",
                "List Box",
                "Image"});

        final LibraryPanelCellRenderer libraryPanelCellRenderer = new LibraryPanelCellRenderer();
        list.setCellRenderer(libraryPanelCellRenderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        listener = e -> {
            designer.setWidgetToAdd(list.getSelectedIndex());
            designer.getCaptionChanger().closeCaptionChanger();
        };

        list.addListSelectionListener(listener);

        final DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_COPY_OR_MOVE, new DragableComponent(dragSource, designer, widgetFactory));

        final JScrollPane listScroller = new JScrollPane(list);

        add(listScroller);
    }

    public void setState(final boolean state) {
        list.setEnabled(state);
    }

    class LibraryPanelCellRenderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                final JList list,
                final Object value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus) {

            final String s = value.toString();
            setText(s);
            final URL resource = getClass().getResource("/org/pdf/forms/res/" + s + ".gif");
            final ImageIcon longIcon = new ImageIcon(resource);
            setIcon(longIcon);
            if (isSelected) {
                setBackground(new Color(236, 233, 216));
                setForeground(Color.BLACK);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
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
