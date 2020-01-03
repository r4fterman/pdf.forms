package org.pdf.forms.gui.properties.customcomponents.colorcombobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

public class ColorComboBoxEditor implements ComboBoxEditor {

    private final JTextField editor;

    private final transient EventListenerList listenerList = new EventListenerList();

    public ColorComboBoxEditor(
            final Color initialColor,
            final JComboBox<Object> colorBox) {
        editor = new JTextField("");
        editor.setBackground(initialColor);
        editor.setEditable(false);

        editor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event) {
                if (!colorBox.isPopupVisible()) {
                    colorBox.setPopupVisible(true);
                }
            }
        });

    }

    @Override
    public void addActionListener(final ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    @Override
    public Component getEditorComponent() {
        return editor;
    }

    @Override
    public Object getItem() {
        return editor.getBackground();
    }

    @Override
    public void removeActionListener(final ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    @Override
    public void selectAll() {
        // ignore
    }

    @Override
    public void setItem(final Object newValue) {
        if (newValue instanceof Color) {
            final Color color = (Color) newValue;
            editor.setBackground(color);
            editor.setText("");
        } else if (newValue == null) {
            editor.setBackground(null);
            editor.setText("mixed");
        }
    }
}
