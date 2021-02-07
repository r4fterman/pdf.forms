package org.pdf.forms.readers.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdf.forms.model.configuration.MenuConfiguration;

class MenuConfigurationFileTest {

    @Test
    void write_configuration(@TempDir final Path configFir) {
        new MenuConfigurationFile(configFir.toFile()).writeMenuConfiguration(MenuConfiguration.DEFAULT, configFir.toFile());

        final MenuConfigurationFile menuConfigurationFile = new MenuConfigurationFile(configFir.toFile());
        MenuConfiguration menuConfiguration = menuConfigurationFile.getMenuConfiguration();

        assertThat(menuConfiguration, is(MenuConfiguration.DEFAULT));
    }
 }
