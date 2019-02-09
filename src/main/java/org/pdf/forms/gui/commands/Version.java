package org.pdf.forms.gui.commands;

public class Version {

    public static final Version DEV = new Version("DEV", "SNAPSHOT");
    public static final Version CURRENT_VERSION = new Version("0", "8b05");

    public static Version of(final String version) {
        final String[] split = version.split("\\.");
        return new Version(split[0], split[1]);
    }

    private final String majorVersion;
    private final String minorVersion;

    private Version(
            final String majorVersion,
            final String minorVersion) {

        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public String getValue() {
        return String.join(".", majorVersion, minorVersion);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
