package org.pdf.forms.gui.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class VersionTest {

    @Test
    void getValue_should_render_current_version_value() {
        assertThat(Version.CURRENT_VERSION.getValue(), is("0.8b05"));
    }

    @Test
    void getValue_should_render_dev_version_value() {
        assertThat(Version.DEV.getValue(), is("DEV.SNAPSHOT"));
    }
}
