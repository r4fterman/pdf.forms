package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlType
public class Item {

    private String value;
    @Deprecated
    private String item;

    public Item() {
        // need default constructor for JAXB
        this.value = "";
    }

    public Item(final String value) {
        this.value = value;
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return item value
     * @since 0.1.0
     * @deprecated Use method #getValue() instead.
     */
    @Deprecated
    @XmlAttribute
    public String getItem() {
        return Optional.ofNullable(item).orElse("");
    }

    /**
     * @param item item value
     * @since 0.1.0
     * @deprecated Use method #setValue(String) instead
     */
    @Deprecated
    public void setItem(final String item) {
        this.item = item;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Item) {
            final Item item = (Item) o;
            return Objects.equals(value, item.value)
                    && Objects.equals(item, item.item);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, item);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .add("item='" + item + "'")
                .toString();
    }
}
