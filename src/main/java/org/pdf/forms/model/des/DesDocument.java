package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "document")
public class DesDocument {

    private Property property;
    private JavaScriptContent javascript;
    private List<Page> page;

    public DesDocument() {
        this.property = new Property();
        this.javascript = new JavaScriptContent();
        this.page = new ArrayList<>();
    }

    public DesDocument(final Version version) {
        setProperty(new Property("version", version.getVersion()));
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public Optional<JavaScriptContent> getJavaScript() {
        return Optional.ofNullable(javascript);
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
        if (o instanceof DesDocument) {
            final DesDocument desDocument = (DesDocument) o;
            return Objects.equals(property, desDocument.property)
                    && Objects.equals(javascript, desDocument.javascript)
                    && Objects.equals(page, desDocument.page);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, javascript, page);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DesDocument.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .add("javaScriptContent=" + javascript)
                .add("pages=" + page)
                .toString();
    }
}
