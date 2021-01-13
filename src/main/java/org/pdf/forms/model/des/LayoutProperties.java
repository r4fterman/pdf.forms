package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "layout")
public class LayoutProperties {

    @XmlElement(name = "sizeandposition")
    private SizeAndPosition sizeAndPosition;
    private Margins margins;
    private Caption caption;

    public LayoutProperties() {
        this.sizeAndPosition = new SizeAndPosition();
        this.margins = new Margins();
        this.caption = new Caption();
    }

    public SizeAndPosition getSizeAndPosition() {
        return sizeAndPosition;
    }

    public void setSizeAndPosition(final SizeAndPosition sizeAndPosition) {
        this.sizeAndPosition = sizeAndPosition;
    }

    public Margins getMargins() {
        return margins;
    }

    public void setMargins(final Margins margins) {
        this.margins = margins;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(final Caption caption) {
        this.caption = caption;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof LayoutProperties) {
            final LayoutProperties that = (LayoutProperties) o;
            return Objects.equals(sizeAndPosition, that.sizeAndPosition)
                    && Objects.equals(margins, that.margins)
                    && Objects.equals(caption, that.caption);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeAndPosition, margins, caption);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LayoutProperties.class.getSimpleName() + "[", "]")
                .add("sizeAndPosition=" + sizeAndPosition)
                .add("margins=" + margins)
                .add("caption=" + caption)
                .toString();
    }
}
