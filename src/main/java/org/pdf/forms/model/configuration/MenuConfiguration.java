package org.pdf.forms.model.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pdf.forms.readers.configuration.ItemBuilder;
import org.pdf.forms.readers.configuration.MenuBuilder;
import org.pdf.forms.readers.configuration.MenuConfigurationBuilder;

@XmlRootElement(name = "menu_configuration")
@XmlType(propOrder = "menu")
public class MenuConfiguration {

    public static final MenuConfiguration DEFAULT = new MenuConfigurationBuilder()
            .addMenu(buildFileMenu())
            .addMenu(buildInsertMenu())
            .addMenu(buildLayoutMenu())
            .addMenu(buildWindowMenu())
            .addMenu(buildHelpMenu())
            .build();

    private static Menu buildInsertMenu() {
        return new MenuBuilder().withName("Insert").withVisible(true)
                .addItem(new ItemBuilder().withCommand("INSERT_PAGE").withName("Insert Page").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("REMOVE_PAGE").withName("Remove Page").withVisible(true).build())
                .build();
    }

    private static Menu buildLayoutMenu() {
        return new MenuBuilder().withName("Layout").withVisible(true)
                .addItem(new ItemBuilder().withCommand("ALIGN").withName("Align").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("GROUP").withName("Group").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("UNGROUP").withName("Ungroup").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("BRING_TO_FRONT").withName("Bring To Front").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("SEND_TO_BACK").withName("Send To Back").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("BRING_FORWARDS").withName("Bring Forwards").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("SEND_BACKWARDS").withName("Send Backwards").withVisible(true)
                        .build())
                .build();
    }

    private static Menu buildFileMenu() {
        return new MenuBuilder().withName("File").withVisible(true)
                .addItem(new ItemBuilder().withCommand("NEW").withName("New").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("OPEN").withName("Open Designer File").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("RECENT_OPEN").withName("Recently Designer Files")
                        .withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("CLOSE").withName("Close").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("IMPORT").withName("Import PDF Documents").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("RECENT_IMPORT").withName("Recently Imported PDF Documents")
                        .withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("SAVE_FILE").withName("Save Designer File").withVisible(true)
                        .build())
                .addItem(new ItemBuilder().withCommand("SAVE_FILE_AS").withName("Save Designer File As")
                        .withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("PUBLISH").withName("Publish PDF Document").withVisible(true)
                        .build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("FONT_MANAGEMENT").withName("Font Management")
                        .withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("EXIT").withName("Exit").withVisible(true).build())
                .build();
    }

    private static Menu buildWindowMenu() {
        return new MenuBuilder().withName("Window").withVisible(true)
                .addItem(new ItemBuilder().withCommand("TOOLBARS").withName("Toolbars").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("SCRIPT_EDITOR").withName("Script Editor").withVisible(true)
                        .build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("HIERARCHY").withName("Hierarchy").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("LIBRARY").withName("Library").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("PROPERTIES").withName("Properties").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("LAYOUT").withName("Layout").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("BORDER").withName("Border").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("OBJECT").withName("Object").withVisible(true).build())
                .addItem(buildSeparator())
                .addItem(new ItemBuilder().withCommand("FONT").withName("Font").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("PARAGRAPH").withName("Paragraph").withVisible(true).build())
                .build();
    }

    private static Menu buildHelpMenu() {
        return new MenuBuilder().withName("Help").withVisible(true)
                .addItem(new ItemBuilder().withCommand("WEBSITE").withName("Visit Website").withVisible(true).build())
                .addItem(new ItemBuilder().withCommand("ABOUT").withName("About").withVisible(true).build())
                .build();
    }

    private static Item buildSeparator() {
        return new ItemBuilder()
                .withCommand("SEPARATOR")
                .withName("Separator")
                .withVisible(true)
                .build();
    }

    private List<Menu> menu;

    public MenuConfiguration() {
        this.menu = new ArrayList<>();
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(final List<Menu> menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof MenuConfiguration) {
            final MenuConfiguration that = (MenuConfiguration) o;
            return Objects.equals(menu, that.menu);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenuConfiguration.class.getSimpleName() + "[", "]")
                .add("menus=" + menu)
                .toString();
    }
}
