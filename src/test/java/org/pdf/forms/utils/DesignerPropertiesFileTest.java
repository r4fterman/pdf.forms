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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;

class DesignerPropertiesFileTest {

    @Test
    void getRecentDesDocuments_from_non_existing_properties_file_should_return_list_with_null_entries(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final String[] recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is(nullValue()));
        assertThat(recentDesFiles[1], is(nullValue()));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Disabled(value = "Does not work on headless Travis CI")
    void getRecentDesDocuments_from_existing_properties_file_should_return_list_with_entries() throws Exception {
        final URL url = DesignerPropertiesFileTest.class.getResource("/.properties.xml");
        assertThat(url, is(notNullValue()));

        final File configDir = new File(url.toURI()).getParentFile();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir);

        final String[] recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is("/example/foo/bar/test.des"));
        assertThat(recentDesFiles[1], is("c:\\Benutzer\\Foo\\Bar\\test.des"));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Test
    void getRecentPdfDocuments_from_non_existing_properties_file_should_return_list_with_null_entries(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final String[] recentPdfFiles = designerPropertiesFile.getRecentPDFDocuments();

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
    void addRecentDocument_to_DES_list_should_be_added_on_top(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        designerPropertiesFile.addRecentDesignerDocument("/usr/local/example1.des");
        designerPropertiesFile.addRecentDesignerDocument("/usr/local/example2.des");

        final String[] recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles.length, is(6));
        assertThat(recentDesFiles[0], is("/usr/local/example2.des"));
        assertThat(recentDesFiles[1], is("/usr/local/example1.des"));
        assertThat(recentDesFiles[2], is(nullValue()));
        assertThat(recentDesFiles[3], is(nullValue()));
        assertThat(recentDesFiles[4], is(nullValue()));
        assertThat(recentDesFiles[5], is(nullValue()));
    }

    @Test
    void addRecentDocument_to_PDF_list_should_be_added_on_top(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        designerPropertiesFile.addRecentPDFDocument("/usr/local/example1.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example2.pdf");

        final String[] recentDesFiles = designerPropertiesFile.getRecentPDFDocuments();

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
    void getCustomFonts_from_non_existing_properties_file_should_return_empty_map(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(anEmptyMap()));
    }

    @Test
    void getCustomFonts_from_existing_properties_file_should_return_filled_empty_map() throws Exception {
        final URL url = DesignerPropertiesFileTest.class.getResource("/.properties.xml");
        assertThat(url, is(notNullValue()));

        final File configDir = new File(url.toURI()).getParentFile();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir);

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(aMapWithSize(2)));
        assertThat(customFonts, hasEntry("Helvetica", "/path/to/font/helvetica"));
        assertThat(customFonts, hasEntry("Arial", "/path/to/font/arial"));
    }

    @Test
    void addCustomFont_should_store_font_name_and_path(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        designerPropertiesFile.addCustomFont("Courier", "/path/to/font/courier");
        designerPropertiesFile.addCustomFont("TimesNewRoman", "/path/to/font/timesnewroman");

        final Map<String, String> customFonts = designerPropertiesFile.getCustomFonts();
        assertThat(customFonts, is(aMapWithSize(2)));
        assertThat(customFonts, hasEntry("Courier", "/path/to/font/courier"));
        assertThat(customFonts, hasEntry("TimesNewRoman", "/path/to/font/timesnewroman"));
    }

    @Test
    void checkForModelUpdate_should_return_false_for_initialized_document(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final Document document = designerPropertiesFile.getDocument();
        assertThat(designerPropertiesFile.checkForModelUpdate(document), is(false));
    }

}
