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
 * MainFrame.java
 * ---------------
 */
package org.pdf.forms.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

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

    private LibraryPanel palette;

    private FontPropertiesTab properties;

    public MainFrame() {
        Rule horizontalRuler = new Rule(INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        Rule verticalRuler = new Rule(INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        IDesigner designerPanel = new Designer(INSET, horizontalRuler, verticalRuler, this);
//		designerPanel.addMouseListener(new DesignerMouseListener(designerPanel));
//		designerPanel.addMouseMotionListener(new DesignerMouseMotionListener(designerPanel));

        JScrollPane scroll = new JScrollPane();

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setViewportView((Component) designerPanel);
        scroll.setColumnHeaderView(horizontalRuler);
        scroll.setRowHeaderView(verticalRuler);

        palette = new LibraryPanel(designerPanel);

        properties = new FontPropertiesTab(designerPanel);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, palette, properties);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, rightSplit);
        mainSplit.setResizeWeight(1);
        mainSplit.setDividerLocation(350);
        getContentPane().add(mainSplit, BorderLayout.CENTER);

//		JButton save = new JButton("Save");
//		save.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser chooser = new JFileChooser();
//				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//				
//				int approved=chooser.showSaveDialog(null);
//				if(approved==JFileChooser.APPROVE_OPTION){
//					
//					File file = chooser.getSelectedFile();
//					
//					ArrayList widgets = designerPanel.getWidgets();
//					
//					Writer writer = new Writer();
//					writer.write(widgets,file);
//				}
//			}
//		});

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(640, 480));
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainFrame();
    }

    public void resetPaletteButtons() {
        palette.resetButtons();
    }

    public void setPropertiesCompound(Set widget) {
        properties.setProperties(widget);
    }

    public void setPropertiesToolBar(Set widgets) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addWidgetToHierarchy(IWidget widget) {
    }

    public void removeWidgetFromHierarchy(IWidget widget) {
    }

    public void displayPage(int page) {
    }

    public int getTotalNoOfPages() {
        return 0;
    }

    public int getCurrentPage() {
        return 0;
    }

    public void setFormsDocument(FormsDocument formsDocument) {
    }

    public IDesigner getDesigner() {
        return null;
    }

    public void setCurrentDesignerFileName(String currentFileName) {
    }

    public String getCurrentDesignerFileName() {
        return null;
    }

    public FormsDocument getFormsDocument() {
        return null;
    }

    public void setCurrentPage(int currentPage) {
    }

    public void setPanelsState(boolean state) {
    }

    public void setTotalNoOfDisplayedPages(int totalNoOfDisplayedPages) {
    }

    public void addPageToHierarchyPanel(int pdfPage, Page newPage) {
    }

    public void updateHierarchyPanelUI(IWidget widget, String name) {
    }

    public void removePageFromHierarchyPanel(int index) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateHierarchy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDockableVisible(String dockable, boolean visible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setScaling(double scaling) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getScaling() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setBorderVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setFontVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setHierarchyVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setJavaScriptEditorVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setLayoutVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setLibraryVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setObjectVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public void setParagraphVisible(boolean visible) {
        // TODO Auto-generated method stub

    }

    public int getDesignerCompoundContent() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setDesignerCompoundContent(int content) {
        // TODO Auto-generated method stub

    }

    public DesignerCompound getDesignerCompound() {
        // TODO Auto-generated method stub
        return null;
    }

    public double getCurrentSelectedScaling() {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getCurrentScaling() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setCurrentSelectedScaling(double scaling) {
        // TODO Auto-generated method stub

    }

    public void updateAvailiableFonts() {
        // TODO Auto-generated method stub

    }

    public void addWidgetToPage(IWidget widget) {
        // TODO Auto-generated method stub

    }

    public int getNextArrayNumberForName(String name, IWidget widget) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleArrayNumberOnWidgetDeletion(Set selectedWidgets) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public WidgetArrays getWidgetArrays() {
        // TODO Auto-generated method stub
        return null;
    }

    public void renameWidget(String oldName, String name, IWidget widget) {
        // TODO Auto-generated method stub

    }

    public void updateHierarchyPanelUI() {
        // TODO Auto-generated method stub

    }

}