package org.pdf.forms.gui.toolbars;

import javax.swing.*;

import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.components.ButtonWithID;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class ReportToolbar extends VLToolBar {

    private final ButtonWithID bugReportButton;

    public ReportToolbar(final CommandListener commandListener) {
        this.bugReportButton = createBugReportButton(commandListener);

        add(bugReportButton);
    }

    private ButtonWithID createBugReportButton(final CommandListener commandListener) {
        final ButtonWithID button = new ButtonWithID(
                "Email bug report",
                new ImageIcon("/org/pdf/forms/res/email.png"),
                "Email bug report",
                Commands.BUG_REPORT
        );
        button.addActionListener(commandListener);
        return button;
    }

    public void setState(final boolean enabled) {
        bugReportButton.setEnabled(enabled);
    }
}
