package org.pdf.forms.gui.properties.customcomponents.colorcombobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorCellRenderer implements ListCellRenderer<Object> {

    private final Logger logger = LoggerFactory.getLogger(ColorCellRenderer.class);

    // width doesn't matter as combobox will size
    private final Dimension preferredSize = new Dimension(0, 20);

    private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(
            final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        final JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Color) {
            renderer.setBackground((Color) value);
            renderer.setText("");
            //} else if (value instanceof String && value.equals("Custom")) {
            // Custom color found
        } else {
            logger.warn("Unexpected list cell renderer value {} of class {}", value, value.getClass().getCanonicalName());
        }

        renderer.setPreferredSize(preferredSize);
        return renderer;
    }
}
