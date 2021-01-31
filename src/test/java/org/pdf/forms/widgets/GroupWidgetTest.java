package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.xmlunit.diff.DefaultNodeMatcher;

class GroupWidgetTest {

    @Test
    void persist_widget_into_xml() throws Exception {
        final GroupWidget groupWidget = new GroupWidget();

        final String serialize = new WidgetFileWriter(groupWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize, isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                .ignoreWhitespace()
        );
    }

    private File getFile() throws URISyntaxException {
        final URL url = GroupWidgetTest.class.getResource("groupWidget_persisted.xml");
        return new File(url.toURI());
    }
}
