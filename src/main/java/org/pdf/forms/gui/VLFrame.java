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
import java.util.Objects;
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
import org.pdf.forms.gui.designer.gui.DesignerPanel;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.editor.JavaScriptEditorPanel;
import org.pdf.forms.gui.hierarchy.HierarchyPanel;
import org.pdf.forms.gui.library.LibraryPanel;
import org.pdf.forms.gui.properties.PropertiesPanel;
import org.pdf.forms.gui.properties.PropertyChanger;
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

public class VLFrame extends JFrame implements IMainFrame {

    private IDesigner designer;

    private JavaScriptEditorPanel javaScriptEditor;
    private LibraryPanel libraryPanel;
    private HierarchyPanel hierarchyPanel;
    private DesignerPanel designerPanel;
    private PropertiesPanel propertiesPanel;

    private final Map<String, Dockable> dockableNames = new HashMap<>();
    private DocumentToolBar documentToolBar;
    private WidgetPropertiesToolBar propertiesToolBar;
    private WidgetAlignmentAndOrderToolbar widgetAlignmentAndOrderToolbar;
    private final WidgetArrays widgetArrays = new WidgetArrays();
    private MenuConfigurationFile menuConfigurationFile;
    private WindowConfigurationFile windowConfigurationFile;

    // the desktop (which will contain dockables)
    private DockingDesktop desk;
    private final String version;
    private ToolBarContainer toolbarContainer;
    private final WidgetFactory widgetFactory;
    private final Configuration configuration;

    private int designerCompoundContent = DesignerPanel.DESIGNER;

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

        final Rule horizontalRuler = new Rule(IMainFrame.INSET, Rule.HORIZONTAL, true);
        horizontalRuler.setPreferredWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        final Rule verticalRuler = new Rule(IMainFrame.INSET, Rule.VERTICAL, true);
        verticalRuler.setPreferredHeight(Toolkit.getDefaultToolkit().getScreenSize().height);

        final Commands commands = new Commands(this, version, fontHandler, widgetFactory, configuration);
        final CommandListener commandListener = new CommandListener(commands);

        splashWindow.setProgress(2, "Initializing window");
        toolbarContainer = initializeWindow(commandListener, horizontalRuler, verticalRuler, fontHandler);

        splashWindow.setProgress(3, "Setting up designer panel");
        initializeJavaScriptPanel();
        initializeLibraryPanel();
        initializeHierarchyPanel();
        initializeDesignerPanel(horizontalRuler, verticalRuler, fontHandler);

        splashWindow.setProgress(4, "Setting up properties panels");
        initializePropertiesPanels(fontHandler);

        splashWindow.setProgress(5, "Creating Tool- and MenuBars");
        fillToolbarPanel(toolbarContainer, commandListener, fontHandler);
        getContentPane().add(toolbarContainer, BorderLayout.CENTER);

        splashWindow.setProgress(6, "Creating blank document");
        commands.executeCommand(Commands.INSERT_PAGE);

        splashWindow.setProgress(7, "Set docking panes");
        setupMenuBar();
        setupDockingPanes();
        setTitle(currentDesignerFileName + " - PDF Forms Designer Version " + version);
    }

    private void fillToolbarPanel(
            final ToolBarContainer toolbarContainer,
            final CommandListener commandListener,
            final FontHandler fontHandler) {
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
    }

    private void initializeDesignerPanel(
            final Rule horizontalRuler,
            final Rule verticalRuler,
            final FontHandler fontHandler) {
        designerPanel = new DesignerPanel(designer, horizontalRuler, verticalRuler, this, fontHandler);
    }

    private void initializeLibraryPanel() {
        libraryPanel = new LibraryPanel(designer, widgetFactory);
    }

    private void initializeHierarchyPanel() {
        hierarchyPanel = new HierarchyPanel(designer);
    }

    private void initializeJavaScriptPanel() {
        javaScriptEditor = new JavaScriptEditorPanel();
    }

    private void initializePropertiesPanels(final FontHandler fontHandler) {
        propertiesPanel = new PropertiesPanel(designer, fontHandler);
    }

    private ToolBarContainer initializeWindow(
            final CommandListener commandListener,
            final Rule horizontalRuler,
            final Rule verticalRuler,
            final FontHandler fontHandler) {
        addWindowListener(new FrameCloser());
        desk = new DockingDesktop();

        final ToolBarContainer toolbarContainer = ToolBarContainer.createDefaultContainer(true, false, true, false);
        // insert our desktop as the only one component of the frame
        toolbarContainer.add(desk, BorderLayout.CENTER);

        designer = new Designer(IMainFrame.INSET, horizontalRuler, verticalRuler, this, version, fontHandler, this.widgetFactory, configuration);
        final DefaultTransferHandler dth = new DefaultTransferHandler(designer, this, version, this.widgetFactory, configuration);
        designer.setTransferHandler(dth);

        final File configDir = new File(configuration.getConfigDirectory(), "configuration");

        menuConfigurationFile = new MenuConfigurationFile(commandListener, designer, this, configDir, configuration);
        windowConfigurationFile = new WindowConfigurationFile(configDir, configuration);

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

        return toolbarContainer;
    }

    private Optional<String> getNameFromDockable(final Dockable name) {
        return dockableNames.entrySet().stream()
                .filter(entry -> name == entry.getValue())
                .findFirst()
                .map(Map.Entry::getKey)
                .or(() -> propertiesPanel.getNameFromDockable(name));
    }

    @Override
    public void displayPage(final int page) {
        if (page >= 1 && page <= getTotalNoOfPages()) {
            currentPage = page;

            designer.displayPage(formsDocument.getPage(currentPage));

            final Set<IWidget> widgets = designer.getSelectedWidgets();
            setPropertiesCompound(widgets);
            setPropertiesToolBar(widgets);

            designerPanel.setCurrentDesignerPage(currentPage);
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

        if (content == DesignerPanel.DESIGNER) {
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
        designerPanel.setState(state);
        javaScriptEditor.setState(state);
        documentToolBar.setSaveState(state);
        documentToolBar.setState(state);
        menuConfigurationFile.setState(state);
        designerPanel.setState(true);
    }

    @Override
    public int getDesignerCompoundContent() {
        return designerCompoundContent;
    }

    @Override
    public void setPanelsState(final boolean state) {
        hierarchyPanel.setState(state);
        libraryPanel.setState(state);
        designerPanel.setState(state);
        javaScriptEditor.setState(state);
        menuConfigurationFile.setState(state);
        documentToolBar.setSaveState(state);

        designer.getWidgetSelection().hideGroupingButtons();
    }

    @Override
    public void setTotalNoOfDisplayedPages(final int totalNoOfDisplayedPages) {
        designerPanel.setTotalNoOfDisplayedPages(totalNoOfDisplayedPages);
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

        propertiesPanel.setProperties(flattenWidgets);
        javaScriptEditor.setScript(flattenWidgets);
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
        for (int i = 0; i < numberOfRecentDocs; i++) {
            final JMenuItem menuItem = addDocumentToMenuEntry(recentDocs[i], i);
            file.add(menuItem);

            menuItem.addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                // handle missing files here
                new OpenDesignerFileCommand(this, version, widgetFactory, configuration).openDesignerFile(fileName);
            });
        }
    }

    private void addRecentPDFFilesAsMenuEntries(final JMenu file) {
        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory());
        final String[] recentDocs = properties.getRecentPDFDocuments();

        final int numberOfRecentDocs = properties.getNumberRecentDocumentsToDisplay();
        for (int i = 0; i < numberOfRecentDocs; i++) {
            final JMenuItem menuItem = addDocumentToMenuEntry(recentDocs[i], i);
            file.add(menuItem);

            menuItem.addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                // handle missing files here
                new ImportPdfCommand(this, version, widgetFactory, configuration).importPDF(fileName);
            });
        }
    }

    private JMenuItem addDocumentToMenuEntry(
            final String fileNameToAdd,
            final int i) {
        final String path = Objects.requireNonNullElse(fileNameToAdd, "");
        final String shortenedFileName = FileUtil.getShortenedFileName(path, File.separator);

        final JMenuItem menuItem = new JMenuItem(i + 1 + ": " + shortenedFileName);
        menuItem.setName(path);
        menuItem.setVisible(!shortenedFileName.isEmpty());

        return menuItem;
    }

    private void setupDockingPanes() {
        // set the initial dockable
        desk.addDockable(designerPanel);
        dockableNames.put("Designer", designerPanel);

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.SCRIPT_EDITOR)) {
            desk.split(designerPanel, javaScriptEditor, DockingConstants.SPLIT_TOP);
            desk.setDockableHeight(javaScriptEditor, .22);
            dockableNames.put("Script Editor", javaScriptEditor);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.HIERARCHY)) {
            desk.split(designerPanel, hierarchyPanel, DockingConstants.SPLIT_LEFT);
            desk.setDockableWidth(hierarchyPanel, .15);
            dockableNames.put("Hierarchy", hierarchyPanel);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.LIBRARY)) {
            desk.split(designerPanel, libraryPanel, DockingConstants.SPLIT_RIGHT);
            desk.setDockableWidth(libraryPanel, .32);
            dockableNames.put("Library", libraryPanel);
        }

        if (windowConfigurationFile.isWindowVisible(WindowConfigurationFile.PROPERTIES)) {
            desk.split(libraryPanel, propertiesPanel, DockingConstants.SPLIT_BOTTOM);
            desk.setDockableHeight(propertiesPanel, .74);
            propertiesPanel.addDockables(desk);
            dockableNames.put("Properties", propertiesPanel);
        }

        desk.addDockableStateWillChangeListener(event -> {
            final DockableState current = event.getCurrentState();
            if (current.getDockable() == designerPanel
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
            designerPanel.closePdfDecoderFile();

            System.exit(0);
        }
    }

    @Override
    public void updateHierarchyPanelUI() {
        hierarchyPanel.updateUI();
    }

    @Override
    public DesignerPanel getDesignerPanel() {
        return designerPanel;
    }

    @Override
    public double getCurrentSelectedScaling() {
        return documentToolBar.getCurrentSelectedScaling();
    }

    @Override
    public double getCurrentScaling() {
        return designerPanel.getCurrentScaling();
    }

    @Override
    public void setCurrentSelectedScaling(final double scaling) {
        documentToolBar.setCurrentlySelectedScaling(scaling);
    }

    @Override
    public void updateAvailableFonts() {
        propertiesPanel.updateAvailableFonts();
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
            final IWidget widget) {

        if (widgetArrays.isWidgetArrayInList(name)) {
            widgetArrays.addWidgetToArray(name, widget);
            return widgetArrays.getNextArrayIndex(name);
        }

        final List<Page> pages = formsDocument.getPages();
        for (final Page page : pages) {
            final List<IWidget> widgetsOnPage = page.getWidgets();
            for (final IWidget widgetOnPage : widgetsOnPage) {
                final String widgetName = widgetOnPage.getWidgetName();
                if (name.equals(widgetName) && !widgetOnPage.equals(widget)) {
                    /*
                     * another widget of this name already exists, and this is the first
                     * we've heard of it
                     */

                    widgetArrays.addWidgetToArray(name, widgetOnPage); // add the original widget
                    widgetArrays.addWidgetToArray(name, widget); // add the new widget

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
