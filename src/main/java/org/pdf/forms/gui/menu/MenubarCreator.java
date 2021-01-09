package org.pdf.forms.gui.menu;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.swing.*;

import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;

public class MenubarCreator {

    private final List<Integer> checkboxMenuItemCommands = List.of(
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

    private JMenuBar createMenubar(final List<Menu> menus) {
        final JMenuBar jMenuBar = new JMenuBar();

        menus.stream()
                .filter(Menu::isVisible)
                .map(this::createJMenu)
                .forEach(jMenuBar::add);

        return jMenuBar;
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
        return getToMenuItemCreationFunction(cmd)
                .apply(item);
    }

    private Function<Item, JMenuItem> getToMenuItemCreationFunction(final int cmd) {
        if (checkboxMenuItemCommands.contains(cmd)) {
            return this::createSelectedCheckboxMenuItem;
        }
        return commandToMenuItemMapper.getOrDefault(cmd, this::createMenuItem);
    }

    private JMenuItem createMenuItem(final Item item) {
        final SwingMenuItem menuItem = new SwingMenuItem(item.getName());
        menuItem.setID(Commands.fromValue(item.getCommand()));
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private JMenuItem createGroupMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Grouped.gif");
        return createMenuItemWithIcon(item, icon);
    }

    private JMenuItem createUngroupMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Ungrouped.gif");
        return createMenuItemWithIcon(item, icon);
    }

    private JMenuItem createBringToFrontMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Bring to Front.gif");
        return createAlignAndOrderMenuItem(item.getName(), icon);
    }

    private JMenuItem createSendToBackMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Send to Back.gif");
        return createAlignAndOrderMenuItem(item.getName(), icon);
    }

    private JMenuItem createBringForwardsMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Bring Forwards.gif");
        return createAlignAndOrderMenuItem(item.getName(), icon);
    }

    private JMenuItem createSendBackwardsMenuItem(final Item item) {
        final ImageIcon icon = createButtonImageIcon("/org/pdf/forms/res/Send Backwards.gif");
        return createAlignAndOrderMenuItem(item.getName(), icon);
    }

    private JMenuItem createSelectedCheckboxMenuItem(final Item item) {
        final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(item.getName(), true);
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private JMenuItem createMenuItemWithIcon(
            final Item item,
            final ImageIcon icon) {
        final SwingMenuItem menuItem = new SwingMenuItem(item.getName());
        menuItem.setID(Commands.fromValue(item.getCommand()));
        menuItem.setIcon(icon);
        menuItem.addActionListener(commandListener);
        return menuItem;

    }

    private JMenu createMenu(final Item item) {
        return new JMenu(item.getName());
    }

    private JMenu createAlignmentMenu(final Item item) {
        final JMenu alignMenu = createMenu(item);

        Arrays.stream(WidgetAlignmentAndOrder.getAlignButtons())
                .filter(buttonImagePath -> !isSeparator(buttonImagePath))
                .map(buttonImagePath -> {
                    final List<String> pathSegments = Arrays.asList(buttonImagePath.split("/"));
                    final String fileNameWithExtension = pathSegments.get(pathSegments.size() - 1);
                    final String fileName = removeFileExtension(fileNameWithExtension);

                    final ImageIcon icon = createButtonImageIcon(buttonImagePath);
                    return createAlignAndOrderMenuItem(fileName, icon);
                })
                .forEach(alignMenu::add);

        return alignMenu;
    }

    private JMenuItem createAlignAndOrderMenuItem(
            final String name,
            final ImageIcon icon) {
        final SwingMenuItem menuItem = new SwingMenuItem(name);
        menuItem.setIcon(icon);
        menuItem.addActionListener(commandListener);
        return menuItem;
    }

    private boolean isSeparator(final String value) {
        return separators.contains(value);
    }

    public JMenuBar getMenuBar() {
        return menubar;
    }

    private ImageIcon createButtonImageIcon(final String buttonImagePath) {
        final URL url = MenubarCreator.class.getResource(buttonImagePath);
        return new ImageIcon(url);
    }

    private String removeFileExtension(final String fileNameWithExtension) {
        final int index = fileNameWithExtension.lastIndexOf(".");
        if (index < 0) {
            return fileNameWithExtension;
        }
        return fileNameWithExtension.substring(0, index);
    }
}
