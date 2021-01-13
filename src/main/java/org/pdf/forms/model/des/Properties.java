package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Properties {

    private FontProperties font;
    private ObjectProperties object;
    private LayoutProperties layout;
    private BorderProperties border;
    private ParagraphProperties paragraph;
    @XmlElement(name = "caption_properties")
    private CaptionProperties captionProperties;

    public FontProperties getFont() {
        return font;
    }

    public void setFont(final FontProperties font) {
        this.font = font;
    }

    public ObjectProperties getObject() {
        return object;
    }

    public void setObject(final ObjectProperties object) {
        this.object = object;
    }

    public LayoutProperties getLayout() {
        return layout;
    }

    public void setLayout(final LayoutProperties layout) {
        this.layout = layout;
    }

    public BorderProperties getBorder() {
        return border;
    }

    public void setBorder(final BorderProperties border) {
        this.border = border;
    }

    public ParagraphProperties getParagraph() {
        return paragraph;
    }

    public void setParagraph(final ParagraphProperties paragraph) {
        this.paragraph = paragraph;
    }

    public CaptionProperties getCaptionProperties() {
        return captionProperties;
    }

    public void setCaptionProperties(final CaptionProperties captionProperties) {
        this.captionProperties = captionProperties;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Properties) {
            final Properties that = (Properties) o;
            return Objects.equals(font, that.font)
                    && Objects.equals(object, that.object)
                    && Objects.equals(layout, that.layout)
                    && Objects.equals(border, that.border)
                    && Objects.equals(paragraph, that.paragraph)
                    && Objects.equals(captionProperties, that.captionProperties);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(font, object, layout, border, paragraph, captionProperties);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Properties.class.getSimpleName() + "[", "]")
                .add("font=" + font)
                .add("object=" + object)
                .add("layout=" + layout)
                .add("border=" + border)
                .add("paragraph=" + paragraph)
                .add("captionProperties=" + captionProperties)
                .toString();
    }
}
