package org.pdf.forms.model.properties;

import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "properties")
public class CustomProperties {

    public static final CustomProperties DEFAULT = new CustomProperties();

    @XmlElement(name = "recentdesfiles")
    private RecentDesFiles recentDesFiles;
    @XmlElement(name = "recentpdffiles")
    private RecentPdfFiles recentPdfFiles;
    @XmlElement(name = "customfonts")
    private CustomFonts customFonts;

    public RecentDesFiles getRecentDesFiles() {
        return recentDesFiles;
    }

    public void setRecentDesFiles(final RecentDesFiles recentDesFiles) {
        this.recentDesFiles = recentDesFiles;
    }

    public RecentPdfFiles getRecentPdfFiles() {
        return recentPdfFiles;
    }

    public void setRecentPdfFiles(final RecentPdfFiles recentPdfFiles) {
        this.recentPdfFiles = recentPdfFiles;
    }

    public CustomFonts getCustomFonts() {
        return customFonts;
    }

    public void setCustomFonts(final CustomFonts customFonts) {
        this.customFonts = customFonts;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof CustomProperties) {
            final CustomProperties that = (CustomProperties) o;
            return Objects.equals(recentDesFiles, that.recentDesFiles)
                    && Objects.equals(recentPdfFiles, that.recentPdfFiles)
                    && Objects.equals(customFonts, that.customFonts);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recentDesFiles, recentPdfFiles, customFonts);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomProperties.class.getSimpleName() + "[", "]")
                .add("recentDesFiles=" + recentDesFiles)
                .add("recentPdfFiles=" + recentPdfFiles)
                .add("customFonts=" + customFonts)
                .toString();
    }
}
