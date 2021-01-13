package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Document {

    public static final Document DEFAULT = new Document();

    private Property property;
    private JavaScriptContent javascript;
    private List<Page> page;

    public Document() {
        this.page = new ArrayList<>();
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public JavaScriptContent getJavaScript() {
        return javascript;
    }

    public void setJavaScript(final JavaScriptContent javaScript) {
        this.javascript = javaScript;
    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(final List<Page> page) {
        this.page = page;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Document) {
            final Document document = (Document) o;
            return Objects.equals(property, document.property)
                    && Objects.equals(javascript, document.javascript)
                    && Objects.equals(page, document.page);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, javascript, page);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Document.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("javaScriptContent=" + javascript)
                .add("pages=" + page)
                .toString();
    }
}
