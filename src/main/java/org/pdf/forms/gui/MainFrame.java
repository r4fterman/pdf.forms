/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * MainFrame.java
 * ---------------
 */
package org.pdf.forms.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.Designer;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.library.LibraryPanel;
import org.pdf.forms.gui.properties.font.FontPropertiesTab;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetArrays;

public class MainFrame extends JFrame implements IMainFrame {

    private static final int INSET = 15;

    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            e.printStackTrace();
        }

        new MainFrame("TEST");
    }

    private final LibraryPanel palette;

    private final FontPropertiesTab properties;

    public MainFrame(final String version) {
        final Rule horizontalRuler = new Rule(INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        final Rule verticalRuler = new Rule(INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        final IDesigner designerPanel = new Designer(INSET, horizontalRuler, verticalRuler, this, version);
        //		designerPanel.addMouseListener(new DesignerMouseListener(designerPanel));
        //		designerPanel.addMouseMotionListener(new DesignerMouseMotionListener(designerPanel));

        final JScrollPane scroll = new JScrollPane();

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setViewportView((Component) designerPanel);
        scroll.setColumnHeaderView(horizontalRuler);
        scroll.setRowHeaderView(verticalRuler);

        palette = new LibraryPanel(designerPanel);

        properties = new FontPropertiesTab(designerPanel);

        final JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, palette, properties);

        final JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, rightSplit);
        mainSplit.setResizeWeight(1);
        mainSplit.setDividerLocation(350);
        getContentPane().add(mainSplit, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(640, 480));
        setVisible(true);
    }

    @Override
    public void resetPaletteButtons() {
        palette.resetButtons();
    }

    @Override
    public void setPropertiesCompound(final Set<IWidget> widget) {
        properties.setProperties(widget);
    }

    @Override
    public void setPropertiesToolBar(final Set<IWidget> widgets) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addWidgetToHierarchy(final IWidget widget) {
    }

    @Override
    public void removeWidgetFromHierarchy(final IWidget widget) {
    }

    @Override
    public void displayPage(final int page) {
    }

    @Override
    public int getTotalNoOfPages() {
        return 0;
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }

    @Override
    public void setFormsDocument(final FormsDocument formsDocument) {
    }

    @Override
    public IDesigner getDesigner() {
        return null;
    }

    @Override
    public void setCurrentDesignerFileName(final String currentFileName) {
    }

    @Override
    public String getCurrentDesignerFileName() {
        return null;
    }

    @Override
    public FormsDocument getFormsDocument() {
        return null;
    }

    @Override
    public void setCurrentPage(final int currentPage) {
    }

    @Override
    public void setPanelsState(final boolean state) {
    }

    @Override
    public void setTotalNoOfDisplayedPages(final int totalNoOfDisplayedPages) {
    }

    @Override
    public void addPageToHierarchyPanel(
            final int pdfPage,
            final Page newPage) {
    }

    public void updateHierarchyPanelUI(
            final IWidget widget,
            final String name) {
    }

    @Override
    public void removePageFromHierarchyPanel(final int index) {
    }

    @Override
    public void updateHierarchy() {
    }

    @Override
    public void setDockableVisible(
            final String dockable,
            final boolean visible) {
    }

    public void setScaling(final double scaling) {
    }

    public double getScaling() {
        return 0;
    }

    public void setBorderVisible(final boolean visible) {

    }

    public void setFontVisible(final boolean visible) {
    }

    public void setHierarchyVisible(final boolean visible) {
    }

    public void setJavaScriptEditorVisible(final boolean visible) {
    }

    public void setLayoutVisible(final boolean visible) {
    }

    public void setLibraryVisible(final boolean visible) {
    }

    public void setObjectVisible(final boolean visible) {
    }

    public void setParagraphVisible(final boolean visible) {
    }

    @Override
    public int getDesignerCompoundContent() {
        return 0;
    }

    @Override
    public void setDesignerCompoundContent(final int content) {
    }

    @Override
    public DesignerCompound getDesignerCompound() {
        return null;
    }

    @Override
    public double getCurrentSelectedScaling() {
        return 0;
    }

    @Override
    public double getCurrentScaling() {
        return 0;
    }

    @Override
    public void setCurrentSelectedScaling(final double scaling) {
    }

    @Override
    public void updateAvailiableFonts() {
    }

    @Override
    public void addWidgetToPage(final IWidget widget) {
    }

    @Override
    public int getNextArrayNumberForName(
            final String name,
            final IWidget widget) {
        return 0;
    }

    @Override
    public void handleArrayNumberOnWidgetDeletion(final Set selectedWidgets) {
    }

    @Override
    public WidgetArrays getWidgetArrays() {
        return null;
    }

    @Override
    public void renameWidget(
            final String oldName,
            final String name,
            final IWidget widget) {
    }

    @Override
    public void updateHierarchyPanelUI() {
    }

}
