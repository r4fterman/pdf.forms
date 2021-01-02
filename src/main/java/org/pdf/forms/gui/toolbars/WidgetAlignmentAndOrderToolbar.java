package org.pdf.forms.gui.toolbars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.*;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class WidgetAlignmentAndOrderToolbar extends VLToolBar {

    private final List<JButton> buttonsList = new ArrayList<>();

    public WidgetAlignmentAndOrderToolbar(final IDesigner designer) {
        final String[] alignButtons = WidgetAlignmentAndOrder.getAlignButtons();
        final String[] layoutButtons = WidgetAlignmentAndOrder.getOrderButtons();

        final String[] buttons = concat(alignButtons, layoutButtons);

        for (final String url : buttons) {
            if (url.equals("Seperator")) {
                addSeparator();
            } else {
                addButton(designer, url);
            }
        }
    }

    private void addButton(
            final IDesigner designer,
            final String url) {
        final String[] splitFilename = url.split("/");
        final String type = splitFilename[splitFilename.length - 1].split("\\.")[0];

        final JButton button = new JButton(new ImageIcon(getClass().getResource(url)));
        button.setToolTipText(type);
        button.addActionListener(e -> WidgetAlignmentAndOrder.alignAndOrder(designer, type));

        buttonsList.add(button);

        add(button);
    }

    private String[] concat(
            final String[] array1,
            final String[] array2) {
        final List<String> list1 = Arrays.asList(array1);
        final List<String> list2 = Arrays.asList(array2);
        return Stream.concat(list1.stream(), list2.stream()).toArray(String[]::new);
    }

    public void setState(final boolean enabled) {
        for (final JButton button : buttonsList) {
            button.setEnabled(enabled);
        }
    }
}
