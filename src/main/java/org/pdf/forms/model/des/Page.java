package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Page {

    private List<Property> property;
    @XmlElement(name = "pagedata")
    private PageData pageData;
    private List<Widget> widget;
    @XmlElement(name = "radiobuttongroups")
    private RadioButtonGroups radioButtonGroups;
    @XmlElement(name = "checkboxgroups")
    private CheckBoxGroups checkBoxGroups;

    public Page() {
        this.property = new ArrayList<>();
        this.pageData = new PageData();
        this.widget = new ArrayList<>();
        this.radioButtonGroups = new RadioButtonGroups();
        this.checkBoxGroups = new CheckBoxGroups();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(final PageData pageData) {
        this.pageData = pageData;
    }

    public List<Widget> getWidget() {
        return widget;
    }

    public void setWidget(final List<Widget> widget) {
        this.widget = widget;
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

    public Optional<String> getPdfFileLocation() {
        return getProperty("pdffilelocation");
    }

    public Optional<String> getPdfPageNumber() {
        return getProperty("pdfpagenumber");
    }

    public Optional<String> getPageName() {
        return getProperty("pagename");
    }

    public String getPageType() {
        return getProperty("pagetype")
                .orElseGet(() ->
                        getPdfFileLocation()
                        .map(location -> "pdfpage")
                        .orElse("simplepage"));
    }

    private Optional<String> getProperty(final String name) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .map(Property::getValue);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Page) {
            final Page page = (Page) o;
            return Objects.equals(property, page.property)
                    && Objects.equals(pageData, page.pageData)
                    && Objects.equals(widget, page.widget)
                    && Objects.equals(radioButtonGroups, page.radioButtonGroups)
                    && Objects.equals(checkBoxGroups, page.checkBoxGroups);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, pageData, widget, radioButtonGroups, checkBoxGroups);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Page.class.getSimpleName() + "[", "]")
                .add("properties=" + property)
                .add("pagedata=" + pageData)
                .add("widgets=" + widget)
                .add("radioButtonGroups=" + radioButtonGroups)
                .add("checkBoxGroups=" + checkBoxGroups)
                .toString();
    }
}

