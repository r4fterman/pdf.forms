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
 * VLFrame.java
 * ---------------
 */
package org.pdf.forms.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.commands.RecentDocumentType;
import org.pdf.forms.gui.commands.Version;
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

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.event.DockingActionCloseEvent;
import com.vlsolutions.swing.docking.event.DockingActionEvent;
import com.vlsolutions.swing.docking.event.DockingActionListener;
import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;

public class VLFrame extends JFrame implements IMainFrame {

    private final IDesigner designer;

    private FormsDocument formsDocument;

    private final JavaScriptEditorPanel javaScriptEditor = new JavaScriptEditorPanel();

    private final LibraryPanel libraryPanel;
    private final HierarchyPanel hierarchyPanel;

    private final PropertiesCompound propertiesCompound;
    private final DesignerCompound designerCompound;

    private final ParagraphPropertiesTab paragraphPropertiesTab;
    private final BorderPropertiesTab borderPropertiesTab;
    private final LayoutPropertiesTab layoutPropertiesTab;
    private final ObjectPropertiesTab objectPropertiesTab;
    private final FontPropertiesTab fontPropertiesTab;

    private final Map<String, Dockable> dockableNames = new HashMap<>();

    private int currentPage = 0;
    private String currentDesignerFileName = "Untitled";
    private final Commands commands;

    private final DocumentToolBar documentToolBar;
    private final WidgetPropertiesToolBar propertiesToolBar;
    private final WidgetAlignmentAndOrderToolbar widgetAlignmentAndOrderToolbar;

    private final WidgetArrays widgetArrays = new WidgetArrays();

    private final IMainFrame mainFrame = this;

    private final MenuConfiguration menuConfiguration;
    private final WindowConfiguration windowConfiguration;

    // the desktop (which will contain dockables)
    private final DockingDesktop desk = new DockingDesktop();
    // byte array used to save a workspace (custom layout of dockables)
    private byte[] savedWorkpace;

    // action used to save the current workspace
    private final Action saveWorkspaceAction = new AbstractAction("Save Workspace") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            saveWorkspace();
        }
    };

    // action used to reload a workspace
    private final Action loadWorkspaceAction = new AbstractAction("Reload Workspace") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            loadWorkspace();
        }
    };
    private final ToolBarContainer toolbarContainer;

    private int designerCompoundContent = DesignerCompound.DESIGNER;

    /**
     * Default and only frame constructor.
     */
    public VLFrame(
            final SplashWindow splashWindow,
            final Version version) {
        addWindowListener(new FrameCloser());

        toolbarContainer = ToolBarContainer.createDefaultContainer(true, false, true, false);

        // insert our desktop as the only one component of the frame
        toolbarContainer.add(desk, BorderLayout.CENTER);

        /* setup the rulers*/
        final Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        final Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        commands = new Commands(this, version);
        final CommandListener commandListener = new CommandListener(commands);

        designer = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, this, version);
        final DefaultTransferHandler dth = new DefaultTransferHandler(commands, designer);
        designer.setTransferHandler(dth);

        final String userDir = System.getProperty("user.dir");
        final File configDir = new File(userDir, "configuration");

        menuConfiguration = new MenuConfiguration(commandListener, designer, mainFrame, configDir);
        windowConfiguration = new WindowConfiguration(configDir);

        libraryPanel = new LibraryPanel(designer);
        hierarchyPanel = new HierarchyPanel(designer);

        formsDocument = new FormsDocument(version);

        desk.addDockingActionListener(new DockingActionListener() {
            @Override
            public boolean acceptDockingAction(final DockingActionEvent arg0) {
                return true;
            }

            @Override
            public void dockingActionPerformed(final DockingActionEvent arg0) {
                if (arg0.getActionType() == DockingActionEvent.ACTION_CLOSE) {
                    final DockingActionCloseEvent closeAction = (DockingActionCloseEvent) arg0;
                    final Dockable dockable = closeAction.getDockable();
                    menuConfiguration.setDockableVisible(getNameFromDockable(dockable), false);  //@old
                }
            }
        });

        splashWindow.setProgress(2, "Setting up designer panel");
        /* setup the designerTabs */
        designerCompound = new DesignerCompound(designer, horizontalRuler, verticalRuler, this);

        splashWindow.setProgress(3, "Setting up properties panels");
        paragraphPropertiesTab = new ParagraphPropertiesTab(designer);
        borderPropertiesTab = new BorderPropertiesTab(designer);
        layoutPropertiesTab = new LayoutPropertiesTab(designer);
        objectPropertiesTab = new ObjectPropertiesTab(designer);
        fontPropertiesTab = new FontPropertiesTab(designer);

        /* create a compound to hold the property tabs in */
        propertiesCompound = new PropertiesCompound(objectPropertiesTab, fontPropertiesTab, layoutPropertiesTab,
                borderPropertiesTab, paragraphPropertiesTab);

        //layoutMenu = new LayoutMenu(commandListener, designer);

        /* add the toolbars to the screen*/
        final ToolBarPanel toolBarPanel = toolbarContainer.getToolBarPanelAt(BorderLayout.NORTH);

        documentToolBar = new DocumentToolBar(commandListener);
        toolBarPanel.add(documentToolBar, new ToolBarConstraints(0, 0));

        propertiesToolBar = new WidgetPropertiesToolBar(designer);
        toolBarPanel.add(propertiesToolBar, new ToolBarConstraints(0, 1));

        widgetAlignmentAndOrderToolbar = new WidgetAlignmentAndOrderToolbar(designer);
        toolBarPanel.add(widgetAlignmentAndOrderToolbar, new ToolBarConstraints(0, 2));

        final ReportToolbar reportToolBar = new ReportToolbar(commandListener);
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

        /* add a blank page to strat with */
        commands.executeCommand(Commands.INSERT_PAGE);

        /* setup the VLDocking layout*/
        setupDockingPanes();

        /*setup the menu bars*/
        setupMenuBar();

        setTitle(currentDesignerFileName + " - PDF Forms Designer Version " + version);

        getContentPane().add(toolbarContainer, BorderLayout.CENTER);
    }

    private String getNameFromDockable(final Dockable name) {
        for (final Object o : dockableNames.entrySet()) {
            final Map.Entry entry = (Map.Entry) o;
            if (name == entry.getValue()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void displayPage(final int page) {

        if (page >= 1 && page <= getTotalNoOfPages()) {
            currentPage = page;

            designer.displayPage(formsDocument.getPage(currentPage));

            final Set widgets = designer.getSelectedWidgets();
            setPropertiesCompound(widgets);
            setPropertiesToolBar(widgets);

            designerCompound.setCurrentDesignerPage(currentPage);
        }
    }

    @Override
    public int getTotalNoOfPages() {
        return formsDocument.getNoOfPages();
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setFormsDocument(final FormsDocument formsDocument) {
        this.formsDocument = formsDocument;
    }

    @Override
    public IDesigner getDesigner() {
        return designer;
    }

    @Override
    public void setCurrentDesignerFileName(final String currentDesignerFileName) {
        this.currentDesignerFileName = currentDesignerFileName;
    }

    @Override
    public String getCurrentDesignerFileName() {
        return currentDesignerFileName;
    }

    @Override
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void setDesignerCompoundContent(final int content) {
        designerCompoundContent = content;

        if (content == DesignerCompound.DESIGNER) {
            documentToolBar.setZoomState(false);
            setTotalState(true);
        } else {
            documentToolBar.setZoomState(true);
            setTotalState(false);
        }
    }

    private void setTotalState(final boolean state) {
        final Set<IWidget> setToUse;
        if (state) {
            setToUse = designer.getSelectedWidgets();
        } else {
            setToUse = new HashSet<>();
        }
        setPropertiesCompound(setToUse);
        setPropertiesToolBar(setToUse);

        hierarchyPanel.setHidden(state);
        libraryPanel.setState(state);
        designerCompound.setState(state);
        javaScriptEditor.setState(state);
        documentToolBar.setSaveState(state);
        documentToolBar.setState(state);
        menuConfiguration.setState(state);
        designerCompound.setState(true);
    }

    @Override
    public int getDesignerCompoundContent() {
        return designerCompoundContent;
    }

    @Override
    public void setPanelsState(final boolean state) {
        hierarchyPanel.setState(state);
        libraryPanel.setState(state);
        designerCompound.setState(state);
        javaScriptEditor.setState(state);
        menuConfiguration.setState(state);
        documentToolBar.setSaveState(state);

        designer.getWidgetSelection().hideGroupingButtons();
    }

    @Override
    public void setTotalNoOfDisplayedPages(final int totalNoOfDisplayedPages) {
        designerCompound.setTotalNoOfDisplayedPages(totalNoOfDisplayedPages);
    }

    @Override
    public void addPageToHierarchyPanel(
            final int pdfPage,
            final Page newPage) {
        hierarchyPanel.addPage(pdfPage, newPage);
    }

    @Override
    public void removePageFromHierarchyPanel(final int index) {
        hierarchyPanel.removePage(index);
    }

    @Override
    public void updateHierarchy() {
        hierarchyPanel.updateHierarchy(formsDocument);
    }

    @Override
    public FormsDocument getFormsDocument() {
        return formsDocument;
    }

    public JavaScriptEditorPanel getScriptEditor() {
        return javaScriptEditor;
    }

    @Override
    public void resetPaletteButtons() {
        libraryPanel.resetButtons();
    }

    @Override
    public void setPropertiesToolBar(final Set<IWidget> widgets) {
        menuConfiguration.setProperties(widgets);

        final Set<IWidget> flattenWidgets = getFlatternedWidgets(widgets);

        propertiesToolBar.setProperties(flattenWidgets);
        widgetAlignmentAndOrderToolbar.setState(!flattenWidgets.isEmpty());
    }

    @Override
    public void setPropertiesCompound(final Set<IWidget> tlwwidgets) {
        final Set<IWidget> flatteWidgets = getFlatternedWidgets(tlwwidgets);

        PropertyChanger.updateSizeAndPosition(flatteWidgets);

        //        if (flatteWidgets.isEmpty() && formsDocument != null) {
        //            widgets.add(formsDocument.getPage(currentPage));
        //        }

        propertiesCompound.setProperties(flatteWidgets);

        Set<IWidget> newSet = new HashSet<>(flatteWidgets);
        if (flatteWidgets.isEmpty()) {
            newSet = new HashSet<>();
            //newSet.add(formsDocument);
        }

        javaScriptEditor.setScript(newSet);
    }

    @Override
    public void addWidgetToHierarchy(final IWidget widget) {
        hierarchyPanel.addWidgetToHierarchy(widget, getCurrentPage());
    }

    @Override
    public void removeWidgetFromHierarchy(final IWidget widget) {
        hierarchyPanel.removeWidgetFromHierarchy(widget, getCurrentPage());
    }

    @Override
    public void setDockableVisible(
            final String dockableName,
            final boolean visible) {

        if (dockableName.equals("Toolbars")) {
            toolbarContainer.getToolBarPanelAt(BorderLayout.NORTH).setVisible(visible);
            toolbarContainer.getToolBarPanelAt(BorderLayout.SOUTH).setVisible(visible);
        } else {

            final Dockable dockable = dockableNames.get(dockableName);

            if (visible) {
                desk.addDockable(dockable, desk.getDockableState(dockable).getPosition());
            } else {
                desk.close(dockable);
            }
        }
    }

    private Set<IWidget> getFlatternedWidgets(final Set<IWidget> widgets) {
        final Set<IWidget> set = new HashSet<>();

        for (final IWidget widget : widgets) {
            if (widget.getType() == IWidget.GROUP) {
                set.addAll(getFlatternedWidgets(new HashSet<>(widget.getWidgetsInGroup())));
            } else {
                set.add(widget);
            }
        }

        return set;
    }

    private void setupMenuBar() {
        final JMenuBar menubar = new JMenuBar();

        final JMenu[] menus = menuConfiguration.getMenus();
        for (final JMenu menu : menus) {
            menubar.add(menu);
        }

        commands.recentDocumentsOption(RecentDocumentType.RECENT_DES_FILES, menuConfiguration.getRecentDesignerFilesMenu());
        commands.recentDocumentsOption(RecentDocumentType.RECENT_PDF_FILES, menuConfiguration.getRecentImportedFilesMenu());

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

        if (windowConfiguration.isWindowVisible(WindowConfiguration.SCRIPT_EDITOR)) {
            desk.split(designerCompound, javaScriptEditor, DockingConstants.SPLIT_TOP);
        }

        if (windowConfiguration.isWindowVisible(WindowConfiguration.HIERARCHY)) {
            desk.split(designerCompound, hierarchyPanel, DockingConstants.SPLIT_LEFT);
        }

        if (windowConfiguration.isWindowVisible(WindowConfiguration.LIBRARY)) {
            desk.split(designerCompound, libraryPanel, DockingConstants.SPLIT_RIGHT);
        }

        if (windowConfiguration.isWindowVisible(WindowConfiguration.PROPERTIES)) {
            desk.split(libraryPanel, propertiesCompound, DockingConstants.SPLIT_BOTTOM);
        }

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
        desk.addDockableStateWillChangeListener(event -> {
            final DockableState current = event.getCurrentState();
            if (current.getDockable() == designerCompound) {
                if (event.getFutureState().isClosed()) {
                    // we are facing a closing of the editorPanel
                    event.cancel(); // refuse it
                }
            }
        });

        // cannot reload before a workspace is saved
        loadWorkspaceAction.setEnabled(false);
    }

    private class FrameCloser extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            designerCompound.closePdfDecoderFile();

            System.exit(0);
        }
    }

    /**
     * Save the current workspace into an instance byte array.
     */
    private void saveWorkspace() {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            desk.writeXML(out);
            out.close();
            savedWorkpace = out.toByteArray();
            loadWorkspaceAction.setEnabled(true);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Reloads a saved workspace.
     */
    private void loadWorkspace() {
        try {
            final ByteArrayInputStream in = new ByteArrayInputStream(savedWorkpace);
            desk.readXML(in);
            in.close();
        } catch (final Exception ex) {
            // catch all exceptions, including those of the SAXParser
            ex.printStackTrace();
        }
    }

    @Override
    public void updateHierarchyPanelUI() {
        hierarchyPanel.updateUI();
    }

    @Override
    public DesignerCompound getDesignerCompound() {
        return designerCompound;
    }

    @Override
    public double getCurrentSelectedScaling() {
        return documentToolBar.getCurrentSelectedScaling();
    }

    @Override
    public double getCurrentScaling() {
        return designerCompound.getCurrentScaling();
    }

    @Override
    public void setCurrentSelectedScaling(final double scaling) {
        documentToolBar.setCurrentlySelectedScaling(scaling);
    }

    @Override
    public void updateAvailiableFonts() {
        propertiesCompound.updateAvailiableFonts();
    }

    @Override
    public void addWidgetToPage(final IWidget widget) {
        formsDocument.getPage(currentPage).getWidgets().add(widget);
    }

    @Override
    public void renameWidget(
            final String oldName,
            final String name,
            final IWidget widget) {
        widgetArrays.removeWidgetFromArray(widget, oldName, mainFrame);
    }

    @Override
    public int getNextArrayNumberForName(
            final String name,
            final IWidget widgetToTest) {

        if (widgetArrays.isWidgetArrayInList(name)) { // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        final List pages = formsDocument.getPages();
        for (final Object page1 : pages) {
            final Page page = (Page) page1;
            final List widgetsOnPage = page.getWidgets();

            for (final Object o : widgetsOnPage) {
                final IWidget widget = (IWidget) o;
                final String widgetName = widget.getWidgetName();
                if (name.equals(widgetName) && !widget.equals(widgetToTest)) {
                    /*
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

    @Override
    public void handleArrayNumberOnWidgetDeletion(final Set selectedWidgets) {
        for (final Object widget1 : selectedWidgets) {
            final IWidget widget = (IWidget) widget1;
            widgetArrays.removeWidgetFromArray(widget, widget.getWidgetName(), mainFrame);
        }

    }

    @Override
    public WidgetArrays getWidgetArrays() {
        return widgetArrays;
    }

}
