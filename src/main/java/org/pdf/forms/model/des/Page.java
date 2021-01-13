package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Page {

    private List<Property> property;
    private Pagedata pagedata;
    private List<Widget> widgets;
    @XmlElement(name = "radiobuttongroups")
    private RadioButtonGroups radioButtonGroups;
    @XmlElement(name = "checkboxgroups")
    private CheckBoxGroups checkBoxGroups;

    public Page() {
        this.property = new ArrayList<>();
        this.widgets = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public Pagedata getPagedata() {
        return pagedata;
    }

    public void setPagedata(final Pagedata pagedata) {
        this.pagedata = pagedata;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(final List<Widget> widgets) {
        this.widgets = widgets;
    }

    public RadioButtonGroups getRadioButtonGroups() {
        return radioButtonGroups;
    }

    public void setRadioButtonGroups(final RadioButtonGroups radioButtonGroups) {
        this.radioButtonGroups = radioButtonGroups;
    }

    public CheckBoxGroups getCheckBoxGroups() {
        return checkBoxGroups;
    }

    public void setCheckBoxGroups(final CheckBoxGroups checkBoxGroups) {
        this.checkBoxGroups = checkBoxGroups;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Page) {
            final Page page = (Page) o;
            return Objects.equals(property, page.property)
                    && Objects.equals(pagedata, page.pagedata)
                    && Objects.equals(widgets, page.widgets)
                    && Objects.equals(radioButtonGroups, page.radioButtonGroups)
                    && Objects.equals(checkBoxGroups, page.checkBoxGroups);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, pagedata, widgets, radioButtonGroups, checkBoxGroups);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Page.class.getSimpleName() + "[", "]")
                .add("properties=" + property)
                .add("pagedata=" + pagedata)
                .add("widgets=" + widgets)
                .add("radioButtonGroups=" + radioButtonGroups)
                .add("checkBoxGroups=" + checkBoxGroups)
                .toString();
    }
}
