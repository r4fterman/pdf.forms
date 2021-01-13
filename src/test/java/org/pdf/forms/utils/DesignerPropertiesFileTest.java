package org.pdf.forms.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DesignerPropertiesFileTest {

    @Test
    void getRecentDesDocuments_from_non_existing_properties_file_should_return_list_with_null_entries(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final List<String> recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles, hasSize(6));
        assertThat(recentDesFiles.get(0), is(emptyString()));
        assertThat(recentDesFiles.get(1), is(emptyString()));
        assertThat(recentDesFiles.get(2), is(emptyString()));
        assertThat(recentDesFiles.get(3), is(emptyString()));
        assertThat(recentDesFiles.get(4), is(emptyString()));
        assertThat(recentDesFiles.get(5), is(emptyString()));
    }

    @Test
    void getRecentDesDocuments_from_existing_properties_file_should_return_list_with_entries() throws Exception {
        final URL url = DesignerPropertiesFileTest.class.getResource("/.properties.xml");
        assertThat(url, is(notNullValue()));

        final File configDir = new File(url.toURI()).getParentFile();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir);

        final List<String> recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles, hasSize(6));
        assertThat(recentDesFiles.get(0), is("c:\\Benutzer\\Foo\\Bar\\test.des"));
        assertThat(recentDesFiles.get(1), is("/example/foo/bar/test.des"));
        assertThat(recentDesFiles.get(2), is(emptyString()));
        assertThat(recentDesFiles.get(3), is(emptyString()));
        assertThat(recentDesFiles.get(4), is(emptyString()));
        assertThat(recentDesFiles.get(5), is(emptyString()));
    }

    @Test
    void getRecentPdfDocuments_from_non_existing_properties_file_should_return_list_with_empty_entries(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        final List<String> recentPdfFiles = designerPropertiesFile.getRecentPDFDocuments();

        assertThat(recentPdfFiles, hasSize(6));
        assertThat(recentPdfFiles.get(0), is(emptyString()));
        assertThat(recentPdfFiles.get(1), is(emptyString()));
        assertThat(recentPdfFiles.get(2), is(emptyString()));
        assertThat(recentPdfFiles.get(3), is(emptyString()));
        assertThat(recentPdfFiles.get(4), is(emptyString()));
        assertThat(recentPdfFiles.get(5), is(emptyString()));
    }

    @Test
    void addRecentDocument_to_DES_list_should_be_added_on_top(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        designerPropertiesFile.addRecentDesignerDocument("/usr/local/example1.des");
        designerPropertiesFile.addRecentDesignerDocument("/usr/local/example2.des");

        final List<String> recentDesFiles = designerPropertiesFile.getRecentDesignerDocuments();

        assertThat(recentDesFiles, hasSize(6));
        assertThat(recentDesFiles.get(0), is("/usr/local/example2.des"));
        assertThat(recentDesFiles.get(1), is("/usr/local/example1.des"));
        assertThat(recentDesFiles.get(2), is(emptyString()));
        assertThat(recentDesFiles.get(3), is(emptyString()));
        assertThat(recentDesFiles.get(4), is(emptyString()));
        assertThat(recentDesFiles.get(5), is(emptyString()));
    }

    @Test
    void addRecentDocument_to_PDF_list_should_be_added_on_top(@TempDir final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());

        designerPropertiesFile.addRecentPDFDocument("/usr/local/example1.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example2.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example3.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example4.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example5.pdf");
        designerPropertiesFile.addRecentPDFDocument("/usr/local/example6.pdf");

        final List<String> recentDesFiles = designerPropertiesFile.getRecentPDFDocuments();

        assertThat(recentDesFiles, hasSize(6));
        assertThat(recentDesFiles.get(0), is("/usr/local/example6.pdf"));
        assertThat(recentDesFiles.get(1), is("/usr/local/example5.pdf"));
        assertThat(recentDesFiles.get(2), is("/usr/local/example4.pdf"));
        assertThat(recentDesFiles.get(3), is("/usr/local/example3.pdf"));
        assertThat(recentDesFiles.get(4), is("/usr/local/example2.pdf"));
        assertThat(recentDesFiles.get(5), is("/usr/local/example1.pdf"));
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

}
