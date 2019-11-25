package org.pdf.forms.gui.designer.captionchanger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaptionChangerTest {

    private CaptionChanger captionChanger;

    @BeforeEach
    void setUp() {
        this.captionChanger = new CaptionChanger();
    }

    @Test
    void getAlignment_without_align_should_return_null() {
        final String text = "<html>No ALIGN in this html snippet.";
        final String alignment = captionChanger.getAlignment(text);
        assertThat(alignment, is(nullValue()));
    }

    @Test
    void getAlignment_with_align_should_return_correct_value() {
        final String text = "<html><p align=left>An ALIGN left is used in this html snippet.";
        final String alignment = captionChanger.getAlignment(text);

        assertThat(alignment, is("left"));
    }

    @Test
    void getAlignment_with_incorrect_align_should_return_correct_value() {
        final String text = "<html><p align====left>An ALIGN left is used in this html snippet.";
        final String alignment = captionChanger.getAlignment(text);

        assertThat(alignment, is("left"));
    }
}
