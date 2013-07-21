/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * DesignerCompound.java
 * ---------------
 */
package org.pdf.forms.gui.designer.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import org.jpedal.PdfDecoder;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.toolbars.DesignNavigationToolbar;
import org.pdf.forms.gui.toolbars.NavigationToolbar;
import org.pdf.forms.gui.toolbars.PreviewNavigationToolbar;
import org.pdf.forms.writer.Writer;

public class DesignerCompound extends JTabbedPane implements Dockable, DesignNavigatable, PreviewNavigatable {

    public static final int DESIGNER = 0;
    public static final int PREVIEW = 1;

    DockKey key = new DockKey("Designer");

    NavigationToolbar designToolBar = new DesignNavigationToolbar(this);
    NavigationToolbar previewToolBar = new PreviewNavigationToolbar(this);

    private IMainFrame mainFrame;

    PdfDecoder decodePDF = new PdfDecoder();

    private int currentPdfPage = 1;
    private double previewScaling = 1;
    private String currentFile;

    public DesignerCompound(IDesigner designer,
                            Rule horizontalRuler, Rule verticalRuler, final IMainFrame mainFrame) {

        this.mainFrame = mainFrame;

        ToolBarContainer designerContainer = ToolBarContainer.createDefaultContainer(true, false, true, false, FlowLayout.CENTER);

        JScrollPane designerScrollPane = new JScrollPane((Component) designer,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        designerScrollPane.setColumnHeaderView(horizontalRuler);
        designerScrollPane.setRowHeaderView(verticalRuler);

        designerContainer.add(designerScrollPane, BorderLayout.CENTER);

        ToolBarPanel designToolBarPanel = designerContainer.getToolBarPanelAt(BorderLayout.SOUTH);
        designToolBarPanel.add(designToolBar, new ToolBarConstraints(0, 0));

        addTab("Designer", designerContainer);

        ToolBarContainer previewContainer = ToolBarContainer.createDefaultContainer(true, false, true, false, FlowLayout.CENTER);

        JScrollPane previewScrollPane = new JScrollPane(decodePDF,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //add a border and center
        decodePDF.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
        decodePDF.setInset(35, 35);

        previewContainer.add(previewScrollPane, BorderLayout.CENTER);

        ToolBarPanel previewToolBarPanel = previewContainer.getToolBarPanelAt(BorderLayout.SOUTH);
        previewToolBarPanel.add(previewToolBar, new ToolBarConstraints(0, 0));

        addTab("Preview", previewContainer);

        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (getSelectedIndex() == 0) {
                    mainFrame.setDesignerCompoundContent(DesignerCompound.DESIGNER);
                } else if (getSelectedIndex() == 1) {
                    mainFrame.setDesignerCompoundContent(DesignerCompound.PREVIEW);
                    decodePDF.closePdfFile();

                    try {
                        File file = File.createTempFile("pdfdesigner", ".pdf");
                        file.deleteOnExit();

                        int noOfPages = mainFrame.getTotalNoOfPages();
                        List[] widgets = new ArrayList[noOfPages];

                        FormsDocument documentProperties = mainFrame.getFormsDocument();

                        for (int i = 0; i < noOfPages; i++) {
                            widgets[i] = documentProperties.getPage(i + 1).getWidgets();
                        }

                        Writer writer = new Writer(mainFrame);
                        writer.write(file, widgets, documentProperties.getDocumentProperties());

                        Set fontSubstitutions = writer.getFontSubstitutions();
                        if (!fontSubstitutions.isEmpty()) {
                            String message = "<html>The following fonts cannot be embedded due to licensing<br/" +
                                    "restrictions, so they have been substituted with Helvetica.<br/<br/";

                            for (Iterator it = fontSubstitutions.iterator(); it.hasNext(); ) {
                                String font = (String) it.next();
                                message += font + "<br/";
                            }

                            JOptionPane.showMessageDialog((Component) mainFrame, message);
                        }

                        currentFile = file.getAbsolutePath();
                        decodePDF.openPdfFile(currentFile);

                        decodePDF.setPageParameters((float) previewScaling, 1);
                        decodePDF.decodePage(1);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    previewToolBar.setCurrentPage(currentPdfPage = 1);
                    previewToolBar.setTotalNoOfPages(decodePDF.getPageCount());
                }
            }
        });
    }

    public void closePdfDecoderFile() {
        decodePDF.closePdfFile();
    }

    public void setCurrentDesignerPage(int currentPage) {
        designToolBar.setCurrentPage(currentPage);
    }

    public void setState(boolean state) {
        setEnabled(state);
    }

    public void setTotalNoOfDisplayedPages(int totalNoOfDisplayedPages) {
        designToolBar.setTotalNoOfPages(totalNoOfDisplayedPages);
    }

    public void displayDesignerPage(int page) {
        mainFrame.displayPage(page);
    }

    /// Methods to control designer navigation
    public int getDesignerCurrentPage() {
        return mainFrame.getCurrentPage();
    }

    /// Methods to control preview navigation
    public void displayPreviewPage(int page) {
        try {

            if (page > 0 && page <= decodePDF.getPageCount()) {

                decodePDF.setPageParameters(1, page);
                decodePDF.decodePage(page);

                currentPdfPage = page;

                previewToolBar.setCurrentPage(page);
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        updateUI();

    }

    public int getPreviewCurrentPage() {
        return currentPdfPage;
    }

    public int getTotalNoOfPages() {
        return mainFrame.getTotalNoOfPages();
    }

    public DockKey getDockKey() {
        key.setResizeWeight(1f);
        return key;
    }

    public Component getComponent() {
        return this;
    }

    public void previewZoom(double scaling) {
        previewScaling = scaling;

//		decodePDF.setPageParameters((float) previewScaling, currentPdfPage);
//		decodePDF.invalidate();
//		decodePDF.updateUI();
//		repaint();

        try {
            decodePDF.closePdfFile();
            decodePDF.openPdfFile(currentFile);

            decodePDF.setPageParameters((float) previewScaling, currentPdfPage);
            decodePDF.decodePage(currentPdfPage);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public double getCurrentScaling() {
        // TODO Auto-generated method stub
        return previewScaling;
    }
}
