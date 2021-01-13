package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "object")
public class ObjectProperties {

    private FieldProperties field;
    private ValueProperties value;
    private BindingProperties binding;

    public ObjectProperties() {
        this.field = new FieldProperties();
        this.value = new ValueProperties();
        this.binding = new BindingProperties();
    }

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

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ObjectProperties) {
            final ObjectProperties that = (ObjectProperties) o;
            return Objects.equals(field, that.field)
                    && Objects.equals(value, that.value)
                    && Objects.equals(binding, that.binding);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value, binding);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ObjectProperties.class.getSimpleName() + "[", "]")
                .add("field=" + field)
                .add("value=" + value)
                .add("binding=" + binding)
                .toString();
    }
}
