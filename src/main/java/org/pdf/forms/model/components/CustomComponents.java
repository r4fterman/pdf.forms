package org.pdf.forms.model.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "custom_components")
public class CustomComponents {

    @XmlElement(name = "custom_component")
    private List<CustomComponent> customComponentList;

    public CustomComponents() {
        this.customComponentList = new ArrayList<>();
    }

    public List<CustomComponent> getCustomComponentList() {
        return customComponentList;
    }

    public void setCustomComponentList(final List<CustomComponent> customComponentList) {
        this.customComponentList = customComponentList;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CustomComponents) {
            final CustomComponents that = (CustomComponents) o;
            return Objects.equals(customComponentList, that.customComponentList);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customComponentList);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomComponents.class.getSimpleName() + "[", "]")
                .add("customComponents=" + customComponentList)
                .toString();
    }
}
