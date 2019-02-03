package org.pdf.forms.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;

@ExtendWith(TempDirectory.class)
class DesignerPropertiesFileTest {

    private DesignerPropertiesFile designerPropertiesFile;

    @AfterEach
    void tearDown() {
        if (designerPropertiesFile != null) {
            designerPropertiesFile.destroy();
        }
    }

    @Test
    void getRecentDesDocuments_from_non_existing_properties_file_should_return_list_with_null_entries(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        final String[] recentDesFiles = designerPropertiesFile.getRecentDocuments("recentdesfiles");

        assertThat(recentDesFiles, is(notNullValue()));

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is(nullValue()));
        assertThat(recentDesFiles[1], is(nullValue()));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    void getRecentDesDocuments_from_existing_properties_file_should_return_list_with_entries() throws Exception {
        final URL url = DesignerPropertiesFileTest.class.getResource("/.properties.xml");
        assertThat(url, is(notNullValue()));

        final File configDir = new File(url.toURI()).getParentFile();
        System.out.println("Config dir: " + configDir.getAbsolutePath());
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir);

        final String[] recentDesFiles = designerPropertiesFile.getRecentDocuments("recentdesfiles");

        assertThat(recentDesFiles, is(notNullValue()));

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is("/example/foo/bar/test.des"));
        assertThat(recentDesFiles[1], is("c:\\Benutzer\\Foo\\Bar\\test.des"));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Test
    void getRecentPdfDocuments_from_non_existing_properties_file_should_return_list_with_null_entries(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        final String[] recentPdfFiles = designerPropertiesFile.getRecentDocuments("recentpdffiles");

        assertThat(recentPdfFiles, is(notNullValue()));

        assertThat(recentPdfFiles.length, is(6));
        assertThat(recentPdfFiles[0], is(nullValue()));
        assertThat(recentPdfFiles[1], is(nullValue()));
        assertThat(recentPdfFiles[2], is(nullValue()));
        assertThat(recentPdfFiles[3], is(nullValue()));
        assertThat(recentPdfFiles[4], is(nullValue()));
        assertThat(recentPdfFiles[5], is(nullValue()));
    }

    @Test
    void addRecentDocument_to_DES_list_should_be_added_on_top(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        designerPropertiesFile.addRecentDocument("/usr/local/example1.des", "recentdesfiles");
        designerPropertiesFile.addRecentDocument("/usr/local/example2.des", "recentdesfiles");

        final String[] recentDesFiles = designerPropertiesFile.getRecentDocuments("recentdesfiles");

        assertThat(recentDesFiles, is(notNullValue()));

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is("/usr/local/example2.des"));
        assertThat(recentDesFiles[1], is("/usr/local/example1.des"));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Test
    void addRecentDocument_to_PDF_list_should_be_added_on_top(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        designerPropertiesFile.addRecentDocument("/usr/local/example1.pdf", "recentpdffiles");
        designerPropertiesFile.addRecentDocument("/usr/local/example2.pdf", "recentpdffiles");

        final String[] recentDesFiles = designerPropertiesFile.getRecentDocuments("recentpdffiles");

        assertThat(recentDesFiles, is(notNullValue()));

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is("/usr/local/example2.pdf"));
        assertThat(recentDesFiles[1], is("/usr/local/example1.pdf"));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Test
    void getCustomFonts_from_non_existing_properties_file_should_return_empty_map(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(anEmptyMap()));
    }

    @Test
    void getCustomFonts_from_existing_properties_file_should_return_filled_empty_map() throws Exception {
        final URL url = DesignerPropertiesFileTest.class.getResource("/.properties.xml");
        assertThat(url, is(notNullValue()));

        final File configDir = new File(url.toURI()).getParentFile();
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir);

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(aMapWithSize(2)));
        assertThat(customFonts, hasEntry("Helvetica", "/path/to/font/helvetica"));
        assertThat(customFonts, hasEntry("Arial", "/path/to/font/arial"));
    }

    @Test
    void addCustomFont_should_store_font_name_and_path(@TempDirectory.TempDir final Path configDir) {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        designerPropertiesFile.addCustomFont("Courier", "/path/to/font/courier");
        designerPropertiesFile.addCustomFont("TimesNewRoman", "/path/to/font/timesnewroman");

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(aMapWithSize(2)));
        assertThat(customFonts, hasEntry("Courier", "/path/to/font/courier"));
        assertThat(customFonts, hasEntry("TimesNewRoman", "/path/to/font/timesnewroman"));
    }

    @Test
    void checkAllElementsPresent(@TempDirectory.TempDir final Path configDir) throws Exception {
        designerPropertiesFile = DesignerPropertiesFile.getInstance(configDir.toFile());

        assertThat(designerPropertiesFile.checkAllElementsPresent(), is(true));
    }

}
