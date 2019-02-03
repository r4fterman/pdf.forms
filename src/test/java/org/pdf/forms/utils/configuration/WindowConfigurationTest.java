package org.pdf.forms.utils.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.StringWriter;
import java.nio.file.Path;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;
import org.w3c.dom.Document;

@ExtendWith(TempDirectory.class)
class WindowConfigurationTest {

    private static final String DEFAULT_WINDOW_CONFIGURATION = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<window_configuration>"
            + "<window command=\"SCRIPT_EDITOR\" name=\"Script Editor\" visible=\"true\"/>"
            + "<window command=\"HIERARCHY\" name=\"Hierarchy\" visible=\"true\"/>"
            + "<window command=\"LIBRARY\" name=\"Library\" visible=\"true\"/>"
            + "<window command=\"PROPERTIES\" name=\"Properties\" visible=\"true\"/>"
            + "</window_configuration>";

    @Test
    void isWindowVisible_should_return_true_on_new_created_properties_file_for_known_command(@TempDirectory.TempDir final Path configDir) {
        final WindowConfiguration windowConfiguration = new WindowConfiguration(configDir.toFile());

        assertThat(windowConfiguration.isWindowVisible(WindowConfiguration.SCRIPT_EDITOR), is(true));
        assertThat(windowConfiguration.isWindowVisible(WindowConfiguration.PROPERTIES), is(true));
        assertThat(windowConfiguration.isWindowVisible(WindowConfiguration.LIBRARY), is(true));
        assertThat(windowConfiguration.isWindowVisible(WindowConfiguration.HIERARCHY), is(true));
    }

    @Test
    void isWindowVisible_should_return_false_on_new_created_properties_file_for_unknown_command(@TempDirectory.TempDir final Path configDir) {
        final WindowConfiguration windowConfiguration = new WindowConfiguration(configDir.toFile());

        assertThat(windowConfiguration.isWindowVisible("unknown"), is(false));
    }

    @Test
    void writeDefaultConfiguration(@TempDirectory.TempDir final Path configDir) throws Exception {
        final WindowConfiguration windowConfiguration = new WindowConfiguration(configDir.toFile());

        windowConfiguration.writeDefaultConfiguration();

        final Document document = windowConfiguration.getDoc();
        assertThat(serialize(document), is(DEFAULT_WINDOW_CONFIGURATION));
    }

    private String serialize(final Document document) throws TransformerException {
        final DOMSource domSource = new DOMSource(document);
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final StringWriter stringWriter = new StringWriter();
        final StreamResult streamResult = new StreamResult(stringWriter);
        transformer.transform(domSource, streamResult);
        return stringWriter.toString();
    }
}
