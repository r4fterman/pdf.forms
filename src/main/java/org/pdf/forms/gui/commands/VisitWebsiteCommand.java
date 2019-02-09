package org.pdf.forms.gui.commands;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.jpedal.utils.BrowserLauncher;
import org.pdf.forms.gui.IMainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VisitWebsiteCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(VisitWebsiteCommand.class);
    private final IMainFrame mainFrame;

    VisitWebsiteCommand(final IMainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void execute() {
        visitWebsite();
    }

    private void visitWebsite() {
        try {
            BrowserLauncher.openURL("http://pdfformsdesigne.sourceforge.net");
        } catch (final IOException e) {
            logger.error("Error loading webpage", e);
            JOptionPane.showMessageDialog(null, "Error loading webpage");
        }

        //      JMenuItem about = new JMenuItem("About");
        //      about.addActionListener(new ActionListener() {
        //          public void actionPerformed(ActionEvent actionEvent) {
        //   JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
        //          }
        //      });
        //      help.add(visitWebSite);
        //      help.add(about);
        //
        //      menubar.add(help);

    }

}
