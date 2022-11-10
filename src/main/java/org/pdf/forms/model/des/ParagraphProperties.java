package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paragraph")
public class ParagraphProperties {

    @XmlElement(name = "paragraph_caption")
    private ParagraphCaption paragraphCaption;
    @XmlElement(name = "paragraph_value")
    private ParagraphValue paragraphValue;

    public Optional<ParagraphCaption> getParagraphCaption() {
        return Optional.ofNullable(paragraphCaption);
    }

    public void setParagraphCaption(final ParagraphCaption paragraphCaption) {
        this.paragraphCaption = paragraphCaption;
    }

    public Optional<ParagraphValue> getParagraphValue() {
        return Optional.ofNullable(paragraphValue);
    }

    public void setParagraphValue(final ParagraphValue paragraphValue) {
        this.paragraphValue = paragraphValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ParagraphProperties) {
            final ParagraphProperties that = (ParagraphProperties) o;
            return Objects.equals(paragraphCaption, that.paragraphCaption)
                    && Objects.equals(paragraphValue, that.paragraphValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paragraphCaption, paragraphValue);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParagraphProperties.class.getSimpleName() + "[", "]")
                .add("paragraphCaption=" + paragraphCaption)
                .add("paragraphValue=" + paragraphValue)
                .toString();
    }
}
