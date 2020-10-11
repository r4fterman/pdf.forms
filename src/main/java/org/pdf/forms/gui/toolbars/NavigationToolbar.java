package org.pdf.forms.gui.toolbars;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.vlsolutions.swing.toolbars.VLToolBar;

public abstract class NavigationToolbar extends VLToolBar {

    static final int FIRSTPAGE = 0;
    static final int FBACKPAGE = 1;
    static final int BACKPAGE = 2;
    static final int FORWARDPAGE = 3;
    static final int FFORWARDPAGE = 4;
    static final int LASTPAGE = 5;
    static final int SETPAGE = 6;

    private final JLabel totalNoOfPages = new JLabel();
    private final JTextField currentPageBox = new JTextField(4);

    NavigationToolbar() {
        totalNoOfPages.setText("of 1");
        currentPageBox.setText("1");

        add(Box.createHorizontalGlue());

        addButton("Rewind To Start", "/org/jpedal/examples/simpleviewer/res/start.gif", FIRSTPAGE);
        addButton("Back 10 Pages", "/org/jpedal/examples/simpleviewer/res/fback.gif", FBACKPAGE);
        addButton("Back", "/org/jpedal/examples/simpleviewer/res/back.gif", BACKPAGE);

        add(new JLabel("Page"));
        currentPageBox.setMaximumSize(new Dimension(5, 50));
        currentPageBox.addActionListener(actionEvent -> executeCommand(SETPAGE));
        add(currentPageBox);
        add(totalNoOfPages);

        addButton("Forward", "/org/jpedal/examples/simpleviewer/res/forward.gif", FORWARDPAGE);
        addButton("Forward 10 Pages", "/org/jpedal/examples/simpleviewer/res/fforward.gif", FFORWARDPAGE);
        addButton("Fast Forward To End", "/org/jpedal/examples/simpleviewer/res/end.gif", LASTPAGE);

        add(Box.createHorizontalGlue());

    }

    public abstract void executeCommand(int type);

    public void setTotalNoOfPages(final int noOfPages) {
        totalNoOfPages.setText("of " + noOfPages);
    }

    public void setCurrentPage(final int currentPage) {
        currentPageBox.setText(currentPage + "");
    }

    public void close() {
        totalNoOfPages.setText("");
        currentPageBox.setText("");

        setComponentsEnabled(false);
    }

    public void open() {
        setComponentsEnabled(true);
    }

    private void setComponentsEnabled(final boolean enabled) {
        final Component[] components = getComponents();

        for (int i = 0; i < components.length; i++) {
            final Component component = getComponents()[i];
            component.setEnabled(enabled);
        }
    }

    private void addButton(
            final String tooltip,
            final String url,
            final int type) {
        final JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource(url)));
        button.setToolTipText(tooltip);
        button.addActionListener(actionEvent -> executeCommand(type));

        add(button);
    }

    JTextField getCurrentPageBox() {
        return currentPageBox;
    }
}
