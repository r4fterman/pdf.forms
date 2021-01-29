package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.components.SplitComponent;
import org.xmlunit.matchers.CompareMatcher;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.canonical.Canonicalizer;

class CheckBoxWidgetTest {

    private DesignerPropertiesFile designerPropertiesFile;
    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        this.designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new SplitComponent("captionText", new JCheckBox(), 1, fontHandler);
        this.component = new JCheckBox();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() throws Exception {

        final CheckBoxWidget widget = new CheckBoxWidget(IWidget.CHECK_BOX,
                baseComponent,
                component,
                fontHandler);

        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        final String actualXml = canonicalizeXml(serialize);

        final String expectedContent = Files.readString(getFile().toPath());
        final String expectedXml = canonicalizeXml(expectedContent);

        System.out.println("act:" + actualXml);
        System.out.println("exp:" + expectedXml);

        assertThat(actualXml, CompareMatcher.isSimilarTo(expectedXml).ignoreComments().ignoreWhitespace());
        assertThat(actualXml.length(), is(greaterThanOrEqualTo(2089)));
        assertThat(actualXml.length(), is(lessThanOrEqualTo(2095)));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final CheckBoxWidget widget = new CheckBoxWidget(IWidget.CHECK_BOX,
                baseComponent,
                component,
                new WidgetFileReader(getFile()).getWidget(),
                new FontHandler(designerPropertiesFile));

        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(3141));
    }

    private String canonicalizeXml(final String xml) throws ParsingException, IOException {
        try (StringReader reader = new StringReader(xml);
             ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            final Builder builder = new Builder();
            final Document document = builder.build(reader);

            final Canonicalizer canonicalizer = new Canonicalizer(stream, false);
            canonicalizer.write(document);

            return stream.toString(StandardCharsets.UTF_8);
        }
    }

    private File getFile() throws URISyntaxException {
        final URL url = ComboBoxWidgetTest.class.getResource("checkBoxWidget_persisted.xml");
        return new File(url.toURI());
    }
}
