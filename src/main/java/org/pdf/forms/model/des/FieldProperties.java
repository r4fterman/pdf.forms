package org.pdf.forms.model.des;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class FieldProperties {

    private static final String GROUP_NAME = "Group Name";
    private static final String MAX_CHARS = "Max Chars";
    private static final String LIMIT_LENGTH = "Limit Length";
    private static final String ALLOW_MULTIPLE_LINES = "Allow Multiple Lines";
    private static final String APPEARANCE = "Appearance";
    private static final String PRESENCE = "Presence";
    private static final String ALLOW_CUSTOM_TEXT_ENTRY = "Allow Custom Text Entry";

    private List<Property> property;

    public FieldProperties() {
        this.property = new ArrayList<>();
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(final List<Property> property) {
        this.property = property;
    }

    public boolean allowCustomTextEntry() {
        return getPropertyValue(ALLOW_CUSTOM_TEXT_ENTRY)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public void allowCustomTextEntry(final boolean allow) {
        setPropertyValue(ALLOW_CUSTOM_TEXT_ENTRY, String.valueOf(allow));
    }

    public Optional<String> getPresence() {
        return getPropertyValue(PRESENCE);
    }

    public void setPresence(final String presence) {
        setPropertyValue(PRESENCE, presence);
    }

    public Optional<String> getAppearance() {
        return getPropertyValue(APPEARANCE);
    }

    public void setAppearance(final String appearance) {
        setPropertyValue(APPEARANCE, appearance);
    }

    public boolean allowMultipleLines() {
        return getPropertyValue(ALLOW_MULTIPLE_LINES)
                .map(Boolean::valueOf)
                .orElse(false);
    }

    public void allowMultipleLines(final boolean allow) {
        setPropertyValue(ALLOW_MULTIPLE_LINES, String.valueOf(allow));
    }

    public boolean getLimitLength() {
        return getPropertyValue(LIMIT_LENGTH)
                .map(Boolean::valueOf)
                .orElse(false);
    }

    public void setLimitLength(final boolean limitLength) {
        setPropertyValue(LIMIT_LENGTH, String.valueOf(limitLength));
    }

    public Optional<String> getMaxChars() {
        return getPropertyValue(MAX_CHARS);
    }

    public void setMaxChars(final String maxChars) {
        setPropertyValue(MAX_CHARS, maxChars);
    }

    public Optional<String> getGroupName() {
        return getPropertyValue(GROUP_NAME);
    }

    public void setGroupName(final String groupName) {
        setPropertyValue(GROUP_NAME, groupName);
    }

    private Optional<String> getPropertyValue(final String propertyName) {
        return getProperty(propertyName)
                .map(Property::getValue);
    }

    private void setPropertyValue(
            final String propertyName,
            final String propertyValue) {
        getProperty(propertyName)
                .ifPresentOrElse(
                        p -> p.setValue(propertyValue),
                        () -> property.add(new Property(propertyName, propertyValue))
                );
    }

    private Optional<Property> getProperty(final String propertyName) {
        return getProperty().stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof FieldProperties) {
            final FieldProperties that = (FieldProperties) o;
            return Objects.equals(property, that.property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FieldProperties.class.getSimpleName() + "[", "]")
                .add("property=" + property)
                .toString();
    }
}
