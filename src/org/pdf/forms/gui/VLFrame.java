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
 * VLFrame.java
 * ---------------
 */
package org.pdf.forms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeEvent;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeListener;
import com.vlsolutions.swing.docking.event.DockingActionCloseEvent;
import com.vlsolutions.swing.docking.event.DockingActionEvent;
import com.vlsolutions.swing.docking.event.DockingActionListener;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.designer.Designer;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.editor.JavaScriptEditorPanel;
import org.pdf.forms.gui.hierarchy.HierarchyPanel;
import org.pdf.forms.gui.library.LibraryPanel;
import org.pdf.forms.gui.properties.PropertiesCompound;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.gui.properties.border.BorderPropertiesTab;
import org.pdf.forms.gui.properties.font.FontPropertiesTab;
import org.pdf.forms.gui.properties.layout.LayoutPropertiesTab;
import org.pdf.forms.gui.properties.object.ObjectPropertiesTab;
import org.pdf.forms.gui.properties.paragraph.ParagraphPropertiesTab;
import org.pdf.forms.gui.toolbars.DocumentToolBar;
import org.pdf.forms.gui.toolbars.ReportToolbar;
import org.pdf.forms.gui.toolbars.WidgetAlignmentAndOrderToolbar;
import org.pdf.forms.gui.toolbars.WidgetPropertiesToolBar;
import org.pdf.forms.gui.windows.SplashWindow;
import org.pdf.forms.utils.configuration.MenuConfiguration;
import org.pdf.forms.utils.configuration.WindowConfiguration;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetArrays;

public class VLFrame extends JFrame implements IMainFrame {

    private IDesigner designer;

    private FormsDocument formsDocument = new FormsDocument();

    private JavaScriptEditorPanel javaScriptEditor = new JavaScriptEditorPanel();

    private LibraryPanel libraryPanel;
    private HierarchyPanel hierarchyPanel;

    private PropertiesCompound propertiesCompound;
    private DesignerCompound designerCompound;

    private ParagraphPropertiesTab paragraphPropertiesTab;
    private BorderPropertiesTab borderPropertiesTab;
    private LayoutPropertiesTab layoutPropertiesTab;
    private ObjectPropertiesTab objectPropertiesTab;
    private FontPropertiesTab fontPropertiesTab;

    private Map dockableNames = new HashMap();

    private int currentPage = 0;
    private String currentDesignerFileName = "Untitled";
    private Commands commands = new Commands(this);
    private CommandListener commandListener = new CommandListener(commands);

    private DocumentToolBar documentToolBar;
    private WidgetPropertiesToolBar propertiesToolBar;
    private WidgetAlignmentAndOrderToolbar widgetAlignmentAndOrderToolbar;
    private ReportToolbar reportToolBar;

//    private FileMenu fileMenu = new FileMenu(commandListener);
//    private InsertMenu insertMenu = new InsertMenu(commandListener);
//    private LayoutMenu layoutMenu;
//    private WindowMenu windowMenu;

    private WidgetArrays widgetArrays = new WidgetArrays();

    IMainFrame mainFrame = this;

    private MenuConfiguration menuConfiguration;
    private WindowConfiguration windowConfiguration;

    // the desktop (which will contain dockables)
    DockingDesktop desk = new DockingDesktop();
    // byte array used to save a workspace (custom layout of dockables)
    byte[] savedWorkpace;

    // action used to save the current workspace
    Action saveWorkspaceAction = new AbstractAction("Save Workspace") {
        public void actionPerformed(ActionEvent e) {
            saveWorkspace();
        }
    };

    // action used to reload a workspace
    Action loadWorkspaceAction = new AbstractAction("Reload Workspace") {
        public void actionPerformed(ActionEvent e) {
            loadWorkspace();
        }
    };
    private ToolBarContainer toolbarContainer;

    private int designerCompoundContent = DesignerCompound.DESIGNER;

    /**
     * Default and only frame constructor
     *
     * @param splashWindow
     */
    public VLFrame(SplashWindow splashWindow) {

        addWindowListener(new FrameCloser());

        toolbarContainer = ToolBarContainer.createDefaultContainer(true, false, true, false);

        // insert our desktop as the only one component of the frame
        toolbarContainer.add(desk, BorderLayout.CENTER);

        /** setup the rulers*/
        Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        designer = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, this);
        DefaultTransferHandler dth = new DefaultTransferHandler(commands, designer);
        designer.setTransferHandler(dth);

        menuConfiguration = new MenuConfiguration(commandListener, designer, mainFrame);
        windowConfiguration = new WindowConfiguration();

        libraryPanel = new LibraryPanel(designer);
        hierarchyPanel = new HierarchyPanel(designer);

        desk.addDockingActionListener(new DockingActionListener() {
            public boolean acceptDockingAction(DockingActionEvent arg0) {
//                if (arg0.getActionType() == DockingActionEvent.ACTION_CLOSE) {
//                    DockingActionCloseEvent closeAction = (DockingActionCloseEvent) arg0;
//                    windowMenu.setDockableVisible(getNameFromDockable(closeAction.getDockable()), false);
//                    System.out.println(">> "+desk.getDockableState(javaScriptEditor).getPosition().getRelativeAncestorContainer());
//                    System.out.println(desk.getDockableState(closeAction.getDockable()).getPosition().getRelativeAncestorContainer());
//                }
                return true;
            }

            public void dockingActionPerformed(DockingActionEvent arg0) {
                if (arg0.getActionType() == DockingActionEvent.ACTION_CLOSE) {
                    DockingActionCloseEvent closeAction = (DockingActionCloseEvent) arg0;
                    Dockable dockable = closeAction.getDockable();
//                    if(dockable == propertiesCompound){
//                        windowMenu.setDockableVisible(getNameFromDockable(fontPropertiesTab), false);
//                        windowMenu.setDockableVisible(getNameFromDockable(paragraphPropertiesTab), false);
//                        windowMenu.setDockableVisible(getNameFromDockable(layoutPropertiesTab), false);
//                        windowMenu.setDockableVisible(getNameFromDockable(objectPropertiesTab), false);
//                        windowMenu.setDockableVisible(getNameFromDockable(borderPropertiesTab), false);
//                    }
                    menuConfiguration.setDockableVisible(getNameFromDockable(dockable), false);  //@old
                }
            }
        });

        splashWindow.setProgress(2, "Setting up designer panel");
        /** setup the designerTabs */
        designerCompound = new DesignerCompound(designer, horizontalRuler, verticalRuler, this);

        splashWindow.setProgress(3, "Setting up properties panels");
        paragraphPropertiesTab = new ParagraphPropertiesTab(designer);
        borderPropertiesTab = new BorderPropertiesTab(designer);
        layoutPropertiesTab = new LayoutPropertiesTab(designer);
        objectPropertiesTab = new ObjectPropertiesTab(designer);
        fontPropertiesTab = new FontPropertiesTab(designer);

        /** create a compound to hold the property tabs in */
        propertiesCompound = new PropertiesCompound(objectPropertiesTab, fontPropertiesTab, layoutPropertiesTab,
                borderPropertiesTab, paragraphPropertiesTab);

        //layoutMenu = new LayoutMenu(commandListener, designer);

        /** add the toolbars to the screen*/
        ToolBarPanel toolBarPanel = toolbarContainer.getToolBarPanelAt(BorderLayout.NORTH);

        documentToolBar = new DocumentToolBar(commandListener);
        toolBarPanel.add(documentToolBar, new ToolBarConstraints(0, 0));

        propertiesToolBar = new WidgetPropertiesToolBar(designer);
        toolBarPanel.add(propertiesToolBar, new ToolBarConstraints(0, 1));

        widgetAlignmentAndOrderToolbar = new WidgetAlignmentAndOrderToolbar(designer);
        toolBarPanel.add(widgetAlignmentAndOrderToolbar, new ToolBarConstraints(0, 2));

        reportToolBar = new ReportToolbar(commandListener);
        toolBarPanel.add(reportToolBar, new ToolBarConstraints(0, 3));

//        dockableNames.put("Script Editor", javaScriptEditor);
//        dockableNames.put("Library", libraryPanel);
//        dockableNames.put("Hierarchy", hierarchyPanel);
//        dockableNames.put("Properties", propertiesCompound);
//
//        dockableNames.put("Paragraph", paragraphPropertiesTab);
//        dockableNames.put("Border", borderPropertiesTab);
//        dockableNames.put("Layout", layoutPropertiesTab);
//        dockableNames.put("Object", objectPropertiesTab);
//        dockableNames.put("Font", fontPropertiesTab);

        //windowMenu = new WindowMenu(this);

        splashWindow.setProgress(4, "Creating blank doucment");

        /** add a blank page to strat with */
        commands.executeCommand(Commands.INSERT_PAGE);

        /** setup the VLDocking layout*/
        setupDockingPanes();

        /**setup the menu bars*/
        setupMenuBar();

        setTitle(currentDesignerFileName + " - PDF Forms Designer Version " + Designer.version);

        getContentPane().add(toolbarContainer, BorderLayout.CENTER);
    }

    private String getNameFromDockable(Dockable name) {
        for (Iterator iterator = dockableNames.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (name == entry.getValue())
                return (String) entry.getKey();
        }
        return null;
    }

    public void displayPage(int page) {

        if (page >= 1 && page <= getTotalNoOfPages()) {
            currentPage = page;

            designer.displayPage(formsDocument.getPage(currentPage));

            Set widgets = designer.getSelectedWidgets();
            setPropertiesCompound(widgets);
            setPropertiesToolBar(widgets);

            designerCompound.setCurrentDesignerPage(currentPage);
        }
    }

    public int getTotalNoOfPages() {
        return formsDocument.getNoOfPages();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setFormsDocument(FormsDocument formsDocument) {
        this.formsDocument = formsDocument;
    }

    public IDesigner getDesigner() {
        return designer;
    }

    public void setCurrentDesignerFileName(String currentDesignerFileName) {
        this.currentDesignerFileName = currentDesignerFileName;
    }

    public String getCurrentDesignerFileName() {
        return currentDesignerFileName;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setDesignerCompoundContent(int content) {
        designerCompoundContent = content;

        if (content == DesignerCompound.DESIGNER) {
            documentToolBar.setZoomState(false);
            setTotalState(true);
        } else {
            documentToolBar.setZoomState(true);
            setTotalState(false);
        }
    }

    private void setTotalState(boolean state) {

        Set setToUse = state ? designer.getSelectedWidgets() : new HashSet();
        setPropertiesCompound(setToUse);
        setPropertiesToolBar(setToUse);

        hierarchyPanel.setHidden(state);
        libraryPanel.setState(state);
        designerCompound.setState(state);
        javaScriptEditor.setState(state);

//        insertMenu.setState(state);         //@old
//        fileMenu.setState(state);
//
//        documentToolBar.setSaveState(state);
//        fileMenu.setState(state);
//        documentToolBar.setState(state);
//        fileMenu.setState(state);

        documentToolBar.setSaveState(state);
        documentToolBar.setState(state);
        menuConfiguration.setState(state);
        designerCompound.setState(true);
    }

    public int getDesignerCompoundContent() {
        return designerCompoundContent;
    }

    public void setPanelsState(boolean state) {
        hierarchyPanel.setState(state);
        libraryPanel.setState(state);
        designerCompound.setState(state);
        javaScriptEditor.setState(state);

//        insertMenu.setState(state);    @old
//        fileMenu.setCloseState(state);

        menuConfiguration.setState(state);

        documentToolBar.setSaveState(state);

        designer.getWidgetSelection().hideGroupingButtons();
    }

    public void setTotalNoOfDisplayedPages(int totalNoOfDisplayedPages) {
        designerCompound.setTotalNoOfDisplayedPages(totalNoOfDisplayedPages);
    }

    public void addPageToHierarchyPanel(int pdfPage, Page newPage) {
        hierarchyPanel.addPage(pdfPage, newPage);
    }

    public void removePageFromHierarchyPanel(int index) {
        hierarchyPanel.removePage(index);
    }

    public void updateHierarchy() {
        hierarchyPanel.updateHierarchy(formsDocument);
    }

    //@scale
//    public void setScaling(double scaling) {
//        designer.setScale(scaling);
//    }
//
//    public double getScaling() {
//        return designer.getScale();
//    }

    public FormsDocument getFormsDocument() {
        return formsDocument;
    }

    public JavaScriptEditorPanel getScriptEditor() {
        return javaScriptEditor;
    }

    public void resetPaletteButtons() {
        libraryPanel.resetButtons();
    }

    public void setPropertiesToolBar(Set widgets) {
        //layoutMenu.setProperties(widgets); @old

        menuConfiguration.setProperties(widgets);

        widgets = getFlatternedWidgets(widgets);

        propertiesToolBar.setProperties(widgets);
        widgetAlignmentAndOrderToolbar.setState(!widgets.isEmpty());
    }

    public void setPropertiesCompound(Set widgets) {
        widgets = getFlatternedWidgets(widgets);

        PropertyChanger.updateSizeAndPosition(widgets);

        if (widgets.isEmpty() && formsDocument != null)
            widgets.add(formsDocument.getPage(currentPage));

        propertiesCompound.setProperties(widgets);

        Set newSet = widgets;
        if (widgets.isEmpty()) {
            newSet = new HashSet();
            newSet.add(formsDocument);
        }

        javaScriptEditor.setScript(newSet);
    }

    public void addWidgetToHierarchy(IWidget widget) {
        hierarchyPanel.addWidgetToHierarchy(widget, getCurrentPage());
    }

    public void removeWidgetFromHierarchy(IWidget widget) {
        hierarchyPanel.removeWidgetFromHierarchy(widget, getCurrentPage());
    }

    public void setDockableVisible(String dockableName, boolean visible) {

        if (dockableName.equals("Toolbars")) {
            toolbarContainer.getToolBarPanelAt(BorderLayout.NORTH).setVisible(visible);
            toolbarContainer.getToolBarPanelAt(BorderLayout.SOUTH).setVisible(visible);
        } else {

            Dockable dockable = (Dockable) dockableNames.get(dockableName);

            if (visible) {
                desk.addDockable(dockable, desk.getDockableState(dockable).getPosition());
            } else {
                desk.close(dockable);
            }
        }
    }

    private Set getFlatternedWidgets(Set widgets) {
        Set set = new HashSet();

        for (Iterator it = widgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            if (widget.getType() == IWidget.GROUP)
                set.addAll(getFlatternedWidgets(new HashSet(widget.getWidgetsInGroup())));
            else
                set.add(widget);
        }

        return set;
    }

    private void setupMenuBar() {
        // add sale/reload menus
//        actions.add(saveWorkspaceAction);
//        actions.add(loadWorkspaceAction);

        JMenuBar menubar = new JMenuBar();

        JMenu[] menus = menuConfiguration.getMenus();
        for (int i = 0; i < menus.length; i++) {
            menubar.add(menus[i]);
        }

        commands.recentDocumentsOption("recentdesfiles", menuConfiguration.getRecentDesignerFilesMenu());
        commands.recentDocumentsOption("recentpdffiles", menuConfiguration.getRecentImportedFilesMenu());

        setJMenuBar(menubar);
    }

    private void setupDockingPanes() {

        dockableNames.put("Script Editor", javaScriptEditor);
        dockableNames.put("Library", libraryPanel);
        dockableNames.put("Hierarchy", hierarchyPanel);
        dockableNames.put("Properties", propertiesCompound);

        dockableNames.put("Paragraph", paragraphPropertiesTab);
        dockableNames.put("Border", borderPropertiesTab);
        dockableNames.put("Layout", layoutPropertiesTab);
        dockableNames.put("Object", objectPropertiesTab);
        dockableNames.put("Font", fontPropertiesTab);

        // set the initial dockable
        desk.addDockable(designerCompound);

        if (windowConfiguration.isWindowVisible(WindowConfiguration.SCRIPT_EDITOR))
            desk.split(designerCompound, javaScriptEditor, DockingConstants.SPLIT_TOP);

        if (windowConfiguration.isWindowVisible(WindowConfiguration.HIERARCHY))
            desk.split(designerCompound, hierarchyPanel, DockingConstants.SPLIT_LEFT);

        if (windowConfiguration.isWindowVisible(WindowConfiguration.LIBRARY))
            desk.split(designerCompound, libraryPanel, DockingConstants.SPLIT_RIGHT);

        if (windowConfiguration.isWindowVisible(WindowConfiguration.PROPERTIES))
            desk.split(libraryPanel, propertiesCompound, DockingConstants.SPLIT_BOTTOM);

        // and add a tab into it
        desk.addDockable(propertiesCompound, fontPropertiesTab); // initial nesting : new API call
        desk.createTab(fontPropertiesTab, objectPropertiesTab, 1); // a tab, using standard API;
        desk.createTab(fontPropertiesTab, layoutPropertiesTab, 2); // a tab, using standard API;
        desk.createTab(fontPropertiesTab, borderPropertiesTab, 3); // a tab, using standard API;
        desk.createTab(fontPropertiesTab, paragraphPropertiesTab, 4); // a tab, using standard API;

        desk.setDockableHeight(javaScriptEditor, .22);
        desk.setDockableWidth(hierarchyPanel, .15);
        // desk.setDockableWidth(designerCompound, .6);
        desk.setDockableWidth(libraryPanel, .32);
        desk.setDockableHeight(propertiesCompound, .74);
        //desk.setDockableWidth(propertiesCompound, 350);
        //desk.setDockableHeight(libraryPanel, 150);

        // listen to dockable state changes before they are commited
        desk.addDockableStateWillChangeListener(new DockableStateWillChangeListener() {
            public void dockableStateWillChange(DockableStateWillChangeEvent event) {
                DockableState current = event.getCurrentState();
                if (current.getDockable() == designerCompound) {
                    if (event.getFutureState().isClosed()) {
                        // we are facing a closing of the editorPanel
                        event.cancel(); // refuse it
                    }
                }
            }
        });

        // cannot reload before a workspace is saved
        loadWorkspaceAction.setEnabled(false);
    }

    /**
     * Basic application starter
     *
     * @param args
     *         - main program arguments
     */
    public static void main(String[] args) {

        SplashWindow splashWindow = new SplashWindow();
        splashWindow.setStatusMaximum(4);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        splashWindow.setProgress(1, "Initializing window");
        final VLFrame frame = new VLFrame(splashWindow);

//      get local graphics environment
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

//      get maximum window bounds
        Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
        frame.setSize(maximumWindowBounds.width, maximumWindowBounds.height);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        frame.validate();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });

        splashWindow.setVisible(false);
    }

    private class FrameCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            designerCompound.closePdfDecoderFile();

            System.exit(0);
        }
    }

    /**
     * Save the current workspace into an instance byte array
     */
    private void saveWorkspace() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            desk.writeXML(out);
            out.close();
            savedWorkpace = out.toByteArray();
            loadWorkspaceAction.setEnabled(true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Reloads a saved workspace
     */
    private void loadWorkspace() {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(savedWorkpace);
            desk.readXML(in);
            in.close();
        } catch (Exception ex) {
            // catch all exceptions, including those of the SAXParser
            ex.printStackTrace();
        }
    }

    public void updateHierarchyPanelUI() {
        hierarchyPanel.updateUI();
    }

    public DesignerCompound getDesignerCompound() {
        return designerCompound;
    }

    public double getCurrentSelectedScaling() {
        return documentToolBar.getCurrentSelectedScaling();
    }

    public double getCurrentScaling() {
        return designerCompound.getCurrentScaling();
    }

    public void setCurrentSelectedScaling(double scaling) {
        documentToolBar.setCurrentlySelectedScaling(scaling);
    }

    public void updateAvailiableFonts() {
        propertiesCompound.updateAvailiableFonts();
    }

    public void addWidgetToPage(IWidget widget) {
        formsDocument.getPage(currentPage).getWidgets().add(widget);
    }

    public void renameWidget(String oldName, String name, IWidget widget) {
        widgetArrays.removeWidgetFromArray(widget, oldName, mainFrame);
    }

    public int getNextArrayNumberForName(String name, IWidget widgetToTest) {

        if (widgetArrays.isWidgetArrayInList(name)) { // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        List pages = formsDocument.getPages();
        for (Iterator it = pages.iterator(); it.hasNext(); ) {
            Page page = (Page) it.next();
            List widgetsOnPage = page.getWidgets();

            for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext(); ) {
                IWidget widget = (IWidget) iter.next();
                String widgetName = widget.getWidgetName();
                if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
                    /**
                     * another widget of this name already exists, and this is the first
                     * we've heard of it
                     */

                    widgetArrays.addWidgetToArray(name, widget); // add the original widget
                    widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget

                    return widgetArrays.getNextArrayIndex(name);
                }
            }
        }

        return 0;
    }

    public void handleArrayNumberOnWidgetDeletion(Set selectedWidgets) {
        for (Iterator it = selectedWidgets.iterator(); it.hasNext(); ) {
            IWidget widget = (IWidget) it.next();
            widgetArrays.removeWidgetFromArray(widget, widget.getWidgetName(), mainFrame);
        }

    }

    public WidgetArrays getWidgetArrays() {
        return widgetArrays;
    }

//    public int getNextArrayNumberForName(String name, IWidget widgetToTest) {
//        List otherWidgetsWithSameName = new ArrayList();
//
//        List pages = formsDocument.getPages();
//        for (Iterator it = pages.iterator(); it.hasNext();) {
//            Page page = (Page) it.next();
//            List widgetsOnPage = page.getWidgets();
//
//            for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext();) {
//                IWidget widget = (IWidget) iter.next();
//                String widgetName = widget.getName();
//                if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
//                    otherWidgetsWithSameName.add(widget);
//                }
//            }
//        }
//
//        int size = otherWidgetsWithSameName.size();
//        System.out.println(size + " = size <<<<<<<<<");
//		return size;
//    }
//
//    public void handleArrayNumberOnWidgetDeletion(Set selectedWidgets) {
//        for (Iterator iterator = selectedWidgets.iterator(); iterator.hasNext();) {
//            IWidget widgetToRemove = (IWidget) iterator.next();
//
//            String name = widgetToRemove.getName();
//
//            List otherWidgetsWithSameName = new ArrayList();
//
//            List pages = formsDocument.getPages();
//            for (Iterator it = pages.iterator(); it.hasNext();) {
//                Page page = (Page) it.next();
//                List widgetsOnPage = page.getWidgets();
//
//                for (Iterator iter = widgetsOnPage.iterator(); iter.hasNext();) {
//                    IWidget widget = (IWidget) iter.next();
//                    String widgetName = widget.getName();
//                    if (name.equals(widgetName) && !widgetToRemove.equals(widget)) {
//                        otherWidgetsWithSameName.add(widget);
//                    }
//                }
//            }
//
//            Collections.sort(otherWidgetsWithSameName, new Comparator() {
//                public int compare(Object o1, Object o2) {
//                    IWidget w1 = (IWidget) o1;
//                    IWidget w2 = (IWidget) o2;
//
//                    if (w1.getArrayNumber() < w2.getArrayNumber())
//                        return -1;
//                    else
//                        return 1;
//                }
//            });
//
//            System.out.println("otherWidgetsWithSameName = " + otherWidgetsWithSameName);            
//
//        }
//    }
//
//	public boolean isWidgetPartOfArray(IWidget widget) {
//		// TODO Auto-generated method stub
//		return false;
//	}
}
