package org.pdf.forms.model.des;

import java.util.Objects;
import java.util.StringJoiner;

public class Version {

    private final String versionValue;

    public Version(final String version) {
        this.versionValue = version;
    }

    public String getVersion() {
        return versionValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Version) {
            final Version version = (Version) o;
            return Objects.equals(this.versionValue, version.versionValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionValue);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Version.class.getSimpleName() + "[", "]")
                .add("versionValue='" + versionValue + "'")
                .toString();
    }
}
