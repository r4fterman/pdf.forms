package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.components.PdfTextField;
import org.pdf.forms.widgets.components.SplitComponent;
import org.pdf.forms.writer.MockFontDirectories;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;

import jakarta.xml.bind.JAXBException;

class TextFieldWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile, new MockFontDirectories());

        this.baseComponent = new SplitComponent("captionText", new PdfTextField("text", fontHandler), 1, fontHandler);
        this.component = new JTextField();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final TextFieldWidget textFieldWidget = new TextFieldWidget(IWidget.TEXT_FIELD,
                baseComponent,
                component,
                fontHandler);
        final String serialize = new WidgetFileWriter(textFieldWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize,
                isSimilarTo(expected)
                        .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                        .withDifferenceEvaluator(DifferenceEvaluators.chain(
                                DifferenceEvaluators.Default,
                                new IgnoreFontNameDifferenceEvaluator()
                        ))
                        .ignoreWhitespace()
        );
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final TextFieldWidget textFieldWidget = new TextFieldWidget(IWidget.TEXT_FIELD,
                baseComponent,
                component,
                getWidgetFromFile(),
                fontHandler);
        final String serialize = new WidgetFileWriter(textFieldWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize,
                isSimilarTo(expected)
                        .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                        .withDifferenceEvaluator(DifferenceEvaluators.chain(
                                DifferenceEvaluators.Default,
                                new IgnoreFontNameDifferenceEvaluator()
                        ))
                        .ignoreWhitespace()
        );
    }

    private org.pdf.forms.model.des.Widget getWidgetFromFile() throws IOException, JAXBException, URISyntaxException {
        return new WidgetFileReader(getFile()).getWidget();
    }

    private File getFile() throws URISyntaxException {
        final URL url = TextFieldWidgetTest.class.getResource("textFieldWidget_persisted.xml");
        return new File(url.toURI());
    }
}
