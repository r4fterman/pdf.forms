package org.pdf.forms.gui.toolbars;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.components.ButtonWithID;
import org.pdf.forms.gui.components.ComboBoxWithID;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class DocumentToolBar extends VLToolBar {

    private static final String[] ZOOMS = {
            "100%",
            "75%",
            "50%",
            "25%"
    };

    private final List<JComponent> zoomComponents = new ArrayList<>();
    private final List<JComponent> documentComponents = new ArrayList<>();

    private final CommandListener commandListener;
    private final JButton saveButton;
    private final JComboBox<String> zoomBox;

    public DocumentToolBar(final CommandListener commandListener) {
        this.commandListener = commandListener;

        documentComponents.add(addToolBarButton("/org/pdf/forms/res/New.gif", Commands.NEW, "New"));
        documentComponents.add(addToolBarButton("/org/pdf/forms/res/open.gif", Commands.OPEN, "Open"));

        saveButton = addToolBarButton("/org/pdf/forms/res/save.gif", Commands.SAVE_FILE, "Save");

        addSeparator();

        final JButton zoomIn = addToolBarButton("/org/pdf/forms/res/plus.gif", Commands.ZOOM_IN, "Zoom In");
        zoomIn.setEnabled(false);
        zoomComponents.add(zoomIn);

        zoomBox = new ComboBoxWithID<>(ZOOMS, true, Commands.ZOOM);
        zoomBox.setEnabled(false);
        zoomBox.addActionListener(commandListener);
        zoomBox.setPreferredSize(new Dimension(80, 24));
        add(zoomBox);
        zoomComponents.add(zoomBox);

        final JButton zoomOut = addToolBarButton("/org/pdf/forms/res/minus.gif", Commands.ZOOM_OUT, "Zoom Out");
        zoomOut.setEnabled(false);
        zoomComponents.add(zoomOut);
    }

    private JButton addToolBarButton(
            final String url,
            final int command,
            final String tooltipText) {
        final ImageIcon imageIcon = new ImageIcon(url);
        final ButtonWithID button = new ButtonWithID(imageIcon, tooltipText, command);
        button.addActionListener(commandListener);
        add(button);

        return button;
    }

    public void setSaveState(final boolean state) {
        saveButton.setEnabled(state);
    }

    public void setZoomState(final boolean state) {
        for (final JComponent component: zoomComponents) {
            component.setEnabled(state);
        }
    }

    public void setState(final boolean state) {
        setSaveState(state);

        for (final JComponent component: documentComponents) {
            component.setEnabled(state);
        }
    }

    public double getCurrentSelectedScaling() {
        final String zoomValue = getZoomValue().replace("%", "");
        return Double.parseDouble(zoomValue);
    }

    private String getZoomValue() {
        final String selectedItem = (String) zoomBox.getSelectedItem();
        if (selectedItem == null) {
            return "100%";
        }
        return selectedItem;
    }

    public void setCurrentlySelectedScaling(final double scaling) {
        zoomBox.removeActionListener(commandListener);
        zoomBox.setSelectedItem(scaling + "%");
        zoomBox.addActionListener(commandListener);
    }
}
