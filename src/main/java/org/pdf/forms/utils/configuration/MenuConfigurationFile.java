package org.pdf.forms.utils.configuration;

import java.io.File;
import java.util.Set;

import org.pdf.forms.gui.configuration.MenuConfiguration;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuConfigurationFile extends ConfigurationFile {

//    private final List<JMenuItem> closeItems = new ArrayList<>();
//    private final List<JMenuItem> previewItems = new ArrayList<>();
//    private final List<JMenuItem> alignAndOrderMenuItems = new ArrayList<>();
//    private final List<JMenuItem> groupMenuItems = new ArrayList<>();
//    private final Map<String, JMenuItem> windowNames = new HashMap<>();

    private final MenuConfiguration menuConfiguration;

    public MenuConfigurationFile(
            final File configDir,
            final File configurationDirectory) {
        super(new File(configDir, "menus.xml"), configurationDirectory);
        this.menuConfiguration = new MenuConfigurationFileReader(new File(configDir, "menus.xml"))
                .getMenuConfiguration();
    }

    public MenuConfiguration getMenuConfiguration() {
        return menuConfiguration;
    }

    @Override
    protected void writeToDefaultConfiguration(final Document document) {
        final Element menuConfigurationElement = document.createElement("menu_configuration");
        document.appendChild(menuConfigurationElement);

        writeDefaultFileMenuConfiguration(menuConfigurationElement, document);
        writeDefaultInsertMenuConfiguration(menuConfigurationElement, document);
        writeDefaultLayoutMenuConfiguration(menuConfigurationElement, document);
        writeDefaultWindowMenuConfiguration(menuConfigurationElement, document);
        writeDefaultHelpMenuConfiguration(menuConfigurationElement, document);
    }

    private void writeDefaultHelpMenuConfiguration(
            final Element menuConfigurationElement,
            final Document document) {
        final Element windowElement = document.createElement("menu");
        windowElement.setAttribute("name", "Help");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);

        addItemToXMLTree(windowElement, "Visit Website", "WEBSITE", document);
        addItemToXMLTree(windowElement, "About", "ABOUT", document);
    }

    private void writeDefaultWindowMenuConfiguration(
            final Element menuConfigurationElement,
            final Document document) {
        final Element windowElement = document.createElement("menu");
        windowElement.setAttribute("name", "Window");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);

        addItemToXMLTree(windowElement, "Toolbars", "TOOLBARS", document);
        addItemToXMLTree(windowElement, "Script Editor", "SCRIPT_EDITOR", document);
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(windowElement, "Hierarchy", "HIERARCHY", document);
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(windowElement, "Library", "LIBRARY", document);
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(windowElement, "Properties", "PROPERTIES", document);
        addItemToXMLTree(windowElement, "Layout", "LAYOUT", document);
        addItemToXMLTree(windowElement, "Border", "BORDER", document);
        addItemToXMLTree(windowElement, "Object", "OBJECT", document);
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(windowElement, "Font", "FONT", document);
        addItemToXMLTree(windowElement, "Paragraph", "PARAGRAPH", document);
    }

    private void writeDefaultLayoutMenuConfiguration(
            final Element menuConfigurationElement,
            final Document document) {
        final Element layoutElement = document.createElement("menu");
        layoutElement.setAttribute("name", "Layout");
        layoutElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(layoutElement);

        addItemToXMLTree(layoutElement, "Align", "ALIGN", document);
        addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(layoutElement, "Group", "GROUP", document);
        addItemToXMLTree(layoutElement, "Ungroup", "UNGROUP", document);
        addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(layoutElement, "Bring to Front", "BRING_TO_FRONT", document);
        addItemToXMLTree(layoutElement, "Send to Back", "SEND_TO_BACK", document);
        addItemToXMLTree(layoutElement, "Bring Forwards", "BRING_FORWARDS", document);
        addItemToXMLTree(layoutElement, "Send Backwards", "SEND_BACKWARDS", document);
    }

    private void writeDefaultInsertMenuConfiguration(
            final Element menuConfigurationElement,
            final Document document) {
        final Element insertElement = document.createElement("menu");
        insertElement.setAttribute("name", "Insert");
        insertElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(insertElement);

        addItemToXMLTree(insertElement, "Insert Page", "INSERT_PAGE", document);
        addItemToXMLTree(insertElement, "Remove Page", "REMOVE_PAGE", document);
    }

    private void writeDefaultFileMenuConfiguration(
            final Element menuConfigurationElement,
            final Document document) {
        final Element fileElement = document.createElement("menu");
        fileElement.setAttribute("name", "File");
        fileElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(fileElement);

        addItemToXMLTree(fileElement, "New", "NEW", document);
        addItemToXMLTree(fileElement, "Open Designer File", "OPEN", document);
        addItemToXMLTree(fileElement, "Recently Designer Files", "RECENT_OPEN", document);
        addItemToXMLTree(fileElement, "Close", "CLOSE", document);
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(fileElement, "Import PDF Document", "IMPORT", document);
        addItemToXMLTree(fileElement, "Recently Imported PDF Documents", "RECENT_IMPORT", document);
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(fileElement, "Save Designer File", "SAVE_FILE", document);
        addItemToXMLTree(fileElement, "Save Designer File As", "SAVE_FILE_AS", document);
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(fileElement, "Publish PDF Document", "PUBLISH", document);
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(fileElement, "Font Management", "FONT_MANAGEMENT", document);
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", document);
        addItemToXMLTree(fileElement, "Exit", "EXIT", document);
    }

    private void addItemToXMLTree(
            final Element element,
            final String name,
            final String command,
            final Document document) {
        final Element item = document.createElement("item");
        item.setAttribute("name", name);
        item.setAttribute("visible", "true");
        item.setAttribute("command", command);
        element.appendChild(item);
    }

    private void setCloseState(final boolean state) {
//        for (final JMenuItem menuItem: closeItems) {
//            menuItem.setEnabled(state);
//        }
    }

    public void setState(final boolean state) {
//        setCloseState(state);
//
//        for (final JMenuItem menuItem: previewItems) {
//            menuItem.setEnabled(state);
//        }
    }

    public void setProperties(final Set<IWidget> widgets) {
//        if (widgets.isEmpty()) {
//            setAlignAndOrderMenuItemsEnabled(false);
//            groupMenuItems.get(0).setEnabled(false);
//            groupMenuItems.get(1).setEnabled(false);
//            return;
//        }
//
//        setAlignAndOrderMenuItemsEnabled(true);
//        if (widgets.size() == 1 && widgets.iterator().next().getType() == IWidget.GROUP) {
//            groupMenuItems.get(0).setEnabled(false);
//            groupMenuItems.get(1).setEnabled(true);
//        } else if (widgets.size() > 1) {
//            groupMenuItems.get(0).setEnabled(true);
//            groupMenuItems.get(1).setEnabled(false);
//        }
    }

    private void setAlignAndOrderMenuItemsEnabled(final boolean enabled) {
//        for (final JMenuItem menu: alignAndOrderMenuItems) {
//            menu.setEnabled(enabled);
//        }
    }

    public void setDockableVisible(
            final String dockableName,
            final boolean visible) {
//        final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) windowNames.get(dockableName);
//        menuItem.setSelected(visible);
    }
}
