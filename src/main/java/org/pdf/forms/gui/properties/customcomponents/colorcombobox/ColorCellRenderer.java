package org.pdf.forms.gui.properties.customcomponents.colorcombobox;

import javax.swing.*;
import java.awt.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorCellRenderer extends DefaultListCellRenderer {

    private final Logger logger = LoggerFactory.getLogger(ColorCellRenderer.class);

    // width doesn't matter as combobox will size
    private final Dimension preferredSize = new Dimension(0, 20);

    @Override
    public Component getListCellRendererComponent(
            final JList list,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        final JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Color) {
            renderer.setBackground((Color) value);
            renderer.setText("");
        } else if (value instanceof String && value.equals("Custom")) {
            logger.info("Custom color found");
        } else {
            logger.warn("Unexpected list cell renderer value {} of class {}", value, value.getClass().getCanonicalName());
        }

        renderer.setPreferredSize(preferredSize);
        return renderer;
    }
}
