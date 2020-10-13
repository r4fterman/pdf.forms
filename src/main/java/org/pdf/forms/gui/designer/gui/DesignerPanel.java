package org.pdf.forms.gui.designer.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import org.jpedal.PdfDecoder;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.toolbars.DesignNavigationToolbar;
import org.pdf.forms.gui.toolbars.NavigationToolbar;
import org.pdf.forms.gui.toolbars.PreviewNavigationToolbar;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.writer.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignerPanel extends JTabbedPane implements Dockable, DesignNavigatable, PreviewNavigatable {

    public static final int DESIGNER = 0;
    public static final int PREVIEW = 1;

    private final Logger logger = LoggerFactory.getLogger(DesignerPanel.class);

    private final DockKey key = new DockKey("Designer");

    private final NavigationToolbar designToolBar = new DesignNavigationToolbar(this);
    private final NavigationToolbar previewToolBar = new PreviewNavigationToolbar(this);

    private final PdfDecoder decodePDF = new PdfDecoder();
    private final IMainFrame mainFrame;
    private final FontHandler fontHandler;

    private int currentPdfPage = 1;
    private double previewScaling = 1;
    private String currentFile;

    public DesignerPanel(
            final IDesigner designer,
            final Rule horizontalRuler,
            final Rule verticalRuler,
            final IMainFrame mainFrame,
            final FontHandler fontHandler) {
        this.mainFrame = mainFrame;
        this.fontHandler = fontHandler;

        final ToolBarContainer designerContainer = ToolBarContainer.createDefaultContainer(true, false, true, false, FlowLayout.CENTER);

        final JScrollPane designerScrollPane = new JScrollPane((Component) designer,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        designerScrollPane.setColumnHeaderView(horizontalRuler);
        designerScrollPane.setRowHeaderView(verticalRuler);

        designerContainer.add(designerScrollPane, BorderLayout.CENTER);

        final ToolBarPanel designToolBarPanel = designerContainer.getToolBarPanelAt(BorderLayout.SOUTH);
        designToolBarPanel.add(designToolBar, new ToolBarConstraints(0, 0));

        addTab("Designer", designerContainer);

        final ToolBarContainer previewContainer = ToolBarContainer.createDefaultContainer(true, false, true, false, FlowLayout.CENTER);

        final JScrollPane previewScrollPane = new JScrollPane(decodePDF,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //add a border and center
        decodePDF.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
        decodePDF.setInset(35, 35);

        previewContainer.add(previewScrollPane, BorderLayout.CENTER);

        final ToolBarPanel previewToolBarPanel = previewContainer.getToolBarPanelAt(BorderLayout.SOUTH);
        previewToolBarPanel.add(previewToolBar, new ToolBarConstraints(0, 0));

        addTab("Preview", previewContainer);

        addChangeListener(event -> {
            if (getSelectedIndex() == 0) {
                mainFrame.setDesignerCompoundContent(DesignerPanel.DESIGNER);
            } else if (getSelectedIndex() == 1) {
                mainFrame.setDesignerCompoundContent(DesignerPanel.PREVIEW);
                decodePDF.closePdfFile();

                try {
                    final File previewPDF = writePreviewPDF(mainFrame);

                    currentFile = previewPDF.getAbsolutePath();
                    decodePDF.openPdfFile(currentFile);

                    decodePDF.setPageParameters((float) previewScaling, 1);
                    decodePDF.decodePage(1);
                } catch (final Exception e) {
                    logger.error("Error decoding PDF {}", currentFile, e);
                }

                this.currentPdfPage = 1;
                previewToolBar.setCurrentPage(currentPdfPage);
                previewToolBar.setTotalNoOfPages(decodePDF.getPageCount());
            }
        });
    }

    private File writePreviewPDF(final IMainFrame mainFrame) throws IOException {
        final File file = File.createTempFile("pdfdesigner", ".pdf");
        file.deleteOnExit();

        final int numberOfPages = mainFrame.getTotalNoOfPages();
        final ImmutableMap.Builder<Integer, List<IWidget>> widgets = ImmutableMap.builder();

        final FormsDocument documentProperties = mainFrame.getFormsDocument();

        for (int pageNumber = 0; pageNumber < numberOfPages; pageNumber++) {
            widgets.put(pageNumber, documentProperties.getPage(pageNumber + 1).getWidgets());
        }

        final Writer writer = new Writer(mainFrame, fontHandler);
        writer.write(file, widgets.build(), documentProperties.getDocumentProperties());

        final Set<String> fontSubstitutions = writer.getFontSubstitutions();
        if (!fontSubstitutions.isEmpty()) {
            final StringBuilder builder = new StringBuilder("<html>The following fonts cannot be embedded due to licensing<br/>restrictions, so they have been substituted with Helvetica.<br/<br/");

            for (final String font : fontSubstitutions) {
                builder.append(font).append("<br/");
            }

            JOptionPane.showMessageDialog((Component) mainFrame, builder.toString());
        }
        return file;
    }

    public void closePdfDecoderFile() {
        decodePDF.closePdfFile();
    }

    public void setCurrentDesignerPage(final int currentPage) {
        designToolBar.setCurrentPage(currentPage);
    }

    public void setState(final boolean state) {
        setEnabled(state);
    }

    public void setTotalNoOfDisplayedPages(final int totalNoOfDisplayedPages) {
        designToolBar.setTotalNoOfPages(totalNoOfDisplayedPages);
    }

    @Override
    public void displayDesignerPage(final int page) {
        mainFrame.displayPage(page);
    }

    /// Methods to control designer navigation
    @Override
    public int getDesignerCurrentPage() {
        return mainFrame.getCurrentPage();
    }

    /// Methods to control preview navigation
    @Override
    public void displayPreviewPage(final int page) {
        try {
            if (page > 0 && page <= decodePDF.getPageCount()) {
                decodePDF.setPageParameters(1, page);
                decodePDF.decodePage(page);

                currentPdfPage = page;

                previewToolBar.setCurrentPage(page);
            }
        } catch (final Exception e) {
            logger.error("Error displaying preview page", e);
        }

        updateUI();
    }

    @Override
    public int getPreviewCurrentPage() {
        return currentPdfPage;
    }

    @Override
    public int getTotalNoOfPages() {
        return mainFrame.getTotalNoOfPages();
    }

    @Override
    public DockKey getDockKey() {
        key.setResizeWeight(1f);
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    public void previewZoom(final double scaling) {
        previewScaling = scaling;

        //  decodePDF.setPageParameters((float) previewScaling, currentPdfPage);
        //  decodePDF.invalidate();
        //  decodePDF.updateUI();
        //  repaint();

        try {
            decodePDF.closePdfFile();
            decodePDF.openPdfFile(currentFile);

            decodePDF.setPageParameters((float) previewScaling, currentPdfPage);
            decodePDF.decodePage(currentPdfPage);
        } catch (final Exception e) {
            logger.error("Error during preview zoom", e);
        }
    }

    public double getCurrentScaling() {
        return previewScaling;
    }
}
