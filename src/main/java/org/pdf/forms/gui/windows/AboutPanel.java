package org.pdf.forms.gui.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.commands.VisitWebsiteCommand;

public class AboutPanel extends JPanel {

    private static final String ORIGINAL_JPEDAL_PAGE = "http://www.jpedal.org";
    private static final String COMMERCIAL_JPEDAL_PAGE = "https://www.idrsolutions.com/jpedal/";

    private static final String ORIGINAL_VLDOCKING_PAGE = "http://www.vlsolutions.com/en/products/docking/";
    private static final String GITHUB_VLDOCKING_PAGE = "https://github.com/cmadsen/vldocking";

    private static final String ORIGINAL_ITEXT_PAGE = "http://www.lowagie.com/iText/";
    private static final String COMMERCIAL_ITEXT_PAGE = "https://itextpdf.com/";

    private static final String ORIGINAL_SWING_LAYOUT_PAGE = "https://swing-layout.dev.java.net/";

    private static final Map<String, String> USED_LIBRARIES = Map.of(
            "JPedal", COMMERCIAL_JPEDAL_PAGE,
            "VLDocking", GITHUB_VLDOCKING_PAGE,
            "iText", COMMERCIAL_ITEXT_PAGE,
            "Swing Layout", ORIGINAL_SWING_LAYOUT_PAGE
    );

    private final Font serifFont12 = new Font("Serif", Font.PLAIN, 12);

    public AboutPanel() {
        initComponents();
    }

    private void initComponents() {
        final JLabel projectPageLabel = createUrlLabel(this, VisitWebsiteCommand.GITHUB_PROJECT_PAGE);
        projectPageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JLabel pdfFormDesignerLabel = new JLabel("PDF Forms Designer");
        pdfFormDesignerLabel.setFont(new Font("Serif", Font.BOLD, 14));
        pdfFormDesignerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JLabel usedLibrariesLabel = createLabel("Used Libraries:");

        final JLabel jpedalLabel = createLabel("JPedal - ");
        final JLabel jpedalUrlLabel = createUrlLabel(this, COMMERCIAL_JPEDAL_PAGE);

        final JLabel vldockingLabel = createLabel("VLDocking - ");
        final JLabel vldockingUrlLabel = createUrlLabel(this, GITHUB_VLDOCKING_PAGE);

        final JLabel iTextLabel = createLabel("iText - ");
        final JLabel iTextUrlLabel = createUrlLabel(this, COMMERCIAL_ITEXT_PAGE);

        final JLabel swingLayoutLabel = createLabel("Swing Layout -");
        final JLabel swingLayoutUrlLabel = createUrlLabel(this, ORIGINAL_SWING_LAYOUT_PAGE);

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        final GroupLayout.ParallelGroup usedLibrariesGroup = layout.createParallelGroup(GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                        .add(vldockingLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(vldockingUrlLabel))
                .add(layout.createSequentialGroup()
                        .add(jpedalLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(jpedalUrlLabel))
                .add(layout.createSequentialGroup()
                        .add(iTextLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(iTextUrlLabel))
                .add(layout.createSequentialGroup()
                        .add(swingLayoutLabel)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(swingLayoutUrlLabel));

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(projectPageLabel, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .add(pdfFormDesignerLabel, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .add(usedLibrariesLabel)
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(usedLibrariesGroup)))
                                .addContainerGap())
        );

        final GroupLayout.SequentialGroup topGroup = layout.createSequentialGroup()
                .addContainerGap()
                .add(pdfFormDesignerLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(projectPageLabel)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(usedLibrariesLabel)
                .addPreferredGap(LayoutStyle.RELATED);

        final GroupLayout.SequentialGroup usedLibrariesSequentialGroup = topGroup
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(jpedalLabel)
                        .add(jpedalUrlLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(vldockingLabel)
                        .add(vldockingUrlLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(iTextLabel)
                        .add(iTextUrlLabel))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(swingLayoutLabel)
                        .add(swingLayoutUrlLabel));

        final GroupLayout.SequentialGroup sequentialGroup = usedLibrariesSequentialGroup
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(sequentialGroup));
    }

    private JLabel createLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setFont(serifFont12);
        return label;
    }

    private JLabel createUrlLabel(
            final JPanel details,
            final String linkAddress) {
        final String htmlCenteredLink = "<html><p>" + linkAddress + "</p></html>";
        final JLabel label = new JLabel(htmlCenteredLink);
        label.setFont(serifFont12);
        label.setForeground(Color.blue);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent event) {
                details.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setText("<html><a href=" + linkAddress + ">" + linkAddress + "</a></html>");
            }

            @Override
            public void mouseExited(final MouseEvent event) {
                details.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                label.setText(htmlCenteredLink);
            }

            @Override
            public void mouseClicked(final MouseEvent event) {
                VisitWebsiteCommand.openWebpage(linkAddress);
            }
        });
        return label;
    }

}
