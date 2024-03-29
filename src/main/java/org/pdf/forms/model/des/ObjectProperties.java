package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "object")
public class ObjectProperties {

    private FieldProperties field;
    private ValueProperties value;
    private BindingProperties binding;
    private Items items;
    private Draw draw;

    public FieldProperties getField() {
        return field;
    }

    public void setField(final FieldProperties field) {
        this.field = field;
    }

    public ValueProperties getValue() {
        return value;
    }

    public void setValue(final ValueProperties value) {
        this.value = value;
    }

    public BindingProperties getBinding() {
        return binding;
    }

    public void setBinding(final BindingProperties binding) {
        this.binding = binding;
    }

    public Optional<Items> getItems() {
        return Optional.ofNullable(items);
    }

    public void setItems(final Items items) {
        this.items = items;
    }

    public Draw getDraw() {
        return draw;
    }

    public void setDraw(final Draw draw) {
        this.draw = draw;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ObjectProperties) {
            final ObjectProperties that = (ObjectProperties) o;
            return Objects.equals(field, that.field)
                    && Objects.equals(value, that.value)
                    && Objects.equals(binding, that.binding)
                    && Objects.equals(items, that.items)
                    && Objects.equals(draw, that.draw);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value, binding, items, draw);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ObjectProperties.class.getSimpleName() + "[", "]")
                .add("field=" + field)
                .add("value=" + value)
                .add("binding=" + binding)
                .add("items=" + items)
                .add("draw=" + draw)
                .toString();
    }
}
