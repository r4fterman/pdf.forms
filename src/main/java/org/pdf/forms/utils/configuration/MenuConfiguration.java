package org.pdf.forms.utils.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MenuConfiguration extends ConfigurationFile {

    private final CommandListener commandListener;

    private final List<JMenuItem> closeItems = new ArrayList<>();
    private final List<JMenuItem> previewItems = new ArrayList<>();

    private JMenu recentDesignerFiles;
    private JMenu recentImportedFiles;

    private final List<JMenuItem> alignAndOrderMenuItems = new ArrayList<>();
    private final List<JMenuItem> groupMenuItems = new ArrayList<>();

    private final Map<String, JMenuItem> windowNames = new HashMap<>();

    private final IDesigner designer;

    private final IMainFrame mainFrame;

    private final JMenu[] menus;

    public MenuConfiguration(
            final CommandListener commandListener,
            final IDesigner designer,
            final IMainFrame mainFrame) {
        super("menus.xml");

        this.commandListener = commandListener;
        this.designer = designer;
        this.mainFrame = mainFrame;

        menus = generateMenus();
    }

    public JMenu[] getMenus() {
        return menus;
    }

    @Override
    protected void writeDefaultConfiguration() throws Exception {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();

        setDoc(db.newDocument());

        final Element menuConfigurationElement = getDoc().createElement("menu_configuration");
        getDoc().appendChild(menuConfigurationElement);

        writeDefaultFileMenuConfiguration(menuConfigurationElement);
        writeDefaultInsertMenuConfiguration(menuConfigurationElement);
        writeDefaultLayoutMenuConfiguration(menuConfigurationElement);
        writeDefaultWindowMenuConfiguration(menuConfigurationElement);
        writeDefaultHelpMenuConfiguration(menuConfigurationElement);
    }

    private void writeDefaultHelpMenuConfiguration(final Element menuConfigurationElement) {
        final Element windowElement = getDoc().createElement("menu");
        windowElement.setAttribute("name", "Help");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);

        addItemToXMLTree(windowElement, "Visit Website", "WEBSITE", "true");
        addItemToXMLTree(windowElement, "About", "ABOUT", "true");
    }

    private void writeDefaultWindowMenuConfiguration(final Element menuConfigurationElement) {
        final Element windowElement = getDoc().createElement("menu");
        windowElement.setAttribute("name", "Window");
        windowElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(windowElement);

        addItemToXMLTree(windowElement, "Toolbars", "TOOLBARS", "true");
        addItemToXMLTree(windowElement, "Script Editor", "SCRIPT_EDITOR", "true");
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(windowElement, "Hierarchy", "HIERARCHY", "true");
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(windowElement, "Library", "LIBRARY", "true");
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(windowElement, "Properties", "PROPERTIES", "true");
        addItemToXMLTree(windowElement, "Layout", "LAYOUT", "true");
        addItemToXMLTree(windowElement, "Border", "BORDER", "true");
        addItemToXMLTree(windowElement, "Object", "OBJECT", "true");
        addItemToXMLTree(windowElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(windowElement, "Font", "FONT", "true");
        addItemToXMLTree(windowElement, "Paragraph", "PARAGRAPH", "true");
    }

    private void writeDefaultLayoutMenuConfiguration(final Element menuConfigurationElement) {
        final Element layoutElement = getDoc().createElement("menu");
        layoutElement.setAttribute("name", "Layout");
        layoutElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(layoutElement);

        addItemToXMLTree(layoutElement, "Align", "ALIGN", "true");
        addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(layoutElement, "Group", "GROUP", "true");
        addItemToXMLTree(layoutElement, "Ungroup", "UNGROUP", "true");
        addItemToXMLTree(layoutElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(layoutElement, "Bring to Front", "BRING_TO_FRONT", "true");
        addItemToXMLTree(layoutElement, "Send to Back", "SEND_TO_BACK", "true");
        addItemToXMLTree(layoutElement, "Bring Forwards", "BRING_FORWARDS", "true");
        addItemToXMLTree(layoutElement, "Send Backwards", "SEND_BACKWARDS", "true");
    }

    private void writeDefaultInsertMenuConfiguration(final Element menuConfigurationElement) {
        final Element insertElement = getDoc().createElement("menu");
        insertElement.setAttribute("name", "Insert");
        insertElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(insertElement);

        addItemToXMLTree(insertElement, "Insert Page", "INSERT_PAGE", "true");
        addItemToXMLTree(insertElement, "Remove Page", "REMOVE_PAGE", "true");
    }

    private void writeDefaultFileMenuConfiguration(final Element menuConfigurationElement) {
        final Element fileElement = getDoc().createElement("menu");
        fileElement.setAttribute("name", "File");
        fileElement.setAttribute("visible", "true");
        menuConfigurationElement.appendChild(fileElement);

        addItemToXMLTree(fileElement, "New", "NEW", "true");
        addItemToXMLTree(fileElement, "Open Designer File", "OPEN", "true");
        addItemToXMLTree(fileElement, "Recently Designer Files", "RECENT_OPEN", "true");
        addItemToXMLTree(fileElement, "Close", "CLOSE", "true");
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(fileElement, "Import PDF Document", "IMPORT", "true");
        addItemToXMLTree(fileElement, "Recently Imported PDF Documents", "RECENT_IMPORT", "true");
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(fileElement, "Save Designer File", "SAVE_FILE", "true");
        addItemToXMLTree(fileElement, "Save Designer File As", "SAVE_FILE_AS", "true");
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(fileElement, "Publish PDF Document", "PUBLISH", "true");
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(fileElement, "Font Management", "FONT_MANAGEMENT", "true");
        addItemToXMLTree(fileElement, "Seperator", "SEPERATOR", "true");
        addItemToXMLTree(fileElement, "Exit", "EXIT", "true");
    }

    private void addItemToXMLTree(
            final Element element,
            final String name,
            final String command,
            final String visible) {
        final Element item = getDoc().createElement("item");
        item.setAttribute("name", name);
        item.setAttribute("visible", visible);
        item.setAttribute("command", command);
        element.appendChild(item);
    }

    private JMenu[] generateMenus() {
        final NodeList nl = getDoc().getElementsByTagName("menu");

        final List<JMenu> menus = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            final Element menuElement = (Element) nl.item(i);

            String visible = menuElement.getAttribute("visible");
            boolean isVisible = visible.toLowerCase().equals("true");
            if (!isVisible) {
                continue;
            }

            final String menuName = menuElement.getAttribute("name");
            final JMenu menu = new JMenu(menuName);

            final NodeList menuItems = menuElement.getElementsByTagName("item");
            for (int j = 0; j < menuItems.getLength(); j++) {
                final Element itemElement = (Element) menuItems.item(j);

                visible = itemElement.getAttribute("visible");
                isVisible = visible.toLowerCase().equals("true");
                if (!isVisible) {
                    continue;
                }

                final String itemName = itemElement.getAttribute("name");
                if (itemName.equals("Seperator")) {
                    menu.add(new JSeparator());
                } else {
                    final String command = itemElement.getAttribute("command");
                    //System.out.println("Adding "+itemName+" command ="+ command);
                    addMenuItemMain(itemName, command.hashCode(), menu);
                }
            }
            menus.add(menu);
        }

        return menus.toArray(new JMenu[0]);
    }

    private JMenuItem addBasicMenuItem(
            final String text,
            final int command,
            final JMenu menu) {
        final SwingMenuItem item = new SwingMenuItem(text);
        item.setID(command);
        item.addActionListener(commandListener);
        menu.add(item);

        return item;
    }

    private JMenu addRecentFilesMenu(
            final String text,
            final JMenu menu) {
        final JMenu item = new JMenu(text);
        menu.add(item);
        return item;
    }

    private JMenuItem addAlignAndOrderMenuItems(
            final String text,
            final String type,
            final String url,
            final JMenu menu) {
        final JMenuItem item = new JMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource(url)));
        item.addActionListener(e -> WidgetAlignmentAndOrder.alignAndOrder(designer, type));
        menu.add(item);

        return item;
    }

    private JMenuItem addGoupMenuItems(
            final String text,
            final String url,
            final int command,
            final JMenu menu) {
        final SwingMenuItem item = new SwingMenuItem(text);
        item.setIcon(new ImageIcon(getClass().getResource(url)));
        item.setID(command);
        item.addActionListener(commandListener);
        menu.add(item);

        return item;
    }

    private JCheckBoxMenuItem addMenuItem(final String text) {
        final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text, true);
        menuItem.addActionListener(e -> mainFrame.setDockableVisible(text, menuItem.isSelected()));
        windowNames.put(text, menuItem);

        return menuItem;
    }

    private void addMenuItemMain(
            final String name,
            final int command,
            final JMenu menu) {
        switch (command) {

            case Commands.NEW:
                previewItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.OPEN:
                previewItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.RECENT_OPEN:
                recentDesignerFiles = addRecentFilesMenu(name, menu);
                previewItems.add(recentDesignerFiles);
                break;
            case Commands.CLOSE:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.IMPORT:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.RECENT_IMPORT:
                recentImportedFiles = addRecentFilesMenu(name, menu);
                previewItems.add(recentImportedFiles);
                break;
            case Commands.SAVE_FILE:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.SAVE_FILE_AS:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.PUBLISH:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.FONT_MANAGEMENT:
                closeItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.EXIT:
                addBasicMenuItem(name, command, menu);
                break;
            case Commands.INSERT_PAGE:
                previewItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.REMOVE_PAGE:
                previewItems.add(addBasicMenuItem(name, command, menu));
                break;
            case Commands.ALIGN:

                final JMenu align = new JMenu(name);
                menu.add(align);

                final String[] alignButtons = WidgetAlignmentAndOrder.getAlignButtons();
                for (final String url : alignButtons) {
                    if (!url.equals("Seperator")) {
                        final String[] splitFilename = url.split("/");
                        final String type = splitFilename[splitFilename.length - 1].split("\\.")[0];

                        alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(type, type, url, align));
                    }
                }

                break;
            case Commands.GROUP:
                groupMenuItems.add(addGoupMenuItems(name, "/org/pdf/forms/res/Grouped.gif", Commands.GROUP, menu));
                break;
            case Commands.UNGROUP:
                groupMenuItems.add(addGoupMenuItems(name, "/org/pdf/forms/res/Ungrouped.gif", Commands.UNGROUP, menu));
                break;
            case Commands.BRING_TO_FRONT:
                alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.BRING_TO_FRONT,
                        "/org/pdf/forms/res/Bring to Front.gif", menu));
                break;
            case Commands.SEND_TO_BACK:
                alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.SEND_TO_BACK,
                        "/org/pdf/forms/res/Send to Back.gif", menu));
                break;
            case Commands.BRING_FORWARDS:
                alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.BRING_FORWARDS,
                        "/org/pdf/forms/res/Bring Forwards.gif", menu));
                break;
            case Commands.SEND_BACKWARDS:
                alignAndOrderMenuItems.add(addAlignAndOrderMenuItems(name, WidgetAlignmentAndOrder.SEND_BACKWARDS,
                        "/org/pdf/forms/res/Send Backwards.gif", menu));
                break;
            case Commands.TOOLBARS:
                menu.add(addMenuItem(name));
                break;
            case Commands.SCRIPT_EDITOR:
                menu.add(addMenuItem(name));
                break;
            case Commands.HIERARCHY:
                menu.add(addMenuItem(name));
                break;
            case Commands.LIBRARY:
                menu.add(addMenuItem(name));
                break;
            case Commands.PROPERTIES:
                menu.add(addMenuItem(name));
                break;
            case Commands.LAYOUT:
                menu.add(addMenuItem(name));
                break;
            case Commands.BORDER:
                menu.add(addMenuItem(name));
                break;
            case Commands.OBJECT:
                menu.add(addMenuItem(name));
                break;
            case Commands.FONT:
                menu.add(addMenuItem(name));
                break;
            case Commands.PARAGRAPH:
                menu.add(addMenuItem(name));
                break;
            case Commands.WEBSITE:
                addBasicMenuItem(name, command, menu);
                break;
            case Commands.ABOUT:
                addBasicMenuItem(name, command, menu);
                break;
            default:
                break;
        }
    }

    private void setCloseState(final boolean state) {
        for (final JMenuItem menuItem : closeItems) {
            menuItem.setEnabled(state);
        }
    }

    public void setState(final boolean state) {
        setCloseState(state);

        for (final JMenuItem menuItem : previewItems) {
            menuItem.setEnabled(state);
        }
    }

    public JMenu getRecentDesignerFilesMenu() {
        return recentDesignerFiles;
    }

    public JMenu getRecentImportedFilesMenu() {
        return recentImportedFiles;
    }

    public void setProperties(final Set widgets) {
        if (widgets.isEmpty()) {
            setItemsEnabled(false);
            groupMenuItems.get(0).setEnabled(false);
            groupMenuItems.get(1).setEnabled(false);
        } else {
            setItemsEnabled(true);

            if (widgets.size() == 1 && ((IWidget) widgets.iterator().next()).getType() == IWidget.GROUP) {
                groupMenuItems.get(0).setEnabled(false);
                groupMenuItems.get(1).setEnabled(true);
            } else if (widgets.size() > 1) {
                groupMenuItems.get(0).setEnabled(true);
                groupMenuItems.get(1).setEnabled(false);
            }
        }
    }

    private void setItemsEnabled(final boolean enabled) {
        for (final JMenuItem menu : alignAndOrderMenuItems) {
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
