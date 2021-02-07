package org.pdf.forms.model.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "recentpdffiles")
public class RecentPdfFiles {

    private List<File> file;

    public RecentPdfFiles() {
        this.file = new ArrayList<>();
    }

    public List<File> getFile() {
        return file;
    }

    public void setFile(final List<File> file) {
        this.file = file;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof RecentPdfFiles) {
            final RecentPdfFiles that = (RecentPdfFiles) o;
            return Objects.equals(file, that.file);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RecentPdfFiles.class.getSimpleName() + "[", "]")
                .add("file=" + file)
                .toString();
    }
}
