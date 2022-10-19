package org.pdf.forms.readers.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.model.configuration.Window;
import org.pdf.forms.model.configuration.WindowConfiguration;

class WindowConfigurationFileReaderTest {

    private static final String WINDOW_CONFIGURATION_DISABLED = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<window_configuration>"
            + "<window command=\"SCRIPT_EDITOR\" name=\"Script Editor\" visible=\"false\"/>"
            + "<window command=\"HIERARCHY\" name=\"Hierarchy\" visible=\"false\"/>"
            + "<window command=\"LIBRARY\" name=\"Library\" visible=\"false\"/>"
            + "<window command=\"PROPERTIES\" name=\"Properties\" visible=\"false\"/>"
            + "</window_configuration>";

    private WindowConfiguration windowConfiguration;

    @BeforeEach
    void setUp() throws Exception {
        final WindowConfigurationFileReader reader = new WindowConfigurationFileReader(getFile("/default_windows.xml"));
        this.windowConfiguration = reader.getWindowConfiguration();
    }

    @Test
    void read_window_configuration_from_file() {
        final List<Window> windows = windowConfiguration.getWindow();
        assertThat(windows, hasSize(4));

        assertScriptEditor(windows.get(0));
        assertHierarchy(windows.get(1));
        assertLibrary(windows.get(2));
        assertProperties(windows.get(3));
    }

    @Test
    void isScriptEditorVisible_should_return_true_from_file() {
        assertThat(windowConfiguration.isScriptEditorVisible(), is(true));
    }

    @Test
    void isHierarchyVisible_should_return_true_from_file() {
        assertThat(windowConfiguration.isHierarchyVisible(), is(true));
    }

    @Test
    void isLibraryVisible_should_return_true_from_file() {
        assertThat(windowConfiguration.isLibraryVisible(), is(true));
    }

    @Test
    void isPropertiesVisible_should_return_true_from_file() {
        assertThat(windowConfiguration.isPropertiesVisible(), is(true));
    }

    @Test
    void isScriptEditorVisible_should_return_false_from_content() {
        final WindowConfiguration windowConfiguration = new WindowConfigurationFileReader(WINDOW_CONFIGURATION_DISABLED)
                .getWindowConfiguration();

        assertThat(windowConfiguration.isScriptEditorVisible(), is(false));
    }

    @Test
    void isHierarchyVisible_should_return_false_from_content() {
        final WindowConfiguration windowConfiguration = new WindowConfigurationFileReader(WINDOW_CONFIGURATION_DISABLED)
                .getWindowConfiguration();

        assertThat("Config: "+windowConfiguration, windowConfiguration.isHierarchyVisible(), is(false));
    }

    @Test
    void isLibraryVisible_should_return_false_from_content() {
        final WindowConfiguration windowConfiguration = new WindowConfigurationFileReader(WINDOW_CONFIGURATION_DISABLED)
                .getWindowConfiguration();

        assertThat(windowConfiguration.isLibraryVisible(), is(false));
    }

    @Test
    void isPropertiesVisible_should_return_false_from_content() {
        final WindowConfiguration windowConfiguration = new WindowConfigurationFileReader(WINDOW_CONFIGURATION_DISABLED)
                .getWindowConfiguration();

        assertThat(windowConfiguration.isPropertiesVisible(), is(false));
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
