package org.pdf.forms.gui.menu;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.*;

import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.components.CheckBoxMenuItemWithID;
import org.pdf.forms.gui.components.MenuItemWithID;
import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;

public class MenubarCreator {

    private static final List<Integer> CHECKBOX_MENU_ITEM_COMMANDS = List.of(
            Commands.TOOLBARS,
            Commands.SCRIPT_EDITOR,
            Commands.HIERARCHY,
            Commands.LIBRARY,
            Commands.PROPERTIES,
            Commands.LAYOUT,
            Commands.BORDER,
            Commands.OBJECT,
            Commands.FONT,
            Commands.PARAGRAPH
    );

    private final Map<Integer, Function<Item, JMenuItem>> commandToMenuItemMapper = Map.ofEntries(
            Map.entry(Commands.ALIGN, this::createAlignmentMenu),
            Map.entry(Commands.GROUP, this::createGroupMenuItem),
            Map.entry(Commands.UNGROUP, this::createUngroupMenuItem),
            Map.entry(Commands.BRING_TO_FRONT, this::createBringToFrontMenuItem),
            Map.entry(Commands.SEND_TO_BACK, this::createSendToBackMenuItem),
            Map.entry(Commands.BRING_FORWARDS, this::createBringForwardsMenuItem),
            Map.entry(Commands.SEND_BACKWARDS, this::createSendBackwardsMenuItem)
    );

    private final List<String> separators = List.of(
            "Separator",
            // old legacy value
            "Seperator"
    );

    private final CommandListener commandListener;
    private final JMenuBar menubar;

    public MenubarCreator(
            final List<Menu> menus,
            final CommandListener commandListener) {
        this.commandListener = commandListener;
        this.menubar = createMenubar(menus);
    }

    public JMenuBar getMenuBar() {
        return menubar;
    }

    private JMenuBar createMenubar(final List<Menu> menus) {
        final JMenuBar jMenuBar = new JMenuBar();

        menus.stream()
                .filter(Menu::isVisible)
                .map(this::createJMenu)
                .forEach(jMenuBar::add);

        return jMenuBar;
    }

    private JMenu createAlignmentMenu(final Item item) {
        final JMenu alignMenu = createMenu(item);

        Stream.of(
                WidgetAlignmentAndOrder.ALIGN_LEFT,
                WidgetAlignmentAndOrder.ALIGN_RIGHT,
                WidgetAlignmentAndOrder.ALIGN_TOP,
                WidgetAlignmentAndOrder.ALIGN_BOTTOM,
                "Separator",
                WidgetAlignmentAndOrder.ALIGN_HORIZONTALLY,
                WidgetAlignmentAndOrder.ALIGN_VERTICALLY
        )
                .map(this::createAlignAndOrderMenuItem)
                .forEach(alignMenu::add);

        return alignMenu;
    }

    private JMenu createJMenu(final Menu menu) {
        final JMenu jMenu = new JMenu(menu.getName());

        menu.getItem().stream()
                .filter(Item::isVisible)
                .map(this::createJMenuItem)
                .forEach(jMenu::add);

        return jMenu;
    }

    private Component createJMenuItem(final Item item) {
        if (isSeparator(item.getName())) {
            return new JSeparator();
        }

        final int cmd = Commands.fromValue(item.getCommand());
        return getMenuItemCreationFunction(cmd)
                .apply(item);
    }

    private Function<Item, JMenuItem> getMenuItemCreationFunction(final int cmd) {
        if (CHECKBOX_MENU_ITEM_COMMANDS.contains(cmd)) {
            return this::createSelectedCheckboxMenuItem;
        }
        return commandToMenuItemMapper.getOrDefault(cmd, this::createMenuItem);
    }

    private JMenuItem createMenuItem(final Item item) {
        final int id = Commands.fromValue(item.getCommand());
        final MenuItemWithID menuItem = new MenuItemWithID(item.getName(), id);
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private JMenuItem createGroupMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Grouped.gif");
        return createMenuItemWithIcon(item, icon);
    }

    private JMenuItem createUngroupMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Ungrouped.gif");
        return createMenuItemWithIcon(item, icon);
    }

    private JMenuItem createBringToFrontMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Bring to Front.gif");
        return createMenuItemWithIcon(item.getName(), Commands.BRING_TO_FRONT, icon);
    }

    private JMenuItem createSendToBackMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Send to Back.gif");
        return createMenuItemWithIcon(item.getName(), Commands.SEND_TO_BACK, icon);
    }

    private JMenuItem createBringForwardsMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Bring Forwards.gif");
        return createMenuItemWithIcon(item.getName(), Commands.BRING_FORWARDS, icon);
    }

    private JMenuItem createSendBackwardsMenuItem(final Item item) {
        final ImageIcon icon = createImageIcon("/org/pdf/forms/res/Send Backwards.gif");
        return createMenuItemWithIcon(item.getName(), Commands.SEND_BACKWARDS, icon);
    }

    private JMenuItem createSelectedCheckboxMenuItem(final Item item) {
        final int id = Commands.fromValue(item.getCommand());
        final CheckBoxMenuItemWithID menuItem = new CheckBoxMenuItemWithID(item.getName(), true, id);
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private Component createAlignAndOrderMenuItem(final String alignment) {
        if (isSeparator(alignment)) {
            return new JSeparator();
        }

        final String iconPath = WidgetAlignmentAndOrder.getIconPath(alignment);
        return createMenuItemWithIcon(
                alignment,
                Commands.fromValue(alignment),
                createImageIcon(iconPath));
    }

    private JMenu createMenu(final Item item) {
        return new JMenu(item.getName());
    }

    private JMenuItem createMenuItemWithIcon(
            final Item item,
            final ImageIcon icon) {
        final int id = Commands.fromValue(item.getCommand());
        return createMenuItemWithIcon(item.getName(), id, icon);
    }

    private JMenuItem createMenuItemWithIcon(
            final String name,
            final int commandId,
            final ImageIcon icon) {
        final MenuItemWithID menuItem = new MenuItemWithID(name, icon, commandId);
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private boolean isSeparator(final String value) {
        return separators.contains(value);
    }

    private ImageIcon createImageIcon(final String imagePath) {
        final URL url = MenubarCreator.class.getResource(imagePath);
        return new ImageIcon(url);
    }
}
