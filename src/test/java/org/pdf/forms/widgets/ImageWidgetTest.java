package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import javax.swing.*;
import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.writer.MockFontDirectories;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;

class ImageWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private DesignerPropertiesFile designerPropertiesFile;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        this.designerPropertiesFile =
                new DesignerPropertiesFile(configuration.getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile, new MockFontDirectories());

        this.baseComponent = new JLabel("label");
        this.component = new JCheckBox();
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final ImageWidget imageWidget = new ImageWidget(IWidget.IMAGE,
                baseComponent,
                component,
                new FontHandler(designerPropertiesFile, new MockFontDirectories()));

        final String serialize = new WidgetFileWriter(imageWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize, isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                .withDifferenceEvaluator(DifferenceEvaluators.chain(
                        DifferenceEvaluators.Default,
                        new PropertyWithIteratorNumberDifferenceEvaluator("Image")

                ))
                .ignoreWhitespace()
        );
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final ImageWidget imageWidget = new ImageWidget(IWidget.IMAGE,
                baseComponent,
                component,
                getWidgetFromFile(),
                fontHandler);

        final String serialize = new WidgetFileWriter(imageWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize, isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                .ignoreWhitespace()
        );
    }

    private org.pdf.forms.model.des.Widget getWidgetFromFile() throws IOException, JAXBException, URISyntaxException {
        return new WidgetFileReader(getFile()).getWidget();
    }

    private File getFile() throws URISyntaxException {
        final URL url = ImageWidgetTest.class.getResource("imageWidget_persisted.xml");
        return new File(url.toURI());
    }
}
