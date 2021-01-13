package org.pdf.forms.model.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "recentdesfiles")
public class RecentDesFiles {

    private List<File> file;

    public RecentDesFiles() {
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
        if (o instanceof RecentDesFiles) {
            final RecentDesFiles that = (RecentDesFiles) o;
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
        return new StringJoiner(", ", RecentDesFiles.class.getSimpleName() + "[", "]")
                .add("file=" + file)
                .toString();
    }
}
