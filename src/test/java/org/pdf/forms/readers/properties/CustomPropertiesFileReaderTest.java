package org.pdf.forms.readers.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.pdf.forms.model.properties.CustomFonts;
import org.pdf.forms.model.properties.CustomProperties;
import org.pdf.forms.model.properties.RecentDesFiles;
import org.pdf.forms.model.properties.RecentPdfFiles;

class CustomPropertiesFileReaderTest {

    @Test
    void read_properties_from_file() throws Exception {
        final CustomPropertiesFileReader reader = new CustomPropertiesFileReader(getFile("/.properties.xml"));

        final CustomProperties properties = reader.getCustomProperties();

        assertRecentDesFiles(properties.getRecentDesFiles());
        assertRecentPdfFiles(properties.getRecentPdfFiles());
        assertCustomFonts(properties.getCustomFonts());
    }

    private void assertRecentDesFiles(final RecentDesFiles recentDesFiles) {
        assertThat(recentDesFiles.getFile(), hasSize(2));

        assertThat(recentDesFiles.getFile().get(0).getName(), is("c:\\Benutzer\\Foo\\Bar\\test.des"));
        assertThat(recentDesFiles.getFile().get(1).getName(), is("/example/foo/bar/test.des"));
    }

    private void assertRecentPdfFiles(final RecentPdfFiles recentPdfFiles) {
        assertThat(recentPdfFiles.getFile(), hasSize(2));

        assertThat(recentPdfFiles.getFile().get(0).getName(), is("c:\\Benutzer\\Foo\\Bar\\test.pdf"));
        assertThat(recentPdfFiles.getFile().get(1).getName(), is("/example/foo/bar/test.pdf"));
    }

    private void assertCustomFonts(final CustomFonts customFonts) {
        assertThat(customFonts.getFont(), hasSize(2));

        assertThat(customFonts.getFont().get(0).getName(), is("Helvetica"));
        assertThat(customFonts.getFont().get(0).getPath(), is("/path/to/font/helvetica"));

        assertThat(customFonts.getFont().get(1).getName(), is("Arial"));
        assertThat(customFonts.getFont().get(1).getPath(), is("/path/to/font/arial"));
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = CustomPropertiesFileReaderTest.class.getResource(fileName);
        assertThat("File not found: " + fileName, url, is(notNullValue()));

        return new File(url.toURI());
    }
}
