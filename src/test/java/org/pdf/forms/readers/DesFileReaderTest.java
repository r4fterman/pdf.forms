package org.pdf.forms.readers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pdf.forms.model.des.Document;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.Page;
import org.pdf.forms.model.des.Property;

class DesFileReaderTest {

    @Test
    void read_des_document_from_file() throws Exception {
        final Document document = new DesFileReader(getFile("/example.des")).getDesDocument();

        assertJavaScriptContent(document.getJavaScript());
        assertDocumentProperty(document.getProperty());
        assertPages(document.getPage());
    }

    private void assertJavaScriptContent(final JavaScriptContent javaScriptContent) {
        assertThat(javaScriptContent, is(notNullValue()));
        assertThat(javaScriptContent.getInitialize(), is(emptyString()));

        assertThat(javaScriptContent.getChange(), is(nullValue()));
        assertThat(javaScriptContent.getMouseDown(), is(nullValue()));
        assertThat(javaScriptContent.getMouseEnter(), is(nullValue()));
        assertThat(javaScriptContent.getMouseExit(), is(nullValue()));
        assertThat(javaScriptContent.getMouseUp(), is(nullValue()));
        assertThat(javaScriptContent.getKeystroke(), is(nullValue()));
    }

    private void assertDocumentProperty(final Property property) {
        assertThat(property.getName(), is("version"));
        assertThat(property.getValue(), is("0.8b05"));
    }

    private void assertPages(final List<Page> pages) {
        assertThat(pages, hasSize(2));

        assertPage0(pages.get(0));
        assertPage1(pages.get(1));
    }

    private void assertPage0(final Page page) {
        assertThat(page.getProperty(), hasSize(2));
        assertThat(page.getProperty().get(0).getName(), is("pagetype"));
        assertThat(page.getProperty().get(0).getValue(), is("simplepage"));

        assertThat(page.getPagedata().getProperty(), hasSize(2));
        assertThat(page.getPagedata().getProperty().get(0).getName(), is("width"));
        assertThat(page.getPagedata().getProperty().get(0).getValue(), is("595"));

        assertThat(page.getRadioButtonGroups().getProperty(), hasSize(1));
        assertThat(page.getRadioButtonGroups().getProperty().get(0).getName(), is("buttongroupname"));
        assertThat(page.getRadioButtonGroups().getProperty().get(0).getValue(), is("Radio Button Group1"));

        assertThat(page.getCheckBoxGroups().getProperty(), hasSize(18));
        assertThat(page.getCheckBoxGroups().getProperty().get(17).getName(), is("buttongroupname"));
        assertThat(page.getCheckBoxGroups().getProperty().get(17).getValue(), is("CheckBox Group18"));
    }

    private void assertPage1(final Page page) {
        assertThat(page.getProperty(), hasSize(2));
        assertThat(page.getProperty().get(0).getName(), is("pagetype"));
        assertThat(page.getProperty().get(0).getValue(), is("simplepage"));

        assertThat(page.getPagedata().getProperty(), hasSize(2));
        assertThat(page.getPagedata().getProperty().get(0).getName(), is("width"));
        assertThat(page.getPagedata().getProperty().get(0).getValue(), is("595"));

        assertThat(page.getCheckBoxGroups().getProperty(), is(empty()));
        assertThat(page.getRadioButtonGroups().getProperty(), is(empty()));
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = DesFileReaderTest.class.getResource(fileName);
        assertThat("File not found: " + fileName, url, is(notNullValue()));

        return new File(url.toURI());

    }
}