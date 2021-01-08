package org.pdf.forms.gui.menu;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.*;

import org.jpedal.examples.simpleviewer.gui.swing.SwingMenuItem;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;
import org.pdf.forms.widgets.utils.WidgetAlignmentAndOrder;

public class MenubarCreator {

    private final Map<Integer, Function<Item, JMenuItem>> commandToMenuItemMapper = Map.ofEntries(
            Map.entry(Commands.NEW, this::createMenuItem),
            Map.entry(Commands.OPEN, this::createMenuItem),
            Map.entry(Commands.INSERT_PAGE, this::createMenuItem),
            Map.entry(Commands.REMOVE_PAGE, this::createMenuItem),
            Map.entry(Commands.RECENT_OPEN, this::createMenu),
            Map.entry(Commands.CLOSE, this::createMenuItem),
            Map.entry(Commands.IMPORT, this::createMenuItem),
            Map.entry(Commands.SAVE_FILE, this::createMenuItem),
            Map.entry(Commands.SAVE_FILE_AS, this::createMenuItem),
            Map.entry(Commands.PUBLISH, this::createMenuItem),
            Map.entry(Commands.FONT_MANAGEMENT, this::createMenuItem),
            Map.entry(Commands.RECENT_IMPORT, this::createMenu),
            Map.entry(Commands.EXIT, this::createMenuItem),
            Map.entry(Commands.WEBSITE, this::createMenuItem),
            Map.entry(Commands.ABOUT, this::createMenuItem),
            Map.entry(Commands.ALIGN, this::createAlignmentMenu),
            Map.entry(Commands.GROUP, this::createGroupMenuItem),
            Map.entry(Commands.UNGROUP, this::createUngroupMenuItem),
            Map.entry(Commands.BRING_TO_FRONT, this::createBringToFrontMenuItem),
            Map.entry(Commands.SEND_TO_BACK, this::createSendToBackMenuItem),
            Map.entry(Commands.BRING_FORWARDS, this::createBringForwardsMenuItem),
            Map.entry(Commands.SEND_BACKWARDS, this::createSendBackwardsMenuItem),
            Map.entry(Commands.TOOLBARS, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.SCRIPT_EDITOR, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.HIERARCHY, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.LIBRARY, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.PROPERTIES, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.LAYOUT, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.BORDER, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.OBJECT, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.FONT, this::createSelectedCheckboxMenuItem),
            Map.entry(Commands.PARAGRAPH, this::createSelectedCheckboxMenuItem)
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
                .flatMap(Optional::stream)
                .forEach(jMenu::add);

        return jMenu;
    }

    private Optional<Component> createJMenuItem(final Item item) {
        if (isSeparator(item.getName())) {
            final JSeparator separator = new JSeparator();
            return Optional.of(separator);
        }

        final int cmd = Commands.fromValue(item.getCommand());
        return Optional.ofNullable(commandToMenuItemMapper.get(cmd))
                .map(f -> f.apply(item));
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
