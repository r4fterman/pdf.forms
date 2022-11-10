package org.pdf.forms.model.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Menu {

    private List<Item> item;

    private String name;
    private boolean visible;

    public Menu() {
        this.item = new ArrayList<>();
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(final List<Item> item) {
        this.item = item;
    }

    @XmlAttribute
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Menu) {
            final Menu menu = (Menu) o;
            return Objects.equals(item, menu.item);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Menu.class.getSimpleName() + "[", "]")
                .add("items=" + item)
                .toString();
    }
}
