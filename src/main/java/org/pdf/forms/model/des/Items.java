package org.pdf.forms.model.des;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Items {

    private List<Item> item;

    public Items() {
        this.item = new ArrayList<>();
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(final List<Item> item) {
        this.item = item;
    }

    public List<String> getItemValues() {
        return getItem().stream()
                .map(Item::getValue)
                .collect(toUnmodifiableList());
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Items) {
            final Items items = (Items) o;
            return Objects.equals(item, items.item);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Items.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .toString();
    }
}
