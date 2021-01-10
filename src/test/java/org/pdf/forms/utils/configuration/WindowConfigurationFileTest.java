package org.pdf.forms.utils.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdf.forms.gui.configuration.WindowConfiguration;

class WindowConfigurationFileTest {

    @Test
    void writeDefaultConfiguration(@TempDir final Path configDir) {
        new WindowConfigurationFile(configDir.toFile()).writeWindowConfiguration(WindowConfiguration.DEFAULT, configDir.toFile());

        final WindowConfiguration windowConfiguration = new WindowConfigurationFile(configDir.toFile())
                .getWindowConfiguration();

        assertThat(windowConfiguration, is(WindowConfiguration.DEFAULT));
    }
}
