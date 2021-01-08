package org.pdf.forms.utils.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.configuration.MenuConfiguration;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MenuConfigurationFile extends ConfigurationFile {

    private final List<JMenuItem> closeItems = new ArrayList<>();
    private final List<JMenuItem> previewItems = new ArrayList<>();
    private final List<JMenuItem> alignAndOrderMenuItems = new ArrayList<>();
    private final List<JMenuItem> groupMenuItems = new ArrayList<>();
    private final Map<String, JMenuItem> windowNames = new HashMap<>();

    private final CommandListener commandListener;
    private final IDesigner designer;
    private final IMainFrame mainFrame;
    private final MenuConfiguration menuConfiguration;
    private final JMenu[] menus;

    private JMenu recentDesignerFiles;
    private JMenu recentImportedFiles;

    public MenuConfigurationFile(
            final CommandListener commandListener,
            final IDesigner designer,
            final IMainFrame mainFrame,
            final File configDir,
            final File configurationDirectory) {
        super(new File(configDir, "menus.xml"), configurationDirectory);

        this.commandListener = commandListener;
        this.designer = designer;
        this.mainFrame = mainFrame;
        this.menuConfiguration = new MenuConfigurationFileReader(new File(configDir, "menus.xml")).getMenuConfiguration();

        menus = generateMenus(getDocument());
    }

    public MenuConfiguration getMenuConfiguration() {
        return menuConfiguration;
    }

    private JMenu[] generateMenus(final Document document) {
        final List<JMenu> menuList = new ArrayList<>();

        final NodeList nodeList = document.getElementsByTagName("menu");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element menuElement = (Element) nodeList.item(i);
            if (isElementVisible(menuElement)) {
                final String menuName = menuElement.getAttribute("name");
                final JMenu menu = new JMenu(menuName);
                addItemsToMenu(menuElement, menu);
                menuList.add(menu);
            }
        }

        return menuList.toArray(new JMenu[0]);
    }

    public JMenu[] getMenus() {
        return menus;
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

    private void addItemsToMenu(
            final Element menuElement,
            final JMenu menu) {
        final NodeList menuItems = menuElement.getElementsByTagName("item");
        for (int i = 0; i < menuItems.getLength(); i++) {
            final Element itemElement = (Element) menuItems.item(i);
            if (isElementVisible(itemElement)) {
                final String itemName = itemElement.getAttribute("name");
                if (itemName.equals("Seperator")) {
                    menu.add(new JSeparator());
                } else {
                    final String command = itemElement.getAttribute("command");
                    addMenuItemMain(itemName, Commands.fromValue(command), menu);
                }
            }
        }
    }

    private boolean isElementVisible(final Element element) {
        final String visible = element.getAttribute("visible");
        return visible.equalsIgnoreCase("true");
    }

    private JMenu createMenu(final String text) {
        return new JMenu(text);
    }

    private JMenuItem createAlignAndOrderMenuItem(
            final String text,
            final String type,
            final String url) {
        final JMenuItem item = new JMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource(url)));
        item.addActionListener(e -> WidgetAlignmentAndOrder.alignAndOrder(designer, type));
        return item;
    }

    private JMenuItem createMenuItem(
            final String text,
            final int command) {
        final SwingMenuItem item = new SwingMenuItem(text);
        item.setID(command);
        item.addActionListener(commandListener);
        return item;
    }

    private JMenuItem createMenuItemWithIcon(
            final String text,
            final String url,
            final int command) {
        final SwingMenuItem item = new SwingMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource(url)));
        item.setID(command);
        item.addActionListener(commandListener);
        return item;
    }

    private JCheckBoxMenuItem createCheckBoxMenuItem(final String text) {
        final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(text, true);
        checkBoxMenuItem.addActionListener(e -> mainFrame.setDockableVisible(text, checkBoxMenuItem.isSelected()));
        windowNames.put(text, checkBoxMenuItem);

        return checkBoxMenuItem;
    }

    private void addMenuItemMain(
            final String name,
            final int command,
            final JMenu menu) {
        switch (command) {
            case Commands.NEW:
            case Commands.OPEN:
            case Commands.INSERT_PAGE:
            case Commands.REMOVE_PAGE:
                final JMenuItem item = createMenuItem(name, command);
                menu.add(item);
                previewItems.add(item);
                break;
            case Commands.RECENT_OPEN:
                final JMenu recentOpenMenu = createMenu(name);
                menu.add(recentOpenMenu);
                recentDesignerFiles = recentOpenMenu;
                previewItems.add(recentDesignerFiles);
                break;
            case Commands.CLOSE:
            case Commands.IMPORT:
            case Commands.SAVE_FILE:
            case Commands.SAVE_FILE_AS:
            case Commands.PUBLISH:
            case Commands.FONT_MANAGEMENT:
                final JMenuItem closeMenuItem = createMenuItem(name, command);
                menu.add(closeMenuItem);
                closeItems.add(closeMenuItem);
                break;
            case Commands.RECENT_IMPORT:
                final JMenu recentImportMenu = createMenu(name);
                menu.add(recentImportMenu);
                recentImportedFiles = recentImportMenu;
                previewItems.add(recentImportedFiles);
                break;
            case Commands.EXIT:
            case Commands.WEBSITE:
            case Commands.ABOUT:
                final JMenuItem menuItem = createMenuItem(name, command);
                menu.add(menuItem);
                break;
            case Commands.ALIGN:
                final JMenu alignMenu = new JMenu(name);
                menu.add(alignMenu);

                final String[] alignButtons = WidgetAlignmentAndOrder.getAlignButtons();
                for (final String url: alignButtons) {
                    if (!url.equals("Seperator")) {
                        final String[] splitFilename = url.split("/");
                        final String type = splitFilename[splitFilename.length - 1].split("\\.")[0];

                        final JMenuItem alignOrderMenuItem = createAlignAndOrderMenuItem(type, type, url);
                        alignMenu.add(alignOrderMenuItem);
                        alignAndOrderMenuItems.add(alignOrderMenuItem);
                    }
                }

                break;
            case Commands.GROUP:
                final JMenuItem groupMenuItem = createMenuItemWithIcon(name, "/org/pdf/forms/res/Grouped.gif", command);
                menu.add(groupMenuItem);
                groupMenuItems.add(groupMenuItem);
                break;
            case Commands.UNGROUP:
                final JMenuItem ungroupMenuItem = createMenuItemWithIcon(name,
                        "/org/pdf/forms/res/Ungrouped.gif",
                        command);
                menu.add(ungroupMenuItem);
                groupMenuItems.add(ungroupMenuItem);
                break;
            case Commands.BRING_TO_FRONT:
                final JMenuItem bringToFrontMenuItem = createAlignAndOrderMenuItem(name,
                        WidgetAlignmentAndOrder.BRING_TO_FRONT,
                        "/org/pdf/forms/res/Bring to Front.gif");
                menu.add(bringToFrontMenuItem);
                alignAndOrderMenuItems.add(bringToFrontMenuItem);
                break;
            case Commands.SEND_TO_BACK:
                final JMenuItem sendToBackMenuItem = createAlignAndOrderMenuItem(name,
                        WidgetAlignmentAndOrder.SEND_TO_BACK,
                        "/org/pdf/forms/res/Send to Back.gif");
                menu.add(sendToBackMenuItem);
                alignAndOrderMenuItems.add(sendToBackMenuItem);
                break;
            case Commands.BRING_FORWARDS:
                final JMenuItem bringForwardsMenuItem = createAlignAndOrderMenuItem(name,
                        WidgetAlignmentAndOrder.BRING_FORWARDS,
                        "/org/pdf/forms/res/Bring Forwards.gif");
                menu.add(bringForwardsMenuItem);
                alignAndOrderMenuItems.add(bringForwardsMenuItem);
                break;
            case Commands.SEND_BACKWARDS:
                final JMenuItem sendBackwardsMenuItem = createAlignAndOrderMenuItem(name,
                        WidgetAlignmentAndOrder.SEND_BACKWARDS,
                        "/org/pdf/forms/res/Send Backwards.gif");
                menu.add(sendBackwardsMenuItem);
                alignAndOrderMenuItems.add(sendBackwardsMenuItem);
                break;
            case Commands.TOOLBARS:
            case Commands.SCRIPT_EDITOR:
            case Commands.HIERARCHY:
            case Commands.LIBRARY:
            case Commands.PROPERTIES:
            case Commands.LAYOUT:
            case Commands.BORDER:
            case Commands.OBJECT:
            case Commands.FONT:
            case Commands.PARAGRAPH:
                final JCheckBoxMenuItem checkBoxMenuItem = createCheckBoxMenuItem(name);
                menu.add(checkBoxMenuItem);
                break;
            default:
                break;
        }
    }

    private void setCloseState(final boolean state) {
        for (final JMenuItem menuItem: closeItems) {
            menuItem.setEnabled(state);
        }
    }

    public void setState(final boolean state) {
        setCloseState(state);

        for (final JMenuItem menuItem: previewItems) {
            menuItem.setEnabled(state);
        }
    }

    public JMenu getRecentDesignerFilesMenu() {
        return recentDesignerFiles;
    }

    public JMenu getRecentImportedFilesMenu() {
        return recentImportedFiles;
    }

    public void setProperties(final Set<IWidget> widgets) {
        if (widgets.isEmpty()) {
            setAlignAndOrderMenuItemsEnabled(false);
            groupMenuItems.get(0).setEnabled(false);
            groupMenuItems.get(1).setEnabled(false);
            return;
        }

        setAlignAndOrderMenuItemsEnabled(true);
        if (widgets.size() == 1 && widgets.iterator().next().getType() == IWidget.GROUP) {
            groupMenuItems.get(0).setEnabled(false);
            groupMenuItems.get(1).setEnabled(true);
        } else if (widgets.size() > 1) {
            groupMenuItems.get(0).setEnabled(true);
            groupMenuItems.get(1).setEnabled(false);
        }
    }

    private void setAlignAndOrderMenuItemsEnabled(final boolean enabled) {
        for (final JMenuItem menu: alignAndOrderMenuItems) {
            menu.setEnabled(enabled);
        }
    }

    public void setDockableVisible(
            final String dockableName,
            final boolean visible) {
        final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) windowNames.get(dockableName);
        menuItem.setSelected(visible);
    }
}
