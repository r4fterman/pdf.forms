package org.pdf.forms.gui.toolbars;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.jpedal.examples.simpleviewer.gui.swing.SwingButton;
import org.jpedal.examples.simpleviewer.gui.swing.SwingCombo;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class DocumentToolBar extends VLToolBar {

    private final CommandListener commandListener;

    private final JButton saveButton;

    private final List<JComponent> zoomComponents = new ArrayList<>();
    private final List<JComponent> documentComponents = new ArrayList<>();

    private final SwingCombo zoomBox;

    public DocumentToolBar(final CommandListener commandListener) {
        this.commandListener = commandListener;

        documentComponents.add(addToolBarButton("/org/pdf/forms/res/New.gif", Commands.NEW, "New"));
        documentComponents.add(addToolBarButton("/org/pdf/forms/res/open.gif", Commands.OPEN, "Open"));

        saveButton = addToolBarButton("/org/pdf/forms/res/save.gif", Commands.SAVE_FILE, "Save");

        addSeparator();

        final JButton zoomIn = addToolBarButton("/org/pdf/forms/res/plus.gif", Commands.ZOOM_IN, "Zoom In");
        zoomIn.setEnabled(false);
        zoomComponents.add(zoomIn);

        zoomBox = new SwingCombo(new String[] {
                "100%",
                "75%",
                "50%",
                "25%"
        });
        zoomBox.setEditable(true);
        zoomBox.setID(Commands.ZOOM);
        zoomBox.addActionListener(commandListener);
        zoomBox.setPreferredSize(new Dimension(80, 24));
        add(zoomBox);
        zoomBox.setEnabled(false);
        zoomComponents.add(zoomBox);

        final JButton zoomOut = addToolBarButton("/org/pdf/forms/res/minus.gif", Commands.ZOOM_OUT, "Zoom Out");
        zoomOut.setEnabled(false);
        zoomComponents.add(zoomOut);
    }

    private JButton addToolBarButton(
            final String url,
            final int command,
            final String toolTip) {
        final SwingButton button = new SwingButton();
        button.init(url, command, toolTip);
        button.addActionListener(commandListener);
        add(button);

        return button;
    }

    public void setSaveState(final boolean state) {
        saveButton.setEnabled(state);
    }

    public void setZoomState(final boolean state) {
        for (final JComponent component : zoomComponents) {
            component.setEnabled(state);
        }
    }

    public void setState(final boolean state) {
        setSaveState(state);

        for (final JComponent component : documentComponents) {
            component.setEnabled(state);
        }
    }

    public double getCurrentSelectedScaling() {
        final String zoomValue = getZoomValue().replaceAll("%", "");
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
