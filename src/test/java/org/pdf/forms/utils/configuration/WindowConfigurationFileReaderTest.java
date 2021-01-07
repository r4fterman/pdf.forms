package org.pdf.forms.utils.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pdf.forms.gui.configuration.Window;
import org.pdf.forms.gui.configuration.WindowConfiguration;

class WindowConfigurationFileReaderTest {

    @Test
    void read_window_configuration_from_file() throws Exception {
        final WindowConfigurationFileReader reader = new WindowConfigurationFileReader(getFile("/default_windows.xml"));

        final WindowConfiguration windowConfiguration = reader.getWindowConfiguration();

        assertThat(windowConfiguration, is(notNullValue()));

        final List<Window> windows = windowConfiguration.getWindow();
        assertThat(windows, hasSize(4));

        assertScriptEditor(windows.get(0));
        assertHierarchy(windows.get(1));
        assertLibrary(windows.get(2));
        assertProperties(windows.get(3));
    }

    private void assertScriptEditor(final Window window) {
        assertThat(window.getCommand(), is("SCRIPT_EDITOR"));
        assertThat(window.getName(), is("Script Editor"));
        assertThat(window.isVisible(), is(true));
    }

    private void assertHierarchy(final Window window) {
        assertThat(window.getCommand(), is("HIERARCHY"));
        assertThat(window.getName(), is("Hierarchy"));
        assertThat(window.isVisible(), is(true));
    }

    private void assertLibrary(final Window window) {
        assertThat(window.getCommand(), is("LIBRARY"));
        assertThat(window.getName(), is("Library"));
        assertThat(window.isVisible(), is(true));
    }

    private void assertProperties(final Window window) {
        assertThat(window.getCommand(), is("PROPERTIES"));
        assertThat(window.getName(), is("Properties"));
        assertThat(window.isVisible(), is(true));
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = WindowConfigurationFileReaderTest.class.getResource(fileName);
        assertThat("File not found: " + fileName, url, is(notNullValue()));

        return new File(url.toURI());
    }
}
