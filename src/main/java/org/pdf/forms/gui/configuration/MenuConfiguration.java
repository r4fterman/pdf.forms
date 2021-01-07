package org.pdf.forms.gui.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "menu_configuration")
@XmlType(propOrder = "menu")
public class MenuConfiguration {

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
