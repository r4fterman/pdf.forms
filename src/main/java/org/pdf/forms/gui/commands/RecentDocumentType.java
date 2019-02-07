package org.pdf.forms.gui.commands;

public enum RecentDocumentType {

    RECENT_DES_FILES("recentdesfiles"),
    RECENT_PDF_FILES("recentpdffiles");

    private String value;

    RecentDocumentType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RecentDocumentType{"
                + "value='" + value + '\''
                + '}';
    }
}
