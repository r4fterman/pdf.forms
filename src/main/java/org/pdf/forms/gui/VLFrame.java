package org.pdf.forms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.pdf.forms.Configuration;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.commands.FileUtil;
import org.pdf.forms.gui.commands.ImportPdfCommand;
import org.pdf.forms.gui.commands.OpenDesignerFileCommand;
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
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.configuration.MenuConfigurationFile;
import org.pdf.forms.utils.configuration.WindowConfigurationFile;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetArrays;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VLFrame extends JFrame implements IMainFrame {

    private final Logger logger = LoggerFactory.getLogger(VLFrame.class);

    private final IDesigner designer;
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
    private final DocumentToolBar documentToolBar;
    private final WidgetPropertiesToolBar propertiesToolBar;
    private final WidgetAlignmentAndOrderToolbar widgetAlignmentAndOrderToolbar;
    private final WidgetArrays widgetArrays = new WidgetArrays();
    private final MenuConfigurationFile menuConfigurationFile;
    private final WindowConfigurationFile windowConfigurationFile;

    // the desktop (which will contain dockables)
    private final DockingDesktop desk = new DockingDesktop();
    private final String version;
    private final ToolBarContainer toolbarContainer;
    private final WidgetFactory widgetFactory;
    private final Configuration configuration;

    private int designerCompoundContent = DesignerCompound.DESIGNER;
    private FormsDocument formsDocument;
    private int currentPage = 0;
    private String currentDesignerFileName = "Untitled";

    public VLFrame(
            final SplashWindow splashWindow,
            final String version,
            final FontHandler fontHandler,
            final WidgetFactory widgetFactory,
            final Configuration configuration) {
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.configuration = configuration;

        addWindowListener(new FrameCloser());

        toolbarContainer = ToolBarContainer.createDefaultContainer(true, false, true, false);
        // insert our desktop as the only one component of the frame
        toolbarContainer.add(desk, BorderLayout.CENTER);

        /* setup the rulers*/
        final Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        final Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        final Commands commands = new Commands(this, version, fontHandler, widgetFactory, configuration);
        final CommandListener commandListener = new CommandListener(commands);

        designer = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, this, version, fontHandler, this.widgetFactory, configuration);
        final DefaultTransferHandler dth = new DefaultTransferHandler(designer, this, version, this.widgetFactory, configuration);
        designer.setTransferHandler(dth);

        final File configDir = new File(configuration.getConfigDirectory(), "configuration");

        menuConfigurationFile = new MenuConfigurationFile(commandListener, designer, this, configDir, configuration);
        windowConfigurationFile = new WindowConfigurationFile(configDir, configuration);

        libraryPanel = new LibraryPanel(designer, widgetFactory);
        hierarchyPanel = new HierarchyPanel(designer);

        formsDocument = new FormsDocument(version);

        desk.addDockingActionListener(new DockingActionListener() {
            @Override
            public boolean acceptDockingAction(final DockingActionEvent arg0) {
                return true;
            }

            @Override
            public void dockingActionPerformed(final DockingActionEvent event) {
                if (event.getActionType() == DockingActionEvent.ACTION_CLOSE) {
                    final DockingActionCloseEvent closeAction = (DockingActionCloseEvent) event;
                    final Dockable dockable = closeAction.getDockable();
                    menuConfigurationFile.setDockableVisible(getNameFromDockable(dockable).orElse(""), false);
                }
            }
        });

        splashWindow.setProgress(2, "Setting up designer panel");
        /* setup the designerTabs */
        designerCompound = new DesignerCompound(designer, horizontalRuler, verticalRuler, this, fontHandler);

        splashWindow.setProgress(3, "Setting up properties panels");
        paragraphPropertiesTab = new ParagraphPropertiesTab(designer);
        borderPropertiesTab = new BorderPropertiesTab(designer);
        layoutPropertiesTab = new LayoutPropertiesTab(designer);
        objectPropertiesTab = new ObjectPropertiesTab(designer);
        fontPropertiesTab = new FontPropertiesTab(designer, fontHandler);

        /* create a compound to hold the property tabs in */
        propertiesCompound = new PropertiesCompound(objectPropertiesTab, fontPropertiesTab, layoutPropertiesTab, borderPropertiesTab, paragraphPropertiesTab);

        /* add the toolbars to the screen*/
        final ToolBarPanel toolBarPanel = toolbarContainer.getToolBarPanelAt(BorderLayout.NORTH);

        documentToolBar = new DocumentToolBar(commandListener);
        toolBarPanel.add(documentToolBar, new ToolBarConstraints(0, 0));

        propertiesToolBar = new WidgetPropertiesToolBar(fontHandler, designer);
        toolBarPanel.add(propertiesToolBar, new ToolBarConstraints(0, 1));

        widgetAlignmentAndOrderToolbar = new WidgetAlignmentAndOrderToolbar(designer);
        toolBarPanel.add(widgetAlignmentAndOrderToolbar, new ToolBarConstraints(0, 2));

        final ReportToolbar reportToolBar = new ReportToolbar(commandListener);
        toolBarPanel.add(reportToolBar, new ToolBarConstraints(0, 3));

        splashWindow.setProgress(4, "Creating blank document");

        /* add a blank page to strat with */
        commands.executeCommand(Commands.INSERT_PAGE);

        /* setup the VLDocking layout*/
        setupDockingPanes();

        /*setup the menu bars*/
        setupMenuBar();

        setTitle(currentDesignerFileName + " - PDF Forms Designer Version " + version);

        getContentPane().add(toolbarContainer, BorderLayout.CENTER);
    }

    private Optional<String> getNameFromDockable(final Dockable name) {
        final Set<Map.Entry<String, Dockable>> entries = dockableNames.entrySet();
        for (final Map.Entry<String, Dockable> entry : entries) {
            if (name == entry.getValue()) {
                return Optional.ofNullable(entry.getKey());
            }
        }
        return Optional.empty();
    }

    @Override
    public void displayPage(final int page) {
        if (page >= 1 && page <= getTotalNoOfPages()) {
            currentPage = page;

            designer.displayPage(formsDocument.getPage(currentPage));

            final Set<IWidget> widgets = designer.getSelectedWidgets();
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
        menuConfigurationFile.setState(state);
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
        menuConfigurationFile.setState(state);
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
        menuConfigurationFile.setProperties(widgets);

        final Set<IWidget> flattenWidgets = getFlattenWidgets(widgets);

        propertiesToolBar.setProperties(flattenWidgets);
        widgetAlignmentAndOrderToolbar.setState(!flattenWidgets.isEmpty());
    }

    @Override
    public void setPropertiesCompound(final Set<IWidget> widgets) {
        final Set<IWidget> flattenWidgets = getFlattenWidgets(widgets);
        PropertyChanger.updateSizeAndPosition(flattenWidgets);
        propertiesCompound.setProperties(flattenWidgets);

        final Set<IWidget> newSet = flattenWidgets.stream().collect(Collectors.toUnmodifiableSet());
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

    private Set<IWidget> getFlattenWidgets(final Set<IWidget> widgets) {
        return widgets.stream()
                .map(widget -> {
                    if (widget.getType() == IWidget.GROUP) {
                        return getFlattenWidgets(new HashSet<>(widget.getWidgetsInGroup()));
                    }
                    return Set.of(widget);
                })
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void setupMenuBar() {
        final JMenuBar menubar = new JMenuBar();

        final JMenu[] menus = menuConfigurationFile.getMenus();
        for (final JMenu menu : menus) {
            menubar.add(menu);
        }

        addRecentDesignerFilesAsMenuEntries(menuConfigurationFile.getRecentDesignerFilesMenu());
        addRecentPDFFilesAsMenuEntries(menuConfigurationFile.getRecentImportedFilesMenu());

        setJMenuBar(menubar);
    }

    private void addRecentDesignerFilesAsMenuEntries(final JMenu file) {
        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory());

        final String[] recentDocs = properties.getRecentDesignerDocuments();
        if (recentDocs.length == 0) {
            return;
        }

        final int numberOfRecentDocs = properties.getNumberRecentDocumentsToDisplay();
        final JMenuItem[] recentDocuments = new JMenuItem[numberOfRecentDocs];
        for (int i = 0; i < numberOfRecentDocs; i++) {
            if (recentDocs[i] == null) {
                recentDocs[i] = "";
            }

            final String fileNameToAdd = recentDocs[i];
            final String shortenedFileName = FileUtil.getShortenedFileName(fileNameToAdd, File.separator);

            recentDocuments[i] = new JMenuItem(i + 1 + ": " + shortenedFileName);

            if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                recentDocuments[i].setVisible(false);
            }

            recentDocuments[i].setName(fileNameToAdd);
            recentDocuments[i].addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                // handle missing files here
                new OpenDesignerFileCommand(this, version, widgetFactory, configuration).openDesignerFile(fileName);
            });

            file.add(recentDocuments[i]);
        }
    }

    private void addRecentPDFFilesAsMenuEntries(final JMenu file) {
        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory());
        final String[] recentDocs = properties.getRecentPDFDocuments();
        if (recentDocs == null) {
            return;
        }

        final int numberOfRecentDocs = properties.getNumberRecentDocumentsToDisplay();
        final JMenuItem[] recentDocuments = new JMenuItem[numberOfRecentDocs];
        for (int i = 0; i < numberOfRecentDocs; i++) {
            if (recentDocs[i] == null) {
                recentDocs[i] = "";
            }

            final String fileNameToAdd = recentDocs[i];
            final String shortenedFileName = FileUtil.getShortenedFileName(fileNameToAdd, File.separator);

            recentDocuments[i] = new JMenuItem(i + 1 + ": " + shortenedFileName);

            if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                recentDocuments[i].setVisible(false);
            }

            recentDocuments[i].setName(fileNameToAdd);
            recentDocuments[i].addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                new ImportPdfCommand(this, version, widgetFactory, configuration).importPDF(fileName);
            });

            file.add(recentDocuments[i]);
        }
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

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.SCRIPT_EDITOR)) {
            desk.split(designerCompound, javaScriptEditor, DockingConstants.SPLIT_TOP);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.HIERARCHY)) {
            desk.split(designerCompound, hierarchyPanel, DockingConstants.SPLIT_LEFT);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.LIBRARY)) {
            desk.split(designerCompound, libraryPanel, DockingConstants.SPLIT_RIGHT);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.PROPERTIES)) {
            desk.split(libraryPanel, propertiesCompound, DockingConstants.SPLIT_BOTTOM);
        }

        desk.addDockable(propertiesCompound, fontPropertiesTab);
        desk.createTab(fontPropertiesTab, objectPropertiesTab, 1);
        desk.createTab(fontPropertiesTab, layoutPropertiesTab, 2);
        desk.createTab(fontPropertiesTab, borderPropertiesTab, 3);
        desk.createTab(fontPropertiesTab, paragraphPropertiesTab, 4);

        desk.setDockableHeight(javaScriptEditor, .22);
        desk.setDockableWidth(hierarchyPanel, .15);
        desk.setDockableWidth(libraryPanel, .32);
        desk.setDockableHeight(propertiesCompound, .74);

        // listen to dockable state changes before they are committed
        desk.addDockableStateWillChangeListener(event -> {
            final DockableState current = event.getCurrentState();
            if (current.getDockable() == designerCompound
                    && event.getFutureState().isClosed()) {
                // we are facing a closing of the editorPanel
                // so refuse it
                event.cancel();
            }
        });
    }

    private class FrameCloser extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            designerCompound.closePdfDecoderFile();

            System.exit(0);
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
        propertiesCompound.updateAvailableFonts();
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
        widgetArrays.removeWidgetFromArray(widget, oldName);
        updateHierarchyPanelUI();
    }

    @Override
    public int getNextArrayNumberForName(
            final String name,
            final IWidget widgetToTest) {

        if (widgetArrays.isWidgetArrayInList(name)) { // an array with this name already exists
            widgetArrays.addWidgetToArray(name, widgetToTest); // add the new widget
            return widgetArrays.getNextArrayIndex(name);
        }

        final List<Page> pages = formsDocument.getPages();
        for (final Page page : pages) {
            final List<IWidget> widgetsOnPage = page.getWidgets();
            for (final IWidget widget : widgetsOnPage) {
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
    public void handleArrayNumberOnWidgetDeletion(final Set<IWidget> widgets) {
        for (final IWidget widget : widgets) {
            widgetArrays.removeWidgetFromArray(widget, widget.getWidgetName());
        }
        updateHierarchyPanelUI();
    }

    @Override
    public WidgetArrays getWidgetArrays() {
        return widgetArrays;
    }

}
