package org.pdf.forms.gui.toolbars;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.pdf.forms.gui.commands.CommandListener;

import com.vlsolutions.swing.toolbars.VLToolBar;

public class ReportToolbar extends VLToolBar {

    private final List<JButton> buttonsList = new ArrayList<>();

    public ReportToolbar(final CommandListener commandListener) {

        //     SwingButton button = new SwingButton("Email bug report");
        //        button.init("/org/pdf/forms/res/email.png", Commands.BUGREPORT, "Email bug report");
        //        button.addActionListener(commandListener);
        //        add(button);

        //        return button;

        //     JButton button = new JButton("Email bug report");
        //     button.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/simpleviewer/res/start.gif")));
        //     button.setToolTipText("Email bug report");
        //     button.addActionListener(new ActionListener() {
        //      public void actionPerformed(ActionEvent e) {
        //       WidgetAlignmentAndOrder.alignAndOrder(designer, type);
        //      }
        //     });

        //     buttonsList.add(button);

        //     add(button);
    }

    public void setState(final boolean enabled) {
        for (final JButton button : buttonsList) {
            button.setEnabled(enabled);
        }
    }
}
