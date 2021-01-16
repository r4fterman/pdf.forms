package org.pdf.forms.readers.custom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.pdf.forms.model.components.CustomComponents;

class CustomComponentsFileReaderTest {

    @Test
    void read_custom_component_document_from_file() throws Exception {
        final CustomComponents customComponents = new CustomComponentsFileReader(getFile("/.custom_components.xml"))
                .getCustomComponents();

        assertThat(customComponents, is(notNullValue()));
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = CustomComponentsFileReaderTest.class.getResource(fileName);
        assertThat(url, is(notNullValue()));

        return new File(url.toURI());
    }
}
