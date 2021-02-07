package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "border")
public class BorderProperties {

    private Borders borders;
    @XmlElement(name = "backgroundfill")
    private BackgroundFill backgroundFill;

    public BorderProperties() {
        this.borders = new Borders();
        this.backgroundFill = new BackgroundFill();
    }

    public Borders getBorders() {
        return borders;
    }

    public void setBorders(final Borders borders) {
        this.borders = borders;
    }

    public BackgroundFill getBackgroundFill() {
        return backgroundFill;
    }

    public void setBackgroundFill(final BackgroundFill backgroundFill) {
        this.backgroundFill = backgroundFill;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof BorderProperties) {
            final BorderProperties that = (BorderProperties) o;
            return Objects.equals(borders, that.borders)
                    && Objects.equals(backgroundFill, that.backgroundFill);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(borders, backgroundFill);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BorderProperties.class.getSimpleName() + "[", "]")
                .add("borders=" + borders)
                .add("backgroundFill=" + backgroundFill)
                .toString();
    }
}
