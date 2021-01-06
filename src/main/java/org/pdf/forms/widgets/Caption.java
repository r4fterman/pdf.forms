package org.pdf.forms.widgets;

import java.util.Map;

import org.pdf.forms.widgets.components.SplitComponent;

class Caption {

    static final int DEFAULT_LOCATION = SplitComponent.CAPTION_NONE;

    private static final Map<String, Integer> LOCATIONS = Map.of(
            "left", SplitComponent.CAPTION_LEFT,
            "right", SplitComponent.CAPTION_RIGHT,
            "top", SplitComponent.CAPTION_TOP,
            "bottom", SplitComponent.CAPTION_BOTTOM
    );

    private final String value;

    Caption(final String value) {
        this.value = value;
    }

    public int getLocation() {
        return LOCATIONS.getOrDefault(value.toLowerCase(), DEFAULT_LOCATION);
    }
}
